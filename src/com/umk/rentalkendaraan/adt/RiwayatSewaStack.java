package com.umk.rentalkendaraan.adt;

import com.umk.rentalkendaraan.model.Sewa;

/** ADT Stack berbasis array, LIFO; batas penuh/kosong tidak menyebabkan crash. */
public class RiwayatSewaStack {
    public static final int KAPASITAS_DEFAULT = 5;
    private Sewa[] elemen;
    private int top;
    private int kapasitas;

    public RiwayatSewaStack() { this(KAPASITAS_DEFAULT); }
    public RiwayatSewaStack(int kapasitas) {
        if (kapasitas < 1) throw new IllegalArgumentException("Kapasitas Stack minimal 1.");
        this.kapasitas = kapasitas;
        this.elemen = new Sewa[kapasitas];
        this.top = -1;
    }
    public boolean isEmpty() { return top == -1; }
    public boolean isFull() { return top == kapasitas - 1; }
    public int size() { return top + 1; }
    public int getKapasitas() { return kapasitas; }
    public boolean push(Sewa transaksi) {
        if (transaksi == null) {
            System.out.println("[ADT Stack] GAGAL PUSH: transaksi null.");
            return false;
        }
        if (isFull()) {
            System.out.println("[ADT Stack] PERINGATAN: Stack riwayat penuh (maksimal " + kapasitas + " item)");
            return false;
        }
        elemen[++top] = transaksi;
        return true;
    }
    public Sewa pop() {
        if (isEmpty()) {
            System.out.println("[ADT Stack] GAGAL POP: Stack riwayat kosong");
            return null;
        }
        Sewa hasil = elemen[top];
        elemen[top--] = null;
        return hasil;
    }
    public Sewa peek() {
        if (isEmpty()) {
            System.out.println("[ADT Stack] INFO: Stack riwayat masih kosong");
            return null;
        }
        return elemen[top];
    }
    public void tampilkanIsiStack() {
        System.out.println("=== ISI ADT STACK RIWAYAT TRANSAKSI SEWA (LIFO) ===");
        System.out.printf("Status Kapasitas: %d / %d | isEmpty=%s | isFull=%s%n",
                size(), kapasitas, isEmpty(), isFull());
        if (isEmpty()) System.out.println("[ADT Stack] INFO: Stack riwayat masih kosong");
        for (int i = top; i >= 0; i--) {
            System.out.print(i == top ? "TOP -> " : "       ");
            elemen[i].tampilkanInfo();
        }
    }
}
