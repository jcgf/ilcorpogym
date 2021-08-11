/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelusuarios;

import bohiogym.clases.Funciones;
import entity.GymAsignados;
import entity.GymUsuarios;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import jpa.GymAsignadosJpaController;
import jpa.GymUsuariosJpaController;

/**
 *
 * @author Alvarez
 */
public class lUsuarios extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    GymUsuarios usuarios = null;
    GymUsuariosJpaController vujc = null;
    GymAsignados asignados = null;
    GymAsignadosJpaController gajc = null;
    Object dato[] = null;
    DefaultTableModel model;

    /**
     * Creates new form lUsuarios
     */
    public lUsuarios(EntityManagerFactory factory) {
        initComponents();
        this.factory = factory;
        vujc = new GymUsuariosJpaController(factory);
        gajc = new GymAsignadosJpaController(factory);
        setCargarTabla();
        hilodatos ut = new hilodatos(this);
        Thread r = new Thread(ut);
        r.start();
    }

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"entidad", "", "Identificacion", "Nombre", "Telefono", "Ult. Vence"}) {
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
        Funciones.setOcultarColumnas(jTable1, new int[]{0});
        //470
        jTable1.getColumnModel().getColumn(1).setMinWidth(20);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(20);
        jTable1.getColumnModel().getColumn(2).setMinWidth(70);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(70);
        jTable1.getColumnModel().getColumn(3).setMinWidth(200);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(4).setMinWidth(70);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(70);
        jTable1.getColumnModel().getColumn(5).setMinWidth(100);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(100);

    }

    public void setDatos() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        for (GymUsuarios gp : obtenerusuarios()) {
            if (gp.getEstado() == 1) {
                List<GymAsignados> ga = obtenerultfech(gp.getIdentificacion());
                model.addRow(dato);
                model.setValueAt(gp, model.getRowCount() - 1, 0);
                model.setValueAt(gp.getTipodoc(), model.getRowCount() - 1, 1);
                model.setValueAt(gp.getIdentificacion(), model.getRowCount() - 1, 2);
                model.setValueAt(gp.getNombres() + " " + gp.getApellidos(), model.getRowCount() - 1, 3);
                model.setValueAt(gp.getTelefono(), model.getRowCount() - 1, 4);
                if (ga.size() > 0) {
                    model.setValueAt(Funciones.ddMMyyyy.format(ga.get(ga.size() - 1).getFechafin()), model.getRowCount() - 1, 5);
                } else {
                    model.setValueAt("N/A", model.getRowCount() - 1, 5);
                }
                ga = null;
            }
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

    private List<GymAsignados> obtenerultfech(String id) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymAsignados p WHERE p.iduser.identificacion=:id")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("id", id)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<GymUsuarios> obtenerusuarios() {
        EntityManager em = vujc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymUsuarios p WHERE p.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
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
        tFiltro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bSalir = new javax.swing.JButton();

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
                        .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    public class hilodatos extends Thread {

        private Component component;

        public hilodatos(Component component) {
            this.component = component;
        }

        @Override
        public void run() {
//            JFrame casa = (JFrame) SwingUtilities.getWindowAncestor(this.component);
//            DFrameLoading loading = new DFrameLoading(casa, false);
//            loading.setBounds(casa.getBounds().x, casa.getBounds().y, casa.getBounds().width, casa.getBounds().height);
//            loading.setVisible(true);
//            casa.setEnabled(false);
            setDatos();
//            loading.setVisible(false);
//            casa.setEnabled(true);
//            casa.setVisible(true);
            this.stop();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tFiltro;
    // End of variables declaration//GEN-END:variables
}
