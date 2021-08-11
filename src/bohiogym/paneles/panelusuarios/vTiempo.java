/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelusuarios;

import bohiogym.clases.Funciones;
import static bohiogym.paneles.Usuarios.pSubpanelUsuarios;
import bohiogym.paneles.panelusuarios.subpanel.creditoUser;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import entity.AsignadosDias;
import entity.GymAsignados;
import entity.GymCongelado;
import entity.GymContdias;
import entity.GymPlanes;
import entity.GymUsuarios;
import entity.UserLog;
import entity.VariedadesVentas;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import jpa.AsignadosDiasJpaController;
import jpa.GymAsignadosJpaController;
import jpa.GymContdiasJpaController;
import jpa.GymPlanesJpaController;
import jpa.GymUsuariosJpaController;
import jpa.VariedadesVentasJpaController;

/**
 *
 * @author Alvarez
 */
public class vTiempo extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    private final UserLog user;
    GymUsuarios usuarios = null;
    GymUsuariosJpaController gujc = null;
    GymPlanes gymPlanes = null;
    GymPlanesJpaController gpjc = null;
    GymAsignados asignados = null;
    GymAsignadosJpaController gajc = null;
    AsignadosDias asignadosDias = null;
    AsignadosDiasJpaController adjc = null;
    GymContdiasJpaController gcjc = null;
    VariedadesVentas ventas = null;
    VariedadesVentasJpaController vvjc = null;
    private final Properties props;
    String iduser = null;

    /**
     * Creates new form vTiempo
     */
    public vTiempo(EntityManagerFactory factory, Properties props, UserLog user) {
        initComponents();
        this.props = props;
        this.factory = factory;
        this.user = user;
        gujc = new GymUsuariosJpaController(factory);
        gpjc = new GymPlanesJpaController(factory);
        gajc = new GymAsignadosJpaController(factory);
        adjc = new AsignadosDiasJpaController(factory);
        gcjc = new GymContdiasJpaController(factory);
        vvjc = new VariedadesVentasJpaController(factory);
    }

    public void verificarUser() {
        if (!tIdentifiacion.getText().isEmpty() || tIdentifiacion.getText() != null) {
            List<GymUsuarios> us = this.findUsuario(tIdentifiacion.getText());
            if (us.size() > 0) {
                usuarios = us.get(0);
                iduser = usuarios.getIdentificacion();
                jLabel3.setText(usuarios.getIdentificacion() + " - " + usuarios.getNombres() + " " + usuarios.getApellidos());
                if (searchAsignados(tIdentifiacion.getText()).size() > 0) {
                    asignados = searchAsignados(tIdentifiacion.getText()).get(searchAsignados(tIdentifiacion.getText()).size() - 1);
                    gymPlanes = asignados.getIdplan();
                    ImageIcon icon;
                    jLabel2.setText(gymPlanes.getNombreservicio());
                    if (asignados.getPagocompleto() == 1) {
                        jLabel6.setText("");
                        if (obtenerVenta(iduser).size() > 0) {
                            jLabel6.setForeground(Color.red);
                            jLabel6.setText("El usuario tiene creditos pendientes por pagar");
                        } else {
                            jLabel6.setForeground(new Color(50, 72, 141));
                            jLabel6.setText("El usuario no tiene creditos pendientes por pagar");
                        }
                    } else {
                        float pago = 0, debe = 0;
                        DecimalFormat formato = new DecimalFormat("#,###.00");
                        pago = asignados.getAbono();
                        debe = asignados.getIdplan().getValor() - pago;
                        jLabel6.setText("El usuario tiene una deuda de: $" + formato.format(debe));
                        if (obtenerVenta(iduser).size() > 0) {
                            jLabel6.setForeground(Color.red);
                            jLabel6.setText("El usuario tiene creditos pendientes por pagar");
                        } else {
                            jLabel6.setForeground(new Color(50, 72, 141));
                            jLabel6.setText("El usuario no tiene creditos pendientes por pagar");
                        }
                    }
                    if (asignados.getEstado() == 1) {
                        icon = new ImageIcon(getClass().getResource("/recursos/Avaliable 150x150.png"));
                        jLabel4.setIcon(icon);
                        jLabel5.setForeground(new Color(0, 153, 51));
                        if (Asignadosd(asignados).size() > 0) {
                            jLabel4.setEnabled(true);
                            asignadosDias = Asignadosd(asignados).get(Asignadosd(asignados).size() - 1);
                            jLabel5.setText("El Usuario tiene la membresia activa con " + asignadosDias.getDias() + " días disponibles");
                            if (obtenerVenta(iduser).size() > 0) {
                                jLabel6.setForeground(Color.red);
                                jLabel6.setText("El usuario tiene creditos pendientes por pagar");
                            } else {
                                jLabel6.setForeground(new Color(50, 72, 141));
                                jLabel6.setText("El usuario no tiene creditos pendientes por pagar");
                            }
                            tIdentifiacion.requestFocusInWindow();
                            tIdentifiacion.setText("");
                            tIdentifiacion.selectAll();
                        } else {
                            jLabel4.setEnabled(false);
                            jLabel5.setText("El Usuario tiene la membresia activa y vence el día: " + Funciones.ddMMyyyy.format(asignados.getFechafin()));
                            if (obtenerVenta(iduser).size() > 0) {
                                jLabel6.setForeground(Color.red);
                                jLabel6.setText("El usuario tiene creditos pendientes por pagar");
                            } else {
                                jLabel6.setForeground(new Color(50, 72, 141));
                                jLabel6.setText("El usuario no tiene creditos pendientes por pagar");
                            }
                            tIdentifiacion.requestFocusInWindow();
                            tIdentifiacion.setText("");
                            tIdentifiacion.selectAll();
                        }
                    } else if (asignados.getEstado() == 2) {
                        icon = new ImageIcon(getClass().getResource("/recursos/Unavaliable 150x150.png"));
                        jLabel4.setIcon(icon);
                        jLabel5.setForeground(new Color(179, 80, 72));
                        if (Asignadosd(asignados).size() > 0) {
                            asignadosDias = Asignadosd(asignados).get(Asignadosd(asignados).size() - 1);
                            jLabel5.setText("El Usuario tiene la membresia vencida le quedaron " + asignadosDias.getDias() + " días disponibles");
                            tIdentifiacion.requestFocusInWindow();
                            tIdentifiacion.setText("");
                            tIdentifiacion.selectAll();
                        } else {
                            jLabel5.setText("El Usuario tiene la membresia vencida desde el día: " + Funciones.ddMMyyyy.format(asignados.getFechafin()));
                            tIdentifiacion.requestFocusInWindow();
                            tIdentifiacion.setText("");
                            tIdentifiacion.selectAll();
                        }
                        String mensaje = "¿Desea renovar la membresia del usuario?";
                        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Renovar plan", JOptionPane.YES_NO_OPTION);
                        if (entrada == 0) {
                            veAPlanes();
                        }
                    } else if (asignados.getEstado() == 3) {
                        icon = new ImageIcon(getClass().getResource("/recursos/Unavaliable 150x150.png"));
                        jLabel4.setIcon(icon);
                        jLabel4.setEnabled(false);
                        jLabel5.setForeground(new Color(179, 80, 72));
                        jLabel5.setText("El Usuario tiene la membresia Congelada temporalmente");
                        tIdentifiacion.requestFocusInWindow();
                        tIdentifiacion.setText("");
                        tIdentifiacion.selectAll();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El usuario no tiene un plan asignado");
                    tIdentifiacion.requestFocusInWindow();
                    tIdentifiacion.setText("");
                    tIdentifiacion.selectAll();
                }
            } else {
                JOptionPane.showMessageDialog(null, "El usuario no existe");
                tIdentifiacion.requestFocusInWindow();
                tIdentifiacion.setText("");
                tIdentifiacion.selectAll();
            }
        }
    }

    private List<VariedadesVentas> obtenerVenta(String documento) {
        EntityManager em = vvjc.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM VariedadesVentas f WHERE f.codcliente=:idusua AND f.estado='3'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("idusua", documento)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymAsignados> searchAsignados(String codigo) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymAsignados p WHERE p.iduser.identificacion=:codigo AND (p.estado<>'0') ORDER BY p.fechafin ASC")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("codigo", codigo)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymUsuarios> findUsuario(String nit) {
        EntityManager em = gujc.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM GymUsuarios u WHERE u.identificacion=:nit AND u.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("nit", nit)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<AsignadosDias> Asignadosd(GymAsignados asignados) {
        EntityManager em = adjc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM AsignadosDias p WHERE p.idasignacion=:codigo AND p.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("codigo", asignados)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void restardia() {
        if (asignadosDias != null) {
            String mensaje = "Esta a punto de restar un dia de membresia al usuario.\n¿Esta seguro? ";
            int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Restar día", JOptionPane.YES_NO_OPTION);
            if (entrada == 0) {
                try {
                    asignadosDias.setDias(asignadosDias.getDias() - 1);
                    adjc.edit(asignadosDias);
                    GymContdias gc = new GymContdias();
                    gc.setIdasignacion(asignados);
                    gc.setIdusuario(usuarios);
                    gc.setFechadia(new Date());
                    gc.setEstado(1);
                    gcjc.create(gc);
                    JOptionPane.showMessageDialog(null, "Actualizado");
                    verificarUser();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al restar dia: " + ex.getMessage(), vTiempo.class.getName(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void veAPlanes() {
        aplanes planes = new aplanes(factory, props, user);
        planes.tIdentificacion.setText(iduser);
        pSubpanelUsuarios.removeAll();
        planes.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(planes);
        planes.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        planes.tIdentificacion.requestFocus();
        planes.cargarUsuario();
        planes.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
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
        tIdentifiacion = new javax.swing.JTextField();
        bBuscar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bSalir = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Identificación:");

        tIdentifiacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tIdentifiacionFocusGained(evt);
            }
        });
        tIdentifiacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tIdentifiacionKeyPressed(evt);
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
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("...");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("...");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel4MouseReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("...");

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        jLabel6.setForeground(new java.awt.Color(255, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("...");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel6MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tIdentifiacion, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bBuscar)
                        .addGap(0, 184, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                            .addComponent(tIdentifiacion)
                            .addComponent(bBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tIdentifiacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tIdentifiacionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            verificarUser();
        }
    }//GEN-LAST:event_tIdentifiacionKeyPressed

    private void jLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseReleased
        restardia();
    }//GEN-LAST:event_jLabel4MouseReleased

    private void bBuscarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseReleased
        verificarUser();
    }//GEN-LAST:event_bBuscarMouseReleased

    private void tIdentifiacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tIdentifiacionFocusGained
        tIdentifiacion.selectAll();
    }//GEN-LAST:event_tIdentifiacionFocusGained

    private void jLabel6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseReleased
        final creditoUser credit = new creditoUser(null, true, factory, iduser);
        credit.setLocationRelativeTo(null);
        credit.setVisible(true);
    }//GEN-LAST:event_jLabel6MouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bBuscar;
    public javax.swing.JButton bSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTextField tIdentifiacion;
    // End of variables declaration//GEN-END:variables
}
