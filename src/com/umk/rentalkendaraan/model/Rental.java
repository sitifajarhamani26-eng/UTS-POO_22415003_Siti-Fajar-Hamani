package com.umk.rentalkendaraan.model;

import com.umk.rentalkendaraan.adt.RiwayatSewaStack;
import java.util.ArrayList;

/** Pengelola koleksi armada, pelanggan, transaksi, dan riwayat LIFO. */
public class Rental {
    private String namaRental;
    private String alamatRental;
    private ArrayList<Kendaraan> daftarKendaraan;
    private ArrayList<Penyewa> daftarPenyewa;
    private ArrayList<Sewa> daftarSewa;
    private RiwayatSewaStack riwayatStack;
    private double totalPendapatan;

    public Rental() { this("Rental Kendaraan Kendari Jaya", "Kendari", RiwayatSewaStack.KAPASITAS_DEFAULT); }
    public Rental(String namaRental, String alamatRental, int kapasitasStack) {
        setNamaRental(namaRental);
        setAlamatRental(alamatRental);
        this.daftarKendaraan = new ArrayList<>();
        this.daftarPenyewa = new ArrayList<>();
        this.daftarSewa = new ArrayList<>();
        this.riwayatStack = new RiwayatSewaStack(kapasitasStack);
        this.totalPendapatan = 0.0;
    }
    public String getNamaRental() { return namaRental; }
    public void setNamaRental(String namaRental) {
        if (namaRental == null || namaRental.trim().isEmpty()) throw new IllegalArgumentException("Nama rental tidak boleh kosong.");
        this.namaRental = namaRental.trim();
    }
    public String getAlamatRental() { return alamatRental; }
    public void setAlamatRental(String alamatRental) {
        if (alamatRental == null || alamatRental.trim().isEmpty()) throw new IllegalArgumentException("Alamat rental tidak boleh kosong.");
        this.alamatRental = alamatRental.trim();
    }
    public double getTotalPendapatan() { return totalPendapatan; }
    public RiwayatSewaStack getRiwayatStack() { return riwayatStack; }

    public void tambahKendaraan(Kendaraan kendaraan) {
        if (kendaraan == null) throw new IllegalArgumentException("Kendaraan tidak boleh null.");
        if (cariKendaraanByKode(kendaraan.getKodeKendaraan()) != null)
            throw new IllegalArgumentException("Kode kendaraan sudah terdaftar.");
        daftarKendaraan.add(kendaraan);
    }
    public void tambahPenyewa(Penyewa penyewa) {
        if (penyewa == null) throw new IllegalArgumentException("Penyewa tidak boleh null.");
        if (cariPenyewaById(penyewa.getIdPenyewa()) != null)
            throw new IllegalArgumentException("ID penyewa sudah terdaftar.");
        daftarPenyewa.add(penyewa);
    }
    public Kendaraan cariKendaraanByKode(String kode) {
        if (kode == null) return null;
        for (Kendaraan k : daftarKendaraan) if (k.getKodeKendaraan().equalsIgnoreCase(kode.trim())) return k;
        return null;
    }
    public Penyewa cariPenyewaById(String id) {
        if (id == null) return null;
        for (Penyewa p : daftarPenyewa) if (p.getIdPenyewa().equalsIgnoreCase(id.trim())) return p;
        return null;
    }
    public ArrayList<Kendaraan> cariKendaraan(String key) {
        ArrayList<Kendaraan> hasil = new ArrayList<>();
        if (key == null) return hasil;
        for (Kendaraan k : daftarKendaraan)
            if (k.getKodeKendaraan().toLowerCase().contains(key.trim().toLowerCase())
                    || k.getNamaKendaraan().toLowerCase().contains(key.trim().toLowerCase())) hasil.add(k);
        return hasil;
    }
    public ArrayList<Penyewa> cariPenyewa(String key) {
        ArrayList<Penyewa> hasil = new ArrayList<>();
        if (key == null) return hasil;
        for (Penyewa p : daftarPenyewa)
            if (p.getIdPenyewa().toLowerCase().contains(key.trim().toLowerCase())
                    || p.getNamaLengkap().toLowerCase().contains(key.trim().toLowerCase())) hasil.add(p);
        return hasil;
    }
    public Sewa buatTransaksiSewa(String kode, String kdKnd, String idPny, int hari) {
        if (kode == null || kode.trim().isEmpty()) throw new IllegalArgumentException("Kode transaksi tidak boleh kosong.");
        for (Sewa s : daftarSewa) if (s.getKodeSewa().equalsIgnoreCase(kode.trim()))
            throw new IllegalArgumentException("Kode transaksi sudah digunakan.");
        Kendaraan kendaraan = cariKendaraanByKode(kdKnd);
        Penyewa penyewa = cariPenyewaById(idPny);
        if (kendaraan == null) throw new IllegalArgumentException("Kendaraan " + kdKnd + " tidak ditemukan.");
        if (penyewa == null) throw new IllegalArgumentException("Penyewa " + idPny + " tidak ditemukan.");
        if (!kendaraan.isTersedia())
            throw new IllegalStateException("ATURAN BISNIS DILANGGAR: Kendaraan [" + kendaraan.getKodeKendaraan()
                    + " - " + kendaraan.getNamaKendaraan() + "] sedang disewa dan tidak dapat disewa lagi!");
        if (hari < 1 || hari > 365) throw new IllegalArgumentException("Lama hari penyewaan harus minimal 1 hari dan maksimal 365 hari.");
        if (!penyewa.isEligibleMenyewa()) throw new IllegalArgumentException("Penyewa nonaktif tidak dapat menyewa.");
        if (riwayatStack.isFull()) {
            System.out.println("[ADT Stack] PERINGATAN: Stack riwayat penuh (maksimal "
                    + riwayatStack.getKapasitas() + " item)");
            throw new IllegalStateException("Stack riwayat penuh; transaksi ditolak.");
        }
        Sewa transaksi = new Sewa(kode, kendaraan, penyewa, hari);
        if (!riwayatStack.push(transaksi)) {
            transaksi.batalkanSewa();
            throw new IllegalStateException("Gagal menyimpan transaksi ke Stack.");
        }
        daftarSewa.add(transaksi);
        totalPendapatan += transaksi.getTotalBiaya();
        return transaksi;
    }
    public Sewa batalkanTransaksiTerakhir() {
        Sewa terakhir = riwayatStack.pop();
        if (terakhir != null && terakhir.isAktif()) {
            double biayaDikembalikan = terakhir.getTotalBiaya();
            terakhir.batalkanSewa();
            totalPendapatan -= biayaDikembalikan;
        }
        return terakhir;
    }
    public void tampilkanSemuaKendaraan() {
        System.out.println("--- DAFTAR ARMADA KENDARAAN ---");
        System.out.println("Kode     | Nama Kendaraan          | Jenis   | Plat Nomor  | Tarif Harian   | Status");
        for (Kendaraan k : daftarKendaraan) k.tampilkanInfo();
    }
    public void tampilkanSemuaPenyewa() {
        System.out.println("--- DAFTAR PENYEWA ---");
        System.out.println("ID       | Nama Lengkap            | Nomor Identitas    | Telepon        | Status   | Transaksi Aktif");
        for (Penyewa p : daftarPenyewa) p.tampilkanInfo();
    }
    public void tampilkanRingkasanLaporan() {
        int aktif = 0, tersedia = 0;
        for (Sewa s : daftarSewa) if (s.isAktif()) aktif++;
        for (Kendaraan k : daftarKendaraan) if (k.isTersedia()) tersedia++;
        System.out.println("=== RINGKASAN LAPORAN RENTAL ===");
        System.out.println("Nama Rental          : " + namaRental);
        System.out.println("Alamat               : " + alamatRental);
        System.out.println("Total Armada         : " + daftarKendaraan.size() + " (Tersedia: " + tersedia + ")");
        System.out.println("Total Penyewa        : " + daftarPenyewa.size());
        System.out.println("Transaksi Aktif      : " + aktif);
        System.out.println("Riwayat Stack        : " + riwayatStack.size() + "/" + riwayatStack.getKapasitas());
        System.out.printf("Total Pendapatan     : Rp %,.0f%n", totalPendapatan);
    }
}
