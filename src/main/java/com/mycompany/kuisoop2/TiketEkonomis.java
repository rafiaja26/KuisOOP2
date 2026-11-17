public class TiketEkonomis extends TiketAcara {
    private static final long serialVersionUID = 1L;

    public TiketEkonomis(String kode, String nama, int kuota) {
        super(kode, nama, kuota);
    }

    @Override
    public long getHargaSatuan() { return 50000; }

    @Override
    public String getKategori() { return "Ekonomis"; }
}
