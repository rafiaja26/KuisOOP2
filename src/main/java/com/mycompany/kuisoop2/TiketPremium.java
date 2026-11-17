public class TiketPremium extends TiketAcara {
    private static final long serialVersionUID = 1L;

    public TiketPremium(String kode, String nama, int kuota) {
        super(kode, nama, kuota);
    }

    @Override
    public long getHargaSatuan() { return 500000; }

    @Override
    public String getKategori() { return "Premium"; }
}
