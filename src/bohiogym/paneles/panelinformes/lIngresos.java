/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelinformes;

import bohiogym.clases.Funciones;
import entity.VariedadesIngresos;
import entity.GymTerceros;
import entity.GymUsuarios;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
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
import jpa.VariedadesIngresosJpaController;
import jpa.GymTercerosJpaController;
import jpa.GymUsuariosJpaController;
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
public class lIngresos extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    VariedadesIngresos variedadesIngresos = null;
    VariedadesIngresosJpaController vijc = null;
    GymUsuarios usuarios = null;
    GymUsuariosJpaController vujc = null;
    GymTerceros terceros = null;
    GymTercerosJpaController vtjc = null;
    DefaultTableModel model;
    Object dato[] = null;

    /**
     * Creates new form lIngresos
     */
    public lIngresos(EntityManagerFactory factory) {
        initComponents();
        this.factory = factory;
        vijc = new VariedadesIngresosJpaController(factory);
        vujc = new GymUsuariosJpaController(factory);
        vtjc = new GymTercerosJpaController(factory);
        setCargarTabla();
        jDateChooser2.setDate(new Date());
        jDateChooser3.setDate(new Date());
    }

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"id", "Nº", "Cliente", "Fecha", "Fecha Real", "Descripcion", "Total", "Usuario", "estado"}) {
            Class[] types = new Class[]{
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
        double total = 0;
        DecimalFormat formato = new DecimalFormat("#,###.00");
        for (VariedadesIngresos vi : vijc.findVariedadesIngresosEntities()) {
            if (vi.getEstado() != 0 && vi.getFechaingreso().before(new Date(jDateChooser3.getDate().getYear(), jDateChooser3.getDate().getMonth(), jDateChooser3.getDate().getDate(), 0, 0, 0)) && vi.getFechaingreso().after(new Date(jDateChooser2.getDate().getYear(), jDateChooser2.getDate().getMonth(), jDateChooser2.getDate().getDate(), 23, 59, 59))) {
                model.addRow(dato);
                model.setValueAt(vi, model.getRowCount() - 1, 0);
                model.setValueAt(vi.getCodigoingreso(), model.getRowCount() - 1, 1);
                if (obtenerUser(vi.getCodcliente()).size() > 0) {
                    usuarios = obtenerUser(vi.getCodcliente()).get(0);
                    model.setValueAt(usuarios.getNombres() + " " + usuarios.getApellidos(), model.getRowCount() - 1, 2);
                } else if (obtenerTercero(vi.getCodcliente()).size() > 0) {
                    terceros = obtenerTercero(vi.getCodcliente()).get(0);
                    model.setValueAt(terceros.getRazonsocial(), model.getRowCount() - 1, 2);
                } else {
                    model.setValueAt(" ", model.getRowCount() - 1, 2);
                }
                model.setValueAt(Funciones.ddMMyyyy.format(vi.getFechaingreso()), model.getRowCount() - 1, 3);
                model.setValueAt(vi.getFechaingresotiempo(), model.getRowCount() - 1, 4);
                model.setValueAt(vi.getIngresoItemList().get(0).getDescripcion(), model.getRowCount() - 1, 5);
                total = total + vi.getValortotal();
                model.setValueAt(formato.format(vi.getValortotal()), model.getRowCount() - 1, 6);
                model.setValueAt(vi.getIdusuario(), model.getRowCount() - 1, 7);
                model.setValueAt(vi.getEstado(), model.getRowCount() - 1, 8);
            }
        }
        jLabel1.setText("Total acumulado: " + formato.format(total));
        for (int row = 0; row < jTable1.getRowCount(); row++) {
            int rowHeight = jTable1.getRowHeight();
            for (int column = 0; column < jTable1.getColumnCount(); column++) {
                Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            jTable1.setRowHeight(row, rowHeight);
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
                File file = new File(ruta + "InformeIngresosdel" + Funciones.ddMMyyyy2.format(jDateChooser2.getDate()) + "al" + Funciones.ddMMyyyy2.format(jDateChooser3.getDate()) + ".xls");
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
            excelSheet.addCell(new Label(3, 0, "DESCRIPCION", cf2));
            excelSheet.addCell(new Label(4, 0, "TOTAL", cf2));
            int count = 1;
            for (int i = 0; i < model.getRowCount(); i++) {
                String parte1[] = model.getValueAt(i, 5).toString().split("<html><div style=\"width:215;\">");
                String parte2[] = parte1[1].split("</div></html>");
                excelSheet.addCell(new Label(0, count, model.getValueAt(i, 1).toString(), cf));//Nª
                excelSheet.addCell(new Label(1, count, model.getValueAt(i, 2).toString(), cf));//CLIENTE
                excelSheet.addCell(new Label(2, count, model.getValueAt(i, 3).toString(), cf));//FECHA
                excelSheet.addCell(new Label(3, count, parte2[0], cf));//DESCRIPCION
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

        tFiltro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        bSalir = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        bExportar = new javax.swing.JButton();

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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tFiltroKeyTyped(evt);
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

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel4MouseReleased(evt);
            }
        });

        jLabel5.setText("Fecha:");

        jLabel1.setText("...");

        bExportar.setBackground(new java.awt.Color(50, 72, 141));
        bExportar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bExportar.setForeground(new java.awt.Color(255, 255, 255));
        bExportar.setText("Exportar");
        bExportar.setContentAreaFilled(false);
        bExportar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bExportar.setFocusable(false);
        bExportar.setOpaque(true);
        bExportar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bExportarMouseReleased(evt);
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bExportar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tFiltroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tFiltroFocusGained
        tFiltro.selectAll();
    }//GEN-LAST:event_tFiltroFocusGained

    private void tFiltroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFiltroKeyTyped

    }//GEN-LAST:event_tFiltroKeyTyped

    private void tFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFiltroKeyPressed
        TableRowSorter sorter = new TableRowSorter(model);
        sorter.setRowFilter(RowFilter.regexFilter(tFiltro.getText().toUpperCase()));
        jTable1.setRowSorter(sorter);
        for (int row = 0; row < jTable1.getRowCount(); row++) {
            int rowHeight = jTable1.getRowHeight();
            for (int column = 0; column < jTable1.getColumnCount(); column++) {
                Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            jTable1.setRowHeight(row, rowHeight);
        }
    }//GEN-LAST:event_tFiltroKeyPressed

    private void jLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseReleased
        if (Funciones.ddMMyyyy.format(jDateChooser2.getDate()).equals(Funciones.ddMMyyyy.format(jDateChooser3.getDate()))) {
            Date fecha1 = jDateChooser2.getDate(), fecha2 = jDateChooser3.getDate();
            jDateChooser2.setDate(new Date(fecha1.getYear(), fecha1.getMonth(), fecha1.getDate(), 0, 0, 0));
            jDateChooser3.setDate(new Date(fecha2.getYear(), fecha2.getMonth(), fecha2.getDate(), 23, 59, 59));
        }
        setDatos();
    }//GEN-LAST:event_jLabel4MouseReleased

    private void bExportarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bExportarMouseReleased
        exportarToExcel();
    }//GEN-LAST:event_bExportarMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bExportar;
    public javax.swing.JButton bSalir;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tFiltro;
    // End of variables declaration//GEN-END:variables
}
