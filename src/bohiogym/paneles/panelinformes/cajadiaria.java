/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelinformes;

import bohiogym.clases.Funciones;
import entity.EgresoItem;
import entity.UserLog;
import entity.UserPermisos;
import entity.VariedadesAbono;
import entity.VariedadesCaja;
import entity.VariedadesDevoluciones;
import entity.VariedadesEgresos;
import entity.VariedadesIngresos;
import entity.VariedadesSuministro;
import entity.VariedadesVentas;
import entity.VentasServicios;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import jpa.EgresoItemJpaController;
import jpa.UserLogJpaController;
import jpa.VariedadesCajaJpaController;
import jpa.VariedadesDevolucionesJpaController;
import jpa.VariedadesEgresosJpaController;
import jpa.VariedadesIngresosJpaController;
import jpa.VariedadesSuministroJpaController;
import jpa.VariedadesVentasJpaController;

/**
 *
 * @author Alvarez
 */
public class cajadiaria extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    private final UserLog userLog;
    VariedadesCaja caja = null;
    VariedadesCajaJpaController vcjc = null;
    VariedadesVentas ventas = null;
    VariedadesVentasJpaController vvjc = null;
    VariedadesDevoluciones devoluciones = null;
    VariedadesDevolucionesJpaController vdjc = null;
    VariedadesEgresos egresos = null;
    VariedadesEgresosJpaController vejc = null;
    VariedadesIngresos ingresos = null;
    VariedadesIngresosJpaController vijc = null;
    VariedadesSuministro suministro = null;
    VariedadesSuministroJpaController vsjc = null;
    UserLogJpaController uljc = null;
    EgresoItemJpaController iijc = null;
    DefaultTableModel model, model2;
    Object dato[] = null, dato2[] = null;
    int mensual = 0, tiquetera = 0, dia = 0;

    /**
     * Creates new form cajadiaria
     */
    public cajadiaria(EntityManagerFactory factory, UserLog userLog) {
        initComponents();
        this.factory = factory;
        this.userLog = userLog;
        vcjc = new VariedadesCajaJpaController(factory);
        vvjc = new VariedadesVentasJpaController(factory);
        vdjc = new VariedadesDevolucionesJpaController(factory);
        vejc = new VariedadesEgresosJpaController(factory);
        vijc = new VariedadesIngresosJpaController(factory);
        vsjc = new VariedadesSuministroJpaController(factory);
        uljc = new UserLogJpaController(factory);
        iijc = new EgresoItemJpaController(factory);
        jDateChooser1.setDate(new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate(), 0, 0, 0));
        iniciarCaja(jDateChooser1.getDate());
        setCargarTablaIngresos();
        setCargarTablaEgresos();
        setdatosIngreso();
        setdatosEgresos();
        calculaEstado();
    }

    private boolean validaAdmin() {
        boolean admin = false;
        for (UserPermisos up : getPermisos(userLog)) {
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

    private List<VariedadesCaja> obtenerCaja(Date date) {
        EntityManager em = vcjc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM VariedadesCaja c WHERE c.fecha=:date AND (c.estado='1' OR c.estado='2')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("date", date)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void iniciarCaja(Date dato) {
        if (obtenerCaja(dato).size() > 0) {
            caja = obtenerCaja(dato).get(0);
            jLabel6.setText(String.valueOf(caja.getValorinicial()));
            jLabel2.setText(String.valueOf(caja.getEstadofinal()));
        } else {
            caja = new VariedadesCaja();
            float inicial = Float.parseFloat(JOptionPane.showInputDialog("Ingrese el saldo inicial de la caja: "));
            caja.setValorinicial(inicial);
            caja.setEstado(1);
            caja.setFecha(dato);
            caja.setEstadofinal(0);
            vcjc.create(caja);
            jLabel6.setText(String.valueOf(caja.getValorinicial()));
            jLabel2.setText(String.valueOf(caja.getEstadofinal()));
        }
    }

    private DefaultTableModel getModeloIngresos() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"id", "Tipo", "#", "Valor"}) {
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

    private void setCargarTablaIngresos() {
        model = getModeloIngresos();
        jTable2.setModel(model);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Funciones.setOcultarColumnas(jTable2, new int[]{0});
        //226
        jTable2.getColumnModel().getColumn(1).setMinWidth(100);
        jTable2.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(100);
        jTable2.getColumnModel().getColumn(2).setMinWidth(56);
        jTable2.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(56);
        jTable2.getColumnModel().getColumn(3).setMinWidth(70);
        jTable2.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(70);
    }

    private DefaultTableModel getModeloEgresos() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"id", "Tipo", "#", "Valor"}) {
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

    private void setCargarTablaEgresos() {
        model2 = getModeloEgresos();
        jTable1.setModel(model2);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Funciones.setOcultarColumnas(jTable1, new int[]{0});
        //226
        jTable2.getColumnModel().getColumn(1).setMinWidth(100);
        jTable2.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(100);
        jTable2.getColumnModel().getColumn(2).setMinWidth(56);
        jTable2.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(56);
        jTable2.getColumnModel().getColumn(3).setMinWidth(70);
        jTable2.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(70);
    }

    private void setdatosIngreso() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        List<VariedadesIngresos> vis = obtenerIngresos();
        List<VariedadesVentas> vvs = obtenerVentas();
        if (validaAdmin()) {
            for (VariedadesIngresos vi : vis) {
                if (Funciones.ddMMyyyy.format(vi.getFechaingreso()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model.addRow(dato);
                    model.setValueAt(vi, model.getRowCount() - 1, 0);
                    model.setValueAt("INGRESO", model.getRowCount() - 1, 1);
                    model.setValueAt(vi.getCodigoingreso(), model.getRowCount() - 1, 2);
                    model.setValueAt(vi.getValortotal(), model.getRowCount() - 1, 3);
                }
            }
            for (VariedadesVentas vv : vvs) {
                if (Funciones.ddMMyyyy.format(vv.getFechaventa()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model.addRow(dato);
                    model.setValueAt(vv, model.getRowCount() - 1, 0);
                    if (vv.getVentasServiciosList().size() > 0) {
                        model.setValueAt(vv.getVentasServiciosList().get(0).getIdservicio().getNombreservicio(), model.getRowCount() - 1, 1);
                    } else {
                        model.setValueAt("VENTA", model.getRowCount() - 1, 1);
                    }
                    model.setValueAt(vv.getCodigoventa(), model.getRowCount() - 1, 2);
                    model.setValueAt(vv.getValortotal(), model.getRowCount() - 1, 3);
                    for (VentasServicios vs : obtenerServicios(vv)) {
                        if (vs.getIdservicio().getCodigoservicio().equals("30")) {
                            mensual = mensual + 1;
                        }
                        if (vs.getIdservicio().getCodigoservicio().equals("12")) {
                            tiquetera = tiquetera + 1;
                        }
                        if (vs.getIdservicio().getCodigoservicio().equals("01")) {
                            dia = vs.getCantidad();
                        }
                    }
                }
            }
            for (VariedadesAbono abono : obtenerAbono()) {
                if (Funciones.ddMMyyyy.format(abono.getFecha()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model.addRow(dato);
                    model.setValueAt(abono, model.getRowCount() - 1, 0);
                    model.setValueAt("ABONO", model.getRowCount() - 1, 1);
                    model.setValueAt(abono.getIdventa().getCodigoventa(), model.getRowCount() - 1, 2);
                    model.setValueAt(abono.getAbono(), model.getRowCount() - 1, 3);
                }
            }
        } else {
            for (VariedadesIngresos vi : vis) {
                if (Funciones.ddMMyyyy.format(vi.getFechaingresotiempo()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model.addRow(dato);
                    model.setValueAt(vi, model.getRowCount() - 1, 0);
                    model.setValueAt("INGRESO", model.getRowCount() - 1, 1);
                    model.setValueAt(vi.getCodigoingreso(), model.getRowCount() - 1, 2);
                    model.setValueAt(vi.getValortotal(), model.getRowCount() - 1, 3);
                }
            }
            for (VariedadesVentas vv : vvs) {
                if (Funciones.ddMMyyyy.format(vv.getFechaventatiempo()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model.addRow(dato);
                    model.setValueAt(vv, model.getRowCount() - 1, 0);
                    if (vv.getVentasServiciosList().size() > 0) {
                        model.setValueAt(vv.getVentasServiciosList().get(0).getIdservicio().getNombreservicio(), model.getRowCount() - 1, 1);
                    } else {
                        model.setValueAt("VENTA", model.getRowCount() - 1, 1);
                    }
                    model.setValueAt(vv.getCodigoventa(), model.getRowCount() - 1, 2);
                    model.setValueAt(vv.getValortotal(), model.getRowCount() - 1, 3);
                    for (VentasServicios vs : obtenerServicios(vv)) {
                        if (vs.getIdservicio().getCodigoservicio().equals("30")) {
                            mensual = mensual + 1;
                        }
                        if (vs.getIdservicio().getCodigoservicio().equals("12")) {
                            tiquetera = tiquetera + 1;
                        }
                        if (vs.getIdservicio().getCodigoservicio().equals("01")) {
                            dia = vs.getCantidad();
                        }
                    }
                }
            }
            for (VariedadesAbono abono : obtenerAbono()) {
                if (Funciones.ddMMyyyy.format(abono.getFecha()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model.addRow(dato);
                    model.setValueAt(abono, model.getRowCount() - 1, 0);
                    model.setValueAt("ABONO", model.getRowCount() - 1, 1);
                    model.setValueAt(abono.getIdventa().getCodigoventa(), model.getRowCount() - 1, 2);
                    model.setValueAt(abono.getAbono(), model.getRowCount() - 1, 3);
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#");
        float tt = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            tt = tt + Float.parseFloat(model.getValueAt(i, 3).toString());
        }
        jLabel10.setText(df.format(tt));
    }

    private List<VentasServicios> obtenerServicios(VariedadesVentas venta) {
        EntityManager em = vsjc.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM VentasServicios a WHERE a.idventa=:venta AND a.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("venta", venta)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void setdatosEgresos() {
        while (model2.getRowCount() > 0) {
            model2.removeRow(0);
        }
        List<VariedadesDevoluciones> vds = obtenerDevoluciones();
        List<VariedadesEgresos> ves = obtenerEgresos();
        List<VariedadesSuministro> vses = obtenerSuministro();
        if (validaAdmin()) {
            for (VariedadesDevoluciones vd : vds) {
                if (Funciones.ddMMyyyy.format(vd.getFechaventa()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model2.addRow(dato2);
                    model2.setValueAt(vd, model2.getRowCount() - 1, 0);
                    model2.setValueAt("DEVOLUCION", model2.getRowCount() - 1, 1);
                    model2.setValueAt(vd.getCodigodevolucion(), model2.getRowCount() - 1, 2);
                    model2.setValueAt(vd.getValortotal(), model2.getRowCount() - 1, 3);
                }
            }
            for (VariedadesEgresos ve : ves) {
                if (Funciones.ddMMyyyy.format(ve.getFechaegreso()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model2.addRow(dato2);
                    model2.setValueAt(ve, model2.getRowCount() - 1, 0);
                    model2.setValueAt("EGRESO", model2.getRowCount() - 1, 1);
                    model2.setValueAt(ve.getCodigoegreso(), model2.getRowCount() - 1, 2);
                    model2.setValueAt(ve.getValortotal(), model2.getRowCount() - 1, 3);
                }
            }
            for (VariedadesSuministro ve : vses) {
                if (Funciones.ddMMyyyy.format(ve.getFechaventa()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model2.addRow(dato2);
                    model2.setValueAt(ve, model2.getRowCount() - 1, 0);
                    model2.setValueAt("SUMINISTRO", model2.getRowCount() - 1, 1);
                    model2.setValueAt(ve.getCodigosuministro(), model2.getRowCount() - 1, 2);
                    model2.setValueAt(ve.getValortotal(), model2.getRowCount() - 1, 3);
                }
            }
        } else {
            for (VariedadesDevoluciones vd : vds) {
                if (Funciones.ddMMyyyy.format(vd.getFechaventatiempo()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model2.addRow(dato2);
                    model2.setValueAt(vd, model2.getRowCount() - 1, 0);
                    model2.setValueAt("DEVOLUCION", model2.getRowCount() - 1, 1);
                    model2.setValueAt(vd.getCodigodevolucion(), model2.getRowCount() - 1, 2);
                    model2.setValueAt(vd.getValortotal(), model2.getRowCount() - 1, 3);
                }
            }
            for (VariedadesEgresos ve : ves) {
                if (Funciones.ddMMyyyy.format(ve.getFechaegresotiempo()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model2.addRow(dato2);
                    model2.setValueAt(ve, model2.getRowCount() - 1, 0);
                    model2.setValueAt("EGRESO", model2.getRowCount() - 1, 1);
                    model2.setValueAt(ve.getCodigoegreso(), model2.getRowCount() - 1, 2);
                    model2.setValueAt(ve.getValortotal(), model2.getRowCount() - 1, 3);
                }
            }
            for (VariedadesSuministro ve : vses) {
                if (Funciones.ddMMyyyy.format(ve.getFechaventatiempo()).toString().equals(Funciones.ddMMyyyy.format(jDateChooser1.getDate()))) {
                    model2.addRow(dato2);
                    model2.setValueAt(ve, model2.getRowCount() - 1, 0);
                    model2.setValueAt("SUMINISTRO", model2.getRowCount() - 1, 1);
                    model2.setValueAt(ve.getCodigosuministro(), model2.getRowCount() - 1, 2);
                    model2.setValueAt(ve.getValortotal(), model2.getRowCount() - 1, 3);
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#");
        float tt = 0;
        for (int i = 0; i < model2.getRowCount(); i++) {
            tt = tt + Float.parseFloat(model2.getValueAt(i, 3).toString());
        }
        jLabel12.setText(df.format(tt));
    }

    private List<VariedadesAbono> obtenerAbono() {
        EntityManager em = vvjc.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM VariedadesAbono f")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<VariedadesDevoluciones> obtenerDevoluciones() {
        EntityManager em = vdjc.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM VariedadesDevoluciones d WHERE (d.estado='1' OR d.estado='2')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<VariedadesEgresos> obtenerEgresos() {
        EntityManager em = vejc.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM VariedadesEgresos d WHERE (d.estado='1' OR d.estado='2')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<EgresoItem> obtenerItem(VariedadesEgresos ingreso) {
        EntityManager em = iijc.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM EgresoItem a WHERE a.idegreso=:ingreso AND a.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("ingreso", ingreso)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<VariedadesIngresos> obtenerIngresos() {
        EntityManager em = vijc.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM VariedadesIngresos d WHERE (d.estado='1' OR d.estado='2')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<VariedadesVentas> obtenerVentas() {
        EntityManager em = vvjc.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM VariedadesVentas d WHERE (d.estado='1' OR d.estado='2') AND d.efectivo='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<VariedadesSuministro> obtenerSuministro() {
        EntityManager em = vsjc.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM VariedadesSuministro d WHERE (d.estado='1' OR d.estado='2')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void calculaEstado() {
        float canttotal = 0, cantingresos = 0, cantegresos = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            cantingresos = cantingresos + Float.parseFloat(model.getValueAt(i, 3).toString());
        }
        for (int j = 0; j < model2.getRowCount(); j++) {
            cantegresos = cantegresos + Float.parseFloat(model2.getValueAt(j, 3).toString());
        }
        canttotal = (Float.parseFloat(jLabel6.getText()) + cantingresos) - cantegresos;
        jLabel2.setText(String.valueOf(canttotal));
    }

    private void cerrarCaja() {
        DecimalFormat df = new DecimalFormat("#");
        if (caja.getEstado() == 2) {
            JOptionPane.showMessageDialog(null, "La caja ya esta cerrada");
        } else {
            String mensaje = "Esta a punto de cerrar la caja.\n¿Esta seguro de cerrar la caja? ";
            int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Desactivar Articulo", JOptionPane.YES_NO_OPTION);
            if (entrada == 0) {
                try {
                    caja.setValorinicial(Float.parseFloat(jLabel6.getText()));
                    caja.setEstado(2);
                    caja.setFecha(jDateChooser1.getDate());
                    caja.setEstadofinal(Float.parseFloat(jLabel2.getText()));
                    vcjc.edit(caja);
                    jLabel6.setText(df.format(caja.getValorinicial()));
                    jLabel2.setText(df.format(caja.getEstadofinal()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al cerrar la caja: " + ex.getMessage(), cajadiaria.class.getName(), JOptionPane.ERROR_MESSAGE);
                }
            }
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

        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        bSalir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Estado de la caja:");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("...");
        jLabel2.setOpaque(true);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 72, 141));
        jLabel3.setText("Fecha:");

        jDateChooser1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel4MouseReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0)), "Entradas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 153, 0))); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jLabel9.setForeground(new java.awt.Color(0, 153, 0));
        jLabel9.setText("Total:");

        jLabel10.setForeground(new java.awt.Color(0, 153, 0));
        jLabel10.setText("...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(50, 72, 141)), "Salidas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 72, 141))); // NOI18N

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

        jLabel11.setForeground(new java.awt.Color(179, 80, 72));
        jLabel11.setText("Total:");

        jLabel12.setForeground(new java.awt.Color(179, 80, 72));
        jLabel12.setText("...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)))
        );

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        jButton1.setBackground(new java.awt.Color(50, 72, 141));
        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cerrar Caja");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusable(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(50, 72, 141));
        jLabel5.setText("Saldo Inicial:");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("...");
        jLabel6.setOpaque(true);

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("$");
        jLabel7.setOpaque(true);

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("$");
        jLabel8.setOpaque(true);

        jButton2.setBackground(new java.awt.Color(50, 72, 141));
        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Informe");
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusable(false);
        jButton2.setOpaque(true);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)))
                        .addGap(12, 12, 12)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseReleased
        iniciarCaja(jDateChooser1.getDate());
        setdatosIngreso();
        setdatosEgresos();
        calculaEstado();
    }//GEN-LAST:event_jLabel4MouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cerrarCaja();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String mensaje = "";
        DecimalFormat df = new DecimalFormat("#");
        for (int i = 0; i < model2.getRowCount(); i++) {
            for (int j = 0; j < obtenerItem((VariedadesEgresos) model2.getValueAt(i, 0)).size(); j++) {
                String[] tex = obtenerItem((VariedadesEgresos) model2.getValueAt(i, 0)).get(j).getDescripcion().split("<html><div style=\"width:215;\">");
                String[] tex2 = tex[1].split("</div></html>");
                mensaje = mensaje + tex2[0] + ": " + df.format(obtenerItem((VariedadesEgresos) model2.getValueAt(i, 0)).get(j).getTotal()) + "\n";
            }
        }
        JOptionPane.showMessageDialog(null, "Resumen de servicios y egresos del dia: " + Funciones.ddMMyyyy.format(jDateChooser1.getDate())
                + "\n Cantidad de Mensualidades Vendidas: " + mensual
                + "\n Cantidad de Tiqueteras Vendidas: " + tiquetera
                + "\n Cantidad de Planes Diarios Vendidos: " + dia
                + "\n Por un total de: " + jLabel10.getText()
                + "\n ---------------------------------"
                + "\n Egresos:"
                + "\n" + mensaje
                + "\n Por un total de: " + jLabel12.getText(), "Resumen Diario", JOptionPane.INFORMATION_MESSAGE);

        mensual = 0;
        tiquetera = 0;
        dia = 0;
        mensaje = "";
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bSalir;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
