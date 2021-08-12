/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelusuarios;

import bohiogym.clases.Funciones;
import bohiogym.paneles.Usuarios;
import bohiogym.paneles.panelarticulos.ventas;
import entity.AsignadosDias;
import entity.GymAsignados;
import entity.GymCongelado;
import entity.GymPlanes;
import entity.GymUsuarios;
import entity.UserLog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import jpa.AsignadosDiasJpaController;
import jpa.GymAsignadosJpaController;
import jpa.GymCongeladoJpaController;
import jpa.GymPlanesJpaController;
import jpa.GymUsuariosJpaController;

/**
 *
 * @author Juan
 */
public class aplanes extends javax.swing.JPanel {
    
    private final Properties props;
    private final UserLog user;
    private final EntityManagerFactory factory;
    GymUsuarios usuarios = null;
    GymUsuariosJpaController gujc = null;
    GymPlanes gymPlanes = null;
    GymPlanesJpaController gpjc = null;
    GymAsignados asignados = null;
    GymAsignadosJpaController gajc = null;
    AsignadosDias asignadosDias = null;
    AsignadosDiasJpaController adjc = null;
    GymCongelado congelado = null;
    GymCongeladoJpaController gcjc = null;

    /**
     * Creates new form aplanes
     */
    public aplanes(EntityManagerFactory factory, Properties props, UserLog user) {
        initComponents();
        this.props = props;
        this.factory = factory;
        this.user = user;
        gujc = new GymUsuariosJpaController(factory);
        gpjc = new GymPlanesJpaController(factory);
        gajc = new GymAsignadosJpaController(factory);
        adjc = new AsignadosDiasJpaController(factory);
        gcjc = new GymCongeladoJpaController(factory);
        tIdentificacion.requestFocus();
        jDateChooser1.setDate(new Date());
        jDateChooser2.setDate(new Date());
        try {
            jDateChooser3.setDate(Funciones.ddMMyyyy.parse("01/01/1900"));
            jDateChooser3.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al iniciar fecha descongelacicon: \n" + ex.getMessage());
        }
        binactivar.setEnabled(false);
        bGuardar.setEnabled(false);
        jCheckBox3.setEnabled(false);
    }
    
    public void cargarUsuario() {
        DecimalFormat df = new DecimalFormat("#");
        if (!tIdentificacion.getText().isEmpty() || tIdentificacion.getText() != null) {
            List<GymUsuarios> us = this.findUsuario(tIdentificacion.getText());
            if (us.size() > 0) {
                usuarios = us.get(0);
                bGuardar.setEnabled(true);
                binactivar.setEnabled(true);
                jLabel2.setText(usuarios.getNombres() + " " + usuarios.getApellidos());
                if (searchAsignados(tIdentificacion.getText()).size() > 0) {
                    jCheckBox3.setEnabled(true);
                    asignados = searchAsignados(tIdentificacion.getText()).get(0);
                    gymPlanes = asignados.getIdplan();
                    jTextField1.setText(asignados.getIdplan().getCodigoservicio());
                    jLabel5.setText(asignados.getIdplan().getNombreservicio());
                    jDateChooser1.setDate(asignados.getFechainicio());
                    jDateChooser2.setDate(asignados.getFechafin());
                    if (asignados.getPagocompleto() == 1) {
                        jCheckBox1.setSelected(true);
                        jCheckBox2.setSelected(false);
                    } else {
                        jCheckBox1.setSelected(false);
                        jCheckBox2.setSelected(true);
                    }
                    jTextField3.setText(df.format(asignados.getAbono()));
                    if (Asignadosd(asignados).size() > 0) {
                        asignadosDias = Asignadosd(asignados).get(0);
                        jTextField2.setText(String.valueOf(asignadosDias.getDias()));
                    }
                    if (asignados.getEstado() == 3) {
                        jCheckBox3.setSelected(true);
                    } else {
                        jCheckBox3.setSelected(false);
                    }
                } else {
                    String mensaje = "El usuario no tiene un plan asignado, ¿desea asignarle uno?";
                    int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Crear plan", JOptionPane.YES_NO_OPTION);
                    if (entrada == 0) {
                        jTextField1.requestFocus();
                        bGuardar.setEnabled(true);
                    } else {
                        limpiar();
                        binactivar.setEnabled(false);
                        bGuardar.setEnabled(false);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "El usuario no existe");
                limpiar();
            }
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
    
    private void buscarPlan() {
        if (gpjc == null) {
            gpjc = new GymPlanesJpaController(factory);
        }
        if (jTextField1.getText() == null || jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para realizar la busqueda");
        } else {
            if (searchPlan(jTextField1.getText()).size() > 0) {
                gymPlanes = searchPlan(jTextField1.getText()).get(0);
                jLabel5.setText(gymPlanes.getNombreservicio());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(jDateChooser1.getDate());
                calendar.add(Calendar.DAY_OF_YEAR, gymPlanes.getDias());
                jDateChooser2.setDate(calendar.getTime());
                bGuardar.setEnabled(true);
                binactivar.setEnabled(true);
            } else {
                String mensaje = "El plan no existe, ¿desea crearlo?";
                int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Cerrar Documento", JOptionPane.YES_NO_OPTION);
                if (entrada == 0) {
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
    
    private void saveAplan() {
        if (usuarios == null) {
            JOptionPane.showMessageDialog(null, "Debe buscar un usuario");
        } else if (gymPlanes == null) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un plan");
        } else {
            if (asignados == null) {
                asignados = new GymAsignados();
                asignados.setIduser(usuarios);
                asignados.setIdplan(gymPlanes);
                asignados.setFechainicio(jDateChooser1.getDate());
                asignados.setFechafin(jDateChooser2.getDate());
                if (jCheckBox1.isSelected()) {
                    asignados.setPagocompleto(1);
                } else {
                    asignados.setPagocompleto(0);
                }
                asignados.setAbono(Float.parseFloat(jTextField3.getText()));
                if (jCheckBox3.isSelected() == true) {
                    asignados.setEstado(3);
                    congelado = new GymCongelado();
                    congelado.setIdasignacion(asignados);
                    congelado.setFechacongela(new Date());
                    congelado.setFechadescongela(jDateChooser3.getDate());
                    congelado.setEstado(1);
                    gcjc.create(congelado);
                } else {
                    asignados.setEstado(1);
                }
                gajc.create(asignados);
                String pal = "TIQUETERA";
                boolean relt = gymPlanes.getNombreservicio().contains(pal);
                if (relt) {
                    if (Integer.valueOf(jTextField2.getText()) > 0) {
                        asignadosDias = new AsignadosDias();
                        asignadosDias.setIdasignacion(asignados);
                        asignadosDias.setDias(Integer.parseInt(jTextField2.getText()));
                        asignadosDias.setEstado(1);
                        adjc.create(asignadosDias);
                        JOptionPane.showMessageDialog(null, "Plan asignado exitosamente");
                        veVentas();
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe añadir dias a la tiquetera");
                        jTextField2.requestFocusInWindow();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Plan asignado exitosamente");
                    veVentas();
                    limpiar();
                }
            } else {
                try {
                    asignados.setIduser(asignados.getIduser());
                    asignados.setIdplan(asignados.getIdplan());
                    asignados.setFechainicio(jDateChooser1.getDate());
                    asignados.setFechafin(jDateChooser2.getDate());
                    if (jCheckBox1.isSelected()) {
                        asignados.setPagocompleto(1);
                    } else {
                        asignados.setPagocompleto(0);
                    }
                    GymAsignados asi = searchAsignados(tIdentificacion.getText()).get(0);
                    asignados.setAbono(Float.parseFloat(jTextField3.getText()));
                    if (jCheckBox3.isSelected() == true) {
                        asignados.setEstado(3);
                        if (obtenerCongelado(asignados).size() > 0) {
                            congelado = obtenerCongelado(asignados).get(obtenerCongelado(asignados).size() - 1);
                            congelado.setFechacongela(new Date());
                            congelado.setFechadescongela(jDateChooser3.getDate());
                            gcjc.edit(congelado);
                        } else {
                            congelado = new GymCongelado();
                            congelado.setIdasignacion(asignados);
                            congelado.setFechacongela(new Date());
                            congelado.setFechadescongela(jDateChooser3.getDate());
                            congelado.setEstado(1);
                            gcjc.create(congelado);
                        }
                    } else {
                        asignados.setEstado(1);
                        if (obtenerCongelado(asignados).size() > 0) {
                            congelado = obtenerCongelado(asignados).get(obtenerCongelado(asignados).size() - 1);
                            int dias = (int) ((new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 0, 0, 0).getTime() - congelado.getFechacongela().getTime()) / 86400000);                            
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(asignados.getFechafin());
                            calendar.add(Calendar.DAY_OF_YEAR, dias);
                            jDateChooser2.setDate(calendar.getTime());
                            asignados.setFechafin(jDateChooser2.getDate());
                        }
                    }
                    gajc.edit(asignados);
                    if (asignadosDias == null) {
                        String pal = "TIQUETERA";
                        boolean relt = gymPlanes.getNombreservicio().contains(pal);
                        if (relt) {
                            if (Integer.valueOf(jTextField2.getText()) > 0) {
                                asignadosDias = new AsignadosDias();
                                asignadosDias.setIdasignacion(asi);
                                asignadosDias.setDias(Integer.parseInt(jTextField2.getText()));
                                asignadosDias.setEstado(1);
                                adjc.create(asignadosDias);
                                JOptionPane.showMessageDialog(null, "Plan asignado exitosamente");
                                limpiar();
                            } else {
                                JOptionPane.showMessageDialog(null, "Debe añadir dias a la tiquetera");
                                jTextField2.requestFocusInWindow();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Plan asignado exitosamente");
                            limpiar();
                        }
                    } else {
                        String pal = "TIQUETERA";
                        boolean relt = gymPlanes.getNombreservicio().contains(pal);
                        if (relt) {
                            if (Integer.valueOf(jTextField2.getText()) > 0) {
                                asignadosDias.setIdasignacion(asi);
                                asignadosDias.setDias(Integer.parseInt(jTextField2.getText()));
                                asignadosDias.setEstado(1);
                                adjc.create(asignadosDias);
                                JOptionPane.showMessageDialog(null, "Plan asignado exitosamente");
                                limpiar();
                            } else {
                                JOptionPane.showMessageDialog(null, "Debe añadir dias a la tiquetera");
                                jTextField2.requestFocusInWindow();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Plan asignado exitosamente");
                            limpiar();
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al acutalizar plan: " + ex.getMessage(), aplanes.class.getName(), JOptionPane.ERROR_MESSAGE);
                }
            }
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
    
    private List<GymAsignados> searchAsignados(String codigo) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymAsignados p WHERE p.iduser.identificacion=:codigo AND (p.estado='1' OR p.estado='3')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("codigo", codigo)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    private List<GymCongelado> obtenerCongelado(GymAsignados asignados) {
        EntityManager em = adjc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymCongelado p WHERE p.idasignacion=:codigo AND p.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("codigo", asignados)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    private void limpiar() {
        usuarios = null;
        gymPlanes = null;
        asignados = null;
        asignadosDias = null;
        tIdentificacion.setText(null);
        jTextField1.setText(null);
        jTextField2.setText("0");
        jTextField3.setText("0");
        jDateChooser1.setDate(new Date());
        jDateChooser2.setDate(new Date());
        jLabel2.setText("...");
        jLabel5.setText("...");
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        jCheckBox3.setSelected(false);
        jCheckBox3.setEnabled(false);
    }
    
    private void inactivar() {
        String mensaje = "Esta a punto de inactivar el plan al usuario.\n¿Esta seguro de desactivar el plan? ";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Crear Usuario", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            if (asignados != null) {
                try {
                    asignados.setEstado(0);
                    gajc.edit(asignados);
                    if (Asignadosd(asignados).size() > 0) {
                        JOptionPane.showMessageDialog(null, "Usuario inactivado exitosamente");
                    }
                    limpiar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + ex.getMessage(), gUsuarios.class.getName(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void veVentas() {
        ventas vent = new ventas(factory, user, props);
        vent.jTextField2.setText(tIdentificacion.getText());
        vent.jTextField4.setText(jTextField1.getText());
        vent.jTextField5.setText("1");
        this.removeAll();
        vent.setBounds(0, 0, 492, 449);
        this.add(vent);
        vent.cargaUser();
        vent.cargaarticulotext();
        vent.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuarios.pSubpanelUsuarios.removeAll();
                Usuarios.pSubpanelUsuarios.validate();
                Usuarios.pSubpanelUsuarios.repaint();
            }
        });
        vent.jTextField4.requestFocus();
        vent.setVisible(true);
        this.validate();
        this.repaint();
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
        tIdentificacion = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        bGuardar = new javax.swing.JButton();
        binactivar = new javax.swing.JButton();
        bSalir = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField3 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();

        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Identificación:");

        tIdentificacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tIdentificacionFocusGained(evt);
            }
        });
        tIdentificacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tIdentificacionKeyPressed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("...");

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

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 72, 141));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Datos del Usuario");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(50, 72, 141));
        jLabel4.setText("Codigo plan:");

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("...");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(50, 72, 141));
        jLabel6.setText("Fecha Inicio:");

        jDateChooser1.setForeground(new java.awt.Color(102, 204, 0));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(50, 72, 141));
        jLabel7.setText("Fecha Fin:");

        jDateChooser2.setForeground(new java.awt.Color(102, 204, 0));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(50, 72, 141));
        jLabel8.setText("Dias Tiquera:");

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField2.setText("0");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
        });

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(50, 72, 141));
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Pago Completo");
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox1.setFocusable(false);

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox2.setForeground(new java.awt.Color(50, 72, 141));
        jCheckBox2.setText("Abono");
        jCheckBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox2.setFocusable(false);

        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.setText("0");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
        });

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jCheckBox3.setForeground(new java.awt.Color(50, 72, 141));
        jCheckBox3.setText("Congelar");
        jCheckBox3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox3.setFocusable(false);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jDateChooser3.setForeground(new java.awt.Color(102, 204, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bGuardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tIdentificacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 6, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(binactivar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jCheckBox2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tIdentificacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tIdentificacionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarUsuario();
        }
    }//GEN-LAST:event_tIdentificacionKeyPressed

    private void bGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGuardarMouseReleased
        if (bGuardar.isEnabled()) {
            saveAplan();
        }
    }//GEN-LAST:event_bGuardarMouseReleased

    private void binactivarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_binactivarMouseReleased
        if (binactivar.isEnabled()) {
            inactivar();
        }
    }//GEN-LAST:event_binactivarMouseReleased

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarPlan();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void tIdentificacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tIdentificacionFocusGained
        tIdentificacion.selectAll();
    }//GEN-LAST:event_tIdentificacionFocusGained

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.selectAll();
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        jTextField2.selectAll();
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
        jTextField3.selectAll();
    }//GEN-LAST:event_jTextField3FocusGained

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        String mensaje = "Esta a punto de congelar/reanudar la membresia usuario.\n¿Esta seguro? ";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Congelar", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            if (jCheckBox3.isSelected()) {
                jCheckBox3.setSelected(true);
                jDateChooser3.setEnabled(true);
            } else {
                jDateChooser3.setEnabled(false);
                jCheckBox3.setSelected(false);
            }
        } else {
            if (jCheckBox3.isSelected()) {
                jDateChooser3.setEnabled(false);
                jCheckBox3.setSelected(false);
            } else {
                jCheckBox3.setSelected(true);
                jDateChooser3.setEnabled(true);
            }
        }
    }//GEN-LAST:event_jCheckBox3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bGuardar;
    public javax.swing.JButton bSalir;
    private javax.swing.JButton binactivar;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    public javax.swing.JTextField tIdentificacion;
    // End of variables declaration//GEN-END:variables
}
