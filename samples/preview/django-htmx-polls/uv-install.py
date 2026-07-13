# Creates a venv and installs dependencies with uv.
from __future__ import annotations

import os
import subprocess
import sys
from pathlib import Path


def run(command: list[str]) -> None:
    result = subprocess.run(command)
    if result.returncode != 0:
        sys.exit(result.returncode)


root = Path(__file__).resolve().parent
venv_dir = root / ".venv"
python_path = venv_dir / ("Scripts" if os.name == "nt" else "bin") / (
    "python.exe" if os.name == "nt" else "python"
)

if not python_path.exists():
    run(["uv", "venv", str(venv_dir)])

run(["uv", "pip", "install", "-r", "requirements.txt", "--python", str(python_path)])
