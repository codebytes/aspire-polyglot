const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { test } = require('node:test');

const output = process.env.ASPIRE_PUBLISH_OUTPUT ||
  path.resolve(__dirname, '../aspire-output/bicep');
const modules = ['aca-acr', 'aca', 'cosmos', 'api-identity', 'api-roles-cosmos', 'api', 'frontend'];
const template = (name) => JSON.parse(fs.readFileSync(
  path.join(output, name === 'main' ? 'main.json' : `${name}/${name}.json`), 'utf8',
));
const resources = (name) => Object.values(template(name).resources);
const resource = (name, type) => {
  const found = resources(name).find((item) => item.type === type);
  assert.ok(found, `${name} is missing ${type}`);
  return found;
};
const app = (name) => resource(name, 'Microsoft.App/containerApps');
const environment = (name) => Object.fromEntries(
  app(name).properties.template.containers[0].env.map(({ name, value }) => [name, value]),
);

test('all eight compiled templates have the expected staged inputs', () => {
  for (const name of ['main', ...modules]) assert.ok(template(name).$schema);
  assert.deepEqual(Object.keys(template('main').parameters).sort(),
    ['location', 'principalId', 'resourceGroupName']);
  assert.ok(resource('main', 'Microsoft.Resources/resourceGroups'));
  assert.equal(resources('main').filter((item) => item.type === 'Microsoft.App/containerApps').length, 0);
  for (const name of ['api', 'frontend']) {
    assert.ok(template(name).parameters[`${name}_containerimage`]);
    assert.equal(app(name).properties.template.containers[0].image,
      `[parameters('${name}_containerimage')]`);
  }
});

test('only the frontend has public ingress; its static server preserves /api routes', () => {
  assert.equal(app('api').properties.configuration.ingress.external, false);
  assert.equal(app('frontend').properties.configuration.ingress.external, true);
  assert.equal(app('frontend').properties.configuration.ingress.targetPort, 5000);
  const env = environment('frontend');
  assert.equal(env.YARP_ENABLE_STATIC_FILES, 'true');
  assert.equal(env.REVERSEPROXY__ROUTES__api__MATCH__PATH, '/api/{**catch-all}');
  assert.equal(env.REVERSEPROXY__ROUTES__api__CLUSTERID, 'api');
  assert.equal(env.REVERSEPROXY__CLUSTERS__api__DESTINATIONS__destination1__ADDRESS, 'http://api');
  assert.ok(env.services__api__http__0.includes('https://api.internal.'));
  assert.ok(!Object.keys(env).some((key) => key.includes('PATHREMOVEPREFIX')));
});

test('Cosmos schema and data-plane identity are modeled without account keys', () => {
  const account = resource('cosmos', 'Microsoft.DocumentDB/databaseAccounts');
  assert.equal(account.properties.disableLocalAuth, true);
  assert.ok(account.properties.capabilities.some((item) => item.name === 'EnableServerless'));
  const database = resource('cosmos', 'Microsoft.DocumentDB/databaseAccounts/sqlDatabases');
  assert.equal(database.properties.resource.id, 'recipesdb');
  const container = resource('cosmos', 'Microsoft.DocumentDB/databaseAccounts/sqlDatabases/containers');
  assert.equal(container.properties.resource.id, 'recipes');
  assert.deepEqual(container.properties.resource.partitionKey.paths, ['/id']);
  const role = resource('api-roles-cosmos', 'Microsoft.DocumentDB/databaseAccounts/sqlRoleAssignments');
  assert.ok(role.properties.roleDefinitionId.includes('00000000-0000-0000-0000-000000000002'));
  assert.equal(environment('api').AZURE_TOKEN_CREDENTIALS, 'ManagedIdentityCredential');
  assert.ok(environment('api').ConnectionStrings__recipesdb.includes('Database=recipesdb'));
  assert.equal(app('api').identity.type, 'UserAssigned');
  assert.equal(Object.keys(app('api').identity.userAssignedIdentities).length, 2);
});

test('the generated Dockerfile builds Angular and serves its browser output, not ng serve', () => {
  const dockerfile = fs.readFileSync(path.join(output, 'frontend.Dockerfile'), 'utf8');
  assert.ok(dockerfile.includes('npm ci'));
  assert.ok(dockerfile.includes('RUN npm run build'));
  assert.ok(dockerfile.includes('COPY --from=build /app/dist/recipe-manager/browser /app/wwwroot'));
  assert.ok(!dockerfile.includes('ng serve'));
});

test('generated templates contain no embedded local collector credentials or account keys', () => {
  const forbidden = /AccountKey\s*=|x-otlp-api-key\s*=|-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----|[?&]sig=[A-Za-z0-9%+/]{16,}/i;
  for (const name of ['main', ...modules]) {
    for (const extension of ['bicep', 'json']) {
      const relative = name === 'main' ? `main.${extension}` : `${name}/${name}.${extension}`;
      assert.ok(!forbidden.test(fs.readFileSync(path.join(output, relative), 'utf8')),
        `Potential embedded credential in ${relative}; value omitted`);
    }
  }
});
