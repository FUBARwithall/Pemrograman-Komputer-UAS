package main.java.com.perpustakaan.desktop.dao;

import main.java.com.perpustakaan.desktop.model.Anggota;
import main.java.com.perpustakaan.desktop.util.DatabaseUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Untuk hashing password
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaDAO {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Menyimpan anggota baru ke database.
     * Password akan di-hash sebelum disimpan.
     * @param anggota Objek Anggota yang akan disimpan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean save(Anggota anggota) {
        String sql = "INSERT INTO anggota (nama, email, password_hash, alamat, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, anggota.getNama());
            stmt.setString(2, anggota.getEmail());
            stmt.setString(3, passwordEncoder.encode(anggota.getPasswordHash())); // Hash password
            stmt.setString(4, anggota.getAlamat());
            stmt.setString(5, anggota.getRole());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saving anggota: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil semua anggota dari database.
     * @return List of Anggota objects.
     */
    public List<Anggota> getAll() {
        List<Anggota> anggotaList = new ArrayList<>();
        String sql = "SELECT id, nama, email, password_hash, alamat, tanggal_registrasi, role FROM anggota";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Anggota anggota = new Anggota();
                anggota.setId(rs.getLong("id"));
                anggota.setNama(rs.getString("nama"));
                anggota.setEmail(rs.getString("email"));
                anggota.setPasswordHash(rs.getString("password_hash")); // Ini adalah hash-nya, bukan password asli
                anggota.setAlamat(rs.getString("alamat"));
                anggota.setTanggalRegistrasi(rs.getTimestamp("tanggal_registrasi").toLocalDateTime());
                anggota.setRole(rs.getString("role"));
                anggotaList.add(anggota);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all anggota: " + e.getMessage());
            e.printStackTrace();
        }
        return anggotaList;
    }

    /**
     * Mencari anggota berdasarkan ID.
     * @param id ID anggota.
     * @return Objek Anggota jika ditemukan, null jika tidak.
     */
    public Anggota getById(long id) {
        String sql = "SELECT id, nama, email, password_hash, alamat, tanggal_registrasi, role FROM anggota WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Anggota anggota = new Anggota();
                    anggota.setId(rs.getLong("id"));
                    anggota.setNama(rs.getString("nama"));
                    anggota.setEmail(rs.getString("email"));
                    anggota.setPasswordHash(rs.getString("password_hash"));
                    anggota.setAlamat(rs.getString("alamat"));
                    anggota.setTanggalRegistrasi(rs.getTimestamp("tanggal_registrasi").toLocalDateTime());
                    anggota.setRole(rs.getString("role"));
                    return anggota;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting anggota by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mencari anggota berdasarkan email. Ini akan sangat berguna untuk login.
     * @param email Email anggota.
     * @return Objek Anggota jika ditemukan, null jika tidak.
     */
    public Anggota getByEmail(String email) {
        String sql = "SELECT id, nama, email, password_hash, alamat, tanggal_registrasi, role FROM anggota WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Anggota anggota = new Anggota();
                    anggota.setId(rs.getLong("id"));
                    anggota.setNama(rs.getString("nama"));
                    anggota.setEmail(rs.getString("email"));
                    anggota.setPasswordHash(rs.getString("password_hash"));
                    anggota.setAlamat(rs.getString("alamat"));
                    anggota.setTanggalRegistrasi(rs.getTimestamp("tanggal_registrasi").toLocalDateTime());
                    anggota.setRole(rs.getString("role"));
                    return anggota;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting anggota by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mengupdate data anggota yang sudah ada.
     * Password tidak diupdate di sini, gunakan metode terpisah jika perlu update password.
     * @param anggota Objek Anggota dengan data terbaru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean update(Anggota anggota) {
        String sql = "UPDATE anggota SET nama = ?, email = ?, alamat = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, anggota.getNama());
            stmt.setString(2, anggota.getEmail());
            stmt.setString(3, anggota.getAlamat());
            stmt.setString(4, anggota.getRole());
            stmt.setLong(5, anggota.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating anggota: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus anggota dari database berdasarkan ID.
     * @param id ID anggota yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean delete(long id) {
        String sql = "DELETE FROM anggota WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting anggota: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memverifikasi password yang diinput dengan password hash di database.
     * @param email Email pengguna yang mencoba login.
     * @param rawPassword Password mentah (belum di-hash) yang diinput pengguna.
     * @return Objek Anggota jika kredensial valid, null jika tidak valid.
     */
    public Anggota authenticate(String email, String rawPassword) {
        Anggota anggota = getByEmail(email);
        if (anggota != null) {
            // Verifikasi password yang diinput dengan hash yang tersimpan
            if (passwordEncoder.matches(rawPassword, anggota.getPasswordHash())) {
                return anggota;
            }
        }
        return null; // Kredensial tidak valid
    }
}