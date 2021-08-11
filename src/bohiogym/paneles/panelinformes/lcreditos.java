/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelinformes;

import bohiogym.clases.Funciones;
import bohiogym.paneles.panelinformes.subinformes.historicoAbono;
import entity.VariedadesVentas;
import entity.GymTerceros;
import entity.GymUsuarios;
import entity.VariedadesAbono;
import entity.VariedadesIngresos;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import jpa.VariedadesVentasJpaController;
import jpa.GymTercerosJpaController;
import jpa.GymUsuariosJpaController;
import jpa.VariedadesAbonoJpaController;
import jpa.VariedadesIngresosJpaController;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author Alvarez
 */
public class lcreditos extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    VariedadesVentas variedadesIngresos = null;
    VariedadesVentasJpaController vijc = null;
    VariedadesIngresos ingresos = null;
    VariedadesIngresosJpaController vinjc = null;
    GymUsuarios usuarios = null;
    GymUsuariosJpaController vujc = null;
    GymTerceros terceros = null;
    GymTercerosJpaController vtjc = null;
    DefaultTableModel model;
    VariedadesAbono variedadesAbono = null;
    VariedadesAbonoJpaController vajc = null;
    Object dato[] = null;
    double total = 0;

    /**
     * Creates new form lIngresos
     */
    public lcreditos(EntityManagerFactory factory) {
        initComponents();
        this.factory = factory;
        vijc = new VariedadesVentasJpaController(factory);
        vujc = new GymUsuariosJpaController(factory);
        vtjc = new GymTercerosJpaController(factory);
        vinjc = new VariedadesIngresosJpaController(factory);
        vajc = new VariedadesAbonoJpaController(factory);
        setCargarTabla();
    }

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"id", "Nº", "Cliente", "Fecha", "Fecha Real", "Servicio", "Total", "Usuario", "estado"}) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };

            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false
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
        Funciones.setOcultarColumnas(jTable1, new int[]{0, 4, 7, 8});
        //470+65
        jTable1.getColumnModel().getColumn(1).setMinWidth(30);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(30);
        jTable1.getColumnModel().getColumn(2).setMinWidth(190);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(190);
        jTable1.getColumnModel().getColumn(3).setMinWidth(65);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(65);
        jTable1.getColumnModel().getColumn(5).setMinWidth(115);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(115);
        jTable1.getColumnModel().getColumn(6).setMinWidth(70);
        jTable1.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(70);
    }

    private void setDatos() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        total = 0;
        DecimalFormat formato = new DecimalFormat("#,###.00");
        if (!tFiltro.getText().isEmpty() || tFiltro.getText() != null) {
            for (VariedadesVentas vi : obtenerVenta(tFiltro.getText())) {
                if (vi.getEstado() == 3) {
                    model.addRow(dato);
                    model.setValueAt(vi, model.getRowCount() - 1, 0);
                    model.setValueAt(vi.getCodigoventa(), model.getRowCount() - 1, 1);
                    if (obtenerUser(vi.getCodcliente()).size() > 0) {
                        usuarios = obtenerUser(vi.getCodcliente()).get(0);
                        model.setValueAt(usuarios.getNombres() + " " + usuarios.getApellidos(), model.getRowCount() - 1, 2);
                    } else if (obtenerTercero(vi.getCodcliente()).size() > 0) {
                        terceros = obtenerTercero(vi.getCodcliente()).get(0);
                        model.setValueAt(terceros.getRazonsocial(), model.getRowCount() - 1, 2);
                    } else {
                        model.setValueAt(" ", model.getRowCount() - 1, 2);
                    }
                    model.setValueAt(Funciones.ddMMyyyy.format(vi.getFechaventa()), model.getRowCount() - 1, 3);
                    model.setValueAt(vi.getFechaventatiempo(), model.getRowCount() - 1, 4);
                    if (vi.getVentasServiciosList().size() > 0) {
                        model.setValueAt(vi.getVentasServiciosList().get(0).getIdservicio().getNombreservicio(), model.getRowCount() - 1, 5);
                    } else {
                        model.setValueAt(vi.getVentasArticulosList().get(0).getIdarticulo().getNombrearticulo(), model.getRowCount() - 1, 5);
                    }
                    total = total + vi.getValortotal();
                    model.setValueAt(/*formato.format(*/vi.getValortotal()/*)*/, model.getRowCount() - 1, 6);
                    model.setValueAt(vi.getIdusuario(), model.getRowCount() - 1, 7);
                    model.setValueAt(vi.getEstado(), model.getRowCount() - 1, 8);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar un documento para realizar la busqueda");
        }
        jLabel1.setText("Total sumatoria: " + formato.format(total));
        for (int row = 0; row < jTable1.getRowCount(); row++) {
            int rowHeight = jTable1.getRowHeight();
            for (int column = 0; column < jTable1.getColumnCount(); column++) {
                Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            jTable1.setRowHeight(row, rowHeight);
        }
    }

    private void setAllDatos() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        total = 0;
        DecimalFormat formato = new DecimalFormat("#,###.00");
        if (!tFiltro.getText().isEmpty() || tFiltro.getText() != null) {
            for (VariedadesVentas vi : obtenerAllVenta()) {
                if (vi.getEstado() == 3) {
                    model.addRow(dato);
                    model.setValueAt(vi, model.getRowCount() - 1, 0);
                    model.setValueAt(vi.getCodigoventa(), model.getRowCount() - 1, 1);
                    if (obtenerUser(vi.getCodcliente()).size() > 0) {
                        usuarios = obtenerUser(vi.getCodcliente()).get(0);
                        model.setValueAt(usuarios.getNombres() + " " + usuarios.getApellidos(), model.getRowCount() - 1, 2);
                    } else if (obtenerTercero(vi.getCodcliente()).size() > 0) {
                        terceros = obtenerTercero(vi.getCodcliente()).get(0);
                        model.setValueAt(terceros.getRazonsocial(), model.getRowCount() - 1, 2);
                    } else {
                        model.setValueAt(" ", model.getRowCount() - 1, 2);
                    }
                    model.setValueAt(Funciones.ddMMyyyy.format(vi.getFechaventa()), model.getRowCount() - 1, 3);
                    model.setValueAt(vi.getFechaventatiempo(), model.getRowCount() - 1, 4);
                    if (vi.getVentasServiciosList().size() > 0) {
                        model.setValueAt(vi.getVentasServiciosList().get(0).getIdservicio().getNombreservicio(), model.getRowCount() - 1, 5);
                    } else {
                        model.setValueAt(vi.getVentasArticulosList().get(0).getIdarticulo().getNombrearticulo(), model.getRowCount() - 1, 5);
                    }
                    total = total + vi.getValortotal();
                    model.setValueAt(/*formato.format(*/vi.getValortotal()/*)*/, model.getRowCount() - 1, 6);
                    model.setValueAt(vi.getIdusuario(), model.getRowCount() - 1, 7);
                    model.setValueAt(vi.getEstado(), model.getRowCount() - 1, 8);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar un documento para realizar la busqueda");
        }
        jLabel1.setText("Total sumatoria: " + formato.format(total));
        for (int row = 0; row < jTable1.getRowCount(); row++) {
            int rowHeight = jTable1.getRowHeight();
            for (int column = 0; column < jTable1.getColumnCount(); column++) {
                Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            jTable1.setRowHeight(row, rowHeight);
        }
    }

    private List<VariedadesVentas> obtenerVenta(String documento) {
        EntityManager em = vijc.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM VariedadesVentas f WHERE f.codcliente=:idusua AND f.estado='3'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("idusua", documento)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<VariedadesVentas> obtenerAllVenta() {
        EntityManager em = vijc.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM VariedadesVentas f WHERE f.estado='3'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymUsuarios> obtenerUser(String id) {
        EntityManager em = vujc.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM GymUsuarios u WHERE u.identificacion=:dats AND u.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("dats", id)
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

    private void exportarToExcel() {
        try {
            if (jTable1.getRowCount() > 0) {
                String ruta = System.getProperty("user.home") + "/Desktop/";
                File file = new File(ruta + "InformeCredito" + Funciones.ddMMyyyy2.format(new Date()) + ".xls");
                crearExcel(file);
            } else {
                JOptionPane.showMessageDialog(null, "Debe realizar una busqueda primero");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al exportar a excel: " + ex.getMessage());
        }
    }

    private void crearExcel(File file) {
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
        WritableFont wf2 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        WritableCellFormat cf = new WritableCellFormat(wf);
        WritableCellFormat cf2 = new WritableCellFormat(wf2);
        WritableSheet excelSheet = null;
        WritableWorkbook workbook = null;
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Listado", 0);
            excelSheet = workbook.getSheet(0);
            excelSheet.addCell(new Label(0, 0, "Nº FACTURA", cf2));
            excelSheet.addCell(new Label(1, 0, "CLIENTE", cf2));
            excelSheet.addCell(new Label(2, 0, "FECHA", cf2));
            excelSheet.addCell(new Label(3, 0, "SERVICIO", cf2));
            excelSheet.addCell(new Label(4, 0, "TOTAL", cf2));
            int count = 1;
            for (int i = 0; i < model.getRowCount(); i++) {
                excelSheet.addCell(new Label(0, count, model.getValueAt(i, 1).toString(), cf));//Nª
                excelSheet.addCell(new Label(1, count, model.getValueAt(i, 2).toString(), cf));//CLIENTE
                excelSheet.addCell(new Label(2, count, model.getValueAt(i, 3).toString(), cf));//FECHA
                excelSheet.addCell(new Label(3, count, model.getValueAt(i, 5).toString(), cf));//SERVICIO
                excelSheet.addCell(new Label(4, count, model.getValueAt(i, 6).toString(), cf));//TOTAL
                count++;
            }
            try {
                workbook.write();
                workbook.close();
                JOptionPane.showMessageDialog(null, "Archivo creado con exito");
                System.out.println(file.getAbsolutePath());
                String mensaje = "Desea abrir el Archivo?";
                int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Confirmación", JOptionPane.YES_NO_OPTION);
                if (entrada == 0) {
                    Desktop.getDesktop().open(file);
                }
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            } catch (WriteException ex) {
                System.err.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (WriteException ex) {
            System.err.println(ex.getMessage());
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        Abonar = new javax.swing.JMenuItem();
        VerAbonos = new javax.swing.JMenuItem();
        Finaliza = new javax.swing.JMenuItem();
        tFiltro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        bSalir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        jPopupMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        Abonar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconfinder_money_299107.png"))); // NOI18N
        Abonar.setText("Abonar");
        Abonar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Abonar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                AbonarMouseReleased(evt);
            }
        });
        jPopupMenu1.add(Abonar);

        VerAbonos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconfinder_calendar-clock_299096.png"))); // NOI18N
        VerAbonos.setText("Historico Abonos");
        VerAbonos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        VerAbonos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                VerAbonosMouseReleased(evt);
            }
        });
        jPopupMenu1.add(VerAbonos);

        Finaliza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/check20.png"))); // NOI18N
        Finaliza.setText("Finalizar Credito");
        Finaliza.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                FinalizaMouseReleased(evt);
            }
        });
        jPopupMenu1.add(Finaliza);

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        tFiltro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tFiltroFocusGained(evt);
            }
        });
        tFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tFiltroKeyPressed(evt);
            }
        });

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
        jTable1.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(jTable1);

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });

        jLabel1.setText("...");

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Busqueda rapida");
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox1.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(125, 125, 125)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tFiltroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tFiltroFocusGained
        tFiltro.selectAll();
    }//GEN-LAST:event_tFiltroFocusGained

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        if (jCheckBox1.isSelected()) {
            setAllDatos();
        } else {
            setDatos();
        }
    }//GEN-LAST:event_jLabel2MouseReleased

    private void tFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFiltroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setDatos();
        }
    }//GEN-LAST:event_tFiltroKeyPressed

    private void AbonarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AbonarMouseReleased
        DecimalFormat formato = new DecimalFormat("#");
        if (jTable1.getSelectedRow() > -1) {
            float abono = Float.parseFloat(JOptionPane.showInputDialog(null, "Ingrese el valor a abonar"));
            if (abono <= Float.parseFloat(jTable1.getValueAt(jTable1.getSelectedRow(), 6).toString())) {
                variedadesAbono = new VariedadesAbono();
                variedadesAbono.setIdventa((VariedadesVentas) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                variedadesAbono.setAbono(abono);
                variedadesAbono.setFecha(new Date());
                variedadesAbono.setEstado(1);
                vajc.create(variedadesAbono);
                JOptionPane.showMessageDialog(null, "Abono generado con exito");
            } else {
                JOptionPane.showMessageDialog(null, "No puede abonar mas de lo que se adeuda");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la lista.");
        }
    }//GEN-LAST:event_AbonarMouseReleased

    private void VerAbonosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VerAbonosMouseReleased
        if (jTable1.getSelectedRow() > -1) {
            final historicoAbono pabono = new historicoAbono(null, true, factory, (VariedadesVentas) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
            pabono.setLocationRelativeTo(null);
            pabono.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro de la lista.");
        }
    }//GEN-LAST:event_VerAbonosMouseReleased

    private void FinalizaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FinalizaMouseReleased
        try {
            VariedadesVentas vv = (VariedadesVentas) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            vv.setEstado(2);
            vijc.edit(vv);
            JOptionPane.showMessageDialog(null, "Credito Finalizado con exito.");
            setDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al finalizar credito");
        }
    }//GEN-LAST:event_FinalizaMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Abonar;
    private javax.swing.JMenuItem Finaliza;
    private javax.swing.JMenuItem VerAbonos;
    public javax.swing.JButton bSalir;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tFiltro;
    // End of variables declaration//GEN-END:variables
}
