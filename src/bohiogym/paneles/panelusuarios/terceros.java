/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelusuarios;

import entity.GymTerceros;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import jpa.GymTercerosJpaController;

/**
 *
 * @author Alvarez
 */
public class terceros extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    GymTerceros gymTerceros = null;
    GymTercerosJpaController gtjc = null;

    /**
     * Creates new form terceros
     */
    public terceros(EntityManagerFactory factory) {
        initComponents();
        this.factory = factory;
        bGuardar.setEnabled(false);
        bInactivar.setEnabled(false);
    }

    private void buscarTercero() {
        if (gtjc == null) {
            gtjc = new GymTercerosJpaController(factory);
        }
        if (jTextField1.getText() != null || !jTextField1.getText().isEmpty()) {
            if (obtenerTercero(jTextField1.getText()).size() > 0) {
                gymTerceros = obtenerTercero(jTextField1.getText()).get(0);
                jTextField2.setText(gymTerceros.getRazonsocial());
                jTextField3.setText(gymTerceros.getDireccion());
                jTextField4.setText(gymTerceros.getTelefono());
                jComboBox1.setSelectedItem(gymTerceros.getRazonsocial());
                bGuardar.setEnabled(true);
                bInactivar.setEnabled(true);
            } else {
                String mensaje = "El Tercero no existe, ¿desea crearlo?";
                int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Cerrar Documento", JOptionPane.YES_NO_OPTION);
                if (entrada == 0) {
                    bGuardar.setEnabled(true);
                } else {
                    bGuardar.setEnabled(false);
                    bInactivar.setEnabled(false);
                    limpiar();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar un numero de identificacion para realizar la busqueda");
        }
    }

    private List<GymTerceros> obtenerTercero(String nit) {
        EntityManager em = gtjc.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM GymTerceros t WHERE t.nit=:nit AND t.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("nit", nit)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void crearTercero() {
        if (gtjc == null) {
            gtjc = new GymTercerosJpaController(factory);
        }
        if (gymTerceros == null) {
            if (jTextField1.getText() == null || jTextField1.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un numero de identificacion");
            } else if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar una razon social");
            } else {
                gymTerceros = new GymTerceros();
                gymTerceros.setNit(jTextField1.getText());
                gymTerceros.setRazonsocial(jTextField2.getText().toUpperCase());
                gymTerceros.setDireccion(jTextField3.getText().toUpperCase());
                gymTerceros.setTelefono(jTextField4.getText());
                gymTerceros.setRegimen(jComboBox1.getSelectedItem().toString().toUpperCase());
                gymTerceros.setEstado(1);
                gtjc.create(gymTerceros);
                JOptionPane.showMessageDialog(null, "Tercero creado correctamente");
                limpiar();
            }
        } else {
            try {
                gymTerceros.setNit(jTextField1.getText());
                gymTerceros.setRazonsocial(jTextField2.getText().toUpperCase());
                gymTerceros.setDireccion(jTextField3.getText().toUpperCase());
                gymTerceros.setTelefono(jTextField4.getText());
                gymTerceros.setRegimen(jComboBox1.getSelectedItem().toString().toUpperCase());
                gymTerceros.setEstado(1);
                gtjc.edit(gymTerceros);
                JOptionPane.showMessageDialog(null, "Tercero actualizado correctamente");
                limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + ex.getMessage(), terceros.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void inactivar() {
        String mensaje = "Esta a punto de inactivar el tercero seleccionado.\n¿Esta seguro de desactivar el tercero? ";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Crear Tercero", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            if (gtjc == null) {
                gtjc = new GymTercerosJpaController(factory);
            }
            try {
                gymTerceros.setEstado(0);
                gtjc.edit(gymTerceros);
                JOptionPane.showMessageDialog(null, "Tercero inactivado correctamente");
                limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + ex.getMessage(), terceros.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiar() {
        gymTerceros = null;
        jTextField1.setText(null);
        jTextField2.setText(null);
        jTextField3.setText(null);
        jTextField4.setText(null);
        jComboBox1.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        bSalir = new javax.swing.JButton();
        bBuscar = new javax.swing.JLabel();
        bGuardar = new javax.swing.JButton();
        bInactivar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Identificación:");

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(50, 72, 141));
        jLabel2.setText("Razón social:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 72, 141));
        jLabel3.setText("Dirección:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(50, 72, 141));
        jLabel4.setText("Télefono:");

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COMUN", "SIMPLIFICADO", "ESPECIAL" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox1.setFocusable(false);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(50, 72, 141));
        jLabel5.setText("Régimen:");

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        bBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        bBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bBuscarMouseReleased(evt);
            }
        });

        bGuardar.setBackground(new java.awt.Color(50, 72, 141));
        bGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bGuardar.setForeground(new java.awt.Color(255, 255, 255));
        bGuardar.setText("Guardar");
        bGuardar.setContentAreaFilled(false);
        bGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bGuardar.setFocusable(false);
        bGuardar.setOpaque(true);
        bGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bGuardarMouseReleased(evt);
            }
        });

        bInactivar.setBackground(new java.awt.Color(50, 72, 141));
        bInactivar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bInactivar.setForeground(new java.awt.Color(255, 255, 255));
        bInactivar.setText("Inactivar");
        bInactivar.setContentAreaFilled(false);
        bInactivar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bInactivar.setFocusable(false);
        bInactivar.setOpaque(true);
        bInactivar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bInactivarMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2)
                    .addComponent(jTextField3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jComboBox1, 0, 229, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bInactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bBuscar)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addGap(180, 180, 180)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bInactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bBuscarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseReleased
        buscarTercero();
    }//GEN-LAST:event_bBuscarMouseReleased

    private void bGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGuardarMouseReleased
        if (bGuardar.isEnabled()) {
            crearTercero();
        }
    }//GEN-LAST:event_bGuardarMouseReleased

    private void bInactivarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bInactivarMouseReleased
        if(bInactivar.isEnabled()){
            inactivar();
        }
    }//GEN-LAST:event_bInactivarMouseReleased

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarTercero();
        }
    }//GEN-LAST:event_jTextField1KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bBuscar;
    public javax.swing.JButton bGuardar;
    public javax.swing.JButton bInactivar;
    public javax.swing.JButton bSalir;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
