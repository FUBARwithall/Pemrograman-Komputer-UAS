package main.java.com.perpustakaan.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.perpustakaan.desktop.dao.AnggotaDAO;
import main.java.com.perpustakaan.desktop.model.Anggota;

import java.io.IOException;

public class PrimaryController {

    // FXML elements (harus sama dengan fx:id di primary.fxml)
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessageLabel;

    // Instance dari DAO untuk berinteraksi dengan database
    private AnggotaDAO anggotaDAO = new AnggotaDAO();

    // Metode yang dipanggil saat tombol Login diklik (sesuai onAction="#handleLoginButton" di FXML)
    @FXML
    private void handleLoginButton() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Email dan password tidak boleh kosong!");
            return;
        }

        // Memanggil metode autentikasi dari AnggotaDAO
        Anggota authenticatedAnggota = anggotaDAO.authenticate(email, password);

        if (authenticatedAnggota != null) {
            // Login berhasil
            errorMessageLabel.setText("Login Berhasil! Role: " + authenticatedAnggota.getRole());
            // TODO: Di sini Anda akan mengarahkan pengguna ke tampilan utama aplikasi
            // Sesuai dengan role-nya (misal: dashboard admin atau dashboard anggota)
            // Contoh sederhana:
            // if ("ADMIN".equals(authenticatedAnggota.getRole())) {
            //    App.setRoot("admin_dashboard"); // Misal ada FXML untuk admin
            // } else {
            //    App.setRoot("anggota_dashboard"); // Misal ada FXML untuk anggota
            // }
            // Untuk sekarang, kita hanya menampilkan pesan sukses.
        } else {
            // Login gagal
            errorMessageLabel.setText("Email atau password salah.");
        }
    }

    // Metode inisialisasi controller (dipanggil otomatis oleh FXMLLoader)
    @FXML
    public void initialize() {
        // Kosongkan pesan error saat pertama kali tampilan dimuat
        errorMessageLabel.setText("");
    }
}