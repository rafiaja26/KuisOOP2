import java.io.Serializable;

// Class induk (abstract): data & perilaku yang sama untuk semua jenis tiket
public abstract class TiketAcara implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kode;
    private String nama;
    private int kuota;

    public TiketAcara(String kode, String nama, int kuota) {
        this.kode = kode;
        this.nama = nama;
        this.kuota = kuota;
    }

    // Abstract: tiap jenis tiket WAJIB menentukan harganya sendiri
    public abstract long getHargaSatuan();
    public abstract String getKategori();

    public long hitungTotal(int qty) {
        return getHargaSatuan() * qty;
    }

    // Kuota hanya boleh berkurang lewat method ini (encapsulation)
    public void kurangiKuota(int qty) throws KuotaTidakCukupException {
        if (qty > kuota) {
            throw new KuotaTidakCukupException(nama, qty, kuota);
        }
        kuota -= qty;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public int getKuota() { return kuota; }
}
