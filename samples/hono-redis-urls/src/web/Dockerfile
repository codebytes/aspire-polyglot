FROM node:20-slim
WORKDIR /app
COPY package*.json ./
ARG NPM_CONFIG_REGISTRY=https://registry.npmjs.org/
RUN npm ci
COPY . .
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "5173"]
