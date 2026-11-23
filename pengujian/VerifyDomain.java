import com.umk.rentalkendaraan.model.*;
import com.umk.rentalkendaraan.adt.RiwayatSewaStack;

/** Uji aturan bisnis dan konsistensi state yang tidak terlihat dari output menu. */
public class VerifyDomain {
    private static int passed = 0;
    private static void check(boolean ok, String label) {
        if (!ok) throw new AssertionError(label);
        passed++;
        System.out.println("PASS " + label);
    }
    private static void rejects(Runnable action, String label) {
        boolean rejected = false;
        try { action.run(); } catch (IllegalArgumentException | IllegalStateException ex) { rejected = true; }
        check(rejected, label);
    }
    public static void main(String[] args) {
        Rental rental = new Rental("Demo", "Kendari", 2);
        Kendaraan motor = new Kendaraan("KND-01", "Motor Demo", "Motor", "DT 1000 AA", 125000);
        Kendaraan mobil = new Kendaraan("KND-02", "Mobil Demo", "Mobil", "DT 1000 BB", 300000);
        Penyewa ani = new Penyewa("PNY-01", "Ani", "DEMO123456", "080000000000");
        Penyewa budi = new Penyewa("PNY-02", "Budi", "DEMO123457", "080000000001");
        rental.tambahKendaraan(motor); rental.tambahKendaraan(mobil);
        rental.tambahPenyewa(ani); rental.tambahPenyewa(budi);
        rejects(() -> rental.tambahKendaraan(new Kendaraan("KND-01", "X", "Motor", "DT 1000 AC", 100)), "kode kendaraan unik");
        rejects(() -> rental.tambahPenyewa(new Penyewa("PNY-01", "X", "DEMO123458", "080000000002")), "id penyewa unik");
        rejects(() -> new Kendaraan("K", "X", "Motor", "D", -1), "tarif positif");
        rejects(() -> new Penyewa("P", "X", "123", "080000000000"), "nomor identitas minimal enam");
        check(rental.cariKendaraan("motor").size() == 1, "pencarian kendaraan");
        Sewa satu = rental.buatTransaksiSewa("SEW-001", "KND-01", "PNY-01", 2);
        check(satu.getTotalBiaya() == 250000 && !motor.isTersedia() && ani.getTotalTransaksi() == 1, "transaksi tarif status penyewa");
        rejects(() -> rental.buatTransaksiSewa("SEW-002", "KND-01", "PNY-02", 2), "double booking ditolak");
        rejects(() -> rental.buatTransaksiSewa("SEW-002", "KND-02", "PNY-02", 0), "hari nol ditolak");
        check(mobil.isTersedia() && budi.getTotalTransaksi() == 0, "penolakan tidak ubah state");
        Sewa dua = rental.buatTransaksiSewa("SEW-002", "KND-02", "PNY-02", 1);
        check(rental.getRiwayatStack().peek() == dua && rental.getRiwayatStack().isFull(), "LIFO peek penuh");
        Kendaraan ekstra = new Kendaraan("KND-03", "Tambahan", "Mobil", "DT 1000 CC", 100000);
        rental.tambahKendaraan(ekstra);
        rejects(() -> rental.buatTransaksiSewa("SEW-003", "KND-03", "PNY-01", 1), "overflow transaksi ditolak");
        check(ekstra.isTersedia() && rental.getTotalPendapatan() == 550000, "overflow tanpa efek samping");
        check(rental.batalkanTransaksiTerakhir() == dua && mobil.isTersedia() && budi.getTotalTransaksi() == 0, "undo paling akhir memulihkan unit penyewa");
        check(rental.getTotalPendapatan() == 250000 && rental.getRiwayatStack().peek() == satu, "undo mengurangi pendapatan dan menjaga urutan");
        check(rental.batalkanTransaksiTerakhir() == satu && motor.isTersedia() && ani.getTotalTransaksi() == 0, "undo kedua");
        check(rental.getRiwayatStack().isEmpty() && rental.getTotalPendapatan() == 0, "stack kosong dan pendapatan nol");
        check(rental.batalkanTransaksiTerakhir() == null, "underflow aman");
        rejects(() -> rental.buatTransaksiSewa("SEW-001", "KND-01", "PNY-01", 1), "kode transaksi bekas tidak diduplikasi");
        ani.setStatusAktif(false);
        rejects(() -> rental.buatTransaksiSewa("SEW-004", "KND-01", "PNY-01", 1), "penyewa nonaktif ditolak");
        check(rental.getRiwayatStack().size() == 0, "kondisi akhir konsisten");
        System.out.println("HASIL: " + passed + " uji domain lulus");
    }
}
