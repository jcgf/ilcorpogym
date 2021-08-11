/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelusuarios;

import bohiogym.clases.Database;
import bohiogym.clases.Funciones;
import bohiogym.paneles.panelusuarios.subpanel.otrosDatos;
import entity.AsignadosDias;
import entity.GymAsignados;
import entity.GymUsuarios;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import jpa.AsignadosDiasJpaController;
import jpa.GymAsignadosJpaController;
import jpa.GymUsuariosJpaController;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Alvarez
 */
public class lUsuariosinactivos extends javax.swing.JPanel {

    private final Properties props;
    private final EntityManagerFactory factory;
    GymUsuarios usuarios = null;
    GymUsuariosJpaController vujc = null;
    Object dato[] = null;
    DefaultTableModel model;
    GymAsignados asignados = null;
    GymAsignadosJpaController gajc = null;
    AsignadosDias asignadosDias = null;
    AsignadosDiasJpaController adjc = null;

    /**
     * Creates new form lUsuarios
     */
    public lUsuariosinactivos(EntityManagerFactory factory, Properties props) {
        initComponents();
        this.props = props;
        this.factory = factory;
        gajc = new GymAsignadosJpaController(factory);
        adjc = new AsignadosDiasJpaController(factory);
        setCargarTabla();
    }

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"entidad", "Identificacion", "Nombre", "Plan", "Fecha/dias", "Telefono"}) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };

            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
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
        Funciones.setOcultarColumnas(jTable1, new int[]{0, 1});
        //470
        jTable1.getColumnModel().getColumn(1).setMinWidth(40);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(2).setMinWidth(170);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(170);
        jTable1.getColumnModel().getColumn(3).setMinWidth(130);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(130);
        jTable1.getColumnModel().getColumn(4).setMinWidth(80);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(90);
        jTable1.getColumnModel().getColumn(5).setMinWidth(90);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(90);

    }

    public void setDatos() {
        if (vujc == null) {
            vujc = new GymUsuariosJpaController(factory);
        }
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        List<GymAsignados> inactivos = searchAsignadosInactivos(jDateChooser1.getDate());
        for (int i = 0; i < inactivos.size(); i++) {
            if (searchAsignadosActivos(inactivos.get(i).getIduser().getIdentificacion()).size() == 0) {
                GymAsignados gp = searchAsignadosInactivosCedula(inactivos.get(i).getIduser().getIdentificacion()).get(searchAsignadosInactivosCedula(inactivos.get(i).getIduser().getIdentificacion()).size() - 1);
                model.addRow(dato);
                model.setValueAt(gp, model.getRowCount() - 1, 0);
                model.setValueAt(gp.getIduser().getIdentificacion(), model.getRowCount() - 1, 1);
                model.setValueAt(gp.getIduser().getNombres() + " " + gp.getIduser().getApellidos(), model.getRowCount() - 1, 2);
                model.setValueAt(gp.getIdplan().getNombreservicio(), model.getRowCount() - 1, 3);
                model.setValueAt(Funciones.ddMMyyyy.format(gp.getFechafin()), model.getRowCount() - 1, 4);
                model.setValueAt(gp.getIduser().getTelefono(), model.getRowCount() - 1, 5);
            }
        }
        jLabel1.setText("Cantidad inactivos: " + model.getRowCount());
        for (int row = 0; row < jTable1.getRowCount(); row++) {
            int rowHeight = jTable1.getRowHeight();
            for (int column = 0; column < jTable1.getColumnCount(); column++) {
                Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            jTable1.setRowHeight(row, rowHeight);
        }
    }

    private List<GymAsignados> searchAsignadosInactivos(Date fechafin) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymAsignados p WHERE p.estado='2' AND p.fechafin<:fechafin GROUP BY p.iduser")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("fechafin", fechafin)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymAsignados> searchAsignadosInactivosCedula(String id) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymAsignados p WHERE p.estado='2' AND p.iduser.identificacion=:id")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("id", id)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymAsignados> searchAsignadosActivos(String id) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymAsignados p WHERE p.estado='1' AND p.iduser.identificacion=:id")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("id", id)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymAsignados> searchAsignadosTodos(Date fechafin) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymAsignados p WHERE p.fechafin<:fechafin")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("fechafin", fechafin)
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

    private void imprimir() {
        String men = "¿Desea imprimir listado?";
        int en = JOptionPane.showConfirmDialog(null, men, "Imprimir", JOptionPane.YES_NO_OPTION);
        if (en == 0) {
            try {
                Database db = new Database(props);
                db.ConectarBasedeDatos();
                JasperReport reporte = null;
                String path = System.getProperty("user.home") + "\\Documents\\listaactivos.jasper";
                Map parametro = new HashMap();
                reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
                JasperPrint jprint = JasperFillManager.fillReport(reporte, parametro, db.conexion);
                JasperViewer view = new JasperViewer(jprint, false);
                view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                view.setVisible(true);
                db.DesconectarBasedeDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al imprimir factura tamaño carta: " + ex.getMessage(), lUsuariosinactivos.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportarToExcel() {
        try {
            if (jTable1.getRowCount() > 0) {
                String ruta = System.getProperty("user.home") + "/Desktop/";
                File file = new File(ruta + "InformeInactivosal" + Funciones.ddMMyyyy2.format(jDateChooser1.getDate()) + ".xls");
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
            excelSheet.addCell(new Label(0, 0, "IDENTIFICACION", cf2));
            excelSheet.addCell(new Label(1, 0, "CLIENTE", cf2));
            excelSheet.addCell(new Label(2, 0, "SERVICIO", cf2));
            excelSheet.addCell(new Label(3, 0, "FECHA", cf2));
            excelSheet.addCell(new Label(4, 0, "TELEFONO", cf2));
            int count = 1;
            for (int i = 0; i < model.getRowCount(); i++) {
                excelSheet.addCell(new Label(0, count, model.getValueAt(i, 1).toString(), cf));//Nª
                excelSheet.addCell(new Label(1, count, model.getValueAt(i, 2).toString(), cf));//CLIENTE
                excelSheet.addCell(new Label(2, count, model.getValueAt(i, 3).toString(), cf));//SERVICIO
                excelSheet.addCell(new Label(3, count, model.getValueAt(i, 4).toString(), cf));//FECHA
                excelSheet.addCell(new Label(4, count, model.getValueAt(i, 5).toString(), cf));//TELEFONO
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
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        tFiltro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        bExportar = new javax.swing.JButton();

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/idea.png"))); // NOI18N
        jMenuItem1.setText("Ver Info");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

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
        jTable1.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(jTable1);

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        jLabel1.setText("Cantidad inactivos: ...");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });

        jLabel3.setText("Fecha:");

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
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(bExportar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        setDatos();
    }//GEN-LAST:event_jLabel2MouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (jTable1.getSelectedRow() > -1) {
            GymAsignados asigd = (GymAsignados) model.getValueAt(jTable1.getSelectedRow(), 0);
            final otrosDatos datos = new otrosDatos(null, true);
            datos.txtid.setText(asigd.getIduser().getIdentificacion());
            datos.txtnombre.setText(asigd.getIduser().getNombres() + " " + asigd.getIduser().getApellidos());
            datos.txtnacimiento.setText(Funciones.ddMMyyyy.format(asigd.getIduser().getFechanacimiento()));
            datos.txttelefono.setText(asigd.getIduser().getTelefono());
            datos.txtcorreo.setText(asigd.getIduser().getEmail());
            datos.txtdireccion.setText(asigd.getIduser().getDireccion());
            datos.setLocationRelativeTo(null);
            datos.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void bExportarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bExportarMouseReleased
        exportarToExcel();
    }//GEN-LAST:event_bExportarMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bExportar;
    public javax.swing.JButton bSalir;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tFiltro;
    // End of variables declaration//GEN-END:variables
}
