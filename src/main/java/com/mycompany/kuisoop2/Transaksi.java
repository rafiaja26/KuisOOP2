import java.io.Serializable;

// Association: satu transaksi menghubungkan satu Peserta dengan satu TiketAcara
public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;

    private Peserta peserta;
    private TiketAcara tiket;
    private int qty;
    private long total;

    public Transaksi(Peserta peserta, TiketAcara tiket, int qty) {
        this.peserta = peserta;
        this.tiket = tiket;
        this.qty = qty;
        this.total = tiket.hitungTotal(qty);   // polymorphism: harga tergantung jenis tiket
    }

    public Peserta getPeserta() { return peserta; }
    public TiketAcara getTiket() { return tiket; }
    public int getQty() { return qty; }
    public long getTotal() { return total; }
}
