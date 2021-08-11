/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelarticulos.subarticulos;

import bohiogym.clases.Funciones;
import entity.ArticulosCantidades;
import entity.DevolucionesArticulos;
import entity.SuministroArticulos;
import entity.VariedadesArticulos;
import entity.VariedadesDevoluciones;
import entity.VentasArticulos;
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
import jpa.ArticulosCantidadesJpaController;
import jpa.DevolucionesArticulosJpaController;
import jpa.SuministroArticulosJpaController;
import jpa.VariedadesArticulosJpaController;
import jpa.VariedadesDevolucionesJpaController;
import jpa.VentasArticulosJpaController;

/**
 *
 * @author Juan
 */
public class pSuministro extends javax.swing.JDialog {

    private final EntityManagerFactory factory;
    VariedadesArticulos articulos = null;
    VariedadesArticulosJpaController vajc = null;
    ArticulosCantidades cantidades = null;
    ArticulosCantidadesJpaController acjc = null;
    VentasArticulos ventasArticulos = null;
    VentasArticulosJpaController veajc = null;
    VariedadesDevoluciones devoluciones = null;
    VariedadesDevolucionesJpaController vdjc = null;
    DevolucionesArticulos darticulos = null;
    DevolucionesArticulosJpaController dajc = null;
    SuministroArticulos suministroArticulos = null;
    SuministroArticulosJpaController sajc = null;
    DefaultTableModel model;
    Object dato[] = null;

    /**
     * Creates new form pVenta
     */
    public pSuministro(java.awt.Frame parent, boolean modal, EntityManagerFactory factory) {
        super(parent, modal);
        initComponents();
        this.factory = factory;
        setCargarTabla();
        hilodato ut = new hilodato();
        Thread r = new Thread(ut);
        r.start();
    }

    private DefaultTableModel getModelo() {
        DefaultTableModel modelo = new DefaultTableModel(
                null, new String[]{"id", "Codigo", "Descripcion", "Precio", "Exist."}) {
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
        //391
        jTable1.getColumnModel().getColumn(1).setMinWidth(120);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(120);
        jTable1.getColumnModel().getColumn(2).setMinWidth(181);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(181);
        jTable1.getColumnModel().getColumn(3).setMinWidth(50);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(50);
        jTable1.getColumnModel().getColumn(4).setMinWidth(40);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(40);
    }

    private void setDatos() {
        if (vajc == null) {
            vajc = new VariedadesArticulosJpaController(factory);
        }
        if (acjc == null) {
            acjc = new ArticulosCantidadesJpaController(factory);
        }
        if (veajc == null) {
            veajc = new VentasArticulosJpaController(factory);
        }
        if (vdjc == null) {
            vdjc = new VariedadesDevolucionesJpaController(factory);
        }
        if (dajc == null) {
            dajc = new DevolucionesArticulosJpaController(factory);
        }
        if (sajc == null) {
            sajc = new SuministroArticulosJpaController(factory);
        }
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        List<ArticulosCantidades> art = obtenerCant1();
        List<VentasArticulos> vart = obtenerNegas1();
        List<DevolucionesArticulos> das = obtenerCant2();
        List<SuministroArticulos> sas = obtenerCant3();
        for (VariedadesArticulos va : obtenerArticulo()) {
            int canti = 0;
            model.addRow(dato);
            model.setValueAt(va, model.getRowCount() - 1, 0);
            model.setValueAt("<html><div style=\"width:120;\">" + va.getCodigoarticulo() + "</div></html>", model.getRowCount() - 1, 1);
            model.setValueAt("<html><div style=\"width:181;\">" + va.getNombrearticulo() + "</div></html>", model.getRowCount() - 1, 2);
            for (int i = 0; i < art.size(); i++) {
                if (va.getId() == art.get(i).getIdarticulo().getId()) {
                    canti = canti + art.get(i).getCantidad();
                }
            }
            for (int j = 0; j < vart.size(); j++) {
                if (va.getId() == vart.get(j).getIdarticulo().getId()) {
                    canti = canti - vart.get(j).getCantidad();
                }
            }
            for (int h = 0; h < das.size(); h++) {
                if (va.getId() == das.get(h).getIdarticulo().getId()) {
                    canti = canti + das.get(h).getCantidad();
                }
            }
            for (int k = 0; k < sas.size(); k++) {
                if (va.getId() == sas.get(k).getIdarticulo().getId()) {
                    canti = canti + sas.get(k).getCantidad();
                }
            }
            model.setValueAt(va.getValorcompra(), model.getRowCount() - 1, 3);
            model.setValueAt(canti, model.getRowCount() - 1, 4);
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

    private List<VariedadesArticulos> obtenerArticulo() {
        EntityManager em = vajc.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM VariedadesArticulos a WHERE a.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<ArticulosCantidades> obtenerCant1() {
        EntityManager em = acjc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM ArticulosCantidades c WHERE c.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<VentasArticulos> obtenerNegas1() {
        EntityManager em = veajc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM VentasArticulos c WHERE c.estado='1' OR c.estado='2'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<DevolucionesArticulos> obtenerCant2() {
        EntityManager em = veajc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM DevolucionesArticulos c WHERE c.estado='1' OR c.estado='2'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private List<SuministroArticulos> obtenerCant3() {
        EntityManager em = veajc.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM SuministroArticulos c WHERE c.estado='1' OR c.estado='2'")
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tFiltro = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(50, 72, 141));
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  Selección de articulos");
        jLabel1.setOpaque(true);

        tFiltro.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        tFiltro.setForeground(new java.awt.Color(153, 153, 153));
        tFiltro.setText("Ingrese código o nombre del producto");
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(50, 72, 141));
        jLabel2.setText("Cantidad:");

        jButton1.setBackground(new java.awt.Color(50, 72, 141));
        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Aceptar");
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusable(false);
        jButton1.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tFiltroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tFiltroFocusGained
        tFiltro.selectAll();
    }//GEN-LAST:event_tFiltroFocusGained

    private void tFiltroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFiltroKeyPressed
        TableRowSorter sorter = new TableRowSorter(model);
        sorter.setRowFilter(RowFilter.regexFilter(tFiltro.getText().toUpperCase()));
        jTable1.setRowSorter(sorter);
    }//GEN-LAST:event_tFiltroKeyPressed

    private void tFiltroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFiltroKeyTyped
        if (tFiltro.getText().isEmpty()) {
            Font fuente = new Font("Tahoma", 2, 11);
            tFiltro.setFont(fuente);
            tFiltro.setForeground(new Color(153, 153, 153));
            tFiltro.setText("Ingrese código o nombre del producto");
            tFiltro.selectAll();
        } else {
            Font fuente = new Font("Tahoma", 0, 11);
            tFiltro.setForeground(Color.BLACK);
            tFiltro.setFont(fuente);
        }
    }//GEN-LAST:event_tFiltroKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(pSuministro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pSuministro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pSuministro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pSuministro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                pVenta dialog = new pVenta(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                pVenta dialog = new pVenta(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                pVenta dialog = new pVenta(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                pVenta dialog = new pVenta(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
    }


    public class hilodato extends Thread {

        public hilodato() {
        }

        @Override
        public void run() {
            setDatos();
            this.stop();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    public javax.swing.JTextField jTextField1;
    public javax.swing.JTextField tFiltro;
    // End of variables declaration//GEN-END:variables
}
