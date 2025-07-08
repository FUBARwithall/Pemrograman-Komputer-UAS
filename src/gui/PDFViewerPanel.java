package gui;

import db.DBConnector;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.bson.Document;

public class PDFViewerPanel extends javax.swing.JPanel {

    private PDDocument document;
    private PDFRenderer renderer;
    private int currentPage = 0;
    private int totalPages = 0;
    private int currentDPI = 120;
    private boolean darkMode = false;
    private boolean sepiaMode = false;

    private JLabel imageLabel;
    private JLabel pageIndicator;
    private JScrollPane scrollPane;
    private JButton prevBtn, nextBtn, zoomInBtn, zoomOutBtn, fullscreenBtn;
    private JTextField pageField;
    private JComboBox<String> modeBox;

    private JFrame fullscreenFrame;

    private String pdfPath;
    private String judul;
    private String currentUser;

    public PDFViewerPanel(String pdfPath, String judul, String currentUser1) {
        this.pdfPath = pdfPath;
        this.judul = judul;
        this.currentUser = currentUser1;

        setLayout(new BorderLayout());

        try {
            document = PDDocument.load(new File(pdfPath));
            renderer = new PDFRenderer(document);
            totalPages = document.getNumberOfPages();

            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            scrollPane = new JScrollPane(imageLabel);
            add(scrollPane, BorderLayout.CENTER);

            add(scrollPane, BorderLayout.CENTER);

            // Kontrol Panel
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            prevBtn = new JButton("Previous");
            nextBtn = new JButton("Next");
            zoomOutBtn = new JButton("-");
            zoomInBtn = new JButton("+");
            fullscreenBtn = new JButton("Fullscreen");
            pageField = new JTextField(3);
            JButton goBtn = new JButton("Go");
            pageIndicator = new JLabel();
            modeBox = new JComboBox<>(new String[]{"Normal", "Dark", "Sepia"});

            controlPanel.add(prevBtn);
            controlPanel.add(nextBtn);
            controlPanel.add(zoomOutBtn);
            controlPanel.add(zoomInBtn);
            controlPanel.add(new JLabel("Page:"));
            controlPanel.add(pageField);
            controlPanel.add(goBtn);
            controlPanel.add(pageIndicator);
            controlPanel.add(modeBox);
            controlPanel.add(fullscreenBtn);

            add(controlPanel, BorderLayout.SOUTH);

            // Listener
            prevBtn.addActionListener(e -> changePage(currentPage - 1));
            nextBtn.addActionListener(e -> changePage(currentPage + 1));
            zoomInBtn.addActionListener(e -> {
                currentDPI += 20;
                renderPage();
            });
            zoomOutBtn.addActionListener(e -> {
                currentDPI = Math.max(50, currentDPI - 20);
                renderPage();
            });
            goBtn.addActionListener(e -> {
                try {
                    int target = Integer.parseInt(pageField.getText()) - 1;
                    changePage(target);
                } catch (NumberFormatException ignored) {
                }
            });
            modeBox.addActionListener(e -> {
                String selected = (String) modeBox.getSelectedItem();
                darkMode = "Dark".equals(selected);
                sepiaMode = "Sepia".equals(selected);
                renderPage();
            });
            fullscreenBtn.addActionListener(e -> openFullscreen(judul));

            // Keyboard Navigation
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_RIGHT ->
                            changePage(currentPage + 1);
                        case KeyEvent.VK_LEFT ->
                            changePage(currentPage - 1);
                        case KeyEvent.VK_ADD, KeyEvent.VK_EQUALS -> {
                            currentDPI += 20;
                            renderPage();
                        }
                        case KeyEvent.VK_SUBTRACT, KeyEvent.VK_MINUS -> {
                            currentDPI = Math.max(50, currentDPI - 20);
                            renderPage();
                        }
                        case KeyEvent.VK_ESCAPE -> {
                            if (fullscreenFrame != null) {
                                fullscreenFrame.dispose();
                            }
                        }
                    }
                }
            });

            loadBookmark();
            renderPage();
        } catch (IOException e) {
            add(new JLabel("Gagal menampilkan PDF: " + e.getMessage()), BorderLayout.CENTER);
        }
    }

    private void changePage(int page) {
        if (page >= 0 && page < totalPages) {
            currentPage = page;
            renderPage();
        }
    }

    private void loadBookmark() {
        try {
            var bookmarks = DBConnector.connect().getCollection("bookmarks");
            Document doc = bookmarks.find(
                    new Document("user", currentUser).append("judul", judul)
            ).first();

            if (doc != null) {
                currentPage = doc.getInteger("lastPage", 0); // default ke 0 kalau gagal
            }
        } catch (Exception e) {
            System.err.println("Gagal load bookmark: " + e.getMessage());
        }
    }

    private void renderPage() {
        try {
            BufferedImage image = renderer.renderImageWithDPI(currentPage, currentDPI);
            if (darkMode) {
                image = applyDarkFilter(image);
            }
            if (sepiaMode) {
                image = applySepiaFilter(image);
            }
            imageLabel.setIcon(new ImageIcon(image));
            pageIndicator.setText((currentPage + 1) + " / " + totalPages);
            pageField.setText(String.valueOf(currentPage + 1));
            revalidate();
        } catch (IOException e) {
        }
    }

    private BufferedImage applyDarkFilter(BufferedImage image) {
        BufferedImage darkImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = darkImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        RescaleOp op = new RescaleOp(-1.0f, 255f, null); // invert
        op.filter(darkImage, darkImage);
        g.dispose();
        return darkImage;
    }

    private BufferedImage applySepiaFilter(BufferedImage img) {
        BufferedImage sepia = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int r = (int) (0.393 * c.getRed() + 0.769 * c.getGreen() + 0.189 * c.getBlue());
                int g = (int) (0.349 * c.getRed() + 0.686 * c.getGreen() + 0.168 * c.getBlue());
                int b = (int) (0.272 * c.getRed() + 0.534 * c.getGreen() + 0.131 * c.getBlue());
                sepia.setRGB(x, y, new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b)).getRGB());
            }
        }
        return sepia;
    }

    private void openFullscreen(String judul) {
        fullscreenFrame = new JFrame("Reading: " + judul);
        fullscreenFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fullscreenFrame.setUndecorated(true);
        fullscreenFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fullscreenFrame.setLayout(new BorderLayout());

        PDFViewerPanel fullscreenViewer = new PDFViewerPanel(pdfPath, judul, currentUser);
        fullscreenViewer.currentPage = this.currentPage;
        fullscreenViewer.currentDPI = this.currentDPI;
        fullscreenViewer.darkMode = this.darkMode;
        fullscreenViewer.sepiaMode = this.sepiaMode;
        fullscreenViewer.renderPage();

        fullscreenFrame.add(fullscreenViewer, BorderLayout.CENTER);
        fullscreenFrame.setVisible(true);
        fullscreenViewer.requestFocusInWindow();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void closeDocument() {
        try {
            if (document != null) {
                document.close();
                // Simpan ke MongoDB saat close
                Document bookmark = new Document("user", currentUser)
                        .append("judul", judul)
                        .append("lastPage", currentPage)
                        .append("timestamp", new Date());

                var bookmarks = DBConnector.connect().getCollection("bookmarks");
                bookmarks.deleteOne(new Document("user", currentUser).append("judul", judul)); // hapus lama
                bookmarks.insertOne(bookmark);

            }
        } catch (IOException ignored) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
