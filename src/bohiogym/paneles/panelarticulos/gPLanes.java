/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelarticulos;

import entity.GymPlanes;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import jpa.GymPlanesJpaController;

/**
 *
 * @author Alvarez
 */
public class gPLanes extends javax.swing.JPanel {
    
    private final EntityManagerFactory factory;
    public GymPlanes gymPlanes = null;
    GymPlanesJpaController gpjc = null;

    /**
     * Creates new form gPLanes
     */
    public gPLanes(EntityManagerFactory factory) {
        initComponents();
        this.factory = factory;
        bGuardar.setEnabled(false);
        binactivar.setEnabled(false);
    }
    
    private void buscarPlan() {
        if (gpjc == null) {
            gpjc = new GymPlanesJpaController(factory);
        }
        if (jTextField1.getText() == null || jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para realizar la busqueda");
        } else {
            if (searchPlan(jTextField1.getText()).size() > 0) {
                gymPlanes = searchPlan(jTextField1.getText()).get(0);
                jTextField2.setText(gymPlanes.getNombreservicio());
                jTextField3.setText(String.valueOf(gymPlanes.getValor()));
                jTextField4.setText(String.valueOf(gymPlanes.getDias()));
                bGuardar.setEnabled(true);
                binactivar.setEnabled(true);
            } else {
                String mensaje = "El plan no existe, ¿desea crearlo?";
                int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Cerrar Documento", JOptionPane.YES_NO_OPTION);
                if (entrada == 0) {
                    jTextField2.requestFocus();
                    bGuardar.setEnabled(true);
                    gymPlanes = null;
                } else {
                    limpiar();
                    binactivar.setEnabled(false);
                    bGuardar.setEnabled(false);
                }
            }
        }
    }
    
    private void guardarPlan() {
        if (gpjc == null) {
            gpjc = new GymPlanesJpaController(factory);
        }
        if (gymPlanes == null) {
            if (jTextField1.getText() == null || jTextField1.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para porder continuar");
                jTextField1.requestFocus();
            } else if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para este plan");
            } else if (jTextField3.getText() == null || jTextField3.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un precio para este plan");
            } else {
                gymPlanes = new GymPlanes();
                gymPlanes.setCodigoservicio(jTextField1.getText());
                gymPlanes.setNombreservicio(jTextField2.getText().toUpperCase());
                gymPlanes.setValor(Float.parseFloat(jTextField3.getText()));
                gymPlanes.setDias(Integer.parseInt(jTextField4.getText()));
                gymPlanes.setEstado(1);
                gpjc.create(gymPlanes);
                JOptionPane.showMessageDialog(null, "Plan creado correctamente");
                limpiar();
            }
        } else {
            try {
                if (jTextField1.getText() == null || jTextField1.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para porder continuar");
                    jTextField1.requestFocus();
                } else if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para este plan");
                } else if (jTextField3.getText() == null || jTextField3.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un precio para este plan");
                } else {
                    gymPlanes.setCodigoservicio(jTextField1.getText());
                    gymPlanes.setNombreservicio(jTextField2.getText().toUpperCase());
                    gymPlanes.setValor(Float.parseFloat(jTextField3.getText()));
                    gymPlanes.setDias(Integer.parseInt(jTextField4.getText()));
                    gymPlanes.setEstado(1);
                    gpjc.edit(gymPlanes);
                    JOptionPane.showMessageDialog(null, "Plan modificado correctamente");
                    limpiar();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al modificar plan: " + ex.getLocalizedMessage(), gPLanes.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void Inactivar() {
        String mensaje = "Esta a punto de inactivar el plan seleccionado.\n¿Esta seguro de desactivar el plan? ";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Crear Usuario", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            if (gpjc == null) {
                gpjc = new GymPlanesJpaController(factory);
            }
            try {
                gymPlanes.setEstado(0);
                gpjc.edit(gymPlanes);
                JOptionPane.showMessageDialog(null, "Plan inactivado correctamente");
                limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al inactivar plan: " + ex.getLocalizedMessage(), gPLanes.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private List<GymPlanes> searchPlan(String codigo) {
        EntityManager em = gpjc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymPlanes p WHERE p.codigoservicio=:codigo AND p.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("codigo", codigo)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    private void limpiar() {
        jTextField1.setText(null);
        jTextField2.setText(null);
        jTextField3.setText(null);
        jTextField4.setText(null);
        gymPlanes = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        bBuscar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        bSalir = new javax.swing.JButton();
        binactivar = new javax.swing.JButton();
        bGuardar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Código Plan:");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        bBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        bBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bBuscarMouseReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(50, 72, 141));
        jLabel2.setText("Nombre Plan:");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 72, 141));
        jLabel3.setText("Precio:");

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(51, 102, 255));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 102, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("$");

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        binactivar.setBackground(new java.awt.Color(50, 72, 141));
        binactivar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        binactivar.setForeground(new java.awt.Color(255, 255, 255));
        binactivar.setText("Inactivar");
        binactivar.setContentAreaFilled(false);
        binactivar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        binactivar.setFocusable(false);
        binactivar.setOpaque(true);
        binactivar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                binactivarMouseReleased(evt);
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

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(50, 72, 141));
        jLabel5.setText("Dias:");

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(51, 102, 255));
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bBuscar))
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1)
                            .addComponent(bBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addGap(180, 180, 180)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void binactivarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_binactivarMouseReleased
        if (binactivar.isEnabled()) {
            Inactivar();
        }
    }//GEN-LAST:event_binactivarMouseReleased

    private void bGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGuardarMouseReleased
        if (bGuardar.isEnabled()) {
            guardarPlan();
        }
    }//GEN-LAST:event_bGuardarMouseReleased

    private void bBuscarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseReleased
        buscarPlan();
    }//GEN-LAST:event_bBuscarMouseReleased

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarPlan();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField3.requestFocus();
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField4.requestFocus();
        }
    }//GEN-LAST:event_jTextField3KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bBuscar;
    public javax.swing.JButton bGuardar;
    public javax.swing.JButton bSalir;
    public javax.swing.JButton binactivar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTextField jTextField1;
    public javax.swing.JTextField jTextField2;
    public javax.swing.JTextField jTextField3;
    public javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
