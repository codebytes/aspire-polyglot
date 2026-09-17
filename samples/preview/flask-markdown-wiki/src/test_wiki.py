import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

import main


class MemoryCache:
    def __init__(self):
        self.values = {}

    def get(self, key):
        return self.values.get(key)

    def set(self, key, value, ex):
        self.values[key] = value

    def delete(self, key):
        self.values.pop(key, None)


class WikiTests(unittest.TestCase):
    def setUp(self):
        self.directory = tempfile.TemporaryDirectory()
        self.addCleanup(self.directory.cleanup)
        self.cache = MemoryCache()
        self.db_patch = patch.object(main, "DB_PATH", str(Path(self.directory.name) / "wiki.db"))
        self.cache_patch = patch.object(main, "redis_client", self.cache)
        self.db_patch.start()
        self.cache_patch.start()
        self.addCleanup(self.db_patch.stop)
        self.addCleanup(self.cache_patch.stop)
        main.init_db()
        self.client = main.app.test_client()

    def test_creation_discards_html_left_by_a_previous_database(self):
        self.cache.values["wiki:html:demo"] = "<h1>Old content</h1>"
        response = self.client.post("/new", data={
            "title": "Demo", "content": "# New content",
        }, follow_redirects=True)
        self.assertEqual(response.status_code, 200)
        self.assertIn(b"<h1>New content</h1>", response.data)
        self.assertNotIn(b"<h1>Old content</h1>", response.data)
        self.assertEqual(self.cache.values["wiki:html:demo"], "<h1>New content</h1>")

    def test_edit_refreshes_cached_html(self):
        self.client.get("/page/home")
        response = self.client.post("/page/home", data={
            "content": "# Edited home",
        }, follow_redirects=True)
        self.assertEqual(response.status_code, 200)
        self.assertIn(b"<h1>Edited home</h1>", response.data)
        self.assertEqual(self.cache.values["wiki:html:home"], "<h1>Edited home</h1>")

    def test_duplicate_title_does_not_replace_a_page(self):
        response = self.client.post("/new", data={"title": "Home", "content": "Replacement"})
        self.assertEqual(response.status_code, 400)
        self.assertNotIn(b"Replacement", self.client.get("/page/home").data)


if __name__ == "__main__":
    unittest.main()
