/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.mongodb.client.FindIterable;
import db.DBConnector;
import i18n.I18n;
import java.awt.HeadlessException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.bson.Document;

/**
 *
 * @author ASUS
 */
public class ManageFrame extends javax.swing.JFrame {

    private DefaultTableModel modelKelola;
    private DefaultTableModel modelRiwayat;
    private DefaultTableModel modelBookmark;
    private String currentUser;

    /**
     * Creates new form KelolaBuku
     */
    public ManageFrame(String username) {
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        this.currentUser = username;
        userLabel.setText(username);

        modelKelola = new DefaultTableModel(new String[]{"Judul", "Tanggal Pinjam"}, 0);
        kelolaTabel.setModel(modelKelola); // pastikan ini sesuai nama JTable kamu

        modelRiwayat = new DefaultTableModel(new String[]{"Judul", "Tanggal Pinjam", "Tanggal Kembali", "Status"}, 0);
        riwayatTabel.setModel(modelRiwayat);

        modelBookmark = new DefaultTableModel(new String[]{"Judul", "Halaman Terakhir", "Terakhir Dibaca"}, 0);
        bookmarkTabel.setModel(modelBookmark);

        loadRiwayat();
        loadKelola();
        loadBookmarks();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelRiwayat);
        riwayatTabel.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                String query = searchField.getText();
                if (query.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
                }
            }
        });
    }

    private void loadBookmarks() {
        modelBookmark.setRowCount(0);
        var bookmarks = DBConnector.connect().getCollection("bookmarks");

        var hasil = bookmarks.find(new Document("user", currentUser)).sort(new Document("timestamp", -1));
        int count = 0;

        for (Document doc : hasil) {
            count++;
            String judul = doc.getString("judul");
            int lastPage = doc.getInteger("lastPage", 0);
            Date time = doc.getDate("timestamp");

            modelBookmark.addRow(new Object[]{
                judul,
                lastPage + 1,
                new SimpleDateFormat("dd-MM-yyyy HH:mm").format(time)
            });
        }
    }

    private void loadKelola() {
        modelKelola.setRowCount(0); // Kosongkan tabel dulu

        var peminjaman = DBConnector.connect().getCollection("peminjaman");

        FindIterable<Document> hasil = peminjaman.find(
                new Document("user", currentUser).append("dikembalikan", false)
        );

        for (Document doc : hasil) {
            String judul = doc.getString("judul");
            Date tanggalPinjam = doc.getDate("tanggalPinjam");

            modelKelola.addRow(new Object[]{
                judul,
                tanggalPinjam
            });
        }
    }

    private void loadRiwayat() {
        modelRiwayat.setRowCount(0); // kosongkan dulu

        var peminjaman = DBConnector.connect().getCollection("peminjaman");
        var hasil = peminjaman.find(new Document("user", currentUser));

        for (Document doc : hasil) {
            String judul = doc.getString("judul");
            Date tanggalPinjam = doc.getDate("tanggalPinjam");
            Date tanggalKembali = doc.getDate("tanggalKembali");
            boolean dikembalikan = doc.getBoolean("dikembalikan", false);

            String status = dikembalikan ? "Dikembalikan" : "Belum Kembali";

            modelRiwayat.addRow(new Object[]{
                judul,
                tanggalPinjam,
                tanggalKembali != null ? tanggalKembali : "-",
                status
            });
        }
    }

    private void loadRiwayatFiltered(Date fromDate, Date toDate) {
        modelRiwayat.setRowCount(0);

        var peminjaman = DBConnector.connect().getCollection("peminjaman");

        // Gunakan range filter MongoDB
        Document filter = new Document("user", currentUser)
                .append("tanggalPinjam", new Document("$gte", fromDate).append("$lte", toDate));

        var hasil = peminjaman.find(filter);

        for (Document doc : hasil) {
            String judul = doc.getString("judul");
            Date tanggalPinjam = doc.getDate("tanggalPinjam");
            Date tanggalKembali = doc.getDate("tanggalKembali");
            boolean dikembalikan = doc.getBoolean("dikembalikan", false);

            String status = dikembalikan ? "Dikembalikan" : "Belum Kembali";

            modelRiwayat.addRow(new Object[]{
                judul,
                tanggalPinjam,
                tanggalKembali != null ? tanggalKembali : "-",
                status
            });
        }
    }

    private void reloadText() {
        kembaliBtn.setText(I18n.get("ManageFrame.kembaliBtn.text"));

        borrowedBooksLabel.setText(I18n.get("ManageFrame.borrowedBooksLabel.text"));
        historyLabel.setText(I18n.get("ManageFrame.historyLabel.text"));
        toDateLabel.setText(I18n.get("ManageFrame.toDateLabel.text"));
        fromDateLabel.setText(I18n.get("ManageFrame.fromDateLabel.text"));
        searchLabel.setText(I18n.get("ManageFrame.searchLabel.text"));
        applyBtn.setText(I18n.get("ManageFrame.applyBtn.text"));

        continueFromPreviousLabel.setText(I18n.get("ManageFrame.continueFromPreviousLabel.text"));
        bacaBookmarkBtn.setText(I18n.get("ManageFrame.bacaBookmarkBtn.text"));
    }

    ManageFrame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        userLabel = new javax.swing.JLabel();
        kembaliBtn = new javax.swing.JButton();
        bahasaCmb = new javax.swing.JComboBox<>();
        TabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        kelolaTabel = new javax.swing.JTable();
        kembalikanBtn = new javax.swing.JButton();
        borrowedBooksLabel = new javax.swing.JLabel();
        historyLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        riwayatTabel = new javax.swing.JTable();
        toDateLabel = new javax.swing.JLabel();
        fromDateLabel = new javax.swing.JLabel();
        toDateChooser = new com.toedter.calendar.JDateChooser();
        fromDateChooser = new com.toedter.calendar.JDateChooser();
        searchField = new javax.swing.JTextField();
        applyBtn = new javax.swing.JButton();
        searchLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        continueFromPreviousLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        bookmarkTabel = new javax.swing.JTable();
        bacaBookmarkBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        userLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        userLabel.setText("User");

        kembaliBtn.setBackground(new java.awt.Color(51, 51, 255));
        kembaliBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembaliBtn.setForeground(new java.awt.Color(255, 255, 255));
        kembaliBtn.setText("Back");
        kembaliBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kembaliBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembaliBtnActionPerformed(evt);
            }
        });

        bahasaCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "English", "Indonesia" }));
        bahasaCmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bahasaCmbActionPerformed(evt);
            }
        });

        TabbedPane.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        kelolaTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(kelolaTabel);

        kembalikanBtn.setBackground(new java.awt.Color(51, 51, 255));
        kembalikanBtn.setForeground(new java.awt.Color(255, 255, 255));
        kembalikanBtn.setText("Return");
        kembalikanBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kembalikanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembalikanBtnActionPerformed(evt);
            }
        });

        borrowedBooksLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        borrowedBooksLabel.setText("Borrowed Books");

        historyLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        historyLabel.setText("History");

        jScrollPane3.setPreferredSize(new java.awt.Dimension(452, 100));

        riwayatTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(riwayatTabel);

        toDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        toDateLabel.setText("To date :");

        fromDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fromDateLabel.setText("From date :");

        applyBtn.setBackground(new java.awt.Color(51, 51, 255));
        applyBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        applyBtn.setForeground(new java.awt.Color(255, 255, 255));
        applyBtn.setText("Apply");
        applyBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        applyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyBtnActionPerformed(evt);
            }
        });

        searchLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchLabel.setText("Search :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1354, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(kembalikanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(borrowedBooksLabel)
                            .addComponent(historyLabel)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(searchLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fromDateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toDateLabel)
                                .addGap(7, 7, 7)
                                .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(applyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(borrowedBooksLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kembalikanBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(historyLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchLabel)
                                .addComponent(fromDateLabel))
                            .addComponent(fromDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(toDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(applyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(13, 13, 13)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(toDateLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        TabbedPane.addTab("Borrowed", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1366, 768));

        continueFromPreviousLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        continueFromPreviousLabel.setText("Continue From Previous");

        bookmarkTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(bookmarkTabel);

        bacaBookmarkBtn.setBackground(new java.awt.Color(51, 51, 255));
        bacaBookmarkBtn.setForeground(new java.awt.Color(255, 255, 255));
        bacaBookmarkBtn.setText("Read");
        bacaBookmarkBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bacaBookmarkBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bacaBookmarkBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(continueFromPreviousLabel)
                        .addGap(0, 1097, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bacaBookmarkBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(continueFromPreviousLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bacaBookmarkBtn)
                .addContainerGap())
        );

        TabbedPane.addTab("Continue read", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(userLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(kembaliBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bahasaCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(TabbedPane)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(bahasaCmb, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kembaliBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(userLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kembalikanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembalikanBtnActionPerformed
        // TODO add your handling code here:
        int selectedRow = kelolaTabel.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dikembalikan.");
            return;
        }

        String judul = (String) modelKelola.getValueAt(selectedRow, 0); // kolom judul

        var peminjaman = DBConnector.connect().getCollection("peminjaman");
        var books = DBConnector.connect().getCollection("books");

        try {
            peminjaman.updateOne(
                    new Document("user", currentUser)
                            .append("judul", judul)
                            .append("dikembalikan", false),
                    new Document("$set", new Document("dikembalikan", true)
                            .append("tanggalKembali", new Date()))
            );
            books.updateOne(
                    new Document("Judul", judul),
                    new Document("$set", new Document("Status", "Tersedia"))
            );

            JOptionPane.showMessageDialog(this, "Buku berhasil dikembalikan: " + judul);
            loadKelola(); // refresh tabel setelah pengembalian
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengembalikan buku: " + e.getMessage());
        }
    }//GEN-LAST:event_kembalikanBtnActionPerformed

    private void kembaliBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembaliBtnActionPerformed
        // TODO add your handling code here:
        dispose();
        new MainFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_kembaliBtnActionPerformed

    private void applyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyBtnActionPerformed
        // TODO add your handling code here:
        Date fromDate = fromDateChooser.getDate();
        Date toDate = toDateChooser.getDate();

        if (fromDate == null || toDate == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih rentang tanggal terlebih dahulu.");
            return;
        }

        loadRiwayatFiltered(fromDate, toDate);
    }//GEN-LAST:event_applyBtnActionPerformed

    private void bacaBookmarkBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bacaBookmarkBtnActionPerformed
        // TODO add your handling code here:
        int selectedRow = bookmarkTabel.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dibaca.");
            return;
        }

        String judul = bookmarkTabel.getValueAt(selectedRow, 0).toString();

        // Ambil data dari Mongo berdasarkan judul
        var collection = DBConnector.connect().getCollection("books");
        Document doc = collection.find(new Document("Judul", judul)).first();

        if (doc != null && doc.containsKey("pdfPath")) {
            String pdfPath = doc.getString("pdfPath");

            // Buat panel pembaca
            PDFViewerPanel viewerPanel = new PDFViewerPanel(pdfPath, judul, currentUser);

            // Buat frame pembaca
            JFrame readerFrame = new JFrame("Reading: " + judul);
            readerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            readerFrame.setSize(800, 600);
            readerFrame.setLocationRelativeTo(this);
            readerFrame.add(viewerPanel);

            // Tambahkan listener agar saat frame ditutup, bookmark disimpan
            readerFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    viewerPanel.closeDocument();  // Simpan bookmark
                }

                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    loadBookmarks(); // Refresh isi tabel bookmark
                }
            });

            readerFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Buku tidak memiliki file PDF.");
        }
    }//GEN-LAST:event_bacaBookmarkBtnActionPerformed

    private void bahasaCmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bahasaCmbActionPerformed
        // TODO add your handling code here:
        System.out.println("ComboBox dipilih: " + bahasaCmb.getSelectedItem()); // ← Tambahkan ini

        String selected = (String) bahasaCmb.getSelectedItem();
        if ("English".equals(selected)) {
            I18n.setLocale(Locale.ENGLISH);
        } else if ("Indonesia".equals(selected)) {
            I18n.setLocale(new Locale("id", "ID"));
        }

        reloadText();
    }//GEN-LAST:event_bahasaCmbActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JButton applyBtn;
    private javax.swing.JButton bacaBookmarkBtn;
    private javax.swing.JComboBox<String> bahasaCmb;
    private javax.swing.JTable bookmarkTabel;
    private javax.swing.JLabel borrowedBooksLabel;
    private javax.swing.JLabel continueFromPreviousLabel;
    private com.toedter.calendar.JDateChooser fromDateChooser;
    private javax.swing.JLabel fromDateLabel;
    private javax.swing.JLabel historyLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable kelolaTabel;
    private javax.swing.JButton kembaliBtn;
    private javax.swing.JButton kembalikanBtn;
    private javax.swing.JTable riwayatTabel;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private com.toedter.calendar.JDateChooser toDateChooser;
    private javax.swing.JLabel toDateLabel;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
}
