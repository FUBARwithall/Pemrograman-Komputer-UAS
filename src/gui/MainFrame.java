package gui;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import db.DBConnector;
import i18n.I18n;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.bson.Document;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends javax.swing.JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String currentUser;

    public MainFrame(String username) {
        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        this.currentUser = username;
        welcomeLabel.setText("Welcome " + username + "!");

        this.table = bukuTabel;
        this.tableModel = (DefaultTableModel) bukuTabel.getModel();

        loadDataBuku();
        setupListeners();
        loadKategoriCombo();
    }

    private void loadDataBuku() {
        MongoCollection<Document> collection = DBConnector.connect().getCollection("books");
        FindIterable<Document> docs = collection.find();

        tableModel.setRowCount(0);

        for (Document doc : docs) {
            String judul = doc.getString("Judul");
            String penulis = doc.getString("Penulis");
            String tahunString = doc.getString("Tahun");
            int tahun = 0;
            if (tahunString != null && !tahunString.isEmpty()) {
                try {
                    tahun = Integer.parseInt(tahunString);
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Could not parse 'Tahun' as integer: " + tahunString);
                }
            }
            String kategori = doc.getString("Kategori");
            String status = doc.getString("Status");

            tableModel.addRow(new Object[]{judul, penulis, tahun, kategori, status});
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(600);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);
        table.getColumnModel().getColumn(4).setPreferredWidth(135);
    }

    private void loadDataBukuFiltered() {
        tableModel.setRowCount(0);

        var books = DBConnector.connect().getCollection("books");
        Document filter = new Document();

        String keyword = cariField.getText().trim();
        String selectedKategori = (String) kategoriCmb.getSelectedItem();
        boolean hanyaTersedia = tersediaCheckBox.isSelected();

        if (!keyword.isEmpty()) {
            filter.append("Judul", new Document("$regex", keyword).append("$options", "i"));
        }

        if (selectedKategori != null && !selectedKategori.equalsIgnoreCase("Semua")) {
            filter.append("Kategori", selectedKategori);
        }

        if (hanyaTersedia) {
            filter.append("Status", "Tersedia");
        }

        var hasil = books.find(filter);
        for (Document doc : hasil) {
            String judul = doc.getString("Judul");
            String penulis = doc.getString("Penulis");
            Object tahunObj = doc.get("Tahun");
            String tahun = (tahunObj != null) ? tahunObj.toString() : "-";
            String kategori = doc.getString("Kategori");
            String status = doc.getString("Status");

            tableModel.addRow(new Object[]{judul, penulis, tahun, kategori, status});
        }
    }

    private void setupListeners() {
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String status = (String) tableModel.getValueAt(row, 4);
                pinjamBtn.setEnabled("Tersedia".equalsIgnoreCase(status));
            }
        });
    }

    private void loadKategoriCombo() {
        kategoriCmb.removeAllItems();
        kategoriCmb.addItem("Semua");

        var books = DBConnector.connect().getCollection("books");

        for (String kategori : books.distinct("Kategori", String.class)) {
            if (kategori != null && !kategori.trim().isEmpty()) {
                kategoriCmb.addItem(kategori);
            }
        }
    }

    private void reloadText() {
        kelolaButton.setText(I18n.get("MainFrame.kelolaButton.text"));
        changePassBtn.setText(I18n.get("MainFrame.changePassBtn.text"));
        logoutBtn.setText(I18n.get("MainFrame.logoutBtn.text"));
        jLabel1.setText(I18n.get("MainFrame.jLabel1.text"));
        jLabel2.setText(I18n.get("MainFrame.jLabel2.text"));
        tersediaCheckBox.setText(I18n.get("MainFrame.tersediaCheckBox.text"));
        cariButton.setText(I18n.get("MainFrame.cariButton.text"));
        bacaBtn.setText(I18n.get("MainFrame.bacaBtn.text"));
        pinjamBtn.setText(I18n.get("MainFrame.pinjamBtn.text"));
    }

    private MainFrame() {
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

        jPanel1 = new javax.swing.JPanel();
        logoutBtn = new javax.swing.JButton();
        welcomeLabel = new javax.swing.JLabel();
        kelolaButton = new javax.swing.JButton();
        changePassBtn = new javax.swing.JButton();
        bahasaCmb = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        cariField = new javax.swing.JTextField();
        cariButton = new javax.swing.JButton();
        pinjamBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        bukuTabel = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        kategoriCmb = new javax.swing.JComboBox<>();
        tersediaCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        bacaBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        logoutBtn.setBackground(new java.awt.Color(51, 51, 255));
        logoutBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        logoutBtn.setForeground(new java.awt.Color(255, 255, 255));
        logoutBtn.setText("Logout");
        logoutBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnActionPerformed(evt);
            }
        });

        welcomeLabel.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        welcomeLabel.setText("Welcome");

        kelolaButton.setBackground(new java.awt.Color(51, 51, 255));
        kelolaButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kelolaButton.setForeground(new java.awt.Color(255, 255, 255));
        kelolaButton.setText("My books");
        kelolaButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kelolaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kelolaButtonActionPerformed(evt);
            }
        });

        changePassBtn.setBackground(new java.awt.Color(51, 51, 255));
        changePassBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        changePassBtn.setForeground(new java.awt.Color(255, 255, 255));
        changePassBtn.setText("Change Password");
        changePassBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        changePassBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePassBtnActionPerformed(evt);
            }
        });

        bahasaCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "English", "Indonesia" }));
        bahasaCmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bahasaCmbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(welcomeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bahasaCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(kelolaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(changePassBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1037, 1037, 1037))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(welcomeLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(bahasaCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logoutBtn)
                    .addComponent(kelolaButton)
                    .addComponent(changePassBtn))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cariField.setMinimumSize(new java.awt.Dimension(64, 23));
        cariField.setPreferredSize(new java.awt.Dimension(64, 23));

        cariButton.setBackground(new java.awt.Color(51, 51, 255));
        cariButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cariButton.setForeground(new java.awt.Color(255, 255, 255));
        cariButton.setText("Search");
        cariButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cariButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariButtonActionPerformed(evt);
            }
        });

        pinjamBtn.setBackground(new java.awt.Color(51, 51, 255));
        pinjamBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pinjamBtn.setForeground(new java.awt.Color(255, 255, 255));
        pinjamBtn.setText("Borrow");
        pinjamBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pinjamBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjamBtnActionPerformed(evt);
            }
        });

        bukuTabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bukuTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Judul", "Penulis", "Tahun", "Kategori", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(bukuTabel);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Search book :");

        kategoriCmb.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kategoriCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        kategoriCmb.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tersediaCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        tersediaCheckBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tersediaCheckBox.setText("Only shows available books");
        tersediaCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tersediaCheckBoxActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Category :");

        bacaBtn.setBackground(new java.awt.Color(51, 51, 255));
        bacaBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bacaBtn.setForeground(new java.awt.Color(255, 255, 255));
        bacaBtn.setText("Read");
        bacaBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bacaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bacaBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bacaBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pinjamBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel2)
                                .addGap(31, 31, 31)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(tersediaCheckBox))
                                    .addComponent(kategoriCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cariButton, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cariField, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(kategoriCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tersediaCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cariButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pinjamBtn)
                    .addComponent(bacaBtn))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        // TODO add your handling code here:
        dispose();
        new LoginFrame().setVisible(true);
    }//GEN-LAST:event_logoutBtnActionPerformed

    private void pinjamBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjamBtnActionPerformed
        // TODO add your handling code here:
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }

        String judul = (String) tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 4);

        if (!"Tersedia".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this, "Buku tidak tersedia.");
            return;
        }

        var books = DBConnector.connect().getCollection("books");
        var peminjaman = DBConnector.connect().getCollection("peminjaman");

        long jumlahDipinjam = peminjaman.countDocuments(
                new Document("user", currentUser).append("dikembalikan", false)
        );

        if (jumlahDipinjam >= 3) {
            JOptionPane.showMessageDialog(this, "Kamu sudah meminjam 3 buku. Kembalikan dulu sebelum pinjam lagi.");
            return;
        }

        try {
            books.updateOne(
                    new Document("Judul", judul),
                    new Document("$set", new Document("Status", "Dipinjam"))
            );

            Document trx = new Document("judul", judul)
                    .append("user", currentUser)
                    .append("tanggalPinjam", new java.util.Date())
                    .append("dikembalikan", false);

            peminjaman.insertOne(trx);

            JOptionPane.showMessageDialog(this, "Berhasil pinjam buku: " + judul);
            loadDataBuku();
            pinjamBtn.setEnabled(false);
        } catch (com.mongodb.MongoException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memproses peminjaman: " + e.getMessage(), "Error Peminjaman", JOptionPane.ERROR_MESSAGE);
            System.err.println("MongoDB error during borrow: " + e.getMessage());
        }
    }//GEN-LAST:event_pinjamBtnActionPerformed

    private void kelolaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kelolaButtonActionPerformed
        // TODO add your handling code here:
        dispose();
        new ManageFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_kelolaButtonActionPerformed

    private void tersediaCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tersediaCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tersediaCheckBoxActionPerformed

    private void cariButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariButtonActionPerformed
        // TODO add your handling code here:
        loadDataBukuFiltered();
    }//GEN-LAST:event_cariButtonActionPerformed

    private void bacaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bacaBtnActionPerformed
        // TODO add your handling code here:
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dibaca.");
            return;
        }

        String judul = table.getValueAt(selectedRow, 0).toString();

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
            });

            readerFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Buku tidak memiliki file PDF.");
        }
    }//GEN-LAST:event_bacaBtnActionPerformed

    private void changePassBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePassBtnActionPerformed
        // TODO add your handling code here:
        dispose();
        new ProfileFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_changePassBtnActionPerformed

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bacaBtn;
    private javax.swing.JComboBox<String> bahasaCmb;
    private javax.swing.JTable bukuTabel;
    private javax.swing.JButton cariButton;
    private javax.swing.JTextField cariField;
    private javax.swing.JButton changePassBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> kategoriCmb;
    private javax.swing.JButton kelolaButton;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JButton pinjamBtn;
    private javax.swing.JCheckBox tersediaCheckBox;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
