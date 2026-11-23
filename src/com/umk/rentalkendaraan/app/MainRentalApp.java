package com.umk.rentalkendaraan.app;

import com.umk.rentalkendaraan.model.Kendaraan;
import com.umk.rentalkendaraan.model.Penyewa;
import com.umk.rentalkendaraan.model.Rental;
import com.umk.rentalkendaraan.model.Sewa;
import com.umk.rentalkendaraan.adt.RiwayatSewaStack;
import java.util.Scanner;

/** Antarmuka console tujuh menu untuk sistem rental kendaraan. */
public class MainRentalApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static Rental rental;

    public static void main(String[] args) {
        rental = new Rental("Rental Kendaraan Kendari Jaya", "Kendari", 5);
        inisialisasiDataAwal();
        while (true) {
            tampilkanMenuUtama();
            int pilihan = bacaInteger("Pilih menu (1-7): ");
            switch (pilihan) {
                case 1: menuKendaraan(); break;
                case 2: menuPenyewa(); break;
                case 3: menuBuatTransaksiSewa(); break;
                case 4: menuCariData(); break;
                case 5: menuOperasiAdtStack(); break;
                case 6: rental.tampilkanRingkasanLaporan(); break;
                case 7: System.out.println("Program selesai."); return;
                default: System.out.println("[INPUT TIDAK VALID] Pilih menu 1 sampai 7.");
            }
        }
    }
    private static void inisialisasiDataAwal() {
        rental.tambahKendaraan(new Kendaraan("KND-01", "Toyota Avanza Veloz", "Mobil", "DT 1423 AB", 350000));
        rental.tambahKendaraan(new Kendaraan("KND-02", "Honda Brio Satya", "Mobil", "DT 1822 CD", 300000));
        rental.tambahKendaraan(new Kendaraan("KND-03", "Yamaha NMAX 155", "Motor", "DT 3112 EF", 120000));
        rental.tambahKendaraan(new Kendaraan("KND-06", "Suzuki Ertiga", "Mobil", "DT 6001 GH", 400000));
        rental.tambahKendaraan(new Kendaraan("KND-07", "Daihatsu Xenia", "Mobil", "DT 7001 IJ", 275000));
        rental.tambahKendaraan(new Kendaraan("KND-08", "Honda Vario", "Motor", "DT 8001 KL", 100000));
        // Nomor identitas dan telepon ini data contoh, bukan data pribadi mahasiswa.
        rental.tambahPenyewa(new Penyewa("PNY-01", "Siti Fajar Hamanai", "DEMO000001", "080000000001"));
        rental.tambahPenyewa(new Penyewa("PNY-02", "Ahmad Fauzan", "DEMO000002", "080000000002"));
    }
    private static void tampilkanMenuUtama() {
        System.out.println("\n=======================================================");
        System.out.println("   SISTEM MANAJEMEN RENTAL KENDARAAN (ADT: STACK)");
        System.out.println("   Mahasiswa: Siti Fajar Hamanai | NIM: 22415003");
        System.out.println("=======================================================");
        System.out.println("1. Tambah / tampil kendaraan");
        System.out.println("2. Tambah / tampil penyewa");
        System.out.println("3. Buat transaksi sewa");
        System.out.println("4. Cari data");
        System.out.println("5. Operasi Stack (status, peek, pop)");
        System.out.println("6. Tampilkan ringkasan laporan");
        System.out.println("7. Keluar");
    }
    private static void menuKendaraan() {
        System.out.println("--- TAMBAH KENDARAAN ---");
        System.out.println("1. Tambah dan tampilkan | 2. Hanya tampilkan");
        int aksi = bacaInteger("Pilih: ");
        if (aksi == 1) {
            try {
                String kode = bacaStringTidakKosong("Kode Kendaraan: ");
                String nama = bacaStringTidakKosong("Nama Kendaraan: ");
                String jenis = bacaStringTidakKosong("Jenis Kendaraan: ");
                String plat = bacaStringTidakKosong("Plat Nomor: ");
                double tarif = bacaDouble("Tarif Harian: ");
                Kendaraan k = new Kendaraan(kode, nama, jenis, plat, tarif);
                rental.tambahKendaraan(k);
                System.out.println("Kendaraan " + k.getKodeKendaraan() + " berhasil ditambahkan.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("[ERROR VALIDASI] " + e.getMessage());
            }
        }
        rental.tampilkanSemuaKendaraan();
    }
    private static void menuPenyewa() {
        System.out.println("--- TAMBAH PENYEWA ---");
        System.out.println("1. Tambah dan tampilkan | 2. Hanya tampilkan");
        int aksi = bacaInteger("Pilih: ");
        if (aksi == 1) {
            try {
                String id = bacaStringTidakKosong("ID Penyewa: ");
                String nama = bacaStringTidakKosong("Nama Lengkap: ");
                String nik = bacaStringTidakKosong("Nomor Identitas: ");
                String telp = bacaStringTidakKosong("Nomor Telepon: ");
                Penyewa p = new Penyewa(id, nama, nik, telp);
                rental.tambahPenyewa(p);
                System.out.println("Penyewa " + p.getIdPenyewa() + " berhasil didaftarkan.");
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR VALIDASI] " + e.getMessage());
            }
        }
        rental.tampilkanSemuaPenyewa();
    }
    private static void menuBuatTransaksiSewa() {
        System.out.println("--- BUAT TRANSAKSI PENYEWAAN BARU ---");
        rental.tampilkanSemuaKendaraan();
        rental.tampilkanSemuaPenyewa();
        try {
            String kode = bacaStringTidakKosong("Masukkan Kode Transaksi (misal SEW-001): ");
            String kendaraan = bacaStringTidakKosong("Masukkan Kode Kendaraan: ");
            String penyewa = bacaStringTidakKosong("Masukkan ID Penyewa: ");
            int hari = bacaInteger("Masukkan Lama Sewa (hari): ");
            Sewa transaksi = rental.buatTransaksiSewa(kode, kendaraan, penyewa, hari);
            System.out.println("[TRANSAKSI BERHASIL]");
            System.out.println("Kode Transaksi : " + transaksi.getKodeSewa());
            System.out.println("Kendaraan      : " + transaksi.getKendaraan().getNamaKendaraan() + " (" + transaksi.getKendaraan().getPlatNomor() + ")");
            System.out.println("Penyewa        : " + transaksi.getPenyewa().getNamaLengkap());
            System.out.println("Durasi         : " + transaksi.getLamaHari() + " hari");
            System.out.printf("Total Biaya    : Rp %,.0f (Tarif Rp %,.0f x %d hari)%n",
                    transaksi.getTotalBiaya(), transaksi.getKendaraan().getTarifHarian(), transaksi.getLamaHari());
            System.out.println("Info ADT       : Transaksi otomatis di-PUSH ke Stack riwayat.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[TRANSAKSI DITOLAK] " + e.getMessage());
        }
    }
    private static void menuCariData() {
        System.out.println("1. Cari Kendaraan | 2. Cari Penyewa");
        int aksi = bacaInteger("Pilih: ");
        String kataKunci = bacaStringTidakKosong("Kata kunci: ");
        if (aksi == 1) {
            for (Kendaraan k : rental.cariKendaraan(kataKunci)) k.tampilkanInfo();
        } else if (aksi == 2) {
            for (Penyewa p : rental.cariPenyewa(kataKunci)) p.tampilkanInfo();
        } else System.out.println("[INPUT TIDAK VALID] Kategori pencarian tidak tersedia.");
    }
    private static void menuOperasiAdtStack() {
        RiwayatSewaStack stack = rental.getRiwayatStack();
        System.out.println("1. PEEK | 2. POP/Undo | 3. Tampilkan isi/status");
        int aksi = bacaInteger("Pilih operasi Stack: ");
        if (aksi == 1) {
            Sewa puncak = stack.peek();
            if (puncak != null) puncak.tampilkanInfo();
        } else if (aksi == 2) {
            Sewa dibatalkan = rental.batalkanTransaksiTerakhir();
            if (dibatalkan != null) {
                System.out.println("[POP/UNDO BERHASIL] " + dibatalkan.getKodeSewa());
                stack.tampilkanIsiStack();
            }
        } else if (aksi == 3) stack.tampilkanIsiStack();
        else System.out.println("[INPUT TIDAK VALID] Pilihan operasi tidak tersedia.");
    }
    private static int bacaInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("[INPUT TIDAK VALID] Harap masukkan bilangan bulat (angka), bukan huruf/simbol.");
            }
        }
    }
    private static double bacaDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("[INPUT TIDAK VALID] Harap masukkan angka tarif yang valid.");
            }
        }
    }
    private static String bacaStringTidakKosong(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("[INPUT TIDAK VALID] Input tidak boleh kosong. Silakan ulangi.");
        }
    }
}
