import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".modules"))

from aspire import create_builder

builder = create_builder()

postgres = builder.add_container("pg", "postgres:latest")
postgres.with_environment("POSTGRES_DB", "pollsdb")
postgres.with_environment("POSTGRES_PASSWORD", "postgres")

polls = builder.add_dockerfile("polls", "./src")
polls.with_external_http_endpoints()
polls.with_http_endpoint(target_port=8080, env="PORT")

app = builder.build()
app.run()
