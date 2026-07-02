import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".aspire/modules"))

from aspire_app import create_builder

with create_builder() as builder:
    # PostgreSQL server + database resource. add_postgres() + add_database() is the
    # canonical Aspire pattern: it auto-publishes ConnectionStrings__pollsdb into
    # any container that calls with_reference(pollsdb), so Django picks it up via
    # the parser in pollsite/settings.py. The prior add_container("pg", ...) form
    # never injected the connection string and Django silently fell back to SQLite.
    postgres = builder.add_postgres("pg")
    pollsdb = postgres.add_database("pollsdb")

    polls = builder.add_dockerfile("polls", "./src")
    polls.with_reference(pollsdb)
    # Wait for Postgres to be healthy before starting Django. Without this, on cold launch
    # the polls container can start before pg is reachable, causing migrate to fail with
    # "could not translate host name pg.dev.internal" (DNS race). with_reference() only
    # injects the connection string — it does NOT order startup.
    polls.wait_for(pollsdb)
    polls.with_otlp_exporter()
    polls.with_external_http_endpoints()
    polls.with_http_endpoint(target_port=8080, env="PORT")

    app = builder.build()
    app.run()
