public class TiketReguler extends TiketAcara {
    private static final long serialVersionUID = 1L;

    public TiketReguler(String kode, String nama, int kuota) {
        super(kode, nama, kuota);
    }

    @Override
    public long getHargaSatuan() { return 150000; }

    @Override
    public String getKategori() { return "Reguler"; }
}
