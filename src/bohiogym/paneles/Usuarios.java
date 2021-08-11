/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles;

import bohiogym.paneles.panelusuarios.aplanes;
import bohiogym.paneles.panelusuarios.gUsuarios;
import bohiogym.paneles.panelusuarios.lTerceros;
import bohiogym.paneles.panelusuarios.lUsuarios;
import bohiogym.paneles.panelusuarios.lUsuariosactivos;
import bohiogym.paneles.panelusuarios.lUsuariosinactivos;
import bohiogym.paneles.panelusuarios.terceros;
import bohiogym.paneles.panelusuarios.vTiempo;
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
public class Usuarios extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    private final UserLog userlog;
    private final Properties props;
    UserLogJpaController uljc = null;
    gUsuarios gusu = null;
    lUsuarios lusu = null;
    terceros terc = null;
    public vTiempo tiem = null;
    int gestionusuarios = 2, listadousuarios = 3, asignacionplanes = 4, verificaciontiempo = 5, usuarioactivos = 6, terceros = 7, listadoterceros = 8, usuariosinactivos = 9;

    /**
     * Creates new form Gusuarios
     */
    public Usuarios(EntityManagerFactory factory, UserLog userlog, Properties props) {
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

    private boolean validaGUsuarios() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == gestionusuarios || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veGusuarios() {
        if (gusu == null) {
            gusu = new gUsuarios(factory, props);
        }
        this.pSubpanelUsuarios.removeAll();
        gusu.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(gusu);
        gusu.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        gusu.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
    }

    private boolean validaLUsuarios() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == listadousuarios || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veLusuarios() {
        if (lusu == null) {
            lusu = new lUsuarios(factory);
        }
        this.pSubpanelUsuarios.removeAll();
        lusu.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(lusu);
        lusu.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        lusu.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
    }

    private boolean validaTerceros() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == terceros || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veTerceros() {
        if (terc == null) {
            terc = new terceros(factory);
        }
        this.pSubpanelUsuarios.removeAll();
        terc.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(terc);
        terc.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        terc.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
    }

    private boolean validaTiempo() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == verificaciontiempo || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private boolean validaLTerceros() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == listadoterceros || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veLTerceros() {
        lTerceros lterc = new lTerceros(factory);
        this.pSubpanelUsuarios.removeAll();
        lterc.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(lterc);
        lterc.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        lterc.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
    }

    public void veTiempos() {
        tiem = new vTiempo(factory, props, userlog);
        this.pSubpanelUsuarios.removeAll();
        tiem.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(tiem);
        tiem.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        tiem.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
    }

    private boolean validaAPlanes() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == asignacionplanes || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    public void veAPlanes() {
        aplanes planes = new aplanes(factory, props, userlog);
        this.pSubpanelUsuarios.removeAll();
        planes.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(planes);
        planes.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        planes.tIdentificacion.requestFocus();
        planes.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
    }

    private boolean validaActivos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == usuarioactivos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veLActivos() {
        lUsuariosactivos activos = new lUsuariosactivos(factory, props);
        this.pSubpanelUsuarios.removeAll();
        activos.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(activos);
        activos.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        activos.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
    }

    private boolean validaInactivos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == usuariosinactivos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veLInactivos() {
        lUsuariosinactivos inactivos = new lUsuariosinactivos(factory, props);
        this.pSubpanelUsuarios.removeAll();
        inactivos.setBounds(0, 0, 492, 449);
        pSubpanelUsuarios.add(inactivos);
        inactivos.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelUsuarios.removeAll();
                pSubpanelUsuarios.validate();
                pSubpanelUsuarios.repaint();
            }
        });
        inactivos.setVisible(true);
        pSubpanelUsuarios.validate();
        pSubpanelUsuarios.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bGestionusuarios = new javax.swing.JLabel();
        bListadousuarios = new javax.swing.JLabel();
        bVerificaTiempos = new javax.swing.JLabel();
        bTerceros = new javax.swing.JLabel();
        bListadoTerceros2 = new javax.swing.JLabel();
        pSubpanelUsuarios = new javax.swing.JPanel();
        bAsignacionPlanes = new javax.swing.JLabel();
        bUsuariosActivos = new javax.swing.JLabel();
        bUsuariosInactivos = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(646, 449));
        setPreferredSize(new java.awt.Dimension(646, 449));

        bGestionusuarios.setBackground(new java.awt.Color(255, 255, 255));
        bGestionusuarios.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bGestionusuarios.setForeground(new java.awt.Color(50, 72, 141));
        bGestionusuarios.setText("  Gestion de Usuarios");
        bGestionusuarios.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bGestionusuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bGestionusuarios.setOpaque(true);
        bGestionusuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bGestionusuariosMouseReleased(evt);
            }
        });

        bListadousuarios.setBackground(new java.awt.Color(255, 255, 255));
        bListadousuarios.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bListadousuarios.setForeground(new java.awt.Color(50, 72, 141));
        bListadousuarios.setText("  Listado de Usuarios");
        bListadousuarios.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bListadousuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bListadousuarios.setOpaque(true);
        bListadousuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bListadousuariosMouseReleased(evt);
            }
        });

        bVerificaTiempos.setBackground(new java.awt.Color(255, 255, 255));
        bVerificaTiempos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bVerificaTiempos.setForeground(new java.awt.Color(50, 72, 141));
        bVerificaTiempos.setText("  Verficiación de Tiempo");
        bVerificaTiempos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bVerificaTiempos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bVerificaTiempos.setOpaque(true);
        bVerificaTiempos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bVerificaTiemposMouseReleased(evt);
            }
        });

        bTerceros.setBackground(new java.awt.Color(255, 255, 255));
        bTerceros.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bTerceros.setForeground(new java.awt.Color(50, 72, 141));
        bTerceros.setText("  Terceros");
        bTerceros.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bTerceros.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bTerceros.setOpaque(true);
        bTerceros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bTercerosMouseReleased(evt);
            }
        });

        bListadoTerceros2.setBackground(new java.awt.Color(255, 255, 255));
        bListadoTerceros2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bListadoTerceros2.setForeground(new java.awt.Color(50, 72, 141));
        bListadoTerceros2.setText("  Listado de Terceros");
        bListadoTerceros2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bListadoTerceros2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bListadoTerceros2.setOpaque(true);
        bListadoTerceros2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bListadoTerceros2MouseReleased(evt);
            }
        });

        pSubpanelUsuarios.setBackground(new java.awt.Color(255, 255, 255));
        pSubpanelUsuarios.setMinimumSize(new java.awt.Dimension(492, 449));
        pSubpanelUsuarios.setPreferredSize(new java.awt.Dimension(492, 449));

        javax.swing.GroupLayout pSubpanelUsuariosLayout = new javax.swing.GroupLayout(pSubpanelUsuarios);
        pSubpanelUsuarios.setLayout(pSubpanelUsuariosLayout);
        pSubpanelUsuariosLayout.setHorizontalGroup(
            pSubpanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        pSubpanelUsuariosLayout.setVerticalGroup(
            pSubpanelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        bAsignacionPlanes.setBackground(new java.awt.Color(255, 255, 255));
        bAsignacionPlanes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bAsignacionPlanes.setForeground(new java.awt.Color(50, 72, 141));
        bAsignacionPlanes.setText("  Asignacion de Planes");
        bAsignacionPlanes.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bAsignacionPlanes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bAsignacionPlanes.setOpaque(true);
        bAsignacionPlanes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bAsignacionPlanesMouseReleased(evt);
            }
        });

        bUsuariosActivos.setBackground(new java.awt.Color(255, 255, 255));
        bUsuariosActivos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bUsuariosActivos.setForeground(new java.awt.Color(50, 72, 141));
        bUsuariosActivos.setText("  Usuarios Activos");
        bUsuariosActivos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bUsuariosActivos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bUsuariosActivos.setOpaque(true);
        bUsuariosActivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bUsuariosActivosMouseReleased(evt);
            }
        });

        bUsuariosInactivos.setBackground(new java.awt.Color(255, 255, 255));
        bUsuariosInactivos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bUsuariosInactivos.setForeground(new java.awt.Color(50, 72, 141));
        bUsuariosInactivos.setText("  Usuarios Inactivos");
        bUsuariosInactivos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bUsuariosInactivos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bUsuariosInactivos.setOpaque(true);
        bUsuariosInactivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bUsuariosInactivosMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bGestionusuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bListadousuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bVerificaTiempos, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(bTerceros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bListadoTerceros2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bAsignacionPlanes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(bUsuariosActivos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bUsuariosInactivos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pSubpanelUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bGestionusuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bListadousuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bAsignacionPlanes, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(bVerificaTiempos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bUsuariosActivos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bTerceros, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bListadoTerceros2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bUsuariosInactivos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
            .addComponent(pSubpanelUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bGestionusuariosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGestionusuariosMouseReleased
        if (validaGUsuarios()) {
            veGusuarios();
            bGestionusuarios.setForeground(Color.white);
            bGestionusuarios.setBackground(new Color(50, 72, 141));
            bListadousuarios.setForeground(new Color(50, 72, 141));
            bListadousuarios.setBackground(Color.white);
            bVerificaTiempos.setForeground(new Color(50, 72, 141));
            bVerificaTiempos.setBackground(Color.white);
            bTerceros.setForeground(new Color(50, 72, 141));
            bTerceros.setBackground(Color.white);
            bListadoTerceros2.setForeground(new Color(50, 72, 141));
            bListadoTerceros2.setBackground(Color.white);
            bAsignacionPlanes.setForeground(new Color(50, 72, 141));
            bAsignacionPlanes.setBackground(Color.white);
            bUsuariosActivos.setForeground(new Color(50, 72, 141));
            bUsuariosActivos.setBackground(Color.white);
            bUsuariosInactivos.setForeground(new Color(50, 72, 141));
            bUsuariosInactivos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bGestionusuariosMouseReleased

    private void bListadousuariosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bListadousuariosMouseReleased
        if (validaLUsuarios()) {
            veLusuarios();
            bListadousuarios.setForeground(Color.white);
            bListadousuarios.setBackground(new Color(50, 72, 141));
            bGestionusuarios.setForeground(new Color(50, 72, 141));
            bGestionusuarios.setBackground(Color.white);
            bVerificaTiempos.setForeground(new Color(50, 72, 141));
            bVerificaTiempos.setBackground(Color.white);
            bTerceros.setForeground(new Color(50, 72, 141));
            bTerceros.setBackground(Color.white);
            bListadoTerceros2.setForeground(new Color(50, 72, 141));
            bListadoTerceros2.setBackground(Color.white);
            bAsignacionPlanes.setForeground(new Color(50, 72, 141));
            bAsignacionPlanes.setBackground(Color.white);
            bUsuariosActivos.setForeground(new Color(50, 72, 141));
            bUsuariosActivos.setBackground(Color.white);
            bUsuariosInactivos.setForeground(new Color(50, 72, 141));
            bUsuariosInactivos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bListadousuariosMouseReleased

    private void bTercerosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bTercerosMouseReleased
        if (validaTerceros()) {
            veTerceros();
            bTerceros.setForeground(Color.white);
            bTerceros.setBackground(new Color(50, 72, 141));
            bGestionusuarios.setForeground(new Color(50, 72, 141));
            bGestionusuarios.setBackground(Color.white);
            bVerificaTiempos.setForeground(new Color(50, 72, 141));
            bVerificaTiempos.setBackground(Color.white);
            bListadousuarios.setForeground(new Color(50, 72, 141));
            bListadousuarios.setBackground(Color.white);
            bListadoTerceros2.setForeground(new Color(50, 72, 141));
            bListadoTerceros2.setBackground(Color.white);
            bAsignacionPlanes.setForeground(new Color(50, 72, 141));
            bAsignacionPlanes.setBackground(Color.white);
            bUsuariosActivos.setForeground(new Color(50, 72, 141));
            bUsuariosActivos.setBackground(Color.white);
            bUsuariosActivos.setForeground(new Color(50, 72, 141));
            bUsuariosActivos.setBackground(Color.white);
            bUsuariosInactivos.setForeground(new Color(50, 72, 141));
            bUsuariosInactivos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bTercerosMouseReleased

    private void bVerificaTiemposMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bVerificaTiemposMouseReleased
        if (validaTiempo()) {
            veTiempos();
            bVerificaTiempos.setForeground(Color.white);
            bVerificaTiempos.setBackground(new Color(50, 72, 141));
            bGestionusuarios.setForeground(new Color(50, 72, 141));
            bGestionusuarios.setBackground(Color.white);
            bTerceros.setForeground(new Color(50, 72, 141));
            bTerceros.setBackground(Color.white);
            bListadousuarios.setForeground(new Color(50, 72, 141));
            bListadousuarios.setBackground(Color.white);
            bListadoTerceros2.setForeground(new Color(50, 72, 141));
            bListadoTerceros2.setBackground(Color.white);
            bAsignacionPlanes.setForeground(new Color(50, 72, 141));
            bAsignacionPlanes.setBackground(Color.white);
            bUsuariosActivos.setForeground(new Color(50, 72, 141));
            bUsuariosActivos.setBackground(Color.white);
            bUsuariosInactivos.setForeground(new Color(50, 72, 141));
            bUsuariosInactivos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bVerificaTiemposMouseReleased

    private void bListadoTerceros2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bListadoTerceros2MouseReleased
        if (validaLTerceros()) {
            veLTerceros();
            bListadoTerceros2.setForeground(Color.white);
            bListadoTerceros2.setBackground(new Color(50, 72, 141));
            bGestionusuarios.setForeground(new Color(50, 72, 141));
            bGestionusuarios.setBackground(Color.white);
            bListadousuarios.setForeground(new Color(50, 72, 141));
            bListadousuarios.setBackground(Color.white);
            bTerceros.setForeground(new Color(50, 72, 141));
            bTerceros.setBackground(Color.white);
            bVerificaTiempos.setForeground(new Color(50, 72, 141));
            bVerificaTiempos.setBackground(Color.white);
            bAsignacionPlanes.setForeground(new Color(50, 72, 141));
            bAsignacionPlanes.setBackground(Color.white);
            bUsuariosActivos.setForeground(new Color(50, 72, 141));
            bUsuariosActivos.setBackground(Color.white);
            bUsuariosInactivos.setForeground(new Color(50, 72, 141));
            bUsuariosInactivos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bListadoTerceros2MouseReleased

    private void bAsignacionPlanesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAsignacionPlanesMouseReleased
        if (validaAPlanes()) {
            veAPlanes();
            bAsignacionPlanes.setForeground(Color.white);
            bAsignacionPlanes.setBackground(new Color(50, 72, 141));
            bGestionusuarios.setForeground(new Color(50, 72, 141));
            bGestionusuarios.setBackground(Color.white);
            bListadousuarios.setForeground(new Color(50, 72, 141));
            bListadousuarios.setBackground(Color.white);
            bTerceros.setForeground(new Color(50, 72, 141));
            bTerceros.setBackground(Color.white);
            bVerificaTiempos.setForeground(new Color(50, 72, 141));
            bVerificaTiempos.setBackground(Color.white);
            bListadoTerceros2.setForeground(new Color(50, 72, 141));
            bListadoTerceros2.setBackground(Color.white);
            bUsuariosActivos.setForeground(new Color(50, 72, 141));
            bUsuariosActivos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bAsignacionPlanesMouseReleased

    private void bUsuariosActivosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bUsuariosActivosMouseReleased
        if (validaActivos()) {
            veLActivos();
            bUsuariosActivos.setForeground(Color.white);
            bUsuariosActivos.setBackground(new Color(50, 72, 141));
            bGestionusuarios.setForeground(new Color(50, 72, 141));
            bGestionusuarios.setBackground(Color.white);
            bListadousuarios.setForeground(new Color(50, 72, 141));
            bListadousuarios.setBackground(Color.white);
            bTerceros.setForeground(new Color(50, 72, 141));
            bTerceros.setBackground(Color.white);
            bVerificaTiempos.setForeground(new Color(50, 72, 141));
            bVerificaTiempos.setBackground(Color.white);
            bListadoTerceros2.setForeground(new Color(50, 72, 141));
            bListadoTerceros2.setBackground(Color.white);
            bAsignacionPlanes.setForeground(new Color(50, 72, 141));
            bAsignacionPlanes.setBackground(Color.white);
            bUsuariosInactivos.setForeground(new Color(50, 72, 141));
            bUsuariosInactivos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bUsuariosActivosMouseReleased

    private void bUsuariosInactivosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bUsuariosInactivosMouseReleased
        if (validaInactivos()) {
            veLInactivos();
            bUsuariosInactivos.setForeground(Color.white);
            bUsuariosInactivos.setBackground(new Color(50, 72, 141));
            bGestionusuarios.setForeground(new Color(50, 72, 141));
            bGestionusuarios.setBackground(Color.white);
            bListadousuarios.setForeground(new Color(50, 72, 141));
            bListadousuarios.setBackground(Color.white);
            bTerceros.setForeground(new Color(50, 72, 141));
            bTerceros.setBackground(Color.white);
            bVerificaTiempos.setForeground(new Color(50, 72, 141));
            bVerificaTiempos.setBackground(Color.white);
            bListadoTerceros2.setForeground(new Color(50, 72, 141));
            bListadoTerceros2.setBackground(Color.white);
            bAsignacionPlanes.setForeground(new Color(50, 72, 141));
            bAsignacionPlanes.setBackground(Color.white);
            bUsuariosActivos.setForeground(new Color(50, 72, 141));
            bUsuariosActivos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bUsuariosInactivosMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bAsignacionPlanes;
    private javax.swing.JLabel bGestionusuarios;
    private javax.swing.JLabel bListadoTerceros2;
    private javax.swing.JLabel bListadousuarios;
    private javax.swing.JLabel bTerceros;
    private javax.swing.JLabel bUsuariosActivos;
    private javax.swing.JLabel bUsuariosInactivos;
    private javax.swing.JLabel bVerificaTiempos;
    public static javax.swing.JPanel pSubpanelUsuarios;
    // End of variables declaration//GEN-END:variables
}
