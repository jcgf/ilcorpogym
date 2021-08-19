/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelusuarios;

import bohiogym.clases.Database;
import bohiogym.clases.Funciones;
import bohiogym.clases.SendMail;
import bohiogym.paneles.panelusuarios.subpanel.dMensaje;
import entity.AsignadosDias;
import entity.GymAsignados;
import entity.GymUsuarios;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import jpa.AsignadosDiasJpaController;
import jpa.GymAsignadosJpaController;
import jpa.GymUsuariosJpaController;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Alvarez
 */
public class lUsuariosactivos extends javax.swing.JPanel {

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
    public lUsuariosactivos(EntityManagerFactory factory, Properties props) {
        initComponents();
        this.props = props;
        this.factory = factory;
        gajc = new GymAsignadosJpaController(factory);
        adjc = new AsignadosDiasJpaController(factory);
        setCargarTabla();
        setDatos();
    }

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"entidad", "Identificacion", "Nombre", "Plan", "Fecha/dias"}) {
            Class[] types = new Class[]{
                java.lang.Object.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };

            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
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
        Funciones.setOcultarColumnas(jTable1, new int[]{0, 1});
        //470
        jTable1.getColumnModel().getColumn(1).setMinWidth(40);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(2).setMinWidth(200);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(3).setMinWidth(150);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(150);
        jTable1.getColumnModel().getColumn(4).setMinWidth(120);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(120);

    }
    

    public void setDatos() {
        if (vujc == null) {
            vujc = new GymUsuariosJpaController(factory);
        }
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (GymAsignados gp : searchAsignados()) {
            model.addRow(dato);
            model.setValueAt(gp, model.getRowCount() - 1, 0);
            model.setValueAt(gp.getIduser().getIdentificacion(), model.getRowCount() - 1, 1);
            model.setValueAt(gp.getIduser().getNombres() + " " + gp.getIduser().getApellidos(), model.getRowCount() - 1, 2);
            model.setValueAt(gp.getIdplan().getNombreservicio(), model.getRowCount() - 1, 3);
            if (Asignadosd(gp).size() > 0) {
                asignadosDias = Asignadosd(gp).get(0);
                model.setValueAt(asignadosDias.getDias() + " Días restantes", model.getRowCount() - 1, 4);
            } else {
                model.setValueAt(Funciones.ddMMyyyy.format(gp.getFechafin()), model.getRowCount() - 1, 4);
            }
        }
        jLabel1.setText("Cantidad de usuarios activos: " + model.getRowCount());
        for (int row = 0; row < jTable1.getRowCount(); row++) {
            int rowHeight = jTable1.getRowHeight();
            for (int column = 0; column < jTable1.getColumnCount(); column++) {
                Component comp = jTable1.prepareRenderer(jTable1.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            jTable1.setRowHeight(row, rowHeight);
        }
    }

    private List<GymAsignados> searchAsignados() {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymAsignados p WHERE p.estado='1' ORDER BY p.iduser.nombres ASC")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
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
                JOptionPane.showMessageDialog(null, "Error al imprimir factura tamaño carta: " + ex.getMessage(), lUsuariosactivos.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void enviarmensaje() {
        final dMensaje mensaje = new dMensaje((JFrame) SwingUtilities.getWindowAncestor(this), true, factory);
        mensaje.jLabel2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                SendMail mail = new SendMail();
                ArrayList<GymUsuarios> gus = new ArrayList<GymUsuarios>();
                for (int i = 0; i < jTable1.getSelectedRows().length; i++) {
                    GymAsignados gp = (GymAsignados) jTable1.getValueAt(jTable1.getSelectedRows()[i], 0);
                    if (!gp.getIduser().getEmail().toUpperCase().equals("NO")) {
                        if (!gp.getIduser().getEmail().toUpperCase().equals("NA")) {
                            gus.add(gp.getIduser());
                        }
                    }
                }
                mail.enviaEmail(gus, mensaje.jTextArea1.getText(), factory);
                mensaje.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        mensaje.setLocationRelativeTo(null);
        mensaje.setVisible(true);
    }

    public class hilodatos extends Thread {

        public hilodatos() {
        }

        @Override
        public void run() {
            enviarmensaje();
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
        tFiltro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        bSalir1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        tFiltro.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        tFiltro.setForeground(new java.awt.Color(153, 153, 153));
        tFiltro.setText("Ingrese número de cédula o nombre");
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

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        jLabel1.setText("Cantidad de usuarios activos: ...");

        bSalir1.setBackground(new java.awt.Color(50, 72, 141));
        bSalir1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir1.setForeground(new java.awt.Color(255, 255, 255));
        bSalir1.setText("Imprimir");
        bSalir1.setContentAreaFilled(false);
        bSalir1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir1.setFocusable(false);
        bSalir1.setOpaque(true);
        bSalir1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bSalir1MouseReleased(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconfinder_mail-message-new_118781.png"))); // NOI18N
        jLabel2.setToolTipText("Enviar mensaje");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bSalir1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bSalir1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tFiltroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tFiltroFocusGained
        tFiltro.selectAll();
    }//GEN-LAST:event_tFiltroFocusGained

    private void tFiltroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFiltroKeyTyped
        if (tFiltro.getText().isEmpty()) {
            Font fuente = new Font("Tahoma", 2, 11);
            tFiltro.setFont(fuente);
            tFiltro.setForeground(new Color(153, 153, 153));
            tFiltro.setText("Ingrese número de cédula o nombre");
            tFiltro.selectAll();
        } else {
            Font fuente = new Font("Tahoma", 0, 11);
            tFiltro.setForeground(Color.BLACK);
            tFiltro.setFont(fuente);
        }
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

    private void bSalir1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSalir1MouseReleased
        imprimir();
    }//GEN-LAST:event_bSalir1MouseReleased

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        if (jTable1.getRowCount() > 0) {
            hilodatos ut = new hilodatos();
            Thread r = new Thread(ut);
            r.start();
        } else {
            JOptionPane.showMessageDialog(null, "Realize una busqueda para enviar mensaje");
        }
    }//GEN-LAST:event_jLabel2MouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bSalir;
    public javax.swing.JButton bSalir1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tFiltro;
    // End of variables declaration//GEN-END:variables
}
