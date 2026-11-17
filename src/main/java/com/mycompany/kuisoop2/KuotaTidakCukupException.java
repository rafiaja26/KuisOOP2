// Dilempar jika jumlah tiket yang diminta melebihi sisa kuota
public class KuotaTidakCukupException extends EventException {
    public KuotaTidakCukupException(String namaTiket, int diminta, int sisa) {
        super("Kuota '" + namaTiket + "' tidak cukup (diminta " + diminta + ", sisa " + sisa + ")");
    }
}
