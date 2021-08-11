/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bohiogym.paneles.panelarticulos;

import entity.ArticulosCantidades;
import entity.VariedadesArticulos;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import jpa.ArticulosCantidadesJpaController;
import jpa.VariedadesArticulosJpaController;

/**
 *
 * @author Alvarez
 */
public class gArticulos extends javax.swing.JPanel {

    private final EntityManagerFactory factory;
    VariedadesArticulos articulo = null;
    VariedadesArticulosJpaController gajc = null;
    ArticulosCantidades articulosCantidades = null;
    ArticulosCantidadesJpaController acjc = null;

    /**
     * Creates new form gArticulos
     */
    public gArticulos(EntityManagerFactory factory) {
        initComponents();
        this.factory = factory;
        bGuardar.setEnabled(false);
        binactivar.setEnabled(false);
    }

    private List<VariedadesArticulos> obtenerArticulo(String codigo) {
        EntityManager em = gajc.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM VariedadesArticulos p WHERE p.codigoarticulo=:codigo AND p.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("codigo", codigo)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void cargarArticulo() {
        if (gajc == null) {
            gajc = new VariedadesArticulosJpaController(factory);
        }
        if (jTextField1.getText() == null || jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para realizar la busqueda");
        } else {
            if (obtenerArticulo(jTextField1.getText()).size() > 0) {
                DecimalFormat formato = new DecimalFormat("#,###.00");
                articulo = obtenerArticulo(jTextField1.getText()).get(0);
                jTextField2.setText(articulo.getNombrearticulo());
                jTextField3.setText(formato.format(articulo.getValorcompra()));
                if (articulo.getCiva() == 1) {
                    jCheckBox1.setSelected(true);
                } else {
                    jCheckBox1.setSelected(false);
                }
                jTextField5.setText(formato.format(articulo.getIvacompra()));
                calculatotalcompra();
                jTextField4.setText(formato.format(articulo.getValorventa()));
                if (articulo.getViva() == 1) {
                    jCheckBox2.setSelected(true);
                } else {
                    jCheckBox2.setSelected(false);
                }
                jTextField6.setText(formato.format(articulo.getIvaventa()));
                calculatotalventa();
                jTextField7.setText(String.valueOf(articulo.getCantidadinicial()));
                jTextArea1.setText(articulo.getDescripcion());
                bGuardar.setEnabled(true);
                binactivar.setEnabled(true);
            } else {
                String mensaje = "El articulo no existe, ¿desea crearlo?";
                int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Crear Articulo", JOptionPane.YES_NO_OPTION);
                if (entrada == 0) {
                    String filtro = jTextField1.getText();
                    jTextField2.requestFocus();
                    bGuardar.setEnabled(true);
                    articulo = null;
                    limpiar();
                    jTextField1.setText(filtro);
                } else {
                    limpiar();
                    bGuardar.setEnabled(false);
                    binactivar.setEnabled(false);
                }
            }
        }
    }

    private void inactivarArticulo() {
        String mensaje = "Esta a punto de inactivar el articulo seleccionado.\n¿Esta seguro de desactivar el articulo? ";
        int entrada = JOptionPane.showConfirmDialog(null, mensaje, "Desactivar Articulo", JOptionPane.YES_NO_OPTION);
        if (entrada == 0) {
            if (gajc == null) {
                gajc = new VariedadesArticulosJpaController(factory);
            }
            if (acjc == null) {
                acjc = new ArticulosCantidadesJpaController(factory);
            }
            try {
                articulo.setEstado(0);
                gajc.edit(articulo);
                JOptionPane.showMessageDialog(null, "Articulo inactivado exitosamente");
                limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al inactivar el articulo: " + ex.getMessage(), gArticulos.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void calculatotalcompra() {
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String vsubtotal = jTextField3.getText().replace(".", "");
        String viva = jTextField5.getText().replace(".", "");
        float subtutal = Float.parseFloat(vsubtotal.replace(",", "."));
        float ivasindivision = Float.parseFloat(viva.replace(",", "."));
        float iva = Float.parseFloat(viva.replace(",", ".")) / 100;
        float total = subtutal + (subtutal * iva);
        jTextField3.setText(formato.format(subtutal));
        jTextField5.setText(formato.format(ivasindivision));
        jTextField8.setText(formato.format(total));
    }

    private void calculatotalventa() {
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String vsubtotal = jTextField4.getText().replace(".", "");
        String viva = jTextField6.getText().replace(".", "");
        float subtutal = Float.parseFloat(vsubtotal.replace(",", "."));
        float ivasindivision = Float.parseFloat(viva.replace(",", "."));
        float iva = Float.parseFloat(viva.replace(",", ".")) / 100;
        float total = subtutal + (subtutal * iva);
        jTextField4.setText(formato.format(subtutal));
        jTextField6.setText(formato.format(ivasindivision));
        jTextField9.setText(formato.format(total));
    }

    private void calcularsubtotalcompra() {
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String vtotal = jTextField8.getText().replace(".", "");
        String viva = jTextField5.getText().replace(".", "");
        float total = Float.parseFloat(vtotal.replace(",", "."));
        float ivasindivision = Float.parseFloat(viva.replace(",", "."));
        float iva = Float.parseFloat(viva.replace(",", ".")) / 100;
        float subtotal = total / (1 + (iva));
        String parcial = formato.format(subtotal);
        jTextField3.setText(parcial);
        jTextField5.setText(formato.format(ivasindivision));
        jTextField8.setText(formato.format(total));
    }

    private void calcularsubtotalventa() {
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String vtotal = jTextField9.getText().replace(".", "");
        String viva = jTextField6.getText().replace(".", "");
        float total = Float.parseFloat(vtotal.replace(",", "."));
        float ivasindivision = Float.parseFloat(viva.replace(",", "."));
        float iva = Float.parseFloat(viva.replace(",", ".")) / 100;
        float subtotal = total / (1 + (iva));
        String parcial = formato.format(subtotal);
        jTextField4.setText(parcial);
        jTextField6.setText(formato.format(ivasindivision));
        jTextField9.setText(formato.format(total));
    }

    private void calcularivacompra() {
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String vtotal = jTextField8.getText().replace(".", "");
        String vsubtotal = jTextField3.getText().replace(".", "");
        float total = Float.parseFloat(vtotal.replace(",", "."));
        float subtotal = Float.parseFloat(vsubtotal.replace(",", "."));
        float iva = (((total / subtotal) - 1) * 100);
        String parcial = String.valueOf(formato.format(iva));
        jTextField5.setText(parcial);
        jTextField3.setText(formato.format(subtotal));
        jTextField8.setText(formato.format(total));
    }

    private void calcularivaventa() {
        DecimalFormat formato = new DecimalFormat("#,###.00");
        String vtotal = jTextField9.getText().replace(".", "");
        String vsubtotal = jTextField4.getText().replace(".", "");
        float total = Float.parseFloat(vtotal.replace(",", "."));
        float subtotal = Float.parseFloat(vsubtotal.replace(",", "."));
        float iva = (((total / subtotal) - 1) * 100);
        String parcial = String.valueOf(formato.format(iva));
        jTextField6.setText(parcial);
        jTextField4.setText(formato.format(subtotal));
        jTextField9.setText(formato.format(total));
    }

    private void guardaArticulo() {
        if (gajc == null) {
            gajc = new VariedadesArticulosJpaController(factory);
        }
        if (acjc == null) {
            acjc = new ArticulosCantidadesJpaController(factory);
        }
        if (articulo == null) {
            if (jTextField1.getText() == null || jTextField1.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para el articulo");
            } else if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para el articulo");
            } else {
                articulo = new VariedadesArticulos();
                articulo.setCodigoarticulo(jTextField1.getText());
                articulo.setNombrearticulo(jTextField2.getText().toUpperCase());
                String v1 = jTextField3.getText().replace(".", "");
                float valorcompra = Float.parseFloat(v1.replace(",", "."));
                articulo.setValorcompra(valorcompra);
                if (jCheckBox1.isSelected()) {
                    articulo.setCiva(1);
                } else {
                    articulo.setCiva(0);
                }
                String v2 = jTextField5.getText().replace(".", "");
                float ivacompra = Float.parseFloat(v2.replace(",", "."));
                articulo.setIvacompra(ivacompra);
                String v3 = jTextField4.getText().replace(".", "");
                float valorventa = Float.parseFloat(v3.replace(",", "."));
                articulo.setValorventa(valorventa);
                if (jCheckBox2.isSelected()) {
                    articulo.setViva(1);
                } else {
                    articulo.setViva(0);
                }
                String v4 = jTextField6.getText().replace(".", "");
                float ivaventa = Float.parseFloat(v4.replace(",", "."));
                articulo.setIvaventa(ivaventa);
                articulo.setCantidadinicial(Integer.parseInt(jTextField7.getText()));
                articulo.setDescripcion(jTextArea1.getText().toUpperCase());
                articulo.setEstado(1);
                gajc.create(articulo);
                articulosCantidades = new ArticulosCantidades();
                articulosCantidades.setIdarticulo(articulo);
                articulosCantidades.setCantidad(articulo.getCantidadinicial());
                articulosCantidades.setEstado(1);
                acjc.create(articulosCantidades);
                JOptionPane.showMessageDialog(null, "Articulo creado exitosamente");
                limpiar();
                jTextField1.requestFocus();
            }
        } else {
            try {
                if (jTextField1.getText() == null || jTextField1.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para el articulo");
                } else if (jTextField2.getText() == null || jTextField2.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un nombre para el articulo");
                } else {
                    articulo.setCodigoarticulo(jTextField1.getText());
                    articulo.setNombrearticulo(jTextField2.getText().toUpperCase());
                    String v1 = jTextField3.getText().replace(".", "");
                    float valorcompra = Float.parseFloat(v1.replace(",", "."));
                    articulo.setValorcompra(valorcompra);
                    if (jCheckBox1.isSelected()) {
                        articulo.setCiva(1);
                    } else {
                        articulo.setCiva(0);
                    }
                    String v2 = jTextField5.getText().replace(".", "");
                    float ivacompra = Float.parseFloat(v2.replace(",", "."));
                    articulo.setIvacompra(ivacompra);
                    String v3 = jTextField4.getText().replace(".", "");
                    float valorventa = Float.parseFloat(v3.replace(",", "."));
                    articulo.setValorventa(valorventa);
                    if (jCheckBox2.isSelected()) {
                        articulo.setViva(1);
                    } else {
                        articulo.setViva(0);
                    }
                    String v4 = jTextField6.getText().replace(".", "");
                    float ivaventa = Float.parseFloat(v4.replace(",", "."));
                    articulo.setIvaventa(ivaventa);
                    articulo.setCantidadinicial(Integer.parseInt(jTextField7.getText()));
                    articulo.setDescripcion(jTextArea1.getText().toUpperCase());
                    articulo.setEstado(1);
                    gajc.edit(articulo);
                    articulosCantidades = searchCantidadinicial(articulo).get(0);
                    articulosCantidades.setCantidad(articulo.getCantidadinicial());
                    articulosCantidades.setEstado(1);
                    acjc.edit(articulosCantidades);
                    JOptionPane.showMessageDialog(null, "Articulo actualizado exitosamente");
                    limpiar();
                    jTextField1.requestFocus();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al actualziar articulo: " + e.getMessage(), gArticulos.class.getName(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private List<ArticulosCantidades> searchCantidadinicial(VariedadesArticulos arti) {
        EntityManager em = acjc.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM ArticulosCantidades a WHERE a.idarticulo=:arti AND a.estado='1' ORDER BY a.id ASC")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("arti", arti)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    private void limpiar() {
        jTextField1.setText(null);
        jTextField2.setText(null);
        jTextField3.setText("0");
        jTextField4.setText("0");
        jTextField5.setText("0");
        jTextField6.setText("0");
        jTextField7.setText("0");
        jTextArea1.setText(null);
        jTextField8.setText("0");
        jTextField9.setText("0");
        jCheckBox1.setSelected(true);
        jCheckBox2.setSelected(true);
        articulo = null;
        articulosCantidades = null;
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
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        bBuscar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        bSalir = new javax.swing.JButton();
        binactivar = new javax.swing.JButton();
        bGuardar = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(492, 449));
        setMinimumSize(new java.awt.Dimension(492, 449));

        jSeparator1.setForeground(new java.awt.Color(50, 72, 141));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 72, 141));
        jLabel1.setText("Código Articulo:");

        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        bBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/search24.png"))); // NOI18N
        bBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bBuscarMouseReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(50, 72, 141));
        jLabel2.setText("Nombre Articulo:");

        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 72, 141));
        jLabel3.setText("Valor Unitario Compra:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 102, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("$");

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(51, 102, 255));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.setText("0");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(50, 72, 141));
        jLabel5.setText("Valor Unitario Venta:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 102, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("$");

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(51, 102, 255));
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.setText("0");
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField4KeyPressed(evt);
            }
        });

        bSalir.setBackground(new java.awt.Color(50, 72, 141));
        bSalir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bSalir.setForeground(new java.awt.Color(255, 255, 255));
        bSalir.setText("Atras");
        bSalir.setContentAreaFilled(false);
        bSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bSalir.setFocusable(false);
        bSalir.setOpaque(true);

        binactivar.setBackground(new java.awt.Color(50, 72, 141));
        binactivar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        binactivar.setForeground(new java.awt.Color(255, 255, 255));
        binactivar.setText("Inactivar");
        binactivar.setContentAreaFilled(false);
        binactivar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        binactivar.setFocusable(false);
        binactivar.setOpaque(true);
        binactivar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                binactivarMouseReleased(evt);
            }
        });

        bGuardar.setBackground(new java.awt.Color(50, 72, 141));
        bGuardar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bGuardar.setForeground(new java.awt.Color(255, 255, 255));
        bGuardar.setText("Guardar");
        bGuardar.setContentAreaFilled(false);
        bGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bGuardar.setFocusable(false);
        bGuardar.setOpaque(true);
        bGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bGuardarMouseReleased(evt);
            }
        });

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("IVA");
        jCheckBox1.setFocusable(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setSelected(true);
        jCheckBox2.setText("IVA");
        jCheckBox2.setFocusable(false);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jTextField5.setText("0");
        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField5FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField5KeyPressed(evt);
            }
        });

        jTextField6.setText("0");
        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField6FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField6KeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 102, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("%");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 102, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("%");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(50, 72, 141));
        jLabel11.setText("Descripcion:");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextArea1FocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(50, 72, 141));
        jLabel12.setText("Cantidad inicial:");

        jTextField7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField7.setForeground(new java.awt.Color(51, 102, 255));
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField7.setText("0");
        jTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField7FocusGained(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField7KeyPressed(evt);
            }
        });

        jTextField8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField8.setForeground(new java.awt.Color(51, 102, 255));
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField8.setText("0");
        jTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField8FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField8FocusLost(evt);
            }
        });
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField8KeyPressed(evt);
            }
        });

        jTextField9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField9.setForeground(new java.awt.Color(51, 102, 255));
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.setText("0");
        jTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField9FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField9FocusLost(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField9KeyPressed(evt);
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
                    .addComponent(jTextField2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField8))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bBuscar))
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel12)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField9)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1)
                            .addComponent(bBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField3)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                    .addComponent(jLabel9)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField8))
                                .addGap(0, 2, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(bGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(binactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void binactivarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_binactivarMouseReleased
        if (binactivar.isEnabled()) {
            inactivarArticulo();
        }
    }//GEN-LAST:event_binactivarMouseReleased

    private void bGuardarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bGuardarMouseReleased
        if (bGuardar.isEnabled()) {
            guardaArticulo();
        }
    }//GEN-LAST:event_bGuardarMouseReleased

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            jTextField5.setEditable(true);
            jTextField5.requestFocus();
        } else {
            jTextField5.setEditable(false);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusGained
        jTextField5.selectAll();
    }//GEN-LAST:event_jTextField5FocusGained

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if (jCheckBox2.isSelected()) {
            jTextField6.setEditable(true);
            jTextField6.requestFocus();
        } else {
            jTextField6.setEditable(false);
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jTextField6FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusGained
        jTextField6.selectAll();
    }//GEN-LAST:event_jTextField6FocusGained

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField3.requestFocus();
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jCheckBox1.isSelected()) {
                jTextField5.requestFocus();
            } else {
                jTextField4.requestFocus();
            }
        }
    }//GEN-LAST:event_jTextField3KeyPressed

    private void jTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField8.requestFocus();
        }
    }//GEN-LAST:event_jTextField5KeyPressed

    private void jTextField4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (jCheckBox2.isSelected()) {
                jTextField6.requestFocus();
            } else {
                jTextField7.requestFocus();
            }
        }
    }//GEN-LAST:event_jTextField4KeyPressed

    private void jTextField6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField9.requestFocus();
        }
    }//GEN-LAST:event_jTextField6KeyPressed

    private void jTextField7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextArea1.requestFocus();
        }
    }//GEN-LAST:event_jTextField7KeyPressed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.selectAll();
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        jTextField2.selectAll();
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
        jTextField3.selectAll();
    }//GEN-LAST:event_jTextField3FocusGained

    private void jTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusGained
        jTextField4.selectAll();
    }//GEN-LAST:event_jTextField4FocusGained

    private void jTextField7FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField7FocusGained
        jTextField7.selectAll();
    }//GEN-LAST:event_jTextField7FocusGained

    private void jTextArea1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea1FocusGained
        jTextArea1.selectAll();
    }//GEN-LAST:event_jTextArea1FocusGained

    private void bBuscarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseReleased
        cargarArticulo();
    }//GEN-LAST:event_bBuscarMouseReleased

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarArticulo();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
        if (jTextField3.getText().equals("0") && !jTextField8.getText().equals("0")) {
            calcularsubtotalcompra();
        } else {
            calculatotalcompra();
        }
    }//GEN-LAST:event_jTextField5FocusLost

    private void jTextField8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField8FocusGained
        jTextField8.selectAll();
    }//GEN-LAST:event_jTextField8FocusGained

    private void jTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField8FocusLost
        if (jTextField3.getText().equals("0") && !jTextField5.getText().equals("0")) {
            calcularsubtotalcompra();
        } else if (jTextField5.getText().equals("0") && !jTextField3.getText().equals("0")) {
            calcularivacompra();
        }
    }//GEN-LAST:event_jTextField8FocusLost

    private void jTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusLost
        if (jTextField4.getText().equals("0") && !jTextField9.getText().equals("0")) {
            calcularsubtotalventa();
        } else {
            calculatotalventa();
        }
    }//GEN-LAST:event_jTextField6FocusLost

    private void jTextField9FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusGained
        jTextField9.selectAll();
    }//GEN-LAST:event_jTextField9FocusGained

    private void jTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusLost
        if (jTextField4.getText().equals("0") && !jTextField6.getText().equals("0")) {
            calcularsubtotalventa();
        } else if (jTextField6.getText().equals("0") && !jTextField4.getText().equals("0")) {
            calcularivaventa();
        }
    }//GEN-LAST:event_jTextField9FocusLost

    private void jTextField8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField4.requestFocus();
        }
    }//GEN-LAST:event_jTextField8KeyPressed

    private void jTextField9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField7.requestFocus();
        }
    }//GEN-LAST:event_jTextField9KeyPressed

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        if (jTextField5.getText().equals("0") && !jTextField8.getText().equals("0")) {
            calcularivacompra();
        } else if (!jTextField5.getText().equals("0") && jTextField8.getText().equals("0")) {
            calculatotalcompra();
        }
    }//GEN-LAST:event_jTextField3FocusLost

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
        if (jTextField6.getText().equals("0") && !jTextField9.getText().equals("0")) {
            calcularivaventa();
        } else if (!jTextField6.getText().equals("0") && jTextField9.getText().equals("0")) {
            calculatotalventa();
        }
    }//GEN-LAST:event_jTextField4FocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bBuscar;
    public javax.swing.JButton bGuardar;
    public javax.swing.JButton bSalir;
    public javax.swing.JButton binactivar;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    public javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
