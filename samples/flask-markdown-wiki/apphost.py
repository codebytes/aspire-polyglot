import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".modules"))

from aspire import create_builder

builder = create_builder()

cache = builder.add_container("cache", "redis:latest")

wiki = builder.add_dockerfile("wiki", "./src")
wiki.with_external_http_endpoints()
wiki.with_http_endpoint(target_port=8080, env="PORT")

app = builder.build()
app.run()
