# Node.js OTEL emitter for the standalone Aspire dashboard demo.
FROM node:22-alpine
WORKDIR /app
COPY package.json ./
RUN npm install --omit=dev --no-audit --no-fund
COPY worker.js ./
CMD ["node", "worker.js"]
