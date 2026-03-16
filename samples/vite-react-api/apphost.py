import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".modules"))

from aspire import create_builder

builder = create_builder()

cache = builder.add_redis("cache")

api = builder.add_python_app("api", "./src/api", "main.py") \
    .with_reference(cache) \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()

builder.add_npm_app("web", "./src/web", "dev") \
    .with_reference(api) \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()

app = builder.build()
app.run()
