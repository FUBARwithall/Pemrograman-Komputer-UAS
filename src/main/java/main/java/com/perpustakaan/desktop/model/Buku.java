package main.java.com.perpustakaan.desktop.model;

public class Buku {
    private Long id;
    private String judul;
    private String penulis;
    private String penerbit;
    private Integer tahunTerbit; // Menggunakan Integer agar bisa null jika data tidak ada
    private String isbn;
    private Integer jumlahStok; // Menggunakan Integer agar bisa null jika data tidak ada
    private String lokasiCover; // Path file lokal ke gambar sampul buku

    // --- Konstruktor ---

    // Konstruktor kosong
    public Buku() {
    }

    // Konstruktor untuk membuat objek Buku baru (saat save)
    public Buku(String judul, String penulis, String penerbit, Integer tahunTerbit, String isbn, Integer jumlahStok, String lokasiCover) {
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.isbn = isbn;
        this.jumlahStok = jumlahStok;
        this.lokasiCover = lokasiCover;
    }

    // Konstruktor lengkap (digunakan saat mengambil data dari DB)
    public Buku(Long id, String judul, String penulis, String penerbit, Integer tahunTerbit, String isbn, Integer jumlahStok, String lokasiCover) {
        this.id = id;
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.isbn = isbn;
        this.jumlahStok = jumlahStok;
        this.lokasiCover = lokasiCover;
    }

    // --- Getter dan Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public Integer getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(Integer tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getJumlahStok() {
        return jumlahStok;
    }

    public void setJumlahStok(Integer jumlahStok) {
        this.jumlahStok = jumlahStok;
    }

    public String getLokasiCover() {
        return lokasiCover;
    }

    public void setLokasiCover(String lokasiCover) {
        this.lokasiCover = lokasiCover;
    }

    @Override
    public String toString() {
        return "Buku{" +
               "id=" + id +
               ", judul='" + judul + '\'' +
               ", penulis='" + penulis + '\'' +
               ", penerbit='" + penerbit + '\'' +
               ", tahunTerbit=" + tahunTerbit +
               ", isbn='" + isbn + '\'' +
               ", jumlahStok=" + jumlahStok +
               ", lokasiCover='" + lokasiCover + '\'' +
               '}';
    }
}