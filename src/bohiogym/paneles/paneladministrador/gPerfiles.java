/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.paneladministrador;

import bohiogym.clases.Funciones;
import entity.GymPermisos;
import entity.UserInfo;
import entity.UserLog;
import entity.UserPermisos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import jpa.GymPermisosJpaController;
import jpa.UserInfoJpaController;
import jpa.UserLogJpaController;
import jpa.UserPermisosJpaController;

/**
 *
 * @author Alvarez
 */
public class gPerfiles extends javax.swing.JPanel {
    
    private final EntityManagerFactory factory;
    UserLog userLog = null;
    UserLogJpaController uljc = null;
    UserInfo userInfo = null;
    UserInfoJpaController uijc = null;
    UserPermisos userPermisos = null;
    UserPermisosJpaController upjc = null;
    GymPermisos permisos = null;
    GymPermisosJpaController gpjc = null;
    ImageIcon check = new ImageIcon(getClass().getResource("/recursos/check20.png"));
    ImageIcon error = new ImageIcon(getClass().getResource("/recursos/error20.png"));
    boolean claves = false;
    DefaultTableModel model;
    List<UserPermisos> luserpermisos = null;
    Object dato[] = null;

    /**
     * Creates new form gPerfiles
     */
    public gPerfiles(EntityManagerFactory factory) {
        initComponents();
        this.factory = factory;
        uljc = new UserLogJpaController(factory);
        uijc = new UserInfoJpaController(factory);
        upjc = new UserPermisosJpaController(factory);
        gpjc = new GymPermisosJpaController(factory);
        LVerificacion.setText(null);
        setCargarTabla();
    }
    
    private void verificarClave() {
        if (PClave.getText().equals(PCClave.getText())) {
            LVerificacion.setIcon(check);
            claves = true;
        } else {
            LVerificacion.setIcon(error);
            claves = false;
        }
    }
    
    private List<UserInfo> obtenerUsuario(String usuario) {
        EntityManager em = uijc.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM UserInfo u WHERE u.idlog.usuario=:usuario AND u.idlog.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("usuario", usuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    private void cargaUsuario() {
        List<UserInfo> userInfo = obtenerUsuario(tUsuario.getText().toUpperCase());
        if (userInfo.size() > 0) {
            tIdentificacion.setText(userInfo.get(0).getIdentificacion());
            tNombres.setText(userInfo.get(0).getNombres());
            tApellidos.setText(userInfo.get(0).getApellidos());
            seDatos();
        } else {
            String men = "El usuario no existe, ¿Desea crearlo?";
            int en = JOptionPane.showConfirmDialog(null, men, "crear", JOptionPane.YES_NO_OPTION);
            if (en == 0) {
                String usuario = tUsuario.getText();
                limpiar();
                tUsuario.setText(usuario);
                PClave.requestFocus();
            }
        }
    }
    
    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"entidad", "Permiso", "Estado", "entipermiso"}) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };
            
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return canEdit[colIndex];
            }
        };
        return modelo;
    }
    
    private void setCargarTabla() {
        model = getModelo();
        tPermisos.setModel(model);
        tPermisos.getTableHeader().setReorderingAllowed(false);
        tPermisos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Funciones.setOcultarColumnas(tPermisos, new int[]{0, 2, 3});
        //470
        tPermisos.getColumnModel().getColumn(1).setMinWidth(470);
        tPermisos.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(470);
    }
    
    private void seDatos() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        List<UserPermisos> ups = obtenerPermisos(tUsuario.getText().toUpperCase());
        for (UserPermisos up : ups) {
            if (up.getEstado() == 2 || up.getEstado() == 1) {
                model.addRow(dato);
                model.setValueAt(up, model.getRowCount() - 1, 0);
                model.setValueAt(up.getIdpermiso().getNombrepermiso(), model.getRowCount() - 1, 1);
                model.setValueAt(up.getEstado(), model.getRowCount() - 1, 2);
                model.setValueAt(up.getIdpermiso(), model.getRowCount() - 1, 3);
            }
        }
    }
    
    private List<UserPermisos> obtenerPermisos(String usuario) {
        EntityManager em = upjc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM UserPermisos p WHERE p.iduser.usuario=:usuario")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("usuario", usuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    private void guardaUsuario() {
        if (tUsuario.getText() == null || tUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe Ingresar Un Usuario");
        } else if (tIdentificacion.getText() == null || tIdentificacion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe Ingresar Un Numero de Identificacion");
        } else if (tNombres.getText() == null || tNombres.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe Ingresar Un Nombre");
        } else if (tApellidos.getText() == null || tApellidos.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe Ingresar Un Apellido");
        } else {
            List<UserInfo> ups = obtenerUsuario(tUsuario.getText().toUpperCase());
            if (ups.size() > 0) {
                if (upjc == null) {
                    upjc = new UserPermisosJpaController(factory);
                }
                userLog = ups.get(0).getIdlog();
                userInfo = ups.get(0);
                userInfo.setIdentificacion(tIdentificacion.getText());
                userInfo.setNombres(tNombres.getText().toUpperCase());
                userInfo.setApellidos(tApellidos.getText().toUpperCase());
                try {
                    uijc.edit(userInfo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error Al editar la configuracion del Usuario: " + ex.getMessage(), gPerfiles.class.getName(), JOptionPane.ERROR_MESSAGE);
                }
                luserpermisos = obtenerPermisos(userLog.getUsuario());
                for (int i = 0; i < model.getRowCount(); i++) {
                    UserPermisos userp = null;
                    boolean exist = false;
                    for (UserPermisos up : luserpermisos) {
                        if (model.getValueAt(i, 0) != null) {
                            if (model.getValueAt(i, 0).equals(up)) {
                                exist = true;
                                userp = up;
                                break;
                            }
                        } else {
                            exist = false;
                            break;
                        }
                    }
                    if (!exist) {
                        if (Integer.parseInt(model.getValueAt(i, 2).toString()) != 0) {
                            userp = new UserPermisos();
                            userp.setIduser(userLog);
                            userp.setIdpermiso((GymPermisos) model.getValueAt(i, 3));
                            userp.setEstado(2);
                            upjc.create(userp);
                        }
                    } else {
                        userp = (UserPermisos) model.getValueAt(i, 0);
                        userp.setEstado(Integer.parseInt(model.getValueAt(i, 2).toString()));
                        try {
                            upjc.edit(userp);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error Al editar la configuracion del Usuario: " + ex.getMessage(), gPerfiles.class.getName(), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Datos modificados exitosamente");
                seDatos();
                limpiar();
            } else {
                if (PClave == null || PClave.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar una clave");
                } else if (!claves) {
                    JOptionPane.showMessageDialog(null, "Las claves no coinciden");
                } else {
                    userLog = new UserLog();
                    userLog.setUsuario(tUsuario.getText().toUpperCase());
                    userLog.setClave(PCClave.getText());
                    userLog.setEstado(1);
                    uljc.create(userLog);
                    userInfo = new UserInfo();
                    userInfo.setIdentificacion(tIdentificacion.getText());
                    userInfo.setNombres(tNombres.getText().toUpperCase());
                    userInfo.setApellidos(tApellidos.getText().toUpperCase());
                    userInfo.setIdlog(userLog);
                    userInfo.setEstado(1);
                    uijc.create(userInfo);
                    if (gpjc == null) {
                        gpjc = new GymPermisosJpaController(factory);
                    }
                    if (upjc == null) {
                        upjc = new UserPermisosJpaController(factory);
                    }
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (Integer.parseInt(model.getValueAt(i, 2).toString()) != 0) {
                            userPermisos = new UserPermisos();
                            userPermisos.setIduser(userLog);
                            userPermisos.setIdpermiso((GymPermisos) model.getValueAt(i, 3));
                            userPermisos.setEstado(Integer.parseInt(model.getValueAt(i, 2).toString()));
                            upjc.create(userPermisos);
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Usuario Creado Exitosamente");
                    seDatos();
                    limpiar();
                }
            }
        }
    }
    
    private void removePermiso() {
        if (tPermisos.getSelectedRow() > -1) {
            String mensaje = "Esta a punto de inactivar el permiso seleccionado.\n¿Esta seguro de desactivar el permiso? ";
            int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Desactivar Permiso", JOptionPane.YES_NO_OPTION);
            if (entrada == 0) {
                model.setValueAt(0, tPermisos.getSelectedRow(), 2);
                model.removeRow(tPermisos.getSelectedRow());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un permiso de la lista");
        }
    }
    
    private void limpiar() {
        tUsuario.setText(null);
        tIdentificacion.setText(null);
        tNombres.setText(null);
        tUsuario.setText(null);
        PClave.setText(null);
        PCClave.setText(null);
        tApellidos.setText(null);
        LVerificacion.setIcon(null);
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        userLog = null;
        userInfo = null;
        userPermisos = null;
    }
    
    private void añadirPermiso() {
        final dPermisos permiso = new dPermisos(null, true, factory);
        permiso.bAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean exi = false;
                if (permiso.tPermisos.getSelectedRow() > -1) {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (permiso.tPermisos.getValueAt(permiso.tPermisos.getSelectedRow(), 0).toString().equals(model.getValueAt(i, 3).toString())) {
                            exi = true;
                            break;
                        }
                    }
                    if (exi == false) {
                        model.addRow(dato);
                        model.setValueAt(permiso.tPermisos.getValueAt(permiso.tPermisos.getSelectedRow(), 1), model.getRowCount() - 1, 1);
                        model.setValueAt(1, model.getRowCount() - 1, 2);
                        model.setValueAt(permiso.tPermisos.getValueAt(permiso.tPermisos.getSelectedRow(), 0), model.getRowCount() - 1, 3);
                        permiso.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya Tiene este permiso asignado");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un permiso");
                }
            }
        });
        permiso.setLocationRelativeTo(null);
        permiso.setVisible(true);
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
        tUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        PClave = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        PCClave = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        tIdentificacion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tNombres = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tApellidos = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tPermisos = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        bAñadir = new javax.swing.JLabel();
        bQuitar = new javax.swing.JLabel();
        bSalir = new javax.swing.JButton();
        bGuardar = new javax.swing.JButton();
        LVerificacion = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Usuario:");

        tUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tUsuarioFocusGained(evt);
            }
        });
        tUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tUsuarioKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 72, 141));
        jLabel3.setText("Clave:");

        PClave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PClaveFocusGained(evt);
            }
        });
        PClave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PClaveKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(50, 72, 141));
        jLabel4.setText("Confirmar Clave:");

        PCClave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PCClaveFocusGained(evt);
            }
        });
        PCClave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PCClaveKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                PCClaveKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(50, 72, 141));
        jLabel5.setText("Identificación:");

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

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(50, 72, 141));
        jLabel6.setText("Nombres:");

        tNombres.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNombresFocusGained(evt);
            }
        });
        tNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tNombresKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(50, 72, 141));
        jLabel7.setText("Apellidos:");

        tApellidos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tApellidosFocusGained(evt);
            }
        });

        tPermisos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tPermisos);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(50, 72, 141));
        jLabel8.setText("Permisos:");

        bAñadir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/+ 16x16.png"))); // NOI18N
        bAñadir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bAñadir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bAñadirMouseReleased(evt);
            }
        });

        bQuitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/- 16x16.png"))); // NOI18N
        bQuitar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bQuitar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bQuitarMouseReleased(evt);
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

        bGuardar.setBackground(new java.awt.Color(50, 72, 141));
        bGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bGuardar.setForeground(new java.awt.Color(255, 255, 255));
        bGuardar.setText("Aceptar");
        bGuardar.setContentAreaFilled(false);
        bGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bGuardar.setFocusable(false);
        bGuardar.setOpaque(true);
        bGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bGuardarMouseReleased(evt);
            }
        });

        LVerificacion.setText("jLabel9");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(tNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LVerificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PCClave, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bAñadir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bQuitar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(tIdentificacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(PClave, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tUsuario, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel6))
                        .addGap(0, 240, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tUsuario)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(PClave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                                            .addComponent(LVerificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(PCClave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(73, 73, 73)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(jLabel8))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bAñadir))))
                            .addComponent(bQuitar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void PCClaveKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PCClaveKeyReleased
        verificarClave();
    }//GEN-LAST:event_PCClaveKeyReleased

    private void bQuitarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bQuitarMouseReleased
        removePermiso();
    }//GEN-LAST:event_bQuitarMouseReleased

    private void bGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGuardarMouseReleased
        guardaUsuario();
    }//GEN-LAST:event_bGuardarMouseReleased

    private void bAñadirMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAñadirMouseReleased
        añadirPermiso();
    }//GEN-LAST:event_bAñadirMouseReleased

    private void tUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargaUsuario();
        }
    }//GEN-LAST:event_tUsuarioKeyPressed

    private void tUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tUsuarioFocusGained
        tUsuario.selectAll();
    }//GEN-LAST:event_tUsuarioFocusGained

    private void PClaveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PClaveFocusGained
        PClave.selectAll();
    }//GEN-LAST:event_PClaveFocusGained

    private void PCClaveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PCClaveFocusGained
        PCClave.selectAll();
    }//GEN-LAST:event_PCClaveFocusGained

    private void tIdentificacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tIdentificacionFocusGained
        tIdentificacion.selectAll();
    }//GEN-LAST:event_tIdentificacionFocusGained

    private void tNombresFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNombresFocusGained
        tNombres.selectAll();
    }//GEN-LAST:event_tNombresFocusGained

    private void tApellidosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tApellidosFocusGained
        tApellidos.selectAll();
    }//GEN-LAST:event_tApellidosFocusGained

    private void PClaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PClaveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PCClave.requestFocus();
        }
    }//GEN-LAST:event_PClaveKeyPressed

    private void PCClaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PCClaveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tIdentificacion.requestFocus();
        }
    }//GEN-LAST:event_PCClaveKeyPressed

    private void tIdentificacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tIdentificacionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tNombres.requestFocus();
        }
    }//GEN-LAST:event_tIdentificacionKeyPressed

    private void tNombresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tNombresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tApellidos.requestFocus();
        }
    }//GEN-LAST:event_tNombresKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LVerificacion;
    private javax.swing.JPasswordField PCClave;
    private javax.swing.JPasswordField PClave;
    private javax.swing.JLabel bAñadir;
    private javax.swing.JButton bGuardar;
    private javax.swing.JLabel bQuitar;
    public javax.swing.JButton bSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField tApellidos;
    private javax.swing.JTextField tIdentificacion;
    private javax.swing.JTextField tNombres;
    private javax.swing.JTable tPermisos;
    private javax.swing.JTextField tUsuario;
    // End of variables declaration//GEN-END:variables
}
