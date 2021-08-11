/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles;

import bohiogym.paneles.panelinformes.cajadiaria;
import bohiogym.paneles.panelinformes.egresos;
import bohiogym.paneles.panelinformes.ingresos;
import bohiogym.paneles.panelinformes.lEgresos;
import bohiogym.paneles.panelinformes.lIngresos;
import bohiogym.paneles.panelinformes.lVentas;
import bohiogym.paneles.panelinformes.lcreditos;
import bohiogym.paneles.panelinformes.registroUsuarios;
import entity.UserLog;
import entity.UserPermisos;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import jpa.UserLogJpaController;

/**
 *
 * @author Juan
 */
public class Informes extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    private final UserLog userlog;
    UserLogJpaController uljc = null;
    ingresos ingr = null;
    egresos egre = null;
    int ingresos = 17, informeingresos = 18, egresos = 19, informeegresos = 20, cierrecajadiario = 21, registroxusuarios = 22, informeventas = 23, informecreditos = 24;

    /**
     * Creates new form Contabilidad
     */
    public Informes(EntityManagerFactory factory, UserLog userlog) {
        initComponents();
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

    private boolean validaIngresos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == ingresos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veIngresos() {
        if (ingr == null) {
            ingr = new ingresos(factory, userlog);
        }
        this.pSubpanelInformes.removeAll();
        ingr.setBounds(0, 0, 492, 449);
        pSubpanelInformes.add(ingr);
        ingr.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelInformes.removeAll();
                pSubpanelInformes.validate();
                pSubpanelInformes.repaint();
            }
        });
        ingr.setVisible(true);
        pSubpanelInformes.validate();
        pSubpanelInformes.repaint();
    }

    private boolean validaLIngresos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == informeingresos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veListadoIngreso() {
        lIngresos ling = new lIngresos(factory);
        this.pSubpanelInformes.removeAll();
        ling.setBounds(0, 0, 492, 449);
        pSubpanelInformes.add(ling);
        ling.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelInformes.removeAll();
                pSubpanelInformes.validate();
                pSubpanelInformes.repaint();
            }
        });
        ling.setVisible(true);
        pSubpanelInformes.validate();
        pSubpanelInformes.repaint();
    }

    private boolean validaEgresos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == egresos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veEgreso() {
        if (egre == null) {
            egre = new egresos(factory, userlog);
        }
        this.pSubpanelInformes.removeAll();
        egre.setBounds(0, 0, 492, 449);
        pSubpanelInformes.add(egre);
        egre.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelInformes.removeAll();
                pSubpanelInformes.validate();
                pSubpanelInformes.repaint();
            }
        });
        egre.setVisible(true);
        pSubpanelInformes.validate();
        pSubpanelInformes.repaint();
    }

    private boolean validaLEgresos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == informeegresos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veListadoEgresos() {
        lEgresos lege = new lEgresos(factory);
        this.pSubpanelInformes.removeAll();
        lege.setBounds(0, 0, 492, 449);
        pSubpanelInformes.add(lege);
        lege.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelInformes.removeAll();
                pSubpanelInformes.validate();
                pSubpanelInformes.repaint();
            }
        });
        lege.setVisible(true);
        pSubpanelInformes.validate();
        pSubpanelInformes.repaint();
    }

    private boolean validaCierreCaja() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == cierrecajadiario || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veCierreCaja() {
        cajadiaria caja = new cajadiaria(factory, userlog);
        this.pSubpanelInformes.removeAll();
        caja.setBounds(0, 0, 492, 449);
        pSubpanelInformes.add(caja);
        caja.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelInformes.removeAll();
                pSubpanelInformes.validate();
                pSubpanelInformes.repaint();
            }
        });
        caja.setVisible(true);
        pSubpanelInformes.validate();
        pSubpanelInformes.repaint();
    }

    private boolean validaInfoUsers() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == registroxusuarios || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veInfoUsers() {
        registroUsuarios usuarios = new registroUsuarios(factory);
        this.pSubpanelInformes.removeAll();
        usuarios.setBounds(0, 0, 492, 449);
        pSubpanelInformes.add(usuarios);
        usuarios.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelInformes.removeAll();
                pSubpanelInformes.validate();
                pSubpanelInformes.repaint();
            }
        });
        usuarios.setVisible(true);
        pSubpanelInformes.validate();
        pSubpanelInformes.repaint();
    }

    private boolean validaListadoVenta() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == informeventas || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veListadoVentas() {
        lVentas lege = new lVentas(factory);
        this.pSubpanelInformes.removeAll();
        lege.setBounds(0, 0, 492, 449);
        pSubpanelInformes.add(lege);
        lege.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelInformes.removeAll();
                pSubpanelInformes.validate();
                pSubpanelInformes.repaint();
            }
        });
        lege.setVisible(true);
        pSubpanelInformes.validate();
        pSubpanelInformes.repaint();
    }

    private boolean validaListadoCreditos() {
        boolean tiene = false;
        for (UserPermisos up : getPermisos(userlog)) {
            if (up.getIdpermiso().getId() == informecreditos || up.getIdpermiso().getId() == 1) {
                tiene = true;
                break;
            }
        }
        return tiene;
    }

    private void veListadoCreditos() {
        lcreditos lcredit = new lcreditos(factory);
        this.pSubpanelInformes.removeAll();
        lcredit.setBounds(0, 0, 492, 449);
        pSubpanelInformes.add(lcredit);
        lcredit.bSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pSubpanelInformes.removeAll();
                pSubpanelInformes.validate();
                pSubpanelInformes.repaint();
            }
        });
        lcredit.setVisible(true);
        pSubpanelInformes.validate();
        pSubpanelInformes.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bIngresos = new javax.swing.JLabel();
        bInformeIngresos = new javax.swing.JLabel();
        bEgresos = new javax.swing.JLabel();
        bListadoEgresos = new javax.swing.JLabel();
        bCierreCaja = new javax.swing.JLabel();
        pSubpanelInformes = new javax.swing.JPanel();
        bInformeUsuario = new javax.swing.JLabel();
        bListadoVentas = new javax.swing.JLabel();
        bListadoCreditos = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(646, 449));
        setMinimumSize(new java.awt.Dimension(646, 449));
        setPreferredSize(new java.awt.Dimension(646, 449));

        bIngresos.setBackground(new java.awt.Color(255, 255, 255));
        bIngresos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bIngresos.setForeground(new java.awt.Color(50, 72, 141));
        bIngresos.setText("  Ingresos");
        bIngresos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bIngresos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bIngresos.setOpaque(true);
        bIngresos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bIngresosMouseReleased(evt);
            }
        });

        bInformeIngresos.setBackground(new java.awt.Color(255, 255, 255));
        bInformeIngresos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bInformeIngresos.setForeground(new java.awt.Color(50, 72, 141));
        bInformeIngresos.setText("  Informe de Ingresos");
        bInformeIngresos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bInformeIngresos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bInformeIngresos.setOpaque(true);
        bInformeIngresos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bInformeIngresosMouseReleased(evt);
            }
        });

        bEgresos.setBackground(new java.awt.Color(255, 255, 255));
        bEgresos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bEgresos.setForeground(new java.awt.Color(50, 72, 141));
        bEgresos.setText("  Egresos");
        bEgresos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bEgresos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bEgresos.setOpaque(true);
        bEgresos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bEgresosMouseReleased(evt);
            }
        });

        bListadoEgresos.setBackground(new java.awt.Color(255, 255, 255));
        bListadoEgresos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bListadoEgresos.setForeground(new java.awt.Color(50, 72, 141));
        bListadoEgresos.setText("  Informe de Egresos");
        bListadoEgresos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bListadoEgresos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bListadoEgresos.setOpaque(true);
        bListadoEgresos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bListadoEgresosMouseReleased(evt);
            }
        });

        bCierreCaja.setBackground(new java.awt.Color(255, 255, 255));
        bCierreCaja.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bCierreCaja.setForeground(new java.awt.Color(50, 72, 141));
        bCierreCaja.setText("  Cierre de Caja Diario");
        bCierreCaja.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bCierreCaja.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bCierreCaja.setOpaque(true);
        bCierreCaja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bCierreCajaMouseReleased(evt);
            }
        });

        pSubpanelInformes.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pSubpanelInformesLayout = new javax.swing.GroupLayout(pSubpanelInformes);
        pSubpanelInformes.setLayout(pSubpanelInformesLayout);
        pSubpanelInformesLayout.setHorizontalGroup(
            pSubpanelInformesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        pSubpanelInformesLayout.setVerticalGroup(
            pSubpanelInformesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        bInformeUsuario.setBackground(new java.awt.Color(255, 255, 255));
        bInformeUsuario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bInformeUsuario.setForeground(new java.awt.Color(50, 72, 141));
        bInformeUsuario.setText("  Registros x Usuario");
        bInformeUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bInformeUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bInformeUsuario.setOpaque(true);
        bInformeUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bInformeUsuarioMouseReleased(evt);
            }
        });

        bListadoVentas.setBackground(new java.awt.Color(255, 255, 255));
        bListadoVentas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bListadoVentas.setForeground(new java.awt.Color(50, 72, 141));
        bListadoVentas.setText("  Informe de Ventas");
        bListadoVentas.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bListadoVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bListadoVentas.setOpaque(true);
        bListadoVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bListadoVentasMouseReleased(evt);
            }
        });

        bListadoCreditos.setBackground(new java.awt.Color(255, 255, 255));
        bListadoCreditos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bListadoCreditos.setForeground(new java.awt.Color(50, 72, 141));
        bListadoCreditos.setText("  Informe de Creditos");
        bListadoCreditos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(50, 72, 141)));
        bListadoCreditos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bListadoCreditos.setOpaque(true);
        bListadoCreditos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bListadoCreditosMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bIngresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bInformeIngresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bEgresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bListadoEgresos, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(bCierreCaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bInformeUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bListadoVentas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(bListadoCreditos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pSubpanelInformes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bInformeIngresos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bEgresos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bListadoEgresos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bCierreCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bInformeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bListadoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bListadoCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 143, Short.MAX_VALUE))
            .addComponent(pSubpanelInformes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bIngresosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bIngresosMouseReleased
        if (validaIngresos()) {
            veIngresos();
            bIngresos.setForeground(Color.white);
            bIngresos.setBackground(new Color(50, 72, 141));
            bInformeIngresos.setForeground(new Color(50, 72, 141));
            bInformeIngresos.setBackground(Color.white);
            bEgresos.setForeground(new Color(50, 72, 141));
            bEgresos.setBackground(Color.white);
            bListadoEgresos.setForeground(new Color(50, 72, 141));
            bListadoEgresos.setBackground(Color.white);
            bCierreCaja.setForeground(new Color(50, 72, 141));
            bCierreCaja.setBackground(Color.white);
            bInformeUsuario.setForeground(new Color(50, 72, 141));
            bInformeUsuario.setBackground(Color.white);
            bListadoVentas.setForeground(new Color(50, 72, 141));
            bListadoVentas.setBackground(Color.white);
            bListadoCreditos.setForeground(new Color(50, 72, 141));
            bListadoCreditos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bIngresosMouseReleased

    private void bInformeIngresosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bInformeIngresosMouseReleased
        if (validaLIngresos()) {
            veListadoIngreso();
            bInformeIngresos.setForeground(Color.white);
            bInformeIngresos.setBackground(new Color(50, 72, 141));
            bIngresos.setForeground(new Color(50, 72, 141));
            bIngresos.setBackground(Color.white);
            bEgresos.setForeground(new Color(50, 72, 141));
            bEgresos.setBackground(Color.white);
            bListadoEgresos.setForeground(new Color(50, 72, 141));
            bListadoEgresos.setBackground(Color.white);
            bCierreCaja.setForeground(new Color(50, 72, 141));
            bCierreCaja.setBackground(Color.white);
            bInformeUsuario.setForeground(new Color(50, 72, 141));
            bInformeUsuario.setBackground(Color.white);
            bListadoVentas.setForeground(new Color(50, 72, 141));
            bListadoVentas.setBackground(Color.white);
            bListadoCreditos.setForeground(new Color(50, 72, 141));
            bListadoCreditos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bInformeIngresosMouseReleased

    private void bEgresosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bEgresosMouseReleased
        if (validaEgresos()) {
            veEgreso();
            bEgresos.setForeground(Color.white);
            bEgresos.setBackground(new Color(50, 72, 141));
            bIngresos.setForeground(new Color(50, 72, 141));
            bIngresos.setBackground(Color.white);
            bInformeIngresos.setForeground(new Color(50, 72, 141));
            bInformeIngresos.setBackground(Color.white);
            bListadoEgresos.setForeground(new Color(50, 72, 141));
            bListadoEgresos.setBackground(Color.white);
            bCierreCaja.setForeground(new Color(50, 72, 141));
            bCierreCaja.setBackground(Color.white);
            bInformeUsuario.setForeground(new Color(50, 72, 141));
            bInformeUsuario.setBackground(Color.white);
            bListadoVentas.setForeground(new Color(50, 72, 141));
            bListadoVentas.setBackground(Color.white);
            bListadoCreditos.setForeground(new Color(50, 72, 141));
            bListadoCreditos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bEgresosMouseReleased

    private void bListadoEgresosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bListadoEgresosMouseReleased
        if (validaLEgresos()) {
            veListadoEgresos();
            bListadoEgresos.setForeground(Color.white);
            bListadoEgresos.setBackground(new Color(50, 72, 141));
            bIngresos.setForeground(new Color(50, 72, 141));
            bIngresos.setBackground(Color.white);
            bInformeIngresos.setForeground(new Color(50, 72, 141));
            bInformeIngresos.setBackground(Color.white);
            bEgresos.setForeground(new Color(50, 72, 141));
            bEgresos.setBackground(Color.white);
            bCierreCaja.setForeground(new Color(50, 72, 141));
            bCierreCaja.setBackground(Color.white);
            bInformeUsuario.setForeground(new Color(50, 72, 141));
            bInformeUsuario.setBackground(Color.white);
            bListadoVentas.setForeground(new Color(50, 72, 141));
            bListadoVentas.setBackground(Color.white);
            bListadoCreditos.setForeground(new Color(50, 72, 141));
            bListadoCreditos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bListadoEgresosMouseReleased

    private void bCierreCajaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bCierreCajaMouseReleased
        if (validaCierreCaja()) {
            veCierreCaja();
            bCierreCaja.setForeground(Color.white);
            bCierreCaja.setBackground(new Color(50, 72, 141));
            bIngresos.setForeground(new Color(50, 72, 141));
            bIngresos.setBackground(Color.white);
            bInformeIngresos.setForeground(new Color(50, 72, 141));
            bInformeIngresos.setBackground(Color.white);
            bEgresos.setForeground(new Color(50, 72, 141));
            bEgresos.setBackground(Color.white);
            bListadoEgresos.setForeground(new Color(50, 72, 141));
            bListadoEgresos.setBackground(Color.white);
            bInformeUsuario.setForeground(new Color(50, 72, 141));
            bInformeUsuario.setBackground(Color.white);
            bListadoVentas.setForeground(new Color(50, 72, 141));
            bListadoVentas.setBackground(Color.white);
            bListadoCreditos.setForeground(new Color(50, 72, 141));
            bListadoCreditos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bCierreCajaMouseReleased

    private void bInformeUsuarioMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bInformeUsuarioMouseReleased
        if (validaInfoUsers()) {
            veInfoUsers();
            bInformeUsuario.setForeground(Color.white);
            bInformeUsuario.setBackground(new Color(50, 72, 141));
            bInformeIngresos.setForeground(new Color(50, 72, 141));
            bInformeIngresos.setBackground(Color.white);
            bEgresos.setForeground(new Color(50, 72, 141));
            bEgresos.setBackground(Color.white);
            bListadoEgresos.setForeground(new Color(50, 72, 141));
            bListadoEgresos.setBackground(Color.white);
            bCierreCaja.setForeground(new Color(50, 72, 141));
            bCierreCaja.setBackground(Color.white);
            bIngresos.setForeground(new Color(50, 72, 141));
            bIngresos.setBackground(Color.white);
            bListadoVentas.setForeground(new Color(50, 72, 141));
            bListadoVentas.setBackground(Color.white);
            bListadoCreditos.setForeground(new Color(50, 72, 141));
            bListadoCreditos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bInformeUsuarioMouseReleased

    private void bListadoVentasMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bListadoVentasMouseReleased
        if (validaListadoVenta()) {
            veListadoVentas();
            bListadoVentas.setForeground(Color.white);
            bListadoVentas.setBackground(new Color(50, 72, 141));
            bInformeIngresos.setForeground(new Color(50, 72, 141));
            bInformeIngresos.setBackground(Color.white);
            bEgresos.setForeground(new Color(50, 72, 141));
            bEgresos.setBackground(Color.white);
            bListadoEgresos.setForeground(new Color(50, 72, 141));
            bListadoEgresos.setBackground(Color.white);
            bCierreCaja.setForeground(new Color(50, 72, 141));
            bCierreCaja.setBackground(Color.white);
            bIngresos.setForeground(new Color(50, 72, 141));
            bIngresos.setBackground(Color.white);
            bInformeUsuario.setForeground(new Color(50, 72, 141));
            bInformeUsuario.setBackground(Color.white);
            bListadoCreditos.setForeground(new Color(50, 72, 141));
            bListadoCreditos.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bListadoVentasMouseReleased

    private void bListadoCreditosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bListadoCreditosMouseReleased
        if (validaListadoCreditos()) {
            veListadoCreditos();
            bListadoCreditos.setForeground(Color.white);
            bListadoCreditos.setBackground(new Color(50, 72, 141));
            bInformeIngresos.setForeground(new Color(50, 72, 141));
            bInformeIngresos.setBackground(Color.white);
            bEgresos.setForeground(new Color(50, 72, 141));
            bEgresos.setBackground(Color.white);
            bListadoEgresos.setForeground(new Color(50, 72, 141));
            bListadoEgresos.setBackground(Color.white);
            bCierreCaja.setForeground(new Color(50, 72, 141));
            bCierreCaja.setBackground(Color.white);
            bIngresos.setForeground(new Color(50, 72, 141));
            bIngresos.setBackground(Color.white);
            bInformeUsuario.setForeground(new Color(50, 72, 141));
            bInformeUsuario.setBackground(Color.white);
            bListadoVentas.setForeground(new Color(50, 72, 141));
            bListadoVentas.setBackground(Color.white);
        } else {
            JOptionPane.showMessageDialog(null, "No tiene privilegios para acceder a esta opción", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bListadoCreditosMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bCierreCaja;
    private javax.swing.JLabel bEgresos;
    private javax.swing.JLabel bInformeIngresos;
    private javax.swing.JLabel bInformeUsuario;
    private javax.swing.JLabel bIngresos;
    private javax.swing.JLabel bListadoCreditos;
    private javax.swing.JLabel bListadoEgresos;
    private javax.swing.JLabel bListadoVentas;
    private javax.swing.JPanel pSubpanelInformes;
    // End of variables declaration//GEN-END:variables
}
