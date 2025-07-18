package gui;

import db.DBConnector;
import i18n.I18n;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.bson.Document;
import util.CryptoUtil;

public class ProfileFrame extends javax.swing.JFrame {

    private String currentUser;

    public ProfileFrame(String currentUser) {
        this.currentUser = currentUser;
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private ProfileFrame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void updateProfile() {
        String namaBaru = namaField.getText().trim();
        String oldPass = new String(oldPassField.getPassword());
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        var users = DBConnector.connect().getCollection("users");
        Document userDoc = users.find(new Document("username", currentUser)).first();

        if (userDoc == null) {
            JOptionPane.showMessageDialog(this, "User tidak ditemukan.");
            return;
        }

        try {
            String storedHashedPass = userDoc.getString("passwordHash");
            String inputOldHashed = CryptoUtil.hashPassword(oldPass);

            if (storedHashedPass == null || !inputOldHashed.equals(storedHashedPass)) {
                JOptionPane.showMessageDialog(this, "Password lama salah.");
                return;
            }

            Document update = new Document();

            if (!namaBaru.isBlank()) {
                update.append("username", namaBaru);
            }

            if (!newPass.isBlank()) {
                if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(this, "Konfirmasi password tidak cocok.");
                    return;
                }
                String newHashed = CryptoUtil.hashPassword(newPass);
                update.append("passwordHash", newHashed);
            }

            if (!update.isEmpty()) {
                users.updateOne(new Document("username", currentUser), new Document("$set", update));
                JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui.");
                dispose();
                new MainFrame(currentUser).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada perubahan yang dilakukan.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memperbarui profil: " + e.getMessage());
        }
    }

    private void reloadText() {
        judulLabel.setText(I18n.get("ProfileFrame.judulLabel.text"));
        userLabel.setText(I18n.get("ProfileFrame.userLabel.text"));
        oldPassLabel.setText(I18n.get("ProfileFrame.oldPassLabel.text"));
        newPassLabel.setText(I18n.get("ProfileFrame.newPassLabel.text"));
        confirmPassLabel.setText(I18n.get("ProfileFrame.confirmPassLabel.text"));
        simpanBtn.setText(I18n.get("ProfileFrame.simpanBtn.text"));
        kembaliBtn2.setText(I18n.get("ProfileFrame.kembaliBtn2.text"));
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
        judulLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        oldPassLabel = new javax.swing.JLabel();
        newPassLabel = new javax.swing.JLabel();
        confirmPassLabel = new javax.swing.JLabel();
        namaField = new javax.swing.JTextField();
        oldPassField = new javax.swing.JPasswordField();
        newPassField = new javax.swing.JPasswordField();
        confirmPassField = new javax.swing.JPasswordField();
        simpanBtn = new javax.swing.JButton();
        bahasaCmb = new javax.swing.JComboBox<>();
        kembaliBtn2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        judulLabel.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        judulLabel.setText("Change Password");

        userLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        userLabel.setText("Username :");

        oldPassLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        oldPassLabel.setText("Old Password :");

        newPassLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        newPassLabel.setText("New Password :");

        confirmPassLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        confirmPassLabel.setText("Confirm Password :");

        namaField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        oldPassField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        newPassField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        confirmPassField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        simpanBtn.setBackground(new java.awt.Color(51, 51, 255));
        simpanBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        simpanBtn.setForeground(new java.awt.Color(255, 255, 255));
        simpanBtn.setText("Save");
        simpanBtn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        simpanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanBtnActionPerformed(evt);
            }
        });

        bahasaCmb.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bahasaCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "English", "Indonesia" }));
        bahasaCmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bahasaCmbActionPerformed(evt);
            }
        });

        kembaliBtn2.setBackground(new java.awt.Color(51, 51, 255));
        kembaliBtn2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        kembaliBtn2.setForeground(new java.awt.Color(255, 255, 255));
        kembaliBtn2.setText("Back");
        kembaliBtn2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        kembaliBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembaliBtn2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(kembaliBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bahasaCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(judulLabel)
                        .addGap(541, 541, 541))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(498, 498, 498)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(oldPassLabel)
                            .addComponent(userLabel)
                            .addComponent(newPassLabel)
                            .addComponent(confirmPassLabel))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(namaField)
                            .addComponent(oldPassField)
                            .addComponent(newPassField)
                            .addComponent(confirmPassField, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(717, 717, 717)
                        .addComponent(simpanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(483, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bahasaCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kembaliBtn2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(233, 233, 233)
                .addComponent(judulLabel)
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oldPassField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oldPassLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newPassField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newPassLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmPassField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmPassLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(simpanBtn)
                .addContainerGap(240, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simpanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanBtnActionPerformed
        // TODO add your handling code here:
        updateProfile();
    }//GEN-LAST:event_simpanBtnActionPerformed

    private void bahasaCmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bahasaCmbActionPerformed
        // TODO add your handling code here:
        System.out.println("ComboBox dipilih: " + bahasaCmb.getSelectedItem());

        String selected = (String) bahasaCmb.getSelectedItem();
        if ("English".equals(selected)) {
            I18n.setLocale(Locale.ENGLISH);
        } else if ("Indonesia".equals(selected)) {
            I18n.setLocale(new Locale("id", "ID"));
        }

        reloadText();
    }//GEN-LAST:event_bahasaCmbActionPerformed

    private void kembaliBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembaliBtn2ActionPerformed
        // TODO add your handling code here:
        dispose();
        new MainFrame(currentUser).setVisible(true);
    }//GEN-LAST:event_kembaliBtn2ActionPerformed

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
            java.util.logging.Logger.getLogger(ProfileFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProfileFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProfileFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProfileFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProfileFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> bahasaCmb;
    private javax.swing.JPasswordField confirmPassField;
    private javax.swing.JLabel confirmPassLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel judulLabel;
    private javax.swing.JButton kembaliBtn;
    private javax.swing.JButton kembaliBtn1;
    private javax.swing.JButton kembaliBtn2;
    private javax.swing.JTextField namaField;
    private javax.swing.JPasswordField newPassField;
    private javax.swing.JLabel newPassLabel;
    private javax.swing.JPasswordField oldPassField;
    private javax.swing.JLabel oldPassLabel;
    private javax.swing.JButton simpanBtn;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
}
