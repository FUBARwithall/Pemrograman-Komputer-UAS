package main.java.com.perpustakaan.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects; // Perlu untuk Objects.requireNonNull

/**
 * Kelas utama untuk aplikasi DaringMembaca.
 * Bertanggung jawab untuk inisialisasi dan meluncurkan antarmuka pengguna JavaFX.
 *
 * @author ASUS (sesuaikan dengan nama Anda)
 */
public class App extends Application {

    private static Scene scene; // Menyimpan referensi scene agar bisa diubah jika perlu

    @Override
    public void start(Stage stage) throws IOException {
        // Memuat file FXML untuk tampilan awal (primary.fxml)
        // Pastikan primary.fxml ada di src/main/resources/com/perpustakaan/desktop/primary.fxml
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("primary.fxml")));

        // Membuat Scene baru dari Parent (root)
        scene = new Scene(root, 1000, 700); // Lebar 1000px, Tinggi 700px (bisa disesuaikan)

        // Menambahkan stylesheet (opsional, jika Anda punya styles.css)
        // Pastikan styles.css ada di src/main/resources/com/perpustakaan/desktop/styles.css
        String css = Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm();
        scene.getStylesheets().add(css);

        // Mengatur judul window
        stage.setTitle("DaringMembaca - Sistem Perpustakaan Desktop");

        // Menetapkan scene ke Stage
        stage.setScene(scene);

        // Menampilkan Stage
        stage.show();
    }

    // Metode main untuk meluncurkan aplikasi JavaFX
    public static void main(String[] args) {
        launch(); // Memulai siklus hidup aplikasi JavaFX
    }

    // Metode helper untuk mengganti root dari scene (jika Anda ingin pindah antar tampilan FXML)
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Metode helper untuk memuat file FXML
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(App.class.getResource(fxml + ".fxml")));
        return fxmlLoader.load();
    }
}