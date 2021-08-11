/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles;

import bohiogym.paneles.paneladministrador.cClave;
import bohiogym.paneles.paneladministrador.gPerfiles;
import entity.UserLog;
import entity.UserPermisos;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import jpa.UserLogJpaController;

/**
 *
 * @author Alvarez
 */
public class Administrador extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    private final UserLog userLog;
    gPerfiles gper = null;
    UserLogJpaController uljc;
    int gestionperfiles = 25;

    /**
     * Creates new form Administrador
     */
    public Administrador(EntityManagerFactory factory, UserLog userLog) {
        this.factory = factory;
        this.userLog = userLog;
        initComponents();
        uljc = new UserLogJpaController(factory);
    }

    private List<UserPermisos> getPermisos(UserLog usuario) {
        EntityManager em = uljc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM UserPermisos p WHERE p.iduser=:usuario AND (p.estado='1' OR p.estado='2')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("usuario", usuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private boolean validaGestionPerfiles() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userLog)) {
            if (up.getIdpermiso().getId() == gestionperfiles || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veGestionPerfiles() {
        if (gper == null) {
            gper = new gPerfiles(factory);
        }
        this.pSubpanelAdministrador.removeAll();
        gper.setBounds(0, 0, 492, 449);
        pSubpanelAdministrador.add(gper);
        gper.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelAdministrador.removeAll();
                pSubpanelAdministrador.validate();
                pSubpanelAdministrador.repaint();
            }
        });
        gper.setVisible(true);
        pSubpanelAdministrador.validate();
        pSubpanelAdministrador.repaint();
    }

    private void veCambioClave() {
        final cClave cclave = new cClave(null, true, factory, userLog);
        cclave.setLocationRelativeTo(null);
        cclave.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bGestionPerfiles = new javax.swing.JLabel();
        bCambioClave = new javax.swing.JLabel();
        pSubpanelAdministrador = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(646, 449));
        setMinimumSize(new java.awt.Dimension(646, 449));

        bGestionPerfiles.setBackground(new java.awt.Color(255, 255, 255));
        bGestionPerfiles.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bGestionPerfiles.setForeground(new java.awt.Color(50, 72, 141));
        bGestionPerfiles.setText("  Gestión de Perfiles");
        bGestionPerfiles.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bGestionPerfiles.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bGestionPerfiles.setOpaque(true);
        bGestionPerfiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bGestionPerfilesMouseReleased(evt);
            }
        });

        bCambioClave.setBackground(new java.awt.Color(255, 255, 255));
        bCambioClave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bCambioClave.setForeground(new java.awt.Color(50, 72, 141));
        bCambioClave.setText("  Cambio de Clave");
        bCambioClave.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bCambioClave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bCambioClave.setOpaque(true);
        bCambioClave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bCambioClaveMouseReleased(evt);
            }
        });

        pSubpanelAdministrador.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pSubpanelAdministradorLayout = new javax.swing.GroupLayout(pSubpanelAdministrador);
        pSubpanelAdministrador.setLayout(pSubpanelAdministradorLayout);
        pSubpanelAdministradorLayout.setHorizontalGroup(
            pSubpanelAdministradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        pSubpanelAdministradorLayout.setVerticalGroup(
            pSubpanelAdministradorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bGestionPerfiles, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bCambioClave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pSubpanelAdministrador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bGestionPerfiles, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bCambioClave, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 377, Short.MAX_VALUE))
            .addComponent(pSubpanelAdministrador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bGestionPerfilesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGestionPerfilesMouseReleased
        if (validaGestionPerfiles()) {
            veGestionPerfiles();
            bGestionPerfiles.setForeground(Color.white);
            bGestionPerfiles.setBackground(new Color(50, 72, 141));
            bCambioClave.setForeground(new Color(50, 72, 141));
            bCambioClave.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bGestionPerfilesMouseReleased

    private void bCambioClaveMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bCambioClaveMouseReleased
        veCambioClave();
        bCambioClave.setForeground(Color.white);
        bCambioClave.setBackground(new Color(50, 72, 141));
        bGestionPerfiles.setForeground(new Color(50, 72, 141));
        bGestionPerfiles.setBackground(Color.white);
    }//GEN-LAST:event_bCambioClaveMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bCambioClave;
    private javax.swing.JLabel bGestionPerfiles;
    private javax.swing.JPanel pSubpanelAdministrador;
    // End of variables declaration//GEN-END:variables
}
