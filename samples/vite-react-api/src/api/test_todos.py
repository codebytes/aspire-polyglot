import json
import unittest
from unittest.mock import patch

import redis

import main


class MemoryRedis:
    transaction = redis.Redis.transaction

    def __init__(self):
        self.value = None
        self.version = 0
        self.on_read = None

    def get(self, key):
        return self.value

    def set(self, key, value):
        self.value = value
        self.version += 1

    def pipeline(self, *args, **kwargs):
        return MemoryPipeline(self)


class MemoryPipeline:
    def __init__(self, client):
        self.client = client
        self.pending = None

    def __enter__(self):
        return self

    def __exit__(self, *args):
        pass

    def watch(self, key):
        self.version = self.client.version

    def get(self, key):
        value = self.client.get(key)
        callback = self.client.on_read
        if callback:
            self.client.on_read = None
            callback()
        return value

    def multi(self):
        pass

    def set(self, key, value):
        self.pending = (key, value)

    def execute(self):
        if self.version != self.client.version:
            raise redis.WatchError("Concurrent write")
        self.client.set(*self.pending)
        return [True]


class TodoTests(unittest.TestCase):
    def setUp(self):
        self.client = MemoryRedis()
        patcher = patch.object(main, "redis_client", self.client)
        patcher.start()
        self.addCleanup(patcher.stop)

    def test_concurrent_additions_both_survive(self):
        first = main.Todo(id=1, text="first")
        second = main.Todo(id=2, text="second")
        self.client.on_read = lambda: main.add_todo(second)
        main.add_todo(first)
        self.assertEqual(main.list_todos()["todos"], [
            second.model_dump(), first.model_dump(),
        ])

    def test_addition_racing_deletion_does_not_restore_deleted_todo(self):
        old = main.Todo(id=1, text="delete me")
        new = main.Todo(id=2, text="keep me")
        self.client.set(main.TODOS_KEY, json.dumps([old.model_dump()]))
        self.client.on_read = lambda: main.delete_todo(old.id)
        main.add_todo(new)
        self.assertEqual(main.list_todos()["todos"], [new.model_dump()])

    def test_deletion_racing_addition_preserves_new_todo(self):
        old = main.Todo(id=1, text="delete me")
        new = main.Todo(id=2, text="keep me")
        self.client.set(main.TODOS_KEY, json.dumps([old.model_dump()]))
        self.client.on_read = lambda: main.add_todo(new)
        main.delete_todo(old.id)
        self.assertEqual(main.list_todos()["todos"], [new.model_dump()])


if __name__ == "__main__":
    unittest.main()
