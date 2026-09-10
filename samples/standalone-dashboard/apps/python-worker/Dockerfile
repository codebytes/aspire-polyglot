# Python OTEL emitter for the standalone Aspire dashboard demo.
FROM python:3.12-slim
WORKDIR /app
COPY requirements.txt ./
ARG PIP_INDEX_URL=https://pypi.org/simple/
RUN pip install --no-cache-dir -r requirements.txt
COPY worker.py ./
CMD ["python", "-u", "worker.py"]
