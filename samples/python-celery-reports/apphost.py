import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".modules"))

from aspire import create_builder

builder = create_builder()

redis = builder.add_redis("broker")

builder.add_python_app("api", "./src", "main.py") \
    .with_reference(redis) \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()

builder.add_python_app("worker", "./src", "worker.py") \
    .with_reference(redis)

app = builder.build()
app.run()
