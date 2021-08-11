/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym;

import bohiogym.clases.Database;
import bohiogym.clases.Funciones;
import bohiogym.clases.SendMail;
import bohiogym.paneles.Administrador;
import bohiogym.paneles.Articulos;
import bohiogym.paneles.Informes;
import bohiogym.paneles.Usuarios;
import bohiogym.paneles.message;
import bohiogym.paneles.panelusuarios.subpanel.huellad;
import bohiogym.paneles.panelusuarios.vTiempo;
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
import entity.AsignadosDias;
import entity.GymAsignados;
import entity.GymCongelado;
import entity.GymUsuarios;
import entity.MailMensaje;
import entity.MailSend;
import entity.UserLog;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import jpa.AsignadosDiasJpaController;
import jpa.GymAsignadosJpaController;
import jpa.GymCongeladoJpaController;
import jpa.GymUsuariosJpaController;
import jpa.MailMensajeJpaController;
import jpa.MailSendJpaController;

/**
 *
 * @author Juan
 */
public class principal extends javax.swing.JFrame {

    private final EntityManagerFactory factory;
    private final UserLog userLog;
    private final Properties props;
    Usuarios usuarios = null;
    Articulos articulos = null;
    Informes contabilidad = null;
    Administrador administrador = null;
    private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();
    private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();
    private DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();
    private DPFPTemplate template;
    public static String TEMPLATE_PROPERTY = "template";
    GymUsuarios gymhuellauser = null;
    MailSend mailSend = null;
    MailSendJpaController msjc = null;
    GymUsuariosJpaController gujc = null;
    AsignadosDiasJpaController adjc = null;
    GymAsignadosJpaController gajc = null;
    MailMensaje mensaje = null;
    MailMensajeJpaController mmjc = null;
    GymCongelado congelado = null;
    GymCongeladoJpaController gcjc = null;

    /**
     * Creates new form principal
     */
    public principal(EntityManagerFactory factory, UserLog userLog, Properties props) {
        initComponents();
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.props = props;
        this.factory = factory;
        this.userLog = userLog;
        gujc = new GymUsuariosJpaController(factory);
        gajc = new GymAsignadosJpaController(factory);
        adjc = new AsignadosDiasJpaController(factory);
        msjc = new MailSendJpaController(factory);
        mmjc = new MailMensajeJpaController(factory);
        gcjc = new GymCongeladoJpaController(factory);
        this.setTitle("Gimnasio Ilcorpo");
        hilofinal ut = new hilofinal();
        Thread r = new Thread(ut);
        r.start();
    }

    private void accederUsuarios() {
        if (usuarios == null) {
            usuarios = new Usuarios(factory, userLog, props);
        }
        pPrincipal.removeAll();
        usuarios.setBounds(0, 0, 646, 449);
        pPrincipal.add(usuarios);
        usuarios.setVisible(true);
        pPrincipal.validate();
        pPrincipal.repaint();
    }

    private void accederArticulos() {
        if (articulos == null) {
            articulos = new Articulos(factory, userLog, props);
        }
        pPrincipal.removeAll();
        articulos.setBounds(0, 0, 646, 449);
        pPrincipal.add(articulos);
        articulos.setVisible(true);
        pPrincipal.validate();
        pPrincipal.repaint();
    }

    private void accederContabilidad() {
        if (contabilidad == null) {
            contabilidad = new Informes(factory, userLog);
        }
        pPrincipal.removeAll();
        contabilidad.setBounds(0, 0, 646, 449);
        pPrincipal.add(contabilidad);
        contabilidad.setVisible(true);
        pPrincipal.validate();
        pPrincipal.repaint();
    }

    private void accederAdministrador() {
        if (administrador == null) {
            administrador = new Administrador(factory, userLog);
        }
        pPrincipal.removeAll();
        administrador.setBounds(0, 0, 646, 449);
        pPrincipal.add(administrador);
        administrador.setVisible(true);
        pPrincipal.validate();
        pPrincipal.repaint();
    }

    private void finalizarusuarios() {
        try {
            List<GymAsignados> listaasignados = gajc.findGymAsignadosEntities();
            ArrayList<String> gus = new ArrayList<String>();
            ArrayList<String> fec = new ArrayList<String>();
            for (GymAsignados ga : listaasignados) {
                if (ga.getEstado() == 1) {
                    if (Asignadosd(ga).size() > 0) {
                        AsignadosDias d = Asignadosd(ga).get(Asignadosd(ga).size() - 1);
                        if (d.getDias() == 0) {
                            ga.setEstado(2);
                            gajc.edit(ga);
                        } else if (new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 0, 0, 0).after(ga.getFechafin())) {
                            ga.setEstado(2);
                            gajc.edit(ga);
                        }
                    } else if (new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 0, 0, 0).after(ga.getFechafin())) {
                        ga.setEstado(2);
                        gajc.edit(ga);
                    }
                    int dias = (int) ((new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 0, 0, 0).getTime() - ga.getFechafin().getTime()) / 86400000);
                    if (dias <= 2) {
                        if (ga.getIduser().getEmail().isEmpty() || ga.getIduser().getEmail() == null) {
                            JOptionPane.showMessageDialog(null, "El usuario con identificacion " + ga.getIduser().getIdentificacion() + " esta proximo a vencer y no tiene una correo asociado para le aviso por favor revise");
                        } else {
                            gus.add(ga.getIduser().getEmail());
                            fec.add(Funciones.ddMMyyyy.format(ga.getFechafin()));
                        }
                    }
                }
            }
            boolean se = false;
            List<MailSend> mses = msjc.findMailSendEntities();
            for (MailSend send : mses) {
                if (Funciones.ddMMyyyy.format(send.getFecha()).equals(Funciones.ddMMyyyy.format(new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 0, 0, 0)))) {
                    se = true;
                    break;
                }
            }
            if (!se) {
                if (mensaje == null) {
                    mensaje = mmjc.findMailMensajeEntities().get(0);
                }
                SendMail mail = new SendMail();
                mail.sendEmail(gus, fec, mensaje.getMensaje());
                mailSend = new MailSend();
                mailSend.setEstado(1);
                msjc.create(mailSend);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage() + " Error al verificar usuarios activos");
        }
    }

    private void congelarAllPlanes() {
        String mensaje = "Esta a punto de congelar/descongelar todos los planes activo o congelados, ¿desea continuar?";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Congelar/Descongelar", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            try {
                List<GymAsignados> listaasignados = gajc.findGymAsignadosEntities();
                for (GymAsignados ga : listaasignados) {
                    if (ga.getEstado() == 1) {
                        ga.setEstado(3);
                        gajc.edit(ga);
                        if (obtenerCongelado(ga).size() > 0) {
                            congelado = obtenerCongelado(ga).get(obtenerCongelado(ga).size() - 1);
                            congelado.setFechacongela(new Date());
                            gcjc.edit(congelado);
                        } else {
                            congelado = new GymCongelado();
                            congelado.setIdasignacion(ga);
                            congelado.setFechacongela(new Date());
                            congelado.setEstado(1);
                            gcjc.create(congelado);
                        }
                    } else if (ga.getEstado() == 3) {
                        if (obtenerCongelado(ga).size() > 0) {
                            congelado = obtenerCongelado(ga).get(obtenerCongelado(ga).size() - 1);
                            int dias = (int) ((new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 0, 0, 0).getTime() - congelado.getFechacongela().getTime()) / 86400000);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(ga.getFechafin());
                            calendar.add(Calendar.DAY_OF_YEAR, dias);
                            ga.setFechafin(calendar.getTime());
                            ga.setEstado(1);
                            gajc.edit(ga);
                        } else {
                            ga.setEstado(1);
                            gajc.edit(ga);
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Proceso finalizado exitosamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage() + " Error al congelar los planes");
            }
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

    private void añadirHuella() {
        final huellad hue = new huellad((JFrame) SwingUtilities.getWindowAncestor(this), true, factory);
        String usuariost = JOptionPane.showInputDialog("Ingrese el numero de documento del usuario: ").toString();
        if (findUsuario(usuariost).size() > 0) {
            final GymUsuarios usuarios = findUsuario(usuariost).get(0);
            hue.setIduser(usuarios);
            hue.btnGuardar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hue.guardarHuella(usuarios);
                    hue.Reclutador1.clear();
                    hue.lblImagenHuella.setIcon(null);
                    hue.start();
                    hue.stop();
                    Iniciar2();
                    start2();
                    hue.dispose();
                }
            });
            hue.btnsalir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hue.stop();
                    Iniciar2();
                    start2();
                    hue.dispose();
                }
            });
            hue.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    hue.stop();
                    Iniciar2();
                    start2();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                }

                @Override
                public void windowIconified(WindowEvent e) {
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                }

                @Override
                public void windowActivated(WindowEvent e) {
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                }
            });
            hue.setLocationRelativeTo(null);
            hue.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "El usuario no existe");
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

    private void cambiaMensaje() {
        final message men = new message((JFrame) SwingUtilities.getWindowAncestor(this), true);
        if (mensaje == null) {
            mensaje = mmjc.findMailMensajeEntities().get(0);
        }
        men.jTextArea1.setText(mensaje.getMensaje());
        men.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                men.dispose();
            }
        });
        men.jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mensaje.setMensaje(men.jTextArea1.getText().toUpperCase());
                    mmjc.edit(mensaje);
                    JOptionPane.showMessageDialog(null, "Mensaje modificado Exitosamente");
                    men.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar mensaje: " + ex.getMessage());
                }
            }
        });
        men.setLocationRelativeTo(null);
        men.setVisible(true);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lNombretitulo = new javax.swing.JLabel();
        bUsuarios = new javax.swing.JLabel();
        bArticulos = new javax.swing.JLabel();
        bContabilidad = new javax.swing.JLabel();
        bAdministracion = new javax.swing.JLabel();
        pPrincipal = new javax.swing.JPanel();
        bLBiometrica = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 500));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(800, 500));
        jPanel1.setMinimumSize(new java.awt.Dimension(800, 500));

        jPanel2.setBackground(new java.awt.Color(50, 72, 141));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/32x32 - final.png"))); // NOI18N
        jLabel1.setText("Menu Principal - Gimnasio Ilcorpo.");

        lNombretitulo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lNombretitulo.setForeground(new java.awt.Color(255, 255, 255));
        lNombretitulo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lNombretitulo.setText("jLabel3");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lNombretitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lNombretitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        bUsuarios.setBackground(new java.awt.Color(50, 72, 141));
        bUsuarios.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        bUsuarios.setText("  Usuarios y Terceros");
        bUsuarios.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(50, 72, 141)));
        bUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bUsuarios.setOpaque(true);
        bUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bUsuariosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bUsuariosMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                bUsuariosMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bUsuariosMouseReleased(evt);
            }
        });

        bArticulos.setBackground(new java.awt.Color(50, 72, 141));
        bArticulos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bArticulos.setForeground(new java.awt.Color(255, 255, 255));
        bArticulos.setText("  Articulos y Planes");
        bArticulos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(50, 72, 141)));
        bArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bArticulos.setOpaque(true);
        bArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bArticulosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bArticulosMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                bArticulosMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bArticulosMouseReleased(evt);
            }
        });

        bContabilidad.setBackground(new java.awt.Color(50, 72, 141));
        bContabilidad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bContabilidad.setForeground(new java.awt.Color(255, 255, 255));
        bContabilidad.setText("  Informes");
        bContabilidad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(50, 72, 141)));
        bContabilidad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bContabilidad.setOpaque(true);
        bContabilidad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bContabilidadMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bContabilidadMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                bContabilidadMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bContabilidadMouseReleased(evt);
            }
        });

        bAdministracion.setBackground(new java.awt.Color(50, 72, 141));
        bAdministracion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bAdministracion.setForeground(new java.awt.Color(255, 255, 255));
        bAdministracion.setText("  Administración");
        bAdministracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(50, 72, 141)));
        bAdministracion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bAdministracion.setOpaque(true);
        bAdministracion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bAdministracionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bAdministracionMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                bAdministracionMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bAdministracionMouseReleased(evt);
            }
        });

        pPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        pPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pPrincipal.setMinimumSize(new java.awt.Dimension(596, 449));
        pPrincipal.setPreferredSize(new java.awt.Dimension(596, 449));

        javax.swing.GroupLayout pPrincipalLayout = new javax.swing.GroupLayout(pPrincipal);
        pPrincipal.setLayout(pPrincipalLayout);
        pPrincipalLayout.setHorizontalGroup(
            pPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 646, Short.MAX_VALUE)
        );
        pPrincipalLayout.setVerticalGroup(
            pPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        bLBiometrica.setBackground(new java.awt.Color(50, 72, 141));
        bLBiometrica.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bLBiometrica.setForeground(new java.awt.Color(255, 255, 255));
        bLBiometrica.setText("  Lectura Biométrica");
        bLBiometrica.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(50, 72, 141)));
        bLBiometrica.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bLBiometrica.setOpaque(true);
        bLBiometrica.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bLBiometricaMouseReleased(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconfinder_snowflake_367528.png"))); // NOI18N
        jLabel2.setToolTipText("Congelar/Descongelar planes");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconfinder_mail-message-new_118781.png"))); // NOI18N
        jLabel3.setToolTipText("Envio de Correos");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel3MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(bContabilidad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bArticulos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bUsuarios, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(bAdministracion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bLBiometrica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)))
                .addGap(4, 4, 4)
                .addComponent(pPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bContabilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAdministracion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 234, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bLBiometrica, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private void bUsuariosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bUsuariosMouseEntered
        bUsuarios.setBackground(Color.white);
        bUsuarios.setForeground(new Color(50, 72, 141));
    }//GEN-LAST:event_bUsuariosMouseEntered

    private void bUsuariosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bUsuariosMouseExited
        bUsuarios.setBackground(new Color(50, 72, 141));
        bUsuarios.setForeground(Color.white);
    }//GEN-LAST:event_bUsuariosMouseExited

    private void bUsuariosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bUsuariosMousePressed
        bUsuarios.setBackground(new Color(50, 72, 141));
        bUsuarios.setForeground(Color.white);
    }//GEN-LAST:event_bUsuariosMousePressed

    private void bArticulosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bArticulosMouseEntered
        bArticulos.setBackground(Color.white);
        bArticulos.setForeground(new Color(50, 72, 141));
    }//GEN-LAST:event_bArticulosMouseEntered

    private void bArticulosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bArticulosMouseExited
        bArticulos.setBackground(new Color(50, 72, 141));
        bArticulos.setForeground(Color.white);
    }//GEN-LAST:event_bArticulosMouseExited

    private void bArticulosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bArticulosMousePressed
        bArticulos.setBackground(new Color(50, 72, 141));
        bArticulos.setForeground(Color.white);
    }//GEN-LAST:event_bArticulosMousePressed

    private void bContabilidadMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bContabilidadMouseEntered
        bContabilidad.setBackground(Color.white);
        bContabilidad.setForeground(new Color(50, 72, 141));
    }//GEN-LAST:event_bContabilidadMouseEntered

    private void bContabilidadMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bContabilidadMouseExited
        bContabilidad.setBackground(new Color(50, 72, 141));
        bContabilidad.setForeground(Color.white);
    }//GEN-LAST:event_bContabilidadMouseExited

    private void bContabilidadMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bContabilidadMousePressed
        bContabilidad.setBackground(new Color(50, 72, 141));
        bContabilidad.setForeground(Color.white);
    }//GEN-LAST:event_bContabilidadMousePressed

    private void bAdministracionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAdministracionMouseEntered
        bAdministracion.setBackground(Color.white);
        bAdministracion.setForeground(new Color(50, 72, 141));
    }//GEN-LAST:event_bAdministracionMouseEntered

    private void bAdministracionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAdministracionMouseExited
        bAdministracion.setBackground(new Color(50, 72, 141));
        bAdministracion.setForeground(Color.white);
    }//GEN-LAST:event_bAdministracionMouseExited

    private void bAdministracionMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAdministracionMousePressed
        bAdministracion.setBackground(new Color(50, 72, 141));
        bAdministracion.setForeground(Color.white);
    }//GEN-LAST:event_bAdministracionMousePressed

    private void bUsuariosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bUsuariosMouseReleased
        accederUsuarios();
    }//GEN-LAST:event_bUsuariosMouseReleased

    private void bArticulosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bArticulosMouseReleased
        accederArticulos();
    }//GEN-LAST:event_bArticulosMouseReleased

    private void bContabilidadMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bContabilidadMouseReleased
        accederContabilidad();
    }//GEN-LAST:event_bContabilidadMouseReleased

    private void bAdministracionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAdministracionMouseReleased
        accederAdministrador();
    }//GEN-LAST:event_bAdministracionMouseReleased

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        hilodatos ut = new hilodatos();
        Thread r = new Thread(ut);
        r.start();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        stop2();
    }//GEN-LAST:event_formWindowClosed

    private void bLBiometricaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bLBiometricaMouseReleased
        stop2();
        añadirHuella();
    }//GEN-LAST:event_bLBiometricaMouseReleased

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        congelarAllPlanes();
    }//GEN-LAST:event_jLabel2MouseReleased

    private void jLabel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseReleased
        cambiaMensaje();
    }//GEN-LAST:event_jLabel3MouseReleased

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
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new principal().setVisible(true);
//            }
//        });
    }

    public class hilofinal extends Thread {

        public hilofinal() {
        }

        @Override
        public void run() {
            finalizarusuarios();
            this.stop();
        }
    }

    public class hilodatos extends Thread {

        public hilodatos() {
        }

        @Override
        public void run() {
            Iniciar2();
            start2();
            this.stop();
        }
    }

    public void identificarHuella() throws IOException {
        try {
            //Establece los valores para la sentencia SQL
            Database con = new Database(props);
            con.ConectarBasedeDatos();
            //Obtiene todas las huellas de la bd
            con.sentencia = con.conexion.prepareStatement("SELECT iduser, huella FROM gym_huella");
            ResultSet rs = con.sentencia.executeQuery();
            //Si se encuentra el nombre en la base de datos
            while (rs.next()) {
                //Lee la plantilla de la base de datos
                byte templateBuffer[] = rs.getBytes("huella");
                String nombre = rs.getString("iduser");
                //Crea una nueva plantilla a partir de la guardada en la base de datos
                DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
                //Envia la plantilla creada al objeto contendor de Template del componente de huella digital
                setTemplate(referenceTemplate);
                // Compara las caracteriticas de la huella recientemente capturda con la
                // alguna plantilla guardada en la base de datos que coincide con ese tipo
                DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());
                //compara las plantilas (actual vs bd)
                //Si encuentra correspondencia dibuja el mapa
                //e indica el nombre de la persona que coincidió.
                if (result.isVerified()) {
                    accederUsuarios();
                    usuarios.veTiempos();
                    List<GymUsuarios> usa = this.huellaUser(Integer.parseInt(nombre));
                    gymhuellauser = usa.get(0);
                    usuarios.tiem.tIdentifiacion.setText(gymhuellauser.getIdentificacion());
                    usuarios.tiem.verificarUser();
                    return;
                }
            }
            //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje
            JOptionPane.showMessageDialog(null, "No existe ningún registro que coincida con la huella", "Verificacion de Huella", JOptionPane.ERROR_MESSAGE);
            setTemplate(null);
        } catch (SQLException e) {
            //Si ocurre un error lo indica en la consola
            System.err.println("Error al identificar huella dactilar." + e.getMessage());
        }
    }

    private List<GymUsuarios> huellaUser(int id) {
        EntityManager em = gujc.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM GymUsuarios u WHERE u.id=:id")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("id", id)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    protected void Iniciar2() {
        Lector.addDataListener(new DPFPDataAdapter() {
            @Override
            public void dataAcquired(final DPFPDataEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            ProcesarCaptura(e.getSample());

                        } catch (IOException ex) {
                            Logger.getLogger(principal.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });

        Lector.addReaderStatusListener(new DPFPReaderStatusAdapter() {
            @Override
            public void readerConnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    }
                });
            }

            @Override
            public void readerDisconnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getReaderStatus());
                    }
                });
            }
        });

        Lector.addSensorListener(new DPFPSensorAdapter() {
            @Override
            public void fingerTouched(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    }
                });
            }

            @Override
            public void fingerGone(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    }
                });
            }
        });

        Lector.addErrorListener(new DPFPErrorAdapter() {
            public void errorReader(final DPFPErrorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getError());
                    }
                });
            }
        });
    }

    public DPFPFeatureSet featuresinscripcion;
    public DPFPFeatureSet featuresverificacion;

    public void ProcesarCaptura(DPFPSample sample) throws IOException {
        // Procesar la muestra de la huella y crear un conjunto de características con el propósito de inscripción.
        featuresinscripcion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
        // Procesar la muestra de la huella y crear un conjunto de características con el propósito de verificacion.
        featuresverificacion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
        // Comprobar la calidad de la muestra de la huella y lo añade a su reclutador si es bueno
        if (featuresinscripcion != null) {
            identificarHuella();
        }
    }

    public DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            return null;
        }
    }

    public Image CrearImagenHuella(DPFPSample sample) {
        return DPFPGlobal.getSampleConversionFactory().createImage(sample);
    }

    public void start2() {
        Lector.startCapture();
    }

    public void stop2() {
        Lector.stopCapture();
    }

    public DPFPTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DPFPTemplate template) {
        DPFPTemplate old = this.template;
        this.template = template;
        firePropertyChange(TEMPLATE_PROPERTY, old, template);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bAdministracion;
    private javax.swing.JLabel bArticulos;
    private javax.swing.JLabel bContabilidad;
    private javax.swing.JLabel bLBiometrica;
    private javax.swing.JLabel bUsuarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JLabel lNombretitulo;
    private javax.swing.JPanel pPrincipal;
    // End of variables declaration//GEN-END:variables
}
