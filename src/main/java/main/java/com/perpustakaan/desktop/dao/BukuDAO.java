package main.java.com.perpustakaan.desktop.dao;

import main.java.com.perpustakaan.desktop.model.Buku;
import main.java.com.perpustakaan.desktop.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {

    /**
     * Menyimpan buku baru ke database.
     * @param buku Objek Buku yang akan disimpan.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean save(Buku buku) {
        String sql = "INSERT INTO buku (judul, penulis, penerbit, tahun_terbit, isbn, jumlah_stok, lokasi_cover) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, buku.getJudul());
            stmt.setString(2, buku.getPenulis());
            stmt.setString(3, buku.getPenerbit());
            // Gunakan setInt untuk Integer, dan cek null
            if (buku.getTahunTerbit() != null) {
                stmt.setInt(4, buku.getTahunTerbit());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.setString(5, buku.getIsbn());
            if (buku.getJumlahStok() != null) {
                stmt.setInt(6, buku.getJumlahStok());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            stmt.setString(7, buku.getLokasiCover());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saving buku: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil semua buku dari database.
     * @return List of Buku objects.
     */
    public List<Buku> getAll() {
        List<Buku> bukuList = new ArrayList<>();
        String sql = "SELECT id, judul, penulis, penerbit, tahun_terbit, isbn, jumlah_stok, lokasi_cover FROM buku";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Buku buku = new Buku();
                buku.setId(rs.getLong("id"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenulis(rs.getString("penulis"));
                buku.setPenerbit(rs.getString("penerbit"));
                
                // Cek null untuk Integer
                int tahunTerbit = rs.getInt("tahun_terbit");
                if (rs.wasNull()) {
                    buku.setTahunTerbit(null);
                } else {
                    buku.setTahunTerbit(tahunTerbit);
                }

                buku.setIsbn(rs.getString("isbn"));
                
                // Cek null untuk Integer
                int jumlahStok = rs.getInt("jumlah_stok");
                if (rs.wasNull()) {
                    buku.setJumlahStok(null);
                } else {
                    buku.setJumlahStok(jumlahStok);
                }
                buku.setLokasiCover(rs.getString("lokasi_cover"));
                bukuList.add(buku);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all buku: " + e.getMessage());
            e.printStackTrace();
        }
        return bukuList;
    }

    /**
     * Mencari buku berdasarkan ID.
     * @param id ID buku.
     * @return Objek Buku jika ditemukan, null jika tidak.
     */
    public Buku getById(long id) {
        String sql = "SELECT id, judul, penulis, penerbit, tahun_terbit, isbn, jumlah_stok, lokasi_cover FROM buku WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Buku buku = new Buku();
                    buku.setId(rs.getLong("id"));
                    buku.setJudul(rs.getString("judul"));
                    buku.setPenulis(rs.getString("penulis"));
                    buku.setPenerbit(rs.getString("penerbit"));
                    
                    int tahunTerbit = rs.getInt("tahun_terbit");
                    if (rs.wasNull()) {
                        buku.setTahunTerbit(null);
                    } else {
                        buku.setTahunTerbit(tahunTerbit);
                    }

                    buku.setIsbn(rs.getString("isbn"));
                    
                    int jumlahStok = rs.getInt("jumlah_stok");
                    if (rs.wasNull()) {
                        buku.setJumlahStok(null);
                    } else {
                        buku.setJumlahStok(jumlahStok);
                    }
                    buku.setLokasiCover(rs.getString("lokasi_cover"));
                    return buku;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting buku by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mengupdate data buku yang sudah ada.
     * @param buku Objek Buku dengan data terbaru.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean update(Buku buku) {
        String sql = "UPDATE buku SET judul = ?, penulis = ?, penerbit = ?, tahun_terbit = ?, isbn = ?, jumlah_stok = ?, lokasi_cover = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, buku.getJudul());
            stmt.setString(2, buku.getPenulis());
            stmt.setString(3, buku.getPenerbit());
            if (buku.getTahunTerbit() != null) {
                stmt.setInt(4, buku.getTahunTerbit());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.setString(5, buku.getIsbn());
            if (buku.getJumlahStok() != null) {
                stmt.setInt(6, buku.getJumlahStok());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            stmt.setString(7, buku.getLokasiCover());
            stmt.setLong(8, buku.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating buku: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus buku dari database berdasarkan ID.
     * @param id ID buku yang akan dihapus.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean delete(long id) {
        String sql = "DELETE FROM buku WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting buku: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}