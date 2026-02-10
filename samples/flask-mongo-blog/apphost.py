import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent / ".modules"))

from aspire import create_builder

builder = create_builder()

mongo = builder.add_mongo_db("mongodb") \
    .with_mongo_express()

db = mongo.add_database("blogdb")

builder.add_python_app("blog", "./src", "main.py") \
    .with_reference(db) \
    .with_http_endpoint(env="PORT") \
    .with_external_http_endpoints()

app = builder.build()
app.run()
