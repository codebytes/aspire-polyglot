<!-- Keep in sync with AGENTS.md at the repository root. -->

# Agent instructions: Polyglot Aspire

This repository holds the slides and demos for Chris Ayers' talk **Aspire 13: One AppHost, Many Languages (also presented as Polyglot Aspire)**. The deck is published to <https://chris-ayers.com/aspire-polyglot/>.

## Repository layout

- `slides/`: Marp deck, images in `slides/img/`, themes in `slides/themes/` (including the Aspire light/dark themes and `aspire-motion.css`).
- `samples/<name>/`: one runnable sample per folder, each with its own AppHost (`apphost.cs`, `apphost.ts`, or an `AppHost/` project). The catalog is in `docs/samples-and-development.md`.
- `scripts/`: `Test-Samples.ps1` and `smoke-samples.mjs` validate every sample end-to-end; the Linux preview Dockerfile/compose files support it.
- `.squad/`: Squad agent-team state. Leave it alone unless the task is about the Squad setup.

## Slides

- Write the deck in Markdown with [Marp](https://marp.app/). `slides/Slides.md` is the source; `build/` is generated output and is never edited or committed.
- Keep the frontmatter at the top of the deck:

  ```yaml
  ---
  marp: true
  theme: custom-default
  ---
  ```

- Store themes in `slides/themes/` and images and diagram sources in `slides/img/`. Prefer theme classes and CSS variables over inline styles.
- Write speaker notes as HTML comments (`<!-- -->`).
- Render locally with the Marp CLI version the Pages workflow uses (latest):

  ```bash
  npx @marp-team/marp-cli --theme-set slides/themes --html -o build/index.html -- slides/Slides.md
  npx @marp-team/marp-cli --theme-set slides/themes --html --pdf --allow-local-files -o build/Slides.pdf -- slides/Slides.md
  ```

- `.github/workflows/marp-pages.yml` builds the HTML deck and `Slides.pdf` and publishes both to GitHub Pages. If you add slide assets outside `slides/img/` and `slides/themes/`, add them to the workflow's copy step.

## Shared skills

Use the shared skills from [codebytes/skills](https://github.com/codebytes/skills) (for Copilot CLI: `copilot plugin install https://github.com/codebytes/skills`). Don't copy their instructions or scripts into this repository.

| Task                                                                          | Skill               |
| ----------------------------------------------------------------------------- | ------------------- |
| Write or revise slides, notes, order, or layouts                              | `marp-authoring`    |
| Charts, Mermaid diagrams, or other visuals                                    | `marp-visuals`      |
| Editable `.drawio.svg` diagrams (stored in `slides/img/`)                     | `drawio-diagrams`   |
| Check overflow, clipping, and HTML/PDF parity before presenting or publishing | `marp-slide-review` |

## Build and run

```bash
cd samples/<sample> && aspire run
pwsh ./scripts/Test-Samples.ps1          # runtime smoke test for every sample
node scripts/smoke-samples.mjs --list     # list the samples the smoke suite knows
```

## Talk-specific rules

- Add a new sample under `samples/`, list it in `docs/samples-and-development.md`, and make sure the smoke suite covers it.
- Mermaid diagrams: include the Mermaid `<script>` tag once in the deck when diagrams are used.

## Readme

- Keep the readme sections in this order: Slides, Recordings, Resources, Contact.
- When a new recording of the talk is published, add it at the top of `## Recordings` as `- [Event Year](https://www.youtube.com/watch?v=VIDEO_ID)`. When the video's title differs from the talk's, add an em dash and the recorded title after the event. Keep the list newest first.
- The talk also appears on <https://chris-ayers.com/speaking/> (source: `content/speaking.md` in [codebytes/codebytes.github.io](https://github.com/codebytes/codebytes.github.io)); new events and recordings belong there too.

## General

- Keep changes scoped to the request; don't reformat unrelated slides or code.
- Never commit secrets, keys, or connection strings. Demo placeholders must be obviously fake.
