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

    def test_seeding_discards_home_html_left_by_a_previous_database(self):
        Path(main.DB_PATH).unlink()
        self.cache.values["wiki:html:home"] = "<h1>Previous database</h1>"
        main.init_db()
        response = self.client.get("/page/home")
        self.assertIn(b"Welcome to the Markdown Wiki", response.data)
        self.assertNotIn(b"Previous database", response.data)

    def test_initialization_preserves_an_existing_home_and_its_cache(self):
        self.client.post("/page/home", data={"content": "# My existing home"})
        self.client.get("/page/home")
        main.init_db()
        self.assertEqual(self.cache.values["wiki:html:home"], "<h1>My existing home</h1>")
        self.assertIn(b"My existing home", self.client.get("/page/home").data)

    def test_markdown_removes_active_html_but_preserves_formatting(self):
        response = self.client.post("/page/home", data={
            "content": (
                "# Safe heading\n\n**bold** and [link](https://example.com)\n\n"
                "<script>alert('unsafe')</script>\n"
                '<img src="x" onerror="alert(1)">\n'
                '<a href="javascript:alert(1)">bad link</a>'
            ),
        }, follow_redirects=True)
        self.assertIn(b"<h1>Safe heading</h1>", response.data)
        self.assertIn(b"<strong>bold</strong>", response.data)
        self.assertIn(b'href="https://example.com"', response.data)
        for value in (b"<script", b"onerror", b"javascript:"):
            self.assertNotIn(value, response.data)
            self.assertNotIn(value.decode(), self.cache.values["wiki:html:home"])

    def test_cached_html_is_sanitized_before_display(self):
        self.cache.values["wiki:html:home"] = '<h1>Cached</h1><img src=x onerror="alert(1)">'
        response = self.client.get("/page/home")
        self.assertIn(b"<h1>Cached</h1>", response.data)
        self.assertNotIn(b"onerror", response.data)

    def test_missing_page_escapes_the_slug_in_both_views(self):
        slug = '<img src=x onerror=alert(1)>'
        for suffix in ("", "/edit"):
            response = self.client.get(f"/page/{slug}{suffix}")
            self.assertEqual(response.status_code, 404)
            self.assertNotIn(b"<img", response.data)
            self.assertIn(b"&lt;img", response.data)


if __name__ == "__main__":
    unittest.main()
