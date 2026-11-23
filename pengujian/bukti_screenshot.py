"""Jalankan aplikasi Java per skenario dan tampilkan cuplikan output asli di console."""
from pathlib import Path
import ast
import subprocess
import sys

root = Path(__file__).resolve().parents[1]
number = int(sys.argv[1])
source = ast.parse((root / 'pengujian/verify_scenarios.py').read_text(encoding='utf-8'))
cases = next(ast.literal_eval(node.value) for node in source.body
             if isinstance(node, ast.Assign) and any(isinstance(t, ast.Name) and t.id == 'cases' for t in node.targets))
num, label, inputs, expected = cases[number - 1]
assert num == number
cmd = ['java', '-cp', str(root / 'out'), 'com.umk.rentalkendaraan.app.MainRentalApp']
print(f'SKENARIO {number:02d} | {label}', flush=True)
print('Jalankan:', ' '.join(cmd), flush=True)
result = subprocess.run(cmd, input=inputs, capture_output=True, text=True,
                        encoding='utf-8', errors='replace', timeout=15)
log = root / 'berkas screenshoot' / f'output_asli_{number:02d}.txt'
log.write_text(result.stdout + ('\nSTDERR:\n' + result.stderr if result.stderr else ''), encoding='utf-8')
lines = result.stdout.splitlines()
indices = set(range(min(4, len(lines))))
for fragment in expected:
    matches = [i for i, line in enumerate(lines) if fragment in line]
    if not matches:
        print('GAGAL: keluaran yang dicari tidak ditemukan:', fragment, flush=True)
        sys.exit(1)
    for i in matches[-2:]:
        indices.update(range(max(0, i - 1), min(len(lines), i + 2)))
if result.returncode:
    print('GAGAL Java exit:', result.returncode, result.stderr, flush=True)
    sys.exit(1)
print('--- CUPLIKAN BARIS OUTPUT ASLI PROGRAM (nomor baris log) ---', flush=True)
last = -2
for i in sorted(indices):
    if i > last + 1:
        print('...', flush=True)
    print(f'{i + 1:03d} | {lines[i]}', flush=True)
    last = i
print(f'--- LULUS: {len(expected)} pemeriksaan; log lengkap: output_asli_{number:02d}.txt ---', flush=True)
