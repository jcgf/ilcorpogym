/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelinformes;

import bohiogym.clases.Funciones;
import bohiogym.paneles.panelinformes.subinformes.pIngreso;
import entity.ConsecutivoIngresos;
import entity.IngresoItem;
import entity.UserLog;
import entity.UserPermisos;
import entity.VariedadesIngresos;
import entity.GymTerceros;
import entity.GymUsuarios;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import jpa.ConsecutivoIngresosJpaController;
import jpa.IngresoItemJpaController;
import jpa.UserLogJpaController;
import jpa.VariedadesIngresosJpaController;
import jpa.GymTercerosJpaController;
import jpa.GymUsuariosJpaController;

/**
 *
 * @author Alvarez
 */
public class ingresos extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    private final UserLog userlog;
    UserLogJpaController uljc = null;
    GymUsuariosJpaController vujc = null;
    ConsecutivoIngresosJpaController cijc = null;
    VariedadesIngresos variedadesIngresos = null;
    VariedadesIngresosJpaController vijc = null;
    GymTercerosJpaController vtjc = null;
    IngresoItem ingresoItem = null;
    IngresoItemJpaController iijc = null;
    List<IngresoItem> listItems = null;
    DefaultTableModel model;
    Object dato[] = null;

    /**
     * Creates new form ingresos
     */
    public ingresos(EntityManagerFactory factory, UserLog userLog) {
        initComponents();
        this.factory = factory;
        this.userlog = userLog;
        vujc = new GymUsuariosJpaController(factory);
        vtjc = new GymTercerosJpaController(factory);
        uljc = new UserLogJpaController(factory);
        cijc = new ConsecutivoIngresosJpaController(factory);
        vijc = new VariedadesIngresosJpaController(factory);
        iijc = new IngresoItemJpaController(factory);
        jDateChooser1.setDate(new Date());
        if (validaAdmin()) {
            jDateChooser1.setEnabled(true);
            binactivar.setVisible(true);
        } else {
            binactivar.setVisible(false);
            jDateChooser1.setEnabled(false);
        }
        setCargarTabla();
        jTextField1.setText(String.valueOf(obtenerConsecutivos().get(0).getNumero()));
        if (obtenerVenta(Integer.valueOf(jTextField1.getText())).size() > 0) {
            cargaIngreso();
        } else {
            bGuardar.setEnabled(true);
            bfinalizar.setEnabled(false);
        }
    }

    private void cargaIngreso() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        if (obtenerVenta(Integer.valueOf(jTextField1.getText())).size() > 0) {
            variedadesIngresos = obtenerVenta(Integer.valueOf(jTextField1.getText())).get(0);
            if (variedadesIngresos.getEstado() == 2) {
                if (validaAdmin()) {
                    bGuardar.setEnabled(true);
                    bfinalizar.setEnabled(true);
                } else {
                    bGuardar.setEnabled(false);
                    bfinalizar.setEnabled(false);
                }
            } else {
                bGuardar.setEnabled(true);
                bfinalizar.setEnabled(true);
            }
            jTextField2.setText(variedadesIngresos.getCodcliente());
            if (obteneruser(jTextField2.getText()).size() > 0) {
                jTextField3.setText(obteneruser(jTextField2.getText()).get(0).getNombres() + " " + obteneruser(jTextField2.getText()).get(0).getApellidos());
            } else if (obtenerTercero(jTextField2.getText()).size() > 0) {
                jTextField3.setText(obtenerTercero(jTextField2.getText()).get(0).getRazonsocial());
            } else {
                jTextField3.setText(" ");
            }
            jDateChooser1.setDate(variedadesIngresos.getFechaingreso());
            if (obtenerItem(variedadesIngresos).size() > 0) {
                for (IngresoItem va : obtenerItem(variedadesIngresos)) {
                    model.addRow(dato);
                    model.setValueAt(va, model.getRowCount() - 1, 0);
                    model.setValueAt(va.getIdingreso(), model.getRowCount() - 1, 1);
                    model.setValueAt(va.getDescripcion(), model.getRowCount() - 1, 2);
                    model.setValueAt(va.getValorindividual(), model.getRowCount() - 1, 3);
                    model.setValueAt(va.getCantidad(), model.getRowCount() - 1, 4);
                    model.setValueAt(va.getSubtotal(), model.getRowCount() - 1, 5);
                    model.setValueAt(va.getIva(), model.getRowCount() - 1, 6);
                    model.setValueAt(va.getTotal(), model.getRowCount() - 1, 7);
                    model.setValueAt(va.getEstado(), model.getRowCount() - 1, 8);
                    model.setValueAt(va.getValoriva(), model.getRowCount() - 1, 9);
                }
                for (int row = 0; row < jTable1.getRowCount(); row++) {
                    int rowHeight = jTable1.getRowHeight();
                    for (int column = 0; column < jTable1.getColumnCount(); column++) {
                        Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                        rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
                    }
                    jTable1.setRowHeight(row, rowHeight);
                }
            }
            jLabel6.setText(String.valueOf(variedadesIngresos.getSubtotal()));
            jLabel8.setText(String.valueOf(variedadesIngresos.getValoriva()));
            jLabel10.setText(String.valueOf(variedadesIngresos.getValortotal()));
        }
    }

    private List<IngresoItem> obtenerItem(VariedadesIngresos ingreso) {
        EntityManager em = iijc.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM IngresoItem a WHERE a.idingreso=:ingreso AND a.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("ingreso", ingreso)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<ConsecutivoIngresos> obtenerConsecutivos() {
        EntityManager em = cijc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM ConsecutivoIngresos c WHERE c.estado='1' OR c.estado='2' ORDER BY c.numero ASC")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymUsuarios> obteneruser(String dats) {
        EntityManager em = vujc.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM GymUsuarios u WHERE u.identificacion=:dats AND u.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("dats", dats)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymTerceros> obtenerTercero(String dats) {
        EntityManager em = vtjc.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM GymTerceros u WHERE u.nit=:dats AND u.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("dats", dats)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<VariedadesIngresos> obtenerVenta(int nfactura) {
        EntityManager em = vijc.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM VariedadesIngresos f WHERE f.codigoingreso=:nfactura AND (f.estado='1' OR f.estado='2')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("nfactura", nfactura)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private boolean validaAdmin() {
        boolean admin = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == 1) {
                admin = true;
                break;
            }
        }
        return admin;
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

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"id", "idingreso", "Descripcion", "Valor", " ", "Subtotal", "IVA", "Total", "Estado", "valoriva"}) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };

            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false
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
        jTable1.setModel(model);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Funciones.setOcultarColumnas(jTable1, new int[]{0, 1, 8, 9});
        //470
        jTable1.getColumnModel().getColumn(2).setMinWidth(215);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(215);
        jTable1.getColumnModel().getColumn(3).setMinWidth(60);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(4).setMinWidth(35);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(35);
        jTable1.getColumnModel().getColumn(5).setMinWidth(60);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(6).setMinWidth(40);
        jTable1.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(7).setMinWidth(60);
        jTable1.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(60);
        jTable1.setDefaultRenderer(Object.class, new bohiogym.clases.ColorTablaIngresos());
    }

    private void cargaUser() {
        if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un cliente para continuar");
        } else {
            if (obteneruser(jTextField2.getText()).size() > 0) {
                jTextField3.setText(obteneruser(jTextField2.getText()).get(0).getNombres()
                        + " " + obteneruser(jTextField2.getText()).get(0).getApellidos());
            } else if (obtenerTercero(jTextField2.getText()).size() > 0) {
                jTextField3.setText(obtenerTercero(jTextField2.getText()).get(0).getRazonsocial());
            } else {
                JOptionPane.showMessageDialog(null, "El usuario no existe");
            }
        }
    }

    private void addIngreso() {
        final pIngreso pingre = new pIngreso((JFrame) SwingUtilities.getWindowAncestor(this), true, factory);
        pingre.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(dato);
                model.setValueAt(ingresoItem, model.getRowCount() - 1, 0);
                model.setValueAt("", model.getRowCount() - 1, 1);
                model.setValueAt("<html><div style=\"width:215;\">" + pingre.jTextArea1.getText().toUpperCase() + "</div></html>", model.getRowCount() - 1, 2);
                model.setValueAt(pingre.jTextField1.getText(), model.getRowCount() - 1, 3);
                model.setValueAt(pingre.jTextField3.getText(), model.getRowCount() - 1, 4);
                float subtotal = Float.parseFloat(pingre.jLabel9.getText());
                model.setValueAt(subtotal, model.getRowCount() - 1, 5);
                model.setValueAt(pingre.jTextField2.getText(), model.getRowCount() - 1, 6);
                float total = Float.parseFloat(pingre.jLabel13.getText());
                model.setValueAt(total, model.getRowCount() - 1, 7);
                model.setValueAt(1, model.getRowCount() - 1, 8);
                float totaliva = Float.parseFloat(pingre.jLabel11.getText());
                model.setValueAt(totaliva, model.getRowCount() - 1, 9);
                float subt = 0;
                float iv = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    subt = subt + Float.parseFloat(model.getValueAt(i, 5).toString());
                    iv = iv + Float.parseFloat(model.getValueAt(i, 9).toString());
                }
                for (int row = 0; row < jTable1.getRowCount(); row++) {
                    int rowHeight = jTable1.getRowHeight();
                    for (int column = 0; column < jTable1.getColumnCount(); column++) {
                        Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                        rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
                    }
                    jTable1.setRowHeight(row, rowHeight);
                }
                jLabel6.setText(String.valueOf(subt));
                jLabel8.setText(String.valueOf(iv));
                jLabel10.setText(String.valueOf(iv + subt));
                pingre.dispose();
            }
        });
        pingre.setLocationRelativeTo(null);
        pingre.setVisible(true);
    }

    private void saveIngreso() {
        if (variedadesIngresos == null) {
            variedadesIngresos = new VariedadesIngresos();
            variedadesIngresos.setCodigoingreso(Integer.valueOf(jTextField1.getText()));
            if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                variedadesIngresos.setCodcliente(" ");
            } else {
                variedadesIngresos.setCodcliente(jTextField2.getText());
            }
            variedadesIngresos.setFechaingreso(jDateChooser1.getDate());
            variedadesIngresos.setFechaingresotiempo(new Date());
            variedadesIngresos.setSubtotal(Float.parseFloat(jLabel6.getText()));
            variedadesIngresos.setValoriva(Float.parseFloat(jLabel8.getText()));
            variedadesIngresos.setValortotal(Float.parseFloat(jLabel10.getText()));
            variedadesIngresos.setIdusuario(userlog);
            variedadesIngresos.setEstado(1);
            vijc.create(variedadesIngresos);
            saveItems(variedadesIngresos);
            try {
                ConsecutivoIngresos ci = obtenerc(Integer.parseInt(jTextField1.getText())).get(0);
                ci.setEstado(2);
                cijc.edit(ci);
            } catch (Exception exi) {
                JOptionPane.showMessageDialog(null, "Error al actualizar consecutivo: " + exi.getMessage(), ingresos.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Ingreso guardado");
            cargaIngreso();
            bfinalizar.setEnabled(true);
        } else {
            try {
                variedadesIngresos.setCodigoingreso(Integer.valueOf(jTextField1.getText()));
                if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                    variedadesIngresos.setCodcliente(" ");
                } else {
                    variedadesIngresos.setCodcliente(jTextField2.getText());
                }
                variedadesIngresos.setFechaingreso(jDateChooser1.getDate());
                variedadesIngresos.setFechaingresotiempo(new Date());
                variedadesIngresos.setSubtotal(Float.parseFloat(jLabel6.getText()));
                variedadesIngresos.setValoriva(Float.parseFloat(jLabel8.getText()));
                variedadesIngresos.setValortotal(Float.parseFloat(jLabel10.getText()));
                variedadesIngresos.setIdusuario(userlog);
                variedadesIngresos.setEstado(1);
                vijc.edit(variedadesIngresos);
                saveItems(variedadesIngresos);
                JOptionPane.showMessageDialog(null, "Ingreso guardado");
                cargaIngreso();
                bfinalizar.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage(), ingresos.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void finalizarIngreso() {
        String mensaje = "Esta a punto de finalizar el ingreso, no podra modificarla despues.\n¿Esta seguro de finalizar el ingreso?";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Finalizar", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            variedadesIngresos.setEstado(2);
            try {
                vijc.edit(variedadesIngresos);
                ConsecutivoIngresos cv = obtenerc(Integer.parseInt(jTextField1.getText())).get(0);
                cv.setEstado(3);
                cijc.edit(cv);
                JOptionPane.showMessageDialog(null, "Ingreso finalizado exitosamente");
                cargaIngreso();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al Finalizar el ingreso: " + ex.getMessage(), ingresos.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<ConsecutivoIngresos> obtenerc(int consecutivo) {
        EntityManager em = cijc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM ConsecutivoIngresos c WHERE c.numero=:consecutivo")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("consecutivo", consecutivo)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void saveItems(VariedadesIngresos ingresos) {
        listItems = obtenerItem(ingresos);
        for (int i = 0; i < model.getRowCount(); i++) {
            IngresoItem item = null;
            boolean exist = false;
            for (IngresoItem ii : listItems) {
                if (((String) model.getValueAt(i, 2)).equals(ii.getDescripcion())) {
                    exist = true;
                    item = ii;
                    break;
                }
            }
            if (!exist) {
                item = new IngresoItem();
                item.setIdingreso(ingresos);
                item.setDescripcion(model.getValueAt(i, 2).toString());
                item.setValorindividual(Float.parseFloat(model.getValueAt(i, 3).toString()));
                item.setCantidad(Integer.valueOf(model.getValueAt(i, 4).toString()));
                item.setSubtotal(Float.parseFloat(model.getValueAt(i, 5).toString()));
                item.setIva(Float.parseFloat(model.getValueAt(i, 6).toString()));
                item.setValoriva(Float.parseFloat(model.getValueAt(i, 9).toString()));
                item.setTotal(Float.parseFloat(model.getValueAt(i, 7).toString()));
                item.setEstado(Integer.valueOf(model.getValueAt(i, 8).toString()));
                iijc.create(item);
                model.setValueAt(ingresos, i, 1);
            } else if (item != null) {
                item.setIdingreso(ingresos);
                item.setEstado(Integer.valueOf(model.getValueAt(i, 8).toString()));
                try {
                    iijc.edit(item);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar articulo: " + ex.getMessage(), ingresos.class.getName(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void inactivarIngreso() {
        String mensaje = "Esta a punto de inactivar el ingreso, no podra modificarla despues.\n¿Esta seguro de inactivar el ingreso?";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Finalizar", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            variedadesIngresos.setEstado(0);
            try {
                vijc.edit(variedadesIngresos);
                ConsecutivoIngresos cv = obtenerc(Integer.parseInt(jTextField1.getText())).get(0);
                cv.setEstado(0);
                cijc.edit(cv);
                JOptionPane.showMessageDialog(null, "Ingreso inactivado exitosamente");
                limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al Finalizar el ingreso: " + ex.getMessage(), ingresos.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiar() {
        jTextField1.setText(String.valueOf(obtenerConsecutivos().get(0).getNumero()));
        jTextField2.setText(null);
        jTextField3.setText(null);
        jDateChooser1.setDate(new Date());
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        jLabel6.setText("...");
        jLabel8.setText("...");
        jLabel10.setText("...");
        bfinalizar.setEnabled(false);
        ingresoItem = null;
        variedadesIngresos = null;
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
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        bSalir = new javax.swing.JButton();
        binactivar = new javax.swing.JButton();
        bimprimir = new javax.swing.JButton();
        blimpiar = new javax.swing.JButton();
        bfinalizar = new javax.swing.JButton();
        bGuardar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Número de Ingreso:");

        jTextField1.setForeground(new java.awt.Color(255, 51, 51));
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(50, 72, 141));
        jLabel2.setText("Identifiación Cliente:");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jTextField3.setEditable(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 72, 141));
        jLabel3.setText("Fecha:");

        jDateChooser1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/+ 24x24.png"))); // NOI18N
        jLabel5.setToolTipText("Añadir Articulo o Servicio");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel5MouseReleased(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/- 24x24.png"))); // NOI18N
        jLabel4.setToolTipText("Quitar Articulo o Servicio");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("SubTotal:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("...");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("...");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("IVA:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Total a Pagar:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("...");

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

        bimprimir.setBackground(new java.awt.Color(50, 72, 141));
        bimprimir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bimprimir.setForeground(new java.awt.Color(255, 255, 255));
        bimprimir.setText("Imprimir");
        bimprimir.setContentAreaFilled(false);
        bimprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bimprimir.setFocusable(false);
        bimprimir.setOpaque(true);
        bimprimir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bimprimirMouseReleased(evt);
            }
        });

        blimpiar.setBackground(new java.awt.Color(50, 72, 141));
        blimpiar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        blimpiar.setForeground(new java.awt.Color(255, 255, 255));
        blimpiar.setText("Limpiar");
        blimpiar.setContentAreaFilled(false);
        blimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        blimpiar.setFocusable(false);
        blimpiar.setOpaque(true);
        blimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                blimpiarMouseReleased(evt);
            }
        });

        bfinalizar.setBackground(new java.awt.Color(50, 72, 141));
        bfinalizar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bfinalizar.setForeground(new java.awt.Color(255, 255, 255));
        bfinalizar.setText("Finalizar");
        bfinalizar.setContentAreaFilled(false);
        bfinalizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bfinalizar.setFocusable(false);
        bfinalizar.setOpaque(true);
        bfinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bfinalizarMouseReleased(evt);
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
                        .addComponent(bimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(blimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(bfinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                        .addComponent(jTextField3)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel4))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(11, 11, 11)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bfinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(blimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jTextField2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel4))
                    .addGap(224, 224, 224)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(54, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void binactivarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_binactivarMouseReleased
        inactivarIngreso();
    }//GEN-LAST:event_binactivarMouseReleased

    private void bimprimirMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bimprimirMouseReleased
//        imprimir();
    }//GEN-LAST:event_bimprimirMouseReleased

    private void blimpiarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blimpiarMouseReleased
        limpiar();
    }//GEN-LAST:event_blimpiarMouseReleased

    private void bfinalizarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bfinalizarMouseReleased
        if (bfinalizar.isEnabled()) {
            finalizarIngreso();
        }
    }//GEN-LAST:event_bfinalizarMouseReleased

    private void bGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGuardarMouseReleased
        if (bGuardar.isEnabled()) {
            saveIngreso();
        }
    }//GEN-LAST:event_bGuardarMouseReleased

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargaIngreso();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.selectAll();
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargaUser();
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jLabel5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseReleased
        addIngreso();
    }//GEN-LAST:event_jLabel5MouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bGuardar;
    public javax.swing.JButton bSalir;
    public javax.swing.JButton bfinalizar;
    public javax.swing.JButton bimprimir;
    public javax.swing.JButton binactivar;
    public javax.swing.JButton blimpiar;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
