package main.java.com.perpustakaan.desktop.model;

import java.time.LocalDateTime; // Untuk menangani tanggal dan waktu registrasi

public class Anggota {
    private Long id;
    private String nama;
    private String email;
    private String passwordHash; // Menyimpan password yang sudah di-hash
    private String alamat;
    private LocalDateTime tanggalRegistrasi; // Menggunakan LocalDateTime untuk timestamp
    private String role; // Kolom baru untuk role (misal: ADMIN, ANGGOTA)

    // --- Konstruktor ---

    // Konstruktor kosong (default constructor)
    public Anggota() {
    }

    // Konstruktor untuk membuat objek Anggota baru (saat save, id dan tanggal_registrasi di-generate DB)
    public Anggota(String nama, String email, String passwordHash, String alamat, String role) {
        this.nama = nama;
        this.email = email;
        this.passwordHash = passwordHash;
        this.alamat = alamat;
        this.role = role;
    }

    // Konstruktor lengkap (digunakan saat mengambil data dari DB)
    public Anggota(Long id, String nama, String email, String passwordHash, String alamat, LocalDateTime tanggalRegistrasi, String role) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.passwordHash = passwordHash;
        this.alamat = alamat;
        this.tanggalRegistrasi = tanggalRegistrasi;
        this.role = role;
    }

    // --- Getter dan Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public LocalDateTime getTanggalRegistrasi() {
        return tanggalRegistrasi;
    }

    public void setTanggalRegistrasi(LocalDateTime tanggalRegistrasi) {
        this.tanggalRegistrasi = tanggalRegistrasi;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Anda juga bisa menambahkan method toString() untuk debugging
    @Override
    public String toString() {
        return "Anggota{" +
               "id=" + id +
               ", nama='" + nama + '\'' +
               ", email='" + email + '\'' +
               ", alamat='" + alamat + '\'' +
               ", tanggalRegistrasi=" + tanggalRegistrasi +
               ", role='" + role + '\'' +
               '}';
    }
}