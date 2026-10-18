FROM node:24.22.0-slim
WORKDIR /app
COPY package*.json ./
ARG NPM_CONFIG_REGISTRY=https://registry.npmjs.org/
RUN npm ci
COPY . .
EXPOSE 8080
CMD ["npm", "start"]
