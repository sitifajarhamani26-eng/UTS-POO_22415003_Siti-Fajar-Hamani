"""Jalankan 12 skenario Bab V dari laporan, simpan output program apa adanya."""
from pathlib import Path
import subprocess
import sys

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "out"
SRC = ROOT / "src/com/umk/rentalkendaraan"
RESULTS = ROOT / "pengujian/hasil_console"
java_files = sorted(str(p) for p in SRC.rglob("*.java"))
compile_result = subprocess.run(
    ["javac", "-encoding", "UTF-8", "-d", str(OUT), *java_files],
    capture_output=True, text=True, encoding="utf-8", errors="replace",
)
if compile_result.returncode:
    sys.exit("GAGAL kompilasi:\n" + compile_result.stderr)
RESULTS.mkdir(parents=True, exist_ok=True)
cmd = ["java", "-cp", str(OUT), "com.umk.rentalkendaraan.app.MainRentalApp"]

cases = [
    (1, "Tambah kendaraan", "1\n1\nKND-04\nMitsubishi Xpander\nMobil\nDT 7788 GH\n450000\n7\n", ["Kendaraan KND-04 berhasil ditambahkan", "Mitsubishi Xpander", "TERSEDIA"]),
    (2, "Tambah penyewa", "2\n1\nPNY-03\nRahmawati Putri\nDEMO000003\n080000000003\n7\n", ["Penyewa PNY-03 berhasil didaftarkan", "Rahmawati Putri", "AKTIF", "| 0"]),
    (3, "Sewa tarif dan status", "3\nSEW-001\nKND-01\nPNY-01\n2\n1\n2\n5\n3\n7\n", ["[TRANSAKSI BERHASIL]", "Total Biaya    : Rp 700.000", "Status Kapasitas: 1 / 5", "KND-01   | Toyota Avanza Veloz", "DT 1423 AB  | Rp    350.000 | SEDANG DISEWA"]),

    (4, "Cari dan laporan", "3\nSEW-001\nKND-01\nPNY-01\n2\n4\n1\nAvanza\n6\n7\n", ["Toyota Avanza Veloz", "Transaksi Aktif      : 1", "Total Pendapatan     : Rp 700.000"]),
    (5, "Double booking", "3\nSEW-001\nKND-01\nPNY-01\n2\n3\nSEW-002\nKND-01\nPNY-02\n3\n7\n", ["[TRANSAKSI DITOLAK] ATURAN BISNIS DILANGGAR", "sedang disewa dan tidak dapat disewa lagi!"]),
    (6, "Tarif negatif", "1\n1\nKND-05\nHonda Jazz\nMobil\nDT 1122 ZZ\n-150000\n7\n", ["[ERROR VALIDASI] Tarif harian harus lebih besar dari Rp 0."]),
    (7, "Hari sewa nol", "3\nSEW-002\nKND-02\nPNY-02\n0\n1\n2\n6\n7\n", ["[TRANSAKSI DITOLAK] Lama hari penyewaan harus minimal 1 hari", "KND-02   | Honda Brio Satya", "DT 1822 CD  | Rp    300.000 | TERSEDIA", "Transaksi Aktif      : 0", "Total Pendapatan     : Rp 0"]),
    (8, "Stack kosong", "5\n1\n5\n2\n7\n", ["[ADT Stack] INFO: Stack riwayat masih kosong", "[ADT Stack] GAGAL POP: Stack riwayat kosong"]),
    (9, "LIFO undo", "3\nSEW-001\nKND-01\nPNY-01\n2\n3\nSEW-002\nKND-03\nPNY-02\n1\n5\n1\n5\n2\n5\n3\n6\n7\n", ["TOP -> SEW-001", "[POP/UNDO BERHASIL] SEW-002", "Status Kapasitas: 1 / 5", "Total Pendapatan     : Rp 700.000"]),
    (10, "Stack penuh", "3\nSEW-001\nKND-01\nPNY-01\n1\n3\nSEW-002\nKND-02\nPNY-01\n1\n3\nSEW-003\nKND-03\nPNY-01\n1\n3\nSEW-004\nKND-06\nPNY-02\n1\n3\nSEW-005\nKND-07\nPNY-02\n1\n3\nSEW-006\nKND-08\nPNY-02\n1\n5\n3\n7\n", ["[ADT Stack] PERINGATAN: Stack riwayat penuh (maksimal 5 item)", "[TRANSAKSI DITOLAK] Stack riwayat penuh", "Status Kapasitas: 5 / 5"]),
    (11, "Input huruf", "abc\n3\nSEW-001\nKND-01\nPNY-01\ntiga\n2\n7\n", ["[INPUT TIDAK VALID] Harap masukkan bilangan bulat (angka), bukan huruf/simbol.", "[TRANSAKSI BERHASIL]"]),
    (12, "Input kosong", "1\n1\n\nKND-04\nMitsubishi Xpander\nMobil\nDT 7788 GH\n450000\n7\n", ["[INPUT TIDAK VALID] Input tidak boleh kosong. Silakan ulangi.", "Kendaraan KND-04 berhasil ditambahkan"]),
]

failures = []
for number, label, inputs, expected in cases:
    try:
        result = subprocess.run(cmd, input=inputs, capture_output=True, text=True,
                                encoding="utf-8", errors="replace", timeout=12)
        output = result.stdout
        missing = [fragment for fragment in expected if fragment not in output]
        if result.returncode or missing:
            failures.append(number)
        # Output asli: tidak mengubah angka, nama, atau pesan yang dicetak program.
        (RESULTS / f"skenario_{number:02d}.txt").write_text(
            f"Skenario {number}: {label}\nInput baris demi baris:\n{inputs}"
            f"Exit code: {result.returncode}\nOutput console (stdin piped tidak ter-echo):\n"
            f"{output}\nSTDERR:\n{result.stderr}", encoding="utf-8")
        print(f"{number:02d} {'LULUS' if result.returncode == 0 and not missing else 'GAGAL'} {label}"
              + (f" | tidak ditemukan: {missing}" if missing else ""))
    except subprocess.TimeoutExpired:
        failures.append(number)
        print(f"{number:02d} GAGAL {label}: timeout")
print(f"HASIL: {len(cases) - len(failures)}/{len(cases)} lulus; gagal: {failures}")
sys.exit(bool(failures))
