package com.umk.rentalkendaraan.model;

/** Data pelanggan dan jumlah penyewaan aktif yang tercatat. */
public class Penyewa {
    private String idPenyewa;
    private String namaLengkap;
    private String nomorIdentitas;
    private String nomorTelepon;
    private int totalTransaksi;
    private boolean statusAktif;

    public Penyewa() {
        this("PNY-00", "Penyewa Umum", "000000", "080000000000");
    }
    public Penyewa(String idPenyewa, String namaLengkap, String nomorIdentitas, String nomorTelepon) {
        setIdPenyewa(idPenyewa);
        setNamaLengkap(namaLengkap);
        setNomorIdentitas(nomorIdentitas);
        setNomorTelepon(nomorTelepon);
        this.totalTransaksi = 0;
        this.statusAktif = true;
    }
    private static String wajibIsi(String nilai, String label) {
        if (nilai == null || nilai.trim().isEmpty()) throw new IllegalArgumentException(label + " tidak boleh kosong.");
        return nilai.trim();
    }
    public String getIdPenyewa() { return idPenyewa; }
    public void setIdPenyewa(String idPenyewa) { this.idPenyewa = wajibIsi(idPenyewa, "ID penyewa").toUpperCase(); }
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = wajibIsi(namaLengkap, "Nama penyewa"); }
    public String getNomorIdentitas() { return nomorIdentitas; }
    public void setNomorIdentitas(String nomorIdentitas) {
        String identitas = wajibIsi(nomorIdentitas, "Nomor identitas");
        if (identitas.length() < 6) throw new IllegalArgumentException("Nomor identitas minimal 6 karakter.");
        this.nomorIdentitas = identitas;
    }
    public String getNomorTelepon() { return nomorTelepon; }
    public void setNomorTelepon(String nomorTelepon) {
        String telepon = wajibIsi(nomorTelepon, "Nomor telepon");
        if (telepon.length() < 8 || !telepon.matches("[0-9]+"))
            throw new IllegalArgumentException("Nomor telepon minimal 8 digit angka.");
        this.nomorTelepon = telepon;
    }
    public int getTotalTransaksi() { return totalTransaksi; }
    public void setTotalTransaksi(int totalTransaksi) {
        if (totalTransaksi < 0) throw new IllegalArgumentException("Total transaksi tidak boleh negatif.");
        this.totalTransaksi = totalTransaksi;
    }
    public boolean isStatusAktif() { return statusAktif; }
    public void setStatusAktif(boolean statusAktif) { this.statusAktif = statusAktif; }
    public void tambahJumlahTransaksi() { this.totalTransaksi++; }
    public void kurangiJumlahTransaksi() {
        if (totalTransaksi > 0) this.totalTransaksi--;
    }
    public boolean isEligibleMenyewa() { return statusAktif; }
    public void tampilkanInfo() {
        System.out.printf("%-8s | %-23s | %-18s | %-14s | %-8s | %d%n",
                idPenyewa, namaLengkap, nomorIdentitas, nomorTelepon,
                statusAktif ? "AKTIF" : "NONAKTIF", totalTransaksi);
    }
}
