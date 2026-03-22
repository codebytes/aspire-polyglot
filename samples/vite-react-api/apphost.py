import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".modules"))

from aspire import create_builder

builder = create_builder()

cache = builder.add_container("cache", "redis:latest")

api = builder.add_dockerfile("api", "./src/api")
api.with_environment("REDIS_HOST", "cache")
api.with_environment("REDIS_PORT", "6379")
api.with_http_endpoint(target_port=8080, env="PORT")
api.with_external_http_endpoints()

web = builder.add_dockerfile("web", "./src/web")
web.with_http_endpoint(target_port=5173, env="PORT")
web.with_external_http_endpoints()

app = builder.build()
app.run()
