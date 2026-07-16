import re
from collections import Counter

from fastapi import FastAPI
from fastapi.responses import HTMLResponse
from pydantic import BaseModel

app = FastAPI(title="Aspire Python Text Analyzer")

# Words-per-minute used for the reading-time estimate.
WPM = 200


class AnalyzeRequest(BaseModel):
    text: str


@app.get("/health")
def health():
    return {"status": "healthy"}


@app.get("/", response_class=HTMLResponse)
def index():
    return PAGE


@app.get("/api/info")
def info():
    return {
        "service": "text-analyzer",
        "language": "python",
        "framework": "fastapi",
        "note": "Runs natively via uvicorn in dev; ships as a Docker image on publish.",
    }


@app.post("/api/analyze")
def analyze(req: AnalyzeRequest):
    text = req.text
    words = re.findall(r"\b\w+\b", text.lower())
    sentences = [s for s in re.split(r"[.!?]+", text) if s.strip()]
    top = Counter(words).most_common(5)

    return {
        "characters": len(text),
        "words": len(words),
        "sentences": len(sentences),
        "unique_words": len(set(words)),
        "reading_time_seconds": round(len(words) / WPM * 60, 1),
        "top_words": [{"word": w, "count": c} for w, c in top],
    }


# A tiny self-contained analyzer page served straight from FastAPI (no extra
# container). It POSTs to /api/analyze and renders the stats.
PAGE = """
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Aspire Python Text Analyzer</title>
  <style>
    :root { font-family: system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; color: #e6e6e6; background: #12141c; }
    body { margin: 0; min-height: 100vh; }
    .wrap { max-width: 640px; margin: 0 auto; padding: 3rem 1.5rem; }
    h1 { margin: 0 0 .25rem; font-size: 1.9rem; }
    .sub { margin: 0 0 1.5rem; color: #9aa0b4; font-size: .95rem; }
    textarea { width: 100%; box-sizing: border-box; min-height: 120px; padding: .8rem; border: 1px solid #2c2f3c; border-radius: 8px; background: #1b1e29; color: #e6e6e6; font-size: 1rem; resize: vertical; }
    button { margin-top: .75rem; padding: .65rem 1.2rem; border: none; border-radius: 8px; background: #4b8bbe; color: #0b0d14; font-weight: 600; font-size: 1rem; cursor: pointer; }
    button:hover { background: #5fa0d4; }
    .stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(120px, 1fr)); gap: .75rem; margin-top: 1.5rem; }
    .card { background: #1b1e29; border: 1px solid #2c2f3c; border-radius: 8px; padding: .9rem; text-align: center; }
    .card .num { font-size: 1.6rem; font-weight: 700; color: #ffd43b; }
    .card .lbl { color: #9aa0b4; font-size: .75rem; text-transform: uppercase; letter-spacing: .04em; margin-top: .25rem; }
    .top { margin-top: 1.25rem; }
    .top h2 { font-size: .8rem; text-transform: uppercase; letter-spacing: .04em; color: #9aa0b4; }
    .top ul { list-style: none; padding: 0; margin: 0; display: flex; flex-wrap: wrap; gap: .5rem; }
    .top li { background: #1b1e29; border: 1px solid #2c2f3c; border-radius: 999px; padding: .3rem .8rem; font-size: .9rem; }
    .top li b { color: #ffd43b; }
  </style>
</head>
<body>
  <main class="wrap">
    <h1>&#128221; Aspire Python Text Analyzer</h1>
    <p class="sub">FastAPI &middot; uvicorn &mdash; runs natively in dev, ships as a Docker image on publish.</p>
    <textarea id="text" placeholder="Paste some text to analyze...">Aspire makes polyglot apps easy. The AppHost is C#, but this service is Python. Easy!</textarea>
    <button id="go">Analyze</button>
    <div id="out"></div>
  </main>
  <script>
    const out = document.getElementById('out');
    async function analyze() {
      const text = document.getElementById('text').value;
      const res = await fetch('/api/analyze', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text })
      });
      const d = await res.json();
      const card = (n, l) => `<div class="card"><div class="num">${n}</div><div class="lbl">${l}</div></div>`;
      const top = (d.top_words || []).map(w => `<li>${w.word} <b>${w.count}</b></li>`).join('');
      out.innerHTML =
        '<div class="stats">' +
          card(d.characters, 'Characters') +
          card(d.words, 'Words') +
          card(d.sentences, 'Sentences') +
          card(d.unique_words, 'Unique') +
          card(d.reading_time_seconds + 's', 'Reading time') +
        '</div>' +
        (top ? '<div class="top"><h2>Top words</h2><ul>' + top + '</ul></div>' : '');
    }
    document.getElementById('go').addEventListener('click', analyze);
    analyze();
  </script>
</body>
</html>
"""

