package com.umk.rentalkendaraan.model;

/** Unit armada rental dengan status ketersediaan yang dapat berubah. */
public class Kendaraan {
    private String kodeKendaraan;
    private String namaKendaraan;
    private String jenisKendaraan;
    private String platNomor;
    private double tarifHarian;
    private boolean tersedia;

    public Kendaraan() {
        this.kodeKendaraan = "KND-00";
        this.namaKendaraan = "Belum Ditentukan";
        this.jenisKendaraan = "Mobil";
        this.platNomor = "DT 0000 XX";
        this.tarifHarian = 100000.0;
        this.tersedia = true;
    }

    public Kendaraan(String kodeKendaraan, String namaKendaraan, String jenisKendaraan,
                     String platNomor, double tarifHarian) {
        setKodeKendaraan(kodeKendaraan);
        setNamaKendaraan(namaKendaraan);
        setJenisKendaraan(jenisKendaraan);
        setPlatNomor(platNomor);
        setTarifHarian(tarifHarian);
        this.tersedia = true;
    }

    private static String wajibIsi(String nilai, String label) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " tidak boleh kosong.");
        }
        return nilai.trim();
    }

    public String getKodeKendaraan() { return kodeKendaraan; }
    public void setKodeKendaraan(String kodeKendaraan) {
        this.kodeKendaraan = wajibIsi(kodeKendaraan, "Kode kendaraan").toUpperCase();
    }
    public String getNamaKendaraan() { return namaKendaraan; }
    public void setNamaKendaraan(String namaKendaraan) {
        this.namaKendaraan = wajibIsi(namaKendaraan, "Nama kendaraan");
    }
    public String getJenisKendaraan() { return jenisKendaraan; }
    public void setJenisKendaraan(String jenisKendaraan) {
        this.jenisKendaraan = wajibIsi(jenisKendaraan, "Jenis kendaraan");
    }
    public String getPlatNomor() { return platNomor; }
    public void setPlatNomor(String platNomor) {
        this.platNomor = wajibIsi(platNomor, "Plat nomor").toUpperCase();
    }
    public double getTarifHarian() { return tarifHarian; }
    public void setTarifHarian(double tarifHarian) {
        if (!Double.isFinite(tarifHarian) || tarifHarian <= 0) {
            throw new IllegalArgumentException("Tarif harian harus lebih besar dari Rp 0.");
        }
        this.tarifHarian = tarifHarian;
    }
    public boolean isTersedia() { return tersedia; }
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }

    public void tandaiDisewa() {
        if (!tersedia) throw new IllegalStateException("Kendaraan " + kodeKendaraan + " sedang disewa!");
        this.tersedia = false;
    }
    public void tandaiTersedia() { this.tersedia = true; }
    public double hitungEstimasiBiaya(int lamaHari) {
        if (lamaHari < 1) throw new IllegalArgumentException("Lama hari penyewaan harus minimal 1 hari.");
        return tarifHarian * lamaHari;
    }
    public void tampilkanInfo() {
        System.out.printf("%-8s | %-23s | %-7s | %-11s | Rp %,10.0f | %s%n",
                kodeKendaraan, namaKendaraan, jenisKendaraan, platNomor, tarifHarian,
                tersedia ? "TERSEDIA" : "SEDANG DISEWA");
    }
}
