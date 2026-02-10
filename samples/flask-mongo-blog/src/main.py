import os
import json
from datetime import datetime
from urllib.parse import quote_plus
from flask import Flask, render_template, request, redirect, url_for, jsonify
from pymongo import MongoClient
from bson.objectid import ObjectId
import markdown

app = Flask(__name__)

# Parse MongoDB connection string from Aspire
def get_mongo_client():
    connection_string = os.environ.get('ConnectionStrings__blogdb', 'mongodb://localhost:27017')
    return MongoClient(connection_string)

client = get_mongo_client()
db = client.get_database()
posts_collection = db.posts

# Seed sample data if collection is empty
def seed_data():
    if posts_collection.count_documents({}) == 0:
        sample_posts = [
            {
                "title": "Welcome to Flask + MongoDB Blog",
                "content": """# Welcome!

This is a sample blog post created with **Flask** and **MongoDB**, orchestrated by **.NET Aspire**.

## Features

- Markdown support for rich content
- Tag-based filtering
- Clean, readable design
- MongoDB for flexible document storage

Enjoy exploring this modern blog engine!""",
                "author": "System",
                "tags": ["welcome", "flask", "mongodb"],
                "created_at": datetime.utcnow()
            },
            {
                "title": "Why MongoDB for Blogging?",
                "content": """MongoDB offers several advantages for blog applications:

1. **Flexible Schema** - Add new fields without migrations
2. **Document Model** - Posts are naturally documents
3. **Rich Queries** - Tag arrays, text search, aggregations
4. **Scalability** - Horizontal scaling built-in

The document model maps perfectly to blog posts with varying metadata.""",
                "author": "Tech Writer",
                "tags": ["mongodb", "databases", "architecture"],
                "created_at": datetime.utcnow()
            },
            {
                "title": ".NET Aspire Orchestration",
                "content": """## Aspire Makes It Easy

.NET Aspire provides:

- **Service Discovery** - Components find each other automatically
- **Connection Strings** - Injected via environment variables
- **Development Dashboard** - Monitor all services
- **Multi-Language** - Python, Node.js, .NET all work together

This blog uses `AddPythonApp()` and `AddMongoDB()` with `WithMongoExpress()` for the admin UI.""",
                "author": "DevOps Engineer",
                "tags": ["aspire", "dotnet", "orchestration"],
                "created_at": datetime.utcnow()
            }
        ]
        posts_collection.insert_many(sample_posts)
        print("✓ Seeded 3 sample blog posts")

seed_data()

@app.route('/')
def index():
    page = request.args.get('page', 1, type=int)
    per_page = 10
    skip = (page - 1) * per_page
    
    posts = list(posts_collection.find().sort('created_at', -1).skip(skip).limit(per_page))
    total = posts_collection.count_documents({})
    
    # Add excerpt to each post
    for post in posts:
        post['_id'] = str(post['_id'])
        post['excerpt'] = post['content'][:200] + '...' if len(post['content']) > 200 else post['content']
    
    has_next = (skip + per_page) < total
    has_prev = page > 1
    
    return render_template('index.html', 
                         posts=posts, 
                         page=page,
                         has_next=has_next,
                         has_prev=has_prev)

@app.route('/post/<post_id>')
def view_post(post_id):
    try:
        post = posts_collection.find_one({'_id': ObjectId(post_id)})
        if post:
            post['_id'] = str(post['_id'])
            post['html_content'] = markdown.markdown(post['content'], extensions=['fenced_code', 'tables'])
            return render_template('post.html', post=post)
        return "Post not found", 404
    except:
        return "Invalid post ID", 400

@app.route('/new', methods=['GET'])
def new_post_form():
    return render_template('new.html')

@app.route('/new', methods=['POST'])
def create_post():
    title = request.form.get('title', '').strip()
    content = request.form.get('content', '').strip()
    author = request.form.get('author', '').strip()
    tags_str = request.form.get('tags', '').strip()
    
    if not title or not content or not author:
        return "Title, content, and author are required", 400
    
    tags = [tag.strip() for tag in tags_str.split(',') if tag.strip()]
    
    post = {
        'title': title,
        'content': content,
        'author': author,
        'tags': tags,
        'created_at': datetime.utcnow()
    }
    
    result = posts_collection.insert_one(post)
    return redirect(url_for('view_post', post_id=str(result.inserted_id)))

@app.route('/tag/<tag>')
def view_tag(tag):
    posts = list(posts_collection.find({'tags': tag}).sort('created_at', -1))
    
    for post in posts:
        post['_id'] = str(post['_id'])
        post['excerpt'] = post['content'][:200] + '...' if len(post['content']) > 200 else post['content']
    
    return render_template('tag.html', tag=tag, posts=posts)

@app.route('/api/posts')
def api_posts():
    posts = list(posts_collection.find().sort('created_at', -1).limit(100))
    
    for post in posts:
        post['_id'] = str(post['_id'])
        post['created_at'] = post['created_at'].isoformat()
    
    return jsonify(posts)

@app.route('/health')
def health():
    try:
        # Check MongoDB connection
        client.admin.command('ping')
        return jsonify({'status': 'healthy', 'database': 'connected'}), 200
    except Exception as e:
        return jsonify({'status': 'unhealthy', 'error': str(e)}), 503

if __name__ == '__main__':
    from waitress import serve
    port = int(os.environ.get('PORT', 5000))
    print(f"🚀 Flask blog starting on port {port}")
    serve(app, host='0.0.0.0', port=port)
