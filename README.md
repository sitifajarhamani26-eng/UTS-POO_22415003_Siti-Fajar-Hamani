# Sistem Manajemen Rental Kendaraan — UTS POO

Nama: Siti Fajar Hamani
NIM: 22415003
Topik: Rental Kendaraan (digit terakhir 3)
ADT: Stack berbasis array (digit kedua terakhir 0, 0 mod 3 = 0)

Struktur dan perilaku program ini diselaraskan dengan `Laporan_UTS-POO_22415003_SitiFajarHamanai.docx` di folder induk. Diagram PNG disalin dari gambar yang tertanam pada laporan tersebut. Kode disusun dengan bantuan alat; sumber dan bantuan perlu diungkap secara jujur dalam pengumpulan. Nilai nomor identitas/telepon pada data awal hanyalah data DEMO, bukan identitas asli. Klaim pengujian pada laporan harus dibuktikan dengan menjalankan program sendiri dan screenshot asli.

## Struktur

- `src/com/umk/rentalkendaraan/model/`: Kendaraan, Penyewa, Sewa, Rental
- `src/com/umk/rentalkendaraan/adt/`: RiwayatSewaStack (push, pop, peek, isEmpty, isFull, size)
- `src/com/umk/rentalkendaraan/app/`: MainRentalApp (menu console)
- `diagram/`: diagram kelas dan ADT (PNG)

## Kompilasi dan jalankan

Dari root proyek (JDK 17+):

```bash
javac -encoding UTF-8 -d out src/com/umk/rentalkendaraan/model/*.java src/com/umk/rentalkendaraan/adt/*.java src/com/umk/rentalkendaraan/app/*.java
java -cp out com.umk.rentalkendaraan.app.MainRentalApp
```

Output `out/` diabaikan Git. Data hanya tersimpan selama program berjalan. Program menyediakan menu kendaraan, penyewa, transaksi sewa, pencarian, operasi Stack, dan ringkasan. Transaksi menolak kendaraan yang sedang disewa; total biaya = tarif per hari × lama hari.

## Verifikasi

Dari root proyek dengan JDK dan Python 3 tersedia:

```bash
python3 pengujian/verify_scenarios.py
javac -encoding UTF-8 -cp out -d out pengujian/VerifyDomain.java
java -cp out VerifyDomain
```

Skrip pertama menjalankan 12 alur Bab V laporan dan menyimpan keluaran console apa adanya di `pengujian/hasil_console/`. Skrip kedua menguji state objek (biaya, status kendaraan, jumlah transaksi penyewa, pendapatan, LIFO, overflow, underflow). Folder `berkas screenshoot/` memuat 12 tangkapan layar Windows Terminal untuk 12 skenario. Skenario 06 memperlihatkan keluaran program langsung; skenario lain menampilkan cuplikan baris dari keluaran Java yang dijalankan ulang oleh `pengujian/bukti_screenshot.py`, lengkap dengan nomor baris. Output penuh tetap tersedia dalam `pengujian/hasil_console/`. Gambar ini adalah bukti eksekusi, bukan rekayasa tampilan; cuplikan bukan screenshot sesi input interaktif penuh. Pemilik tugas perlu meninjau dan memasang gambar yang sesuai pada laporan PDF.

Catatan kesesuaian dengan laporan Word: contoh NIK/telepon pada data awal dan skenario 2 diganti data DEMO agar nomor yang menyerupai identitas pribadi tidak disebarkan di repo publik. Tarif NMAX contoh mengikuti diagram ADT (3 hari = Rp 360.000, yaitu Rp 120.000/hari). Output nominal mengikuti locale OS (`700.000` di Windows Indonesia); contoh cuplikan laporan menulis `700,000`. Laporan Word belum diubah; data pribadi, cuplikan output, placeholder screenshot, hash commit, dan link video perlu ditinjau pemilik sebelum mengumpulkan PDF. Diagram di `diagram/` disalin persis dari lampiran Word, bukan gambar baru.
