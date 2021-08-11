/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelinformes;

import bohiogym.clases.Funciones;
import bohiogym.paneles.panelinformes.subinformes.diasAsis;
import entity.AsignadosDias;
import entity.GymAsignados;
import entity.GymContdias;
import entity.GymUsuarios;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import jpa.AsignadosDiasJpaController;
import jpa.GymAsignadosJpaController;
import jpa.GymContdiasJpaController;

/**
 *
 * @author Alvarez
 */
public class registroUsuarios extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    GymAsignadosJpaController gajc = null;
    GymContdiasJpaController adjc = null;
    DefaultTableModel model;
    Object dato[] = null;

    /**
     * Creates new form lIngresos
     */
    public registroUsuarios(EntityManagerFactory factory) {
        initComponents();
        this.factory = factory;
        gajc = new GymAsignadosJpaController(factory);
        adjc = new GymContdiasJpaController(factory);
        setCargarTabla();
    }

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"id", "Plan", "Fecha Inicio", "Fecha Fin", "Estado"}) {
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
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Funciones.setOcultarColumnas(jTable1, new int[]{0});
        //470
        jTable1.getColumnModel().getColumn(1).setMinWidth(160);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(160);
        jTable1.getColumnModel().getColumn(2).setMinWidth(115);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(135);
        jTable1.getColumnModel().getColumn(3).setMinWidth(115);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(135);
        jTable1.getColumnModel().getColumn(4).setMinWidth(80);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(80);
    }

    private void setDatos() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        if (obtenerAsignacion(tFiltro.getText()).size() > 0) {
            jLabel2.setText(obtenerAsignacion(tFiltro.getText()).get(0).getIduser().getNombres() + " " + obtenerAsignacion(tFiltro.getText()).get(0).getIduser().getApellidos());
            for (GymAsignados ga : obtenerAsignacion(tFiltro.getText())) {
                model.addRow(dato);
                model.setValueAt(ga, model.getRowCount() - 1, 0);
                model.setValueAt(ga.getIdplan().getNombreservicio(), model.getRowCount() - 1, 1);
                model.setValueAt(Funciones.ddMMyyyy.format(ga.getFechainicio()), model.getRowCount() - 1, 2);
                model.setValueAt(Funciones.ddMMyyyy.format(ga.getFechafin()), model.getRowCount() - 1, 3);
                if (ga.getEstado() == 1) {
                    model.setValueAt("ACTIVO", model.getRowCount() - 1, 4);
                } else if (ga.getEstado() == 2) {
                    model.setValueAt("FINALIZADO", model.getRowCount() - 1, 4);
                } else if (ga.getEstado() == 3) {
                    model.setValueAt("CONGELADO", model.getRowCount() - 1, 4);
                }
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

    private List<GymAsignados> obtenerAsignacion(String id) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM GymAsignados u WHERE u.iduser.identificacion=:dats AND u.estado<>0")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("dats", id)
                    .getResultList();
        } finally {
            em.close();
        }

    }

    private List<GymContdias> Asignadosd(GymAsignados asignados) {
        EntityManager em = adjc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM GymContdias p WHERE p.idasignacion=:codigo AND p.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("codigo", asignados)
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        tFiltro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        bSalir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/idea.png"))); // NOI18N
        jMenuItem1.setText("Ver mas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel1MouseReleased(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(50, 72, 141));
        jLabel2.setText("...");

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
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tFiltroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tFiltroFocusGained
        tFiltro.selectAll();
    }//GEN-LAST:event_tFiltroFocusGained

    private void tFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFiltroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setDatos();
        }
    }//GEN-LAST:event_tFiltroKeyPressed

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        setDatos();
    }//GEN-LAST:event_jLabel1MouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (jTable1.getSelectedRow() > -1) {
            String pal = "TIQUETERA";
            boolean relt = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString().contains(pal);
            if (relt) {
                List<String> datosdias = new ArrayList<>();
                for (GymContdias d : Asignadosd((GymAsignados) model.getValueAt(jTable1.getSelectedRow(), 0))) {
                    datosdias.add(Funciones.yyyyMMddHHmm2.format(d.getFechadia()));
                }
                final diasAsis datos = new diasAsis(null, true, factory, datosdias);
                datos.jLabel2.setText(((GymAsignados) model.getValueAt(jTable1.getSelectedRow(), 0)).getIduser().getNombres() + " " + ((GymAsignados) model.getValueAt(jTable1.getSelectedRow(), 0)).getIduser().getApellidos());
                datos.setLocationRelativeTo(null);
                datos.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Accion invalida");
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton bSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tFiltro;
    // End of variables declaration//GEN-END:variables
}
