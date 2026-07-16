import { useEffect, useState } from 'react';
import './App.css';

interface Quote {
  id: number;
  text: string;
  author: string;
  createdAt: string;
}

export default function App() {
  const [quotes, setQuotes] = useState<Quote[]>([]);
  const [text, setText] = useState('');
  const [author, setAuthor] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  async function load() {
    try {
      const res = await fetch('/api/quotes');
      if (!res.ok) throw new Error(`GET /api/quotes -> ${res.status}`);
      setQuotes(await res.json());
      setError(null);
    } catch (e) {
      setError(e instanceof Error ? e.message : String(e));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function add(e: React.FormEvent) {
    e.preventDefault();
    if (!text.trim() || !author.trim()) return;
    const res = await fetch('/api/quotes', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text, author }),
    });
    if (res.ok) {
      setText('');
      setAuthor('');
      await load();
    } else {
      setError(`POST /api/quotes -> ${res.status}`);
    }
  }

  return (
    <main className="app">
      <h1>Quotes Board</h1>
      <p className="sub">
        .NET Minimal API + PostgreSQL, orchestrated by a single-file C#{' '}
        <code>apphost.cs</code>.
      </p>

      <form className="new-quote" onSubmit={add}>
        <input
          placeholder="Quote"
          value={text}
          onChange={(e) => setText(e.target.value)}
        />
        <input
          placeholder="Author"
          value={author}
          onChange={(e) => setAuthor(e.target.value)}
        />
        <button type="submit">Add</button>
      </form>

      {error && <p className="error">{error}</p>}
      {loading ? (
        <p>Loading…</p>
      ) : (
        <ul className="quotes">
          {quotes.map((q) => (
            <li key={q.id}>
              <blockquote>“{q.text}”</blockquote>
              <cite>— {q.author}</cite>
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}
