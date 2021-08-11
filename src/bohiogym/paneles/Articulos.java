/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles;

import bohiogym.paneles.panelarticulos.devoluciones;
import bohiogym.paneles.panelarticulos.gArticulos;
import bohiogym.paneles.panelarticulos.gPLanes;
import bohiogym.paneles.panelarticulos.lArticulos;
import bohiogym.paneles.panelarticulos.lPlanes;
import bohiogym.paneles.panelarticulos.suministros;
import bohiogym.paneles.panelarticulos.ventas;
import entity.UserLog;
import entity.UserPermisos;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import jpa.UserLogJpaController;

/**
 *
 * @author Juan
 */
public class Articulos extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    private final Properties props;
    private final UserLog userlog;
    UserLogJpaController uljc = null;
    gPLanes plan = null;
    gArticulos arti = null;
    ventas vent = null;
    devoluciones devo = null;
    suministros sumi = null;
    int gestionservicios = 10, listadoservicios = 11, gestionarticulos = 12, listadoarticulos = 13, ventas = 14, devoluciones = 15, suministro = 16;

    /**
     * Creates new form Articulos
     */
    public Articulos(EntityManagerFactory factory, UserLog userlog, Properties props) {
        initComponents();
        this.props = props;
        this.factory = factory;
        this.userlog = userlog;
        uljc = new UserLogJpaController(factory);
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

    private boolean validaGServicios() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == gestionservicios || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veGServicios() {
        if (plan == null) {
            plan = new gPLanes(factory);
        }
        this.pSubpanelArticulos.removeAll();
        plan.setBounds(0, 0, 492, 449);
        pSubpanelArticulos.add(plan);
        plan.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelArticulos.removeAll();
                pSubpanelArticulos.validate();
                pSubpanelArticulos.repaint();
                plan = null;
            }
        });
        plan.setVisible(true);
        pSubpanelArticulos.validate();
        pSubpanelArticulos.repaint();
    }

    private boolean validaLServicio() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == listadoservicios || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veLservicios() {
        lPlanes lipa = new lPlanes(factory);
        this.pSubpanelArticulos.removeAll();
        lipa.setBounds(0, 0, 492, 449);
        pSubpanelArticulos.add(lipa);
        lipa.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelArticulos.removeAll();
                pSubpanelArticulos.validate();
                pSubpanelArticulos.repaint();
            }
        });
        lipa.setVisible(true);
        pSubpanelArticulos.validate();
        pSubpanelArticulos.repaint();
    }

    private boolean validaGArticulos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == gestionarticulos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veGarticulos() {
        if (arti == null) {
            arti = new gArticulos(factory);
        }
        this.pSubpanelArticulos.removeAll();
        arti.setBounds(0, 0, 492, 449);
        pSubpanelArticulos.add(arti);
        arti.jTextField1.requestFocus();
        arti.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelArticulos.removeAll();
                pSubpanelArticulos.validate();
                pSubpanelArticulos.repaint();
            }
        });
        arti.setVisible(true);
        pSubpanelArticulos.validate();
        pSubpanelArticulos.repaint();
    }

    private boolean validaLArticulos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == listadoarticulos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veLarticulos() {
        lArticulos liar = new lArticulos(factory);
        this.pSubpanelArticulos.removeAll();
        liar.setBounds(0, 0, 492, 449);
        pSubpanelArticulos.add(liar);
        liar.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelArticulos.removeAll();
                pSubpanelArticulos.validate();
                pSubpanelArticulos.repaint();
            }
        });
        liar.setVisible(true);
        pSubpanelArticulos.validate();
        pSubpanelArticulos.repaint();
    }

    private boolean validaVentas() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == ventas || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veVentas() {
        if (vent == null) {
            vent = new ventas(factory, userlog, props);
        }
        this.pSubpanelArticulos.removeAll();
        vent.setBounds(0, 0, 492, 449);
        pSubpanelArticulos.add(vent);
        vent.jTextField4.requestFocus();
        vent.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelArticulos.removeAll();
                pSubpanelArticulos.validate();
                pSubpanelArticulos.repaint();
                vent = null;
            }
        });
        vent.setVisible(true);
        pSubpanelArticulos.validate();
        pSubpanelArticulos.repaint();
    }

    private boolean validaDevoluciones() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == devoluciones || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veDevoluciones() {
        if (devo == null) {
            devo = new devoluciones(factory, userlog);
        }
        this.pSubpanelArticulos.removeAll();
        devo.setBounds(0, 0, 492, 449);
        pSubpanelArticulos.add(devo);
        devo.jTextField4.requestFocus();
        devo.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelArticulos.removeAll();
                pSubpanelArticulos.validate();
                pSubpanelArticulos.repaint();
                devo = null;
            }
        });
        devo.setVisible(true);
        pSubpanelArticulos.validate();
        pSubpanelArticulos.repaint();
    }

    private boolean validaSuministro() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == suministro || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veSuministro() {
        if (sumi == null) {
            sumi = new suministros(factory, userlog);
        }
        this.pSubpanelArticulos.removeAll();
        sumi.setBounds(0, 0, 492, 449);
        pSubpanelArticulos.add(sumi);
        sumi.jTextField4.requestFocus();
        sumi.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelArticulos.removeAll();
                pSubpanelArticulos.validate();
                pSubpanelArticulos.repaint();
                sumi = null;
            }
        });
        sumi.setVisible(true);
        pSubpanelArticulos.validate();
        pSubpanelArticulos.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bGplanes = new javax.swing.JLabel();
        bListadoPlanes = new javax.swing.JLabel();
        bGestionarArticulos = new javax.swing.JLabel();
        bListadoArticulos = new javax.swing.JLabel();
        bVentas = new javax.swing.JLabel();
        pSubpanelArticulos = new javax.swing.JPanel();
        bDevolucion = new javax.swing.JLabel();
        bSuministros = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(646, 449));
        setMinimumSize(new java.awt.Dimension(646, 449));
        setPreferredSize(new java.awt.Dimension(646, 449));

        bGplanes.setBackground(new java.awt.Color(255, 255, 255));
        bGplanes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bGplanes.setForeground(new java.awt.Color(50, 72, 141));
        bGplanes.setText("  Gestión de Servicios");
        bGplanes.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bGplanes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bGplanes.setOpaque(true);
        bGplanes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bGplanesMouseReleased(evt);
            }
        });

        bListadoPlanes.setBackground(new java.awt.Color(255, 255, 255));
        bListadoPlanes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bListadoPlanes.setForeground(new java.awt.Color(50, 72, 141));
        bListadoPlanes.setText("  Listado de Servicios");
        bListadoPlanes.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bListadoPlanes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bListadoPlanes.setOpaque(true);
        bListadoPlanes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bListadoPlanesMouseReleased(evt);
            }
        });

        bGestionarArticulos.setBackground(new java.awt.Color(255, 255, 255));
        bGestionarArticulos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bGestionarArticulos.setForeground(new java.awt.Color(50, 72, 141));
        bGestionarArticulos.setText("  Gestión de Articulos");
        bGestionarArticulos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bGestionarArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bGestionarArticulos.setOpaque(true);
        bGestionarArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bGestionarArticulosMouseReleased(evt);
            }
        });

        bListadoArticulos.setBackground(new java.awt.Color(255, 255, 255));
        bListadoArticulos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bListadoArticulos.setForeground(new java.awt.Color(50, 72, 141));
        bListadoArticulos.setText("  Listado de Articulos");
        bListadoArticulos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bListadoArticulos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bListadoArticulos.setOpaque(true);
        bListadoArticulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bListadoArticulosMouseReleased(evt);
            }
        });

        bVentas.setBackground(new java.awt.Color(255, 255, 255));
        bVentas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bVentas.setForeground(new java.awt.Color(50, 72, 141));
        bVentas.setText("  Ventas");
        bVentas.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bVentas.setOpaque(true);
        bVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bVentasMouseReleased(evt);
            }
        });

        pSubpanelArticulos.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pSubpanelArticulosLayout = new javax.swing.GroupLayout(pSubpanelArticulos);
        pSubpanelArticulos.setLayout(pSubpanelArticulosLayout);
        pSubpanelArticulosLayout.setHorizontalGroup(
            pSubpanelArticulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        pSubpanelArticulosLayout.setVerticalGroup(
            pSubpanelArticulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        bDevolucion.setBackground(new java.awt.Color(255, 255, 255));
        bDevolucion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bDevolucion.setForeground(new java.awt.Color(50, 72, 141));
        bDevolucion.setText("  Devoluciones");
        bDevolucion.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bDevolucion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bDevolucion.setOpaque(true);
        bDevolucion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bDevolucionMouseReleased(evt);
            }
        });

        bSuministros.setBackground(new java.awt.Color(255, 255, 255));
        bSuministros.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSuministros.setForeground(new java.awt.Color(50, 72, 141));
        bSuministros.setText("  Suministros");
        bSuministros.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bSuministros.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSuministros.setOpaque(true);
        bSuministros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bSuministrosMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bGplanes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bListadoPlanes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bGestionarArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(bListadoArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bDevolucion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSuministros, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pSubpanelArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bGplanes, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bListadoPlanes, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bGestionarArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bListadoArticulos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bSuministros, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 182, Short.MAX_VALUE))
            .addComponent(pSubpanelArticulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bGplanesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGplanesMouseReleased
        if (validaGServicios()) {
            veGServicios();
            bGplanes.setForeground(Color.white);
            bGplanes.setBackground(new Color(50, 72, 141));
            bListadoPlanes.setForeground(new Color(50, 72, 141));
            bListadoPlanes.setBackground(Color.white);
            bGestionarArticulos.setForeground(new Color(50, 72, 141));
            bGestionarArticulos.setBackground(Color.white);
            bListadoArticulos.setForeground(new Color(50, 72, 141));
            bListadoArticulos.setBackground(Color.white);
            bVentas.setForeground(new Color(50, 72, 141));
            bVentas.setBackground(Color.white);
            bDevolucion.setForeground(new Color(50, 72, 141));
            bDevolucion.setBackground(Color.white);
            bSuministros.setForeground(new Color(50, 72, 141));
            bSuministros.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bGplanesMouseReleased

    private void bListadoPlanesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bListadoPlanesMouseReleased
        if (validaLServicio()) {
            veLservicios();
            bListadoPlanes.setForeground(Color.white);
            bListadoPlanes.setBackground(new Color(50, 72, 141));
            bGplanes.setForeground(new Color(50, 72, 141));
            bGplanes.setBackground(Color.white);
            bGestionarArticulos.setForeground(new Color(50, 72, 141));
            bGestionarArticulos.setBackground(Color.white);
            bListadoArticulos.setForeground(new Color(50, 72, 141));
            bListadoArticulos.setBackground(Color.white);
            bVentas.setForeground(new Color(50, 72, 141));
            bVentas.setBackground(Color.white);
            bDevolucion.setForeground(new Color(50, 72, 141));
            bDevolucion.setBackground(Color.white);
            bSuministros.setForeground(new Color(50, 72, 141));
            bSuministros.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bListadoPlanesMouseReleased

    private void bGestionarArticulosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGestionarArticulosMouseReleased
        if (validaGArticulos()) {
            veGarticulos();
            bGestionarArticulos.setForeground(Color.white);
            bGestionarArticulos.setBackground(new Color(50, 72, 141));
            bGplanes.setForeground(new Color(50, 72, 141));
            bGplanes.setBackground(Color.white);
            bListadoPlanes.setForeground(new Color(50, 72, 141));
            bListadoPlanes.setBackground(Color.white);
            bListadoArticulos.setForeground(new Color(50, 72, 141));
            bListadoArticulos.setBackground(Color.white);
            bVentas.setForeground(new Color(50, 72, 141));
            bVentas.setBackground(Color.white);
            bDevolucion.setForeground(new Color(50, 72, 141));
            bDevolucion.setBackground(Color.white);
            bSuministros.setForeground(new Color(50, 72, 141));
            bSuministros.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bGestionarArticulosMouseReleased

    private void bListadoArticulosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bListadoArticulosMouseReleased
        if (validaLArticulos()) {
            veLarticulos();
            bListadoArticulos.setForeground(Color.white);
            bListadoArticulos.setBackground(new Color(50, 72, 141));
            bGplanes.setForeground(new Color(50, 72, 141));
            bGplanes.setBackground(Color.white);
            bListadoPlanes.setForeground(new Color(50, 72, 141));
            bListadoPlanes.setBackground(Color.white);
            bGestionarArticulos.setForeground(new Color(50, 72, 141));
            bGestionarArticulos.setBackground(Color.white);
            bVentas.setForeground(new Color(50, 72, 141));
            bVentas.setBackground(Color.white);
            bDevolucion.setForeground(new Color(50, 72, 141));
            bDevolucion.setBackground(Color.white);
            bSuministros.setForeground(new Color(50, 72, 141));
            bSuministros.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bListadoArticulosMouseReleased

    private void bVentasMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bVentasMouseReleased
        if (validaVentas()) {
            veVentas();
            bVentas.setForeground(Color.white);
            bVentas.setBackground(new Color(50, 72, 141));
            bGplanes.setForeground(new Color(50, 72, 141));
            bGplanes.setBackground(Color.white);
            bListadoPlanes.setForeground(new Color(50, 72, 141));
            bListadoPlanes.setBackground(Color.white);
            bGestionarArticulos.setForeground(new Color(50, 72, 141));
            bGestionarArticulos.setBackground(Color.white);
            bListadoArticulos.setForeground(new Color(50, 72, 141));
            bListadoArticulos.setBackground(Color.white);
            bDevolucion.setForeground(new Color(50, 72, 141));
            bDevolucion.setBackground(Color.white);
            bSuministros.setForeground(new Color(50, 72, 141));
            bSuministros.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bVentasMouseReleased

    private void bDevolucionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bDevolucionMouseReleased
        if (validaDevoluciones()) {
            veDevoluciones();
            bDevolucion.setForeground(Color.white);
            bDevolucion.setBackground(new Color(50, 72, 141));
            bGplanes.setForeground(new Color(50, 72, 141));
            bGplanes.setBackground(Color.white);
            bListadoPlanes.setForeground(new Color(50, 72, 141));
            bListadoPlanes.setBackground(Color.white);
            bGestionarArticulos.setForeground(new Color(50, 72, 141));
            bGestionarArticulos.setBackground(Color.white);
            bListadoArticulos.setForeground(new Color(50, 72, 141));
            bListadoArticulos.setBackground(Color.white);
            bVentas.setForeground(new Color(50, 72, 141));
            bVentas.setBackground(Color.white);
            bSuministros.setForeground(new Color(50, 72, 141));
            bSuministros.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bDevolucionMouseReleased

    private void bSuministrosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSuministrosMouseReleased
        if (validaSuministro()) {
            veSuministro();
            bSuministros.setForeground(Color.white);
            bSuministros.setBackground(new Color(50, 72, 141));
            bGplanes.setForeground(new Color(50, 72, 141));
            bGplanes.setBackground(Color.white);
            bListadoPlanes.setForeground(new Color(50, 72, 141));
            bListadoPlanes.setBackground(Color.white);
            bGestionarArticulos.setForeground(new Color(50, 72, 141));
            bGestionarArticulos.setBackground(Color.white);
            bListadoArticulos.setForeground(new Color(50, 72, 141));
            bListadoArticulos.setBackground(Color.white);
            bVentas.setForeground(new Color(50, 72, 141));
            bVentas.setBackground(Color.white);
            bDevolucion.setForeground(new Color(50, 72, 141));
            bDevolucion.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bSuministrosMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bDevolucion;
    private javax.swing.JLabel bGestionarArticulos;
    private javax.swing.JLabel bGplanes;
    private javax.swing.JLabel bListadoArticulos;
    private javax.swing.JLabel bListadoPlanes;
    private javax.swing.JLabel bSuministros;
    private javax.swing.JLabel bVentas;
    private javax.swing.JPanel pSubpanelArticulos;
    // End of variables declaration//GEN-END:variables
}
