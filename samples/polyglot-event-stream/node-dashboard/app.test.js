const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { test } = require('node:test');
const vm = require('node:vm');

test('partial Kafka startup disconnects both consumers before retrying', async () => {
    const retry = Promise.withResolvers();
    let failSubscription = true;
    const consumers = [];
    class Kafka {
        consumer() {
            const isReadings = consumers.length === 1;
            const instance = {
                running: false,
                disconnects: 0,
                async connect() {},
                async subscribe() {
                    assert.equal(this.running, false, 'cannot subscribe while running');
                    if (isReadings && failSubscription) {
                        failSubscription = false;
                        throw new Error('Topic metadata not ready');
                    }
                },
                async run() { this.running = true; },
                async disconnect() {
                    this.running = false;
                    this.disconnects++;
                },
            };
            consumers.push(instance);
            return instance;
        }
    }
    const app = { set() {}, get() {}, listen() {} };
    vm.runInNewContext(fs.readFileSync(path.join(__dirname, 'app.js'), 'utf8'), {
        require(name) {
            if (name === './instrumentation') return {};
            if (name === 'express') return () => app;
            if (name === 'kafkajs') return { Kafka };
            if (name === 'path') return path;
            throw new Error(`Unexpected import: ${name}`);
        },
        __dirname,
        process: { env: {}, on() {} },
        console: { log() {}, error() {} },
        setTimeout(callback) { retry.resolve(callback); },
    });
    const restart = await retry.promise;
    assert.deepEqual(consumers.map(c => c.running), [false, false]);
    assert.deepEqual(consumers.map(c => c.disconnects), [1, 1]);
    await restart();
    assert.deepEqual(consumers.map(c => c.running), [true, true]);
});
