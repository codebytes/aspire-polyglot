import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".aspire/modules"))

from aspire_app import create_builder

with create_builder() as builder:
    cache = builder.add_redis("cache")

    wiki = builder.add_dockerfile("wiki", "./src")
    wiki.with_reference(cache)
    # Wait for Redis to be healthy before starting Flask. Prevents cold-start race where
    # the app could launch before Redis is reachable. with_reference() only injects the
    # connection string — it does NOT order startup.
    wiki.wait_for(cache)
    wiki.with_otlp_exporter()
    wiki.with_external_http_endpoints()
    wiki.with_http_endpoint(target_port=8080, env="PORT")

    app = builder.build()
    app.run()
