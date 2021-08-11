/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym;

import entity.GymKey;
import entity.UserLog;
import entity.UserPermisos;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import jpa.GymKeyJpaController;
import jpa.UserLogJpaController;
import jpa.UserPermisosJpaController;

/**
 *
 * @author Juan
 */
public class login extends javax.swing.JFrame {

    UserLogJpaController uljc = null;
    EntityManagerFactory factory = null;
    UserPermisosJpaController upjc = null;
    GymKeyJpaController gkjc = null;
    Properties props = new Properties();

    /**
     * Creates new form login
     */
    public login() {
        initComponents();
        ParametrosBD();
        factory = Persistence.createEntityManagerFactory("BohioGymPU", props);
        uljc = new UserLogJpaController(factory);
        gkjc = new GymKeyJpaController(factory);
        upjc = new UserPermisosJpaController(factory);
        lAcceso.setText("...");
        this.setIconImage(getIconImage());
        this.setTitle("Gimnasio Ilcorpo");
    }

    private void ParametrosBD() {
        props.put("javax.persistence.jdbc.user", "root");
        props.put("javax.persistence.jdbc.password", "root");
        props.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/bohiogym");
        props.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
    }

    private void logear() {
        if (!tUsuario.getText().isEmpty()) {
            if (!pClave.getText().isEmpty()) {
                List<UserLog> u = findUser(tUsuario.getText().toUpperCase(), pClave.getText());
                if (u.size() > 0) {
                    if (u.get(0).getClave().equals(pClave.getText())) {
                        this.dispose();
                        principal p = new principal(factory, u.get(0), props);
                        p.lNombretitulo.setText(u.get(0).getUserInfoList().get(0).getNombres() + " " + u.get(0).getUserInfoList().get(0).getApellidos());
                        p.setLocationRelativeTo(null);
                        p.setIconImage(getIconImage());
                        p.setVisible(true);
                    } else {
                        lAcceso.setText("Contraseña Invalida");
                        lAcceso.setVisible(true);
                    }
                } else {
                    lAcceso.setVisible(true);
                    lAcceso.setText("Usuario o Contraseña Invalido");
                }
            } else {
                lAcceso.setVisible(true);
                lAcceso.setText("Campo Contraseña Vacio");
            }
        } else {
            lAcceso.setVisible(true);
            lAcceso.setText("Campo Usuario Vacio");
        }
    }

    private List<UserLog> findUser(String log, String pass) {
        EntityManager em = uljc.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM UserLog u WHERE u.usuario=:log AND u.clave=:pass AND u.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("log", log)
                    .setParameter("pass", pass)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("recursos/32x32 - final.png"));
        return retValue;
    }

    private void verificarKey() {
        if (getPermisosAdmin().size() > 0) {
            JOptionPane.showMessageDialog(null, "Configuracion inicial finalizada", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String key = JOptionPane.showInputDialog("Ingrese la Key suministrada por el proveedor: ").toString();
            if (getGymKey(key).size() > 0) {
                confinicial confini = new confinicial(null, true, factory);
                confini.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Lo sentimos la Key ingresada es erronea, intente nuevamente. \n\nSi el error persiste por favor comuniquese con el proveedor.", "Error Key", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<UserPermisos> getPermisosAdmin() {
        EntityManager em = upjc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM UserPermisos p WHERE p.idpermiso.id='1' AND p.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymKey> getGymKey(String llave) {
        EntityManager em = gkjc.getEntityManager();
        try {
            return em.createQuery("SELECT k FROM GymKey k WHERE k.llave=:llave")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("llave", llave)
                    .getResultList();
        } finally {
            em.close();
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pClave = new javax.swing.JPasswordField();
        lAcceso = new javax.swing.JLabel();
        bIniciar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        lConfinicial = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(50, 72, 141));
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  Control de Acceso de Usuarios.");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Usuario:");

        tUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tUsuarioKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Clave:");

        pClave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pClaveKeyPressed(evt);
            }
        });

        lAcceso.setForeground(new java.awt.Color(255, 0, 0));
        lAcceso.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lAcceso.setText("jLabel4");

        bIniciar.setBackground(new java.awt.Color(50, 72, 141));
        bIniciar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bIniciar.setForeground(new java.awt.Color(255, 255, 255));
        bIniciar.setText("Iniciar");
        bIniciar.setContentAreaFilled(false);
        bIniciar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bIniciar.setFocusable(false);
        bIniciar.setOpaque(true);
        bIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bIniciarActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("FuenTech 2018 © All rights reserved.");

        lConfinicial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/cog.png"))); // NOI18N
        lConfinicial.setToolTipText("Configuracion Inicial");
        lConfinicial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lConfinicial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lConfinicialMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tUsuario)
                    .addComponent(pClave)
                    .addComponent(lAcceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lConfinicial)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pClave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lAcceso, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lConfinicial))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void bIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bIniciarActionPerformed
        logear();
    }//GEN-LAST:event_bIniciarActionPerformed

    private void lConfinicialMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lConfinicialMouseReleased
        verificarKey();
    }//GEN-LAST:event_lConfinicialMouseReleased

    private void tUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pClave.requestFocusInWindow();
        }
    }//GEN-LAST:event_tUsuarioKeyPressed

    private void pClaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pClaveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            logear();
        }
    }//GEN-LAST:event_pClaveKeyPressed

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
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bIniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lAcceso;
    private javax.swing.JLabel lConfinicial;
    private javax.swing.JPasswordField pClave;
    private javax.swing.JTextField tUsuario;
    // End of variables declaration//GEN-END:variables
}
