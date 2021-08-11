/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelusuarios;

import bohiogym.clases.Database;
import bohiogym.clases.Funciones;
import bohiogym.paneles.panelusuarios.subpanel.huellad;
import bohiogym.principal;
import static bohiogym.principal.TEMPLATE_PROPERTY;
import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import entity.GymUsuarios;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import jpa.GymUsuariosJpaController;

/**
 *
 * @author Juan
 */
public class gUsuarios extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    GymUsuarios usuarios = null;
    GymUsuariosJpaController gujc = null;
    GymUsuarios gymhuellauser = null;
    private final Properties props;

    /**
     * Creates new form gUsuarios
     */
    public gUsuarios(EntityManagerFactory factory, Properties props) {
        initComponents();
        
        this.props = props;
        this.factory = factory;
        bGuardar.setEnabled(false);
        binactivar.setEnabled(false);
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

    private void cargarUsuario() {
        if (gujc == null) {
            gujc = new GymUsuariosJpaController(factory);
        }
        if (!tIdentificacion.getText().isEmpty() || tIdentificacion.getText() != null) {
            List<GymUsuarios> us = this.findUsuario(tIdentificacion.getText());
            if (us.size() > 0) {
                usuarios = us.get(0);
                bGuardar.setEnabled(true);
                binactivar.setEnabled(true);
                tNombres.setText(usuarios.getNombres());
                tApellidos.setText(usuarios.getApellidos());
                tDireccion.setText(usuarios.getDireccion());
                tEmail.setText(usuarios.getEmail());
                tTelefono.setText(usuarios.getTelefono());
                cTipoId.setSelectedItem(usuarios.getTipodoc());
                dFechanace.setDate(usuarios.getFechanacimiento());
                ledad.setText(String.valueOf(Funciones.calcularEdad(dFechanace.getCalendar())) + " Años");
            } else {
                String mensaje = "El usuario no existe, ¿desea crearlo?";
                int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Cerrar Documento", JOptionPane.YES_NO_OPTION);
                if (entrada == 0) {
                    bGuardar.setEnabled(true);
                    tNombres.requestFocusInWindow();
                } else {
                    limpiar();
                    binactivar.setEnabled(false);
                    bGuardar.setEnabled(false);
                }
            }
        }
    }

    private void guardarUsuario() {
        if (gujc == null) {
            gujc = new GymUsuariosJpaController(factory);
        }
        if (usuarios == null) {
            if (tIdentificacion.getText() == null || tIdentificacion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un numero de identificacion");
                tIdentificacion.requestFocus();
            } else if (tNombres.getText() == null || tNombres.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre de usuario");
                tNombres.requestFocus();
            } else if (tApellidos.getText() == null || tApellidos.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un apellido para el usuario");
                tApellidos.requestFocus();
            } else {
                usuarios = new GymUsuarios();
                usuarios.setTipodoc(cTipoId.getSelectedItem().toString());
                usuarios.setIdentificacion(tIdentificacion.getText());
                usuarios.setNombres(tNombres.getText().toString().toUpperCase());
                usuarios.setApellidos(tApellidos.getText().toString().toUpperCase());
                usuarios.setFechanacimiento(dFechanace.getDate());
                usuarios.setTelefono(tTelefono.getText().toString());
                usuarios.setEmail(tEmail.getText().toString());
                usuarios.setDireccion(tDireccion.getText().toString().toUpperCase());
                usuarios.setEstado(1);
                gujc.create(usuarios);
                JOptionPane.showMessageDialog(null, "Usuario creado exitosamente");
                limpiar();
            }
        } else {
            try {
                if (tIdentificacion.getText() == null || tIdentificacion.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un numero de identificacion");
                    tIdentificacion.requestFocus();
                } else if (tNombres.getText() == null || tNombres.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un nombre de usuario");
                    tNombres.requestFocus();
                } else if (tApellidos.getText() == null || tApellidos.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un apellido para el usuario");
                    tApellidos.requestFocus();
                } else {
                    usuarios.setTipodoc(cTipoId.getSelectedItem().toString());
                    usuarios.setIdentificacion(tIdentificacion.getText());
                    usuarios.setNombres(tNombres.getText().toString().toUpperCase());
                    usuarios.setApellidos(tApellidos.getText().toString().toUpperCase());
                    usuarios.setFechanacimiento(dFechanace.getDate());
                    usuarios.setTelefono(tTelefono.getText().toString());
                    usuarios.setEmail(tEmail.getText().toString());
                    usuarios.setDireccion(tDireccion.getText().toString().toUpperCase());
                    usuarios.setEstado(1);
                    gujc.edit(usuarios);
                    JOptionPane.showMessageDialog(null, "Usuario modificado exitosamente");
                    limpiar();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + ex.getMessage(), gUsuarios.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void inactivar() {
        String mensaje = "Esta a punto de inactivar el usuario seleccionado.\n¿Esta seguro de desactivar el usuario? ";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Crear Usuario", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            if (gujc == null) {
                gujc = new GymUsuariosJpaController(factory);
            }
            try {
                usuarios.setEstado(0);
                gujc.edit(usuarios);
                JOptionPane.showMessageDialog(null, "Usuario inactivado exitosamente");
                limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + ex.getMessage(), gUsuarios.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiar() {
        usuarios = null;
        tIdentificacion.setText(null);
        tNombres.setText(null);
        tApellidos.setText(null);
        tDireccion.setText(null);
        tEmail.setText(null);
        tTelefono.setText(null);
        cTipoId.setSelectedIndex(0);
        dFechanace.setDate(null);
        ledad.setText(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tIdentificacion = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        cTipoId = new javax.swing.JComboBox<>();
        bBuscar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tNombres = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tApellidos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        dFechanace = new com.toedter.calendar.JDateChooser();
        bEdad = new javax.swing.JLabel();
        ledad = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tTelefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tEmail = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tDireccion = new javax.swing.JTextField();
        bGuardar = new javax.swing.JButton();
        bSalir = new javax.swing.JButton();
        binactivar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Identificación:");

        tIdentificacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tIdentificacionKeyPressed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        cTipoId.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cTipoId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CC", "CE", "TD", "P" }));
        cTipoId.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cTipoId.setFocusable(false);

        bBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        bBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bBuscarMouseReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(50, 72, 141));
        jLabel2.setText("Nombres:");

        tNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tNombresKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(50, 72, 141));
        jLabel4.setText("Apellidos:");

        tApellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tApellidosKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(50, 72, 141));
        jLabel5.setText("Fecha Nacimiento:");

        dFechanace.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        bEdad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bEdad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/time.png"))); // NOI18N
        bEdad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        ledad.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ledad.setForeground(new java.awt.Color(0, 102, 153));
        ledad.setText("Edad Años");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(50, 72, 141));
        jLabel8.setText("Telefono:");

        tTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tTelefonoKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(50, 72, 141));
        jLabel9.setText("Email:");

        tEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tEmailKeyPressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(50, 72, 141));
        jLabel10.setText("Dirección:");

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

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconfinder_edit-clear_118917 (1).png"))); // NOI18N
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel3MouseReleased(evt);
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
                    .addComponent(tNombres)
                    .addComponent(tApellidos)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dFechanace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bEdad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ledad, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tEmail)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cTipoId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tIdentificacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bBuscar)
                        .addGap(152, 152, 152))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tDireccion)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(bGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(binactivar, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(tIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cTipoId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tNombres, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tApellidos, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tTelefono)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(bEdad, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                    .addComponent(dFechanace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(ledad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(bSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(binactivar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bBuscarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseReleased
        cargarUsuario();
    }//GEN-LAST:event_bBuscarMouseReleased

    private void bGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGuardarMouseReleased
        if (bGuardar.isEnabled()) {
            guardarUsuario();
        }
    }//GEN-LAST:event_bGuardarMouseReleased

    private void binactivarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_binactivarMouseReleased
        if (binactivar.isEnabled()) {
            inactivar();
        }
    }//GEN-LAST:event_binactivarMouseReleased

    private void tIdentificacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tIdentificacionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarUsuario();
        }
    }//GEN-LAST:event_tIdentificacionKeyPressed

    private void tNombresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tNombresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tApellidos.requestFocus();
        }
    }//GEN-LAST:event_tNombresKeyPressed

    private void tApellidosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tApellidosKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tTelefono.requestFocus();
        }
    }//GEN-LAST:event_tApellidosKeyPressed

    private void tTelefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tTelefonoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tEmail.requestFocus();
        }
    }//GEN-LAST:event_tTelefonoKeyPressed

    private void tEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tEmailKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tDireccion.requestFocus();
        }
    }//GEN-LAST:event_tEmailKeyPressed

    private void jLabel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseReleased
        limpiar();
    }//GEN-LAST:event_jLabel3MouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bBuscar;
    private javax.swing.JLabel bEdad;
    private javax.swing.JButton bGuardar;
    public javax.swing.JButton bSalir;
    private javax.swing.JButton binactivar;
    private javax.swing.JComboBox<String> cTipoId;
    private com.toedter.calendar.JDateChooser dFechanace;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel ledad;
    private javax.swing.JTextField tApellidos;
    private javax.swing.JTextField tDireccion;
    private javax.swing.JTextField tEmail;
    private javax.swing.JTextField tIdentificacion;
    private javax.swing.JTextField tNombres;
    private javax.swing.JTextField tTelefono;
    // End of variables declaration//GEN-END:variables
}
