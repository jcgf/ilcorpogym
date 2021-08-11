/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelarticulos;

import bohiogym.clases.Funciones;
import bohiogym.paneles.panelarticulos.subarticulos.pDevolucion;
import entity.ConsecutivoDevoluciones;
import entity.DevolucionesArticulos;
import entity.UserLog;
import entity.UserPermisos;
import entity.VariedadesArticulos;
import entity.VariedadesDevoluciones;
import entity.GymTerceros;
import entity.GymUsuarios;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import jpa.ConsecutivoDevolucionesJpaController;
import jpa.DevolucionesArticulosJpaController;
import jpa.UserLogJpaController;
import jpa.VariedadesArticulosJpaController;
import jpa.VariedadesDevolucionesJpaController;
import jpa.GymTercerosJpaController;
import jpa.GymUsuariosJpaController;

/**
 *
 * @author Alvarez
 */
public class devoluciones extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    private final UserLog user;
    UserLogJpaController uljc = null;
    GymUsuariosJpaController vujc = null;
    ConsecutivoDevolucionesJpaController cvjc = null;
    DevolucionesArticulos ventasArticulos = null;
    ConsecutivoDevoluciones consecutivoVentas = null;
    VariedadesDevoluciones variedadesVentas = null;
    DevolucionesArticulosJpaController vajc = null;
    List<DevolucionesArticulos> listarticulos = null;
    VariedadesDevolucionesJpaController vvjc = null;
    GymTercerosJpaController vtjc = null;
    VariedadesArticulosJpaController gajc = null;
    DefaultTableModel model;
    Object dato[] = null;

    /**
     * Creates new form ventas
     */
    public devoluciones(EntityManagerFactory factory, UserLog user) {
        initComponents();
        this.factory = factory;
        this.user = user;
        vujc = new GymUsuariosJpaController(factory);
        vtjc = new GymTercerosJpaController(factory);
        uljc = new UserLogJpaController(factory);
        cvjc = new ConsecutivoDevolucionesJpaController(factory);
        vajc = new DevolucionesArticulosJpaController(factory);
        gajc = new VariedadesArticulosJpaController(factory);
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
        if (vvjc == null) {
            vvjc = new VariedadesDevolucionesJpaController(factory);
        }
        if (obtenerVenta(Integer.valueOf(jTextField1.getText())).size() > 0) {
            cargaVenta();
        } else {
            bGuardar.setEnabled(true);
            bfinalizar.setEnabled(false);
        }
        bimprimir.setVisible(false);
    }

    private List<ConsecutivoDevoluciones> obtenerConsecutivos() {
        EntityManager em = cvjc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM ConsecutivoDevoluciones c WHERE (c.estado='1' OR c.estado='2') ORDER BY c.numero ASC")
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

    private boolean validaAdmin() {
        boolean admin = false;
        for (UserPermisos up : getPermisos(user)) {
            if (up.getIdpermiso().getId() == 1) {
                admin = true;
                break;
            }
        }
        return admin;
    }

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"id", "idventa", "idarticulo", "Codigo", "Descripcion", "Valor", "Cant", "Subtotal", "IVA", "Total", "Estado", "valoriva"}) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.String.class,
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
                false, false, false, false, false, false, false, false, false, false, false, false
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
        Funciones.setOcultarColumnas(jTable1, new int[]{0, 1, 2, 10, 11});
        //470
        jTable1.getColumnModel().getColumn(3).setMinWidth(50);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(50);
        jTable1.getColumnModel().getColumn(4).setMinWidth(165);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(165);
        jTable1.getColumnModel().getColumn(5).setMinWidth(60);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(6).setMinWidth(35);
        jTable1.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(35);
        jTable1.getColumnModel().getColumn(7).setMinWidth(60);
        jTable1.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(8).setMinWidth(40);
        jTable1.getTableHeader().getColumnModel().getColumn(8).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(9).setMinWidth(60);
        jTable1.getTableHeader().getColumnModel().getColumn(9).setMaxWidth(60);
        jTable1.setDefaultRenderer(Object.class, new bohiogym.clases.ColorDefaultTableCellRenderer());
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

    private void cargaUser() {
        if (vujc == null) {
            vujc = new GymUsuariosJpaController(factory);
        }
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

    private void cargaArticulo() {
        final pDevolucion ventaa = new pDevolucion((JFrame) SwingUtilities.getWindowAncestor(this), true, factory);
        ventaa.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ventaa.jTable1.getSelectedRow() > -1) {
                    if (ventaa.jTextField1.getText() == null || ventaa.jTextField1.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
                    } else {
                        VariedadesArticulos varti = (VariedadesArticulos) ventaa.jTable1.getValueAt(ventaa.jTable1.getSelectedRow(), 0);
                        boolean exi = false;
                        int j = 0;
                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (varti.getId() == ((VariedadesArticulos) model.getValueAt(i, 2)).getId()) {
                                exi = true;
                                j = i;
                                break;
                            }
                        }
                        if (!exi) {
                            model.addRow(dato);
                            model.setValueAt(ventasArticulos, model.getRowCount() - 1, 0);
                            model.setValueAt("", model.getRowCount() - 1, 1);
                            model.setValueAt(varti, model.getRowCount() - 1, 2);
                            model.setValueAt("<html><div style=\"width:60;\">" + varti.getCodigoarticulo() + "</div></html>", model.getRowCount() - 1, 3);
                            model.setValueAt("<html><div style=\"width:165;\">" + varti.getNombrearticulo() + "</div></html>", model.getRowCount() - 1, 4);
                            model.setValueAt(varti.getValorventa(), model.getRowCount() - 1, 5);
                            model.setValueAt(ventaa.jTextField1.getText(), model.getRowCount() - 1, 6);
                            float subtotal = Float.parseFloat(ventaa.jTextField1.getText()) * varti.getValorventa();
                            model.setValueAt(subtotal, model.getRowCount() - 1, 7);
                            model.setValueAt(varti.getIvaventa(), model.getRowCount() - 1, 8);
                            float iva = (subtotal * varti.getIvaventa()) / 100;
                            float total = iva + subtotal;
                            model.setValueAt(total, model.getRowCount() - 1, 9);
                            model.setValueAt(1, model.getRowCount() - 1, 10);
                            model.setValueAt(iva, model.getRowCount() - 1, 11);
                            float subt = 0;
                            float iv = 0;
                            for (int i = 0; i < model.getRowCount(); i++) {
                                subt = subt + Float.parseFloat(model.getValueAt(i, 7).toString());
                                iv = iv + Float.parseFloat(model.getValueAt(i, 11).toString());
                            }
                            jLabel6.setText(String.valueOf(subt));
                            jLabel8.setText(String.valueOf(iv));
                            jLabel10.setText(String.valueOf(iv + subt));
                            ventaa.dispose();
                        } else {
                            int cantida = Integer.parseInt(model.getValueAt(j, 6).toString()) + Integer.parseInt(ventaa.jTextField1.getText());
                            model.setValueAt(cantida, j, 6);
                            float subtotal = cantida * varti.getValorventa();
                            model.setValueAt(subtotal, j, 7);
                            model.setValueAt(varti.getIvaventa(), j, 8);
                            float iva = (subtotal * varti.getIvaventa()) / 100;
                            float total = iva + subtotal;
                            model.setValueAt(total, j, 9);
                            model.setValueAt(1, j, 10);
                            model.setValueAt(iva, j, 11);
                            float subt = 0;
                            float iv = 0;
                            for (int i = 0; i < model.getRowCount(); i++) {
                                subt = subt + Float.parseFloat(model.getValueAt(i, 7).toString());
                                iv = iv + Float.parseFloat(model.getValueAt(i, 11).toString());
                            }
                            jLabel6.setText(String.valueOf(subt));
                            jLabel8.setText(String.valueOf(iv));
                            jLabel10.setText(String.valueOf(iv + subt));
                            ventaa.dispose();
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
                }
            }
        });
        ventaa.setLocationRelativeTo(null);
        ventaa.setVisible(true);
    }

    private void cargaVenta() {
        if (vvjc == null) {
            vvjc = new VariedadesDevolucionesJpaController(factory);
        }
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        if (obtenerVenta(Integer.valueOf(jTextField1.getText())).size() > 0) {
            variedadesVentas = obtenerVenta(Integer.valueOf(jTextField1.getText())).get(0);
            if (variedadesVentas.getEstado() == 2) {
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
            jTextField2.setText(variedadesVentas.getCodcliente());
            if (obteneruser(jTextField2.getText()).size() > 0) {
                jTextField3.setText(obteneruser(jTextField2.getText()).get(0).getNombres() + " " + obteneruser(jTextField2.getText()).get(0).getApellidos());
            } else if (obtenerTercero(jTextField2.getText()).size() > 0) {
                jTextField3.setText(obtenerTercero(jTextField2.getText()).get(0).getRazonsocial());
            } else {
                jTextField3.setText(" ");
            }
            jDateChooser1.setDate(variedadesVentas.getFechaventa());
            if (obtenerArticulo(variedadesVentas).size() > 0) {
                for (DevolucionesArticulos va : obtenerArticulo(variedadesVentas)) {
                    model.addRow(dato);
                    model.setValueAt(va, model.getRowCount() - 1, 0);
                    model.setValueAt(va.getIddevolucion(), model.getRowCount() - 1, 1);
                    model.setValueAt(va.getIdarticulo(), model.getRowCount() - 1, 2);
                    model.setValueAt("<html><div style=\"width:60;\">" + va.getIdarticulo().getCodigoarticulo() + "</div></html>", model.getRowCount() - 1, 3);
                    model.setValueAt("<html><div style=\"width:165;\">" + va.getIdarticulo().getNombrearticulo() + "</div></html>", model.getRowCount() - 1, 4);
                    model.setValueAt(va.getValorindividual(), model.getRowCount() - 1, 5);
                    model.setValueAt(va.getCantidad(), model.getRowCount() - 1, 6);
                    model.setValueAt(va.getSubtotal(), model.getRowCount() - 1, 7);
                    model.setValueAt(va.getIdarticulo().getIvaventa(), model.getRowCount() - 1, 8);
                    model.setValueAt(va.getTotal(), model.getRowCount() - 1, 9);
                    model.setValueAt(va.getEstado(), model.getRowCount() - 1, 10);
                    model.setValueAt(va.getValoriva(), model.getRowCount() - 1, 11);
                }
            }
            jLabel6.setText(String.valueOf(variedadesVentas.getSubtotal()));
            jLabel8.setText(String.valueOf(variedadesVentas.getValoriva()));
            jLabel10.setText(String.valueOf(variedadesVentas.getValortotal()));
        }
    }

    private List<VariedadesDevoluciones> obtenerVenta(int nfactura) {
        EntityManager em = vvjc.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM VariedadesDevoluciones f WHERE f.codigodevolucion=:nfactura AND (f.estado='1' OR f.estado='2')")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("nfactura", nfactura)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<DevolucionesArticulos> obtenerArticulo(VariedadesDevoluciones venta) {
        EntityManager em = vajc.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM DevolucionesArticulos a WHERE a.iddevolucion=:venta AND a.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("venta", venta)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void saveVenta() {
        if (vvjc == null) {
            vvjc = new VariedadesDevolucionesJpaController(factory);
        }
        if (cvjc == null) {
            cvjc = new ConsecutivoDevolucionesJpaController(factory);
        }
        if (variedadesVentas == null) {
            variedadesVentas = new VariedadesDevoluciones();
            variedadesVentas.setCodigodevolucion(Integer.parseInt(jTextField1.getText()));
            if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                variedadesVentas.setCodcliente(" ");
            } else {
                variedadesVentas.setCodcliente(jTextField2.getText());
            }
            variedadesVentas.setFechaventa(jDateChooser1.getDate());
            variedadesVentas.setFechaventatiempo(new Date());
            variedadesVentas.setSubtotal(Float.parseFloat(jLabel6.getText()));
            variedadesVentas.setValoriva(Float.parseFloat(jLabel8.getText()));
            variedadesVentas.setValortotal(Float.valueOf(jLabel10.getText()));
            variedadesVentas.setIdusuario(user);
            variedadesVentas.setEstado(1);
            vvjc.create(variedadesVentas);
            saveArticulosVenta(variedadesVentas);
            try {
                ConsecutivoDevoluciones cv = obtenerc(Integer.parseInt(jTextField1.getText())).get(0);
                cv.setEstado(2);
                cvjc.edit(cv);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar consecutivo" + e.getMessage(), devoluciones.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Devolucion guardada");
            cargaVenta();
            bfinalizar.setEnabled(true);
        } else {
            try {
                variedadesVentas.setCodigodevolucion(Integer.parseInt(jTextField1.getText()));
                if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                    variedadesVentas.setCodcliente(" ");
                } else {
                    variedadesVentas.setCodcliente(jTextField2.getText());
                }
                variedadesVentas.setFechaventa(jDateChooser1.getDate());
                variedadesVentas.setFechaventatiempo(new Date());
                variedadesVentas.setSubtotal(Float.parseFloat(jLabel6.getText()));
                variedadesVentas.setValoriva(Float.parseFloat(jLabel8.getText()));
                variedadesVentas.setValortotal(Float.valueOf(jLabel10.getText()));
                variedadesVentas.setIdusuario(user);
                variedadesVentas.setEstado(1);
                vvjc.edit(variedadesVentas);
                saveArticulosVenta(variedadesVentas);
                JOptionPane.showMessageDialog(null, "Devolucion guardada");
                cargaVenta();
                bfinalizar.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al guardar la devolucion: " + ex.getMessage(), devoluciones.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<ConsecutivoDevoluciones> obtenerc(int consecutivo) {
        EntityManager em = cvjc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM ConsecutivoDevoluciones c WHERE c.numero=:consecutivo")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("consecutivo", consecutivo)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void saveArticulosVenta(VariedadesDevoluciones venta) {
        if (vajc == null) {
            vajc = new DevolucionesArticulosJpaController(factory);
        }
        listarticulos = obtenerArticulo(venta);
        for (int i = 0; i < model.getRowCount(); i++) {
            DevolucionesArticulos articulos = null;
            boolean exist = false;
            for (DevolucionesArticulos va : listarticulos) {
                if (((VariedadesArticulos) model.getValueAt(i, 2)).getId() == va.getIdarticulo().getId()) {
                    exist = true;
                    articulos = va;
                    break;
                }
            }
            if (!exist) {
                articulos = new DevolucionesArticulos();
                articulos.setIddevolucion(venta);
                articulos.setIdarticulo((VariedadesArticulos) model.getValueAt(i, 2));
                articulos.setValorindividual(Float.parseFloat(model.getValueAt(i, 5).toString()));
                articulos.setCantidad(Integer.parseInt(model.getValueAt(i, 6).toString()));
                articulos.setSubtotal(Float.parseFloat(model.getValueAt(i, 7).toString()));
                articulos.setValoriva(Float.parseFloat(model.getValueAt(i, 11).toString()));
                articulos.setTotal(Float.parseFloat(model.getValueAt(i, 9).toString()));
                articulos.setEstado(Integer.valueOf(model.getValueAt(i, 10).toString()));
                vajc.create(articulos);
                model.setValueAt(venta, i, 1);
            } else if (articulos != null) {
                articulos.setIddevolucion(venta);
                articulos.setEstado(Integer.valueOf(model.getValueAt(i, 10).toString()));
                try {
                    vajc.edit(articulos);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar articulo: " + ex.getMessage(), devoluciones.class.getName(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void removeArticulo() {
        if (jTable1.getSelectedRow() > -1) {
            String mensaje = "Esta a punto de inactivar el articulo seleccionado.\n¿Esta seguro de desactivar el articulo? ";
            int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Desactivar Articulo", JOptionPane.YES_NO_OPTION);
            if (entrada == 0) {
                model.setValueAt(0, jTable1.getSelectedRow(), 10);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un articulo de la lista");
        }
    }

    private void finalizarVenta() {
        String mensaje = "Esta a punto de finalizar la devolucion, no podra modificarla despues.\n¿Esta seguro de finalizar la devolucion? ";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Finalizar", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            if (vvjc == null) {
                vvjc = new VariedadesDevolucionesJpaController(factory);
            }
            if (cvjc == null) {
                cvjc = new ConsecutivoDevolucionesJpaController(factory);
            }
            variedadesVentas.setEstado(2);
            try {
                vvjc.edit(variedadesVentas);
                ConsecutivoDevoluciones cv = obtenerc(Integer.parseInt(jTextField1.getText())).get(0);
                cv.setEstado(3);
                cvjc.edit(cv);
                JOptionPane.showMessageDialog(null, "Devolucion finalizada exitosamente");
                cargaVenta();
            } catch (Exception exe) {
                JOptionPane.showMessageDialog(null, "Error al Finalizar la devolucion: " + exe.getMessage(), devoluciones.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void inactivarVenta() {
        String mensaje = "Esta a punto de inactivar la devolucion y el consecutivo, no podra modificarla despues.\n¿Esta seguro de desactivar la devolucion? ";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Finalizar", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            if (vvjc == null) {
                vvjc = new VariedadesDevolucionesJpaController(factory);
            }
            if (cvjc == null) {
                cvjc = new ConsecutivoDevolucionesJpaController(factory);
            }
            variedadesVentas.setEstado(0);
            try {
                vvjc.edit(variedadesVentas);
                ConsecutivoDevoluciones cv = obtenerc(Integer.parseInt(jTextField1.getText())).get(0);
                cv.setEstado(0);
                cvjc.edit(cv);
                JOptionPane.showMessageDialog(null, "Devolucion inactivada exitosamente");
                limpiar();
            } catch (Exception exe) {
                JOptionPane.showMessageDialog(null, "Error al Finalizar la devolucion: " + exe.getMessage(), devoluciones.class.getName(), JOptionPane.ERROR_MESSAGE);
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
        ventasArticulos = null;
        consecutivoVentas = null;
        variedadesVentas = null;
    }

    private void cargaarticulotext() {
        if (jTextField4.getText() == null || jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para realizar la busqueda");
            jTextField4.requestFocus();
        } else if (jTextField5.getText() == null || jTextField5.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
        } else {
            List<VariedadesArticulos> art = obtenerArticulo(jTextField4.getText());
            DecimalFormat df = new DecimalFormat("#");
            if (art.size() > 0) {
                VariedadesArticulos vart = art.get(0);
                boolean exi = false;
                int j = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 2) == null) {
                        model.removeRow(i);
                        exi = false;
                        break;
                    } else if (art.get(0).getId() == ((VariedadesArticulos) model.getValueAt(i, 2)).getId()) {
                        exi = true;
                        j = i;
                        break;
                    }
                }
                if (!exi) {
                    model.addRow(dato);
                    model.setValueAt(ventasArticulos, model.getRowCount() - 1, 0);
                    model.setValueAt("", model.getRowCount() - 1, 1);
                    model.setValueAt(vart, model.getRowCount() - 1, 2);
                    model.setValueAt("<html><div style=\"width:60;\">" + vart.getCodigoarticulo() + "</div></html>", model.getRowCount() - 1, 3);
                    model.setValueAt("<html><div style=\"width:165;\">" + vart.getNombrearticulo() + "</div></html>", model.getRowCount() - 1, 4);
                    model.setValueAt(vart.getValorventa(), model.getRowCount() - 1, 5);
                    model.setValueAt(jTextField5.getText(), model.getRowCount() - 1, 6);
                    float subtotal = Float.parseFloat(jTextField5.getText()) * vart.getValorventa();
                    model.setValueAt(df.format(subtotal), model.getRowCount() - 1, 7);
                    model.setValueAt(vart.getIvaventa(), model.getRowCount() - 1, 8);
                    float iva = (subtotal * vart.getIvaventa()) / 100;
                    float total = iva + subtotal;
                    model.setValueAt(df.format(total), model.getRowCount() - 1, 9);
                    model.setValueAt(1, model.getRowCount() - 1, 10);
                    model.setValueAt(df.format(iva), model.getRowCount() - 1, 11);
                    float subt = 0;
                    float iv = 0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        subt = subt + Float.parseFloat(model.getValueAt(i, 7).toString());
                        iv = iv + Float.parseFloat(model.getValueAt(i, 11).toString());
                    }
                    jLabel6.setText(String.valueOf(df.format(subt)));
                    jLabel8.setText(String.valueOf(df.format(iv)));
                    jLabel10.setText(String.valueOf(df.format(iv + subt)));
                    jTextField4.setText(null);
                    jTextField5.setText(null);
                    jTextField4.requestFocus();
                } else {
                    int cantida = Integer.parseInt(jTextField5.getText());
                    model.setValueAt(cantida, j, 6);
                    float subtotal = cantida * vart.getValorventa();
                    model.setValueAt(df.format(subtotal), j, 7);
                    model.setValueAt(vart.getIvaventa(), j, 8);
                    float iva = (subtotal * vart.getIvaventa()) / 100;
                    float total = iva + subtotal;
                    model.setValueAt(df.format(total), j, 9);
                    model.setValueAt(1, j, 10);
                    model.setValueAt(df.format(iva), j, 11);
                    float subt = 0;
                    float iv = 0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        subt = subt + Float.parseFloat(model.getValueAt(i, 7).toString());
                        iv = iv + Float.parseFloat(model.getValueAt(i, 11).toString());
                    }
                    jLabel6.setText(String.valueOf(df.format(subt)));
                    jLabel8.setText(String.valueOf(df.format(iv)));
                    jLabel10.setText(String.valueOf(df.format(iv + subt)));
                    jTextField4.setText(null);
                    jTextField5.setText(null);
                    jTextField4.requestFocus();
                }
                for (int row = 0; row < jTable1.getRowCount(); row++) {
                    int rowHeight = jTable1.getRowHeight();
                    for (int column = 0; column < jTable1.getColumnCount(); column++) {
                        Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                        rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
                    }
                    jTable1.setRowHeight(row, rowHeight);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Codigo no existe");
                jTextField4.requestFocus();
            }
        }
    }

    private List<VariedadesArticulos> obtenerArticulo(String codigo) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM VariedadesArticulos p WHERE p.codigoarticulo=:codigo AND p.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("codigo", codigo)
                    .getResultList();
        } finally {
            em.close();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        bSalir = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        bGuardar = new javax.swing.JButton();
        bfinalizar = new javax.swing.JButton();
        binactivar = new javax.swing.JButton();
        blimpiar = new javax.swing.JButton();
        bimprimir = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Número de Devolucion:");

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

        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
        });
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
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel4MouseReleased(evt);
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

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("...");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("SubTotal:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("...");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("IVA:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("...");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Total a Pagar:");

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

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(50, 72, 141));
        jLabel12.setText("Codigo articulo:");

        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField4KeyPressed(evt);
            }
        });

        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField5FocusGained(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField5KeyPressed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(50, 72, 141));
        jLabel13.setText("Cantidad:");

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
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                    .addComponent(jTextField3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(blimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(bfinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(bimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField4))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bfinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(blimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.selectAll();
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        jTextField2.selectAll();
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargaUser();
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jLabel5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseReleased
        cargaArticulo();
    }//GEN-LAST:event_jLabel5MouseReleased

    private void bGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGuardarMouseReleased
        if (bGuardar.isEnabled()) {
            saveVenta();
        }
    }//GEN-LAST:event_bGuardarMouseReleased

    private void bfinalizarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bfinalizarMouseReleased
        if (bfinalizar.isEnabled()) {
            finalizarVenta();
        }
    }//GEN-LAST:event_bfinalizarMouseReleased

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargaVenta();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseReleased
        removeArticulo();
    }//GEN-LAST:event_jLabel4MouseReleased

    private void binactivarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_binactivarMouseReleased
        inactivarVenta();
    }//GEN-LAST:event_binactivarMouseReleased

    private void blimpiarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blimpiarMouseReleased
        limpiar();
    }//GEN-LAST:event_blimpiarMouseReleased

    private void bimprimirMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bimprimirMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_bimprimirMouseReleased

    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField5.requestFocus();
        }
    }//GEN-LAST:event_jTextField4KeyPressed

    private void jTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargaarticulotext();
        }
    }//GEN-LAST:event_jTextField5KeyPressed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        if (jTable1.getSelectedRow() > -1) {
            String[] ite = jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString().split("<html><div style=\"width:60;\">");
            String[] ite2 = ite[1].split("</div></html>");
            jTextField4.setText(ite2[0]);
            jTextField5.setText(jTable1.getValueAt(jTable1.getSelectedRow(), 6).toString());
        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
        jTextField4.selectAll();
    }//GEN-LAST:event_jTextField4FocusGained

    private void jTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusGained
        jTextField5.selectAll();
    }//GEN-LAST:event_jTextField5FocusGained


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
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    public javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
