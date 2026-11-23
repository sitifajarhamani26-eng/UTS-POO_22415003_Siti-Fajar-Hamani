package com.umk.rentalkendaraan.model;

/** Transaksi satu penyewa terhadap satu kendaraan dengan biaya otomatis. */
public class Sewa {
    private String kodeSewa;
    private Kendaraan kendaraan;
    private Penyewa penyewa;
    private int lamaHari;
    private double totalBiaya;
    private boolean aktif;

    public Sewa() {
        this.kodeSewa = "SEW-000";
        this.lamaHari = 1;
        this.totalBiaya = 0.0;
        this.aktif = false;
    }
    public Sewa(String kodeSewa, Kendaraan kendaraan, Penyewa penyewa, int lamaHari) {
        setKodeSewa(kodeSewa);
        setKendaraan(kendaraan);
        setPenyewa(penyewa);
        setLamaHari(lamaHari);
        this.totalBiaya = hitungTotalBiaya();
        prosesMulaiSewa();
    }
    public String getKodeSewa() { return kodeSewa; }
    public void setKodeSewa(String kodeSewa) {
        if (kodeSewa == null || kodeSewa.trim().isEmpty())
            throw new IllegalArgumentException("Kode transaksi tidak boleh kosong.");
        this.kodeSewa = kodeSewa.trim().toUpperCase();
    }
    public Kendaraan getKendaraan() { return kendaraan; }
    public void setKendaraan(Kendaraan kendaraan) {
        if (kendaraan == null) throw new IllegalArgumentException("Kendaraan tidak boleh null.");
        if (aktif) throw new IllegalStateException("Kendaraan transaksi aktif tidak boleh diganti.");
        this.kendaraan = kendaraan;
    }
    public Penyewa getPenyewa() { return penyewa; }
    public void setPenyewa(Penyewa penyewa) {
        if (penyewa == null) throw new IllegalArgumentException("Penyewa tidak boleh null.");
        if (aktif) throw new IllegalStateException("Penyewa transaksi aktif tidak boleh diganti.");
        this.penyewa = penyewa;
    }
    public int getLamaHari() { return lamaHari; }
    public void setLamaHari(int lamaHari) {
        if (lamaHari < 1 || lamaHari > 365)
            throw new IllegalArgumentException("Lama hari penyewaan harus minimal 1 hari dan maksimal 365 hari.");
        if (aktif) throw new IllegalStateException("Durasi transaksi aktif tidak boleh diganti.");
        this.lamaHari = lamaHari;
    }
    public double getTotalBiaya() { return totalBiaya; }
    public boolean isAktif() { return aktif; }
    public double hitungTotalBiaya() {
        if (kendaraan == null) return 0.0;
        this.totalBiaya = kendaraan.getTarifHarian() * this.lamaHari;
        return this.totalBiaya;
    }
    public void prosesMulaiSewa() {
        if (aktif) throw new IllegalStateException("Transaksi sudah aktif.");
        if (kendaraan == null || penyewa == null) throw new IllegalStateException("Transaksi belum lengkap.");
        if (!penyewa.isEligibleMenyewa()) throw new IllegalArgumentException("Penyewa nonaktif tidak dapat menyewa.");
        kendaraan.tandaiDisewa();
        penyewa.tambahJumlahTransaksi();
        this.aktif = true;
    }
    public void batalkanSewa() {
        if (aktif) {
            kendaraan.tandaiTersedia();
            penyewa.kurangiJumlahTransaksi();
            this.aktif = false;
        }
    }
    public void tampilkanInfo() {
        System.out.printf("%-8s | %-24s | %-23s | %2d hari | Rp %,11.0f | %s%n",
                kodeSewa, kendaraan == null ? "-" : kendaraan.getNamaKendaraan(),
                penyewa == null ? "-" : penyewa.getNamaLengkap(), lamaHari, totalBiaya,
                aktif ? "AKTIF" : "BATAL/SELESAI");
    }
}
