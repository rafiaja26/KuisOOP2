// Dilempar jika ID peserta atau kode tiket tidak terdaftar
public class DataTidakDitemukanException extends EventException {
    public DataTidakDitemukanException(String jenis, String kode) {
        super(jenis + " dengan kode '" + kode + "' tidak ditemukan");
    }
}
