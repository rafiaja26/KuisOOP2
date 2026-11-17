import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SistemEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nama;
    private HashMap<String, Peserta> daftarPeserta = new HashMap<>();
    private HashMap<String, TiketAcara> daftarTiket = new HashMap<>();
    private ArrayList<Transaksi> logTransaksi = new ArrayList<>();

    public SistemEvent(String nama) {
        this.nama = nama;
    }

    public void tambahPeserta(String id, String nama) {
        daftarPeserta.put(id, new Peserta(id, nama));
    }

    // Jenis tiket ditentukan dari kata kunci pada nama tiket
    public void tambahTiketAcara(String kode, String nama, int kuota) {
        String n = nama.toLowerCase();
        TiketAcara tiket;
        if (n.contains("premium")) {
            tiket = new TiketPremium(kode, nama, kuota);
        } else if (n.contains("reguler")) {
            tiket = new TiketReguler(kode, nama, kuota);
        } else {
            tiket = new TiketEkonomis(kode, nama, kuota);
        }
        daftarTiket.put(kode, tiket);
    }

    // File tidak ada -> IOException dilempar ke Main (jadi SISTEM FATAL ERROR)
    // Baris yang salah -> ditangkap per baris, program lanjut ke baris berikutnya
    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            int nomor = 0;
            while ((baris = reader.readLine()) != null) {
                nomor++;
                baris = baris.trim();
                if (baris.isEmpty()) continue;

                try {
                    Transaksi trx = prosesBaris(baris);
                    System.out.println("[OK]    Baris " + nomor + ": " + trx.getPeserta().getNama()
                            + " beli " + trx.getQty() + " x " + trx.getTiket().getNama());
                } catch (EventException e) {
                    System.out.println("[GAGAL] Baris " + nomor + ": " + e.getMessage());
                }
            }
        }
    }

    // Contoh baris: ID:ACT-01;KD:OBJ-A;JML:3;
    private Transaksi prosesBaris(String baris) throws EventException {
        // 1. Parsing: pecah per ";" lalu pecah tiap bagian per ":"
        HashMap<String, String> data = new HashMap<>();
        for (String bagian : baris.split(";")) {
            bagian = bagian.trim();
            if (bagian.isEmpty()) continue;
            String[] kv = bagian.split(":", 2);
            if (kv.length != 2) {
                throw new FormatTransaksiException("'" + bagian + "' bukan pasangan KEY:VALUE");
            }
            data.put(kv[0].trim(), kv[1].trim());
        }

        String idPeserta = data.get("ID");
        String kodeTiket = data.get("KD");
        String jml = data.get("JML");
        if (idPeserta == null || kodeTiket == null || jml == null) {
            throw new FormatTransaksiException("field ID, KD, atau JML tidak lengkap");
        }

        int qty;
        try {
            qty = Integer.parseInt(jml);
        } catch (NumberFormatException e) {
            throw new FormatTransaksiException("JML bukan angka: '" + jml + "'");
        }
        if (qty <= 0) {
            throw new FormatTransaksiException("JML harus lebih dari 0");
        }

        // 2. Validasi: peserta & tiket harus ada
        Peserta peserta = daftarPeserta.get(idPeserta);
        if (peserta == null) throw new DataTidakDitemukanException("Peserta", idPeserta);

        TiketAcara tiket = daftarTiket.get(kodeTiket);
        if (tiket == null) throw new DataTidakDitemukanException("Tiket", kodeTiket);

        // 3. Kurangi kuota (bisa melempar KuotaTidakCukupException), lalu catat transaksi
        tiket.kurangiKuota(qty);
        Transaksi trx = new Transaksi(peserta, tiket, qty);
        logTransaksi.add(trx);
        return trx;
    }

    public void simpanLaporanAkhir() {
        System.out.println("--- MENYIMPAN LAPORAN AKHIR ---");

        // Bagian 1: Object Serialization -> backup_data.dat
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("backup_data.dat"))) {
            oos.writeObject(this);
            System.out.println("Serialization berhasil -> backup_data.dat");
        } catch (IOException e) {
            System.out.println("Gagal serialization: " + e.getMessage());
        }

        // Bagian 2: Struk Riwayat Transaksi -> laporan.txt (for biasa + FileWriter)
        try (FileWriter writer = new FileWriter("laporan.txt")) {
            String nl = System.lineSeparator();
            writer.write("===== STRUK RIWAYAT TRANSAKSI - " + nama + " =====" + nl);
            for (int i = 0; i < logTransaksi.size(); i++) {
                Transaksi t = logTransaksi.get(i);
                writer.write((i + 1) + ". "
                        + t.getPeserta().getId() + " (" + t.getPeserta().getNama() + ") | "
                        + t.getTiket().getKode() + " (" + t.getTiket().getNama() + ") | "
                        + "Qty: " + t.getQty() + " | "
                        + "Total: Rp" + t.getTotal() + nl);
            }
            System.out.println("Laporan teks berhasil -> laporan.txt");
        } catch (IOException e) {
            System.out.println("Gagal menulis laporan: " + e.getMessage());
        }
    }
}
