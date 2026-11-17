// Dilempar jika baris di input_transaksi.txt formatnya salah
public class FormatTransaksiException extends EventException {
    public FormatTransaksiException(String pesan) {
        super("Format transaksi salah: " + pesan);
    }
}
