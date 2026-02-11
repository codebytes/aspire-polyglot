import os
import json
from typing import Optional
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import redis
import uvicorn

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Connect to Redis using Aspire connection string
redis_conn_str = os.environ.get("ConnectionStrings__cache", "localhost:6379")
redis_client = redis.Redis.from_url(f"redis://{redis_conn_str}", decode_responses=True)

TODOS_KEY = "todos"

class Todo(BaseModel):
    id: int
    text: str
    completed: bool = False

def get_todos():
    cached = redis_client.get(TODOS_KEY)
    if cached:
        return json.loads(cached)
    return []

def save_todos(todos):
    redis_client.set(TODOS_KEY, json.dumps(todos))

@app.get("/api/health")
def health():
    return {"status": "healthy"}

@app.get("/api/todos")
def list_todos():
    todos = get_todos()
    return {"todos": todos}

@app.post("/api/todos")
def add_todo(todo: Todo):
    todos = get_todos()
    todos.append(todo.dict())
    save_todos(todos)
    return {"todo": todo}

@app.delete("/api/todos/{todo_id}")
def delete_todo(todo_id: int):
    todos = get_todos()
    todos = [t for t in todos if t["id"] != todo_id]
    save_todos(todos)
    return {"success": True}

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 8000))
    uvicorn.run(app, host="0.0.0.0", port=port)
