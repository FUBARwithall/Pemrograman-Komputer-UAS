package main.java.com.perpustakaan.desktop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {

    // Konfigurasi koneksi database Anda
    private static final String URL = "jdbc:mysql://localhost:3306/daring_membaca_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default password XAMPP untuk user 'root' adalah kosong

    /**
     * Metode untuk mendapatkan objek Connection ke database.
     * @return Objek Connection yang aktif ke database.
     * @throws SQLException Jika terjadi kesalahan saat mencoba koneksi ke database.
     */
    public static Connection getConnection() throws SQLException {
        // Driver JDBC untuk MySQL secara otomatis dimuat oleh JVM jika sudah ada di classpath (dari dependency Maven)
        // Anda tidak perlu lagi Class.forName("com.mysql.cj.jdbc.Driver"); di versi JDBC modern.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Metode opsional untuk menutup koneksi, statement, dan resultset.
     * Meskipun sekarang lebih sering menggunakan try-with-resources,
     * metode ini berguna untuk kompatibilitas atau kasus khusus.
     */
    public static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Overload untuk close tanpa ResultSet
    public static void close(Connection conn, PreparedStatement stmt) {
        close(conn, stmt, null);
    }

    // Overload untuk close hanya Connection
    public static void close(Connection conn) {
        close(conn, null, null);
    }
}