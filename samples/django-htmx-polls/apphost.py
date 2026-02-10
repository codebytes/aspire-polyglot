import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".modules"))

from aspire import create_builder

builder = create_builder()

builder.add_python_app("polls", "./src", "run.py") \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()

app = builder.build()
app.run()
