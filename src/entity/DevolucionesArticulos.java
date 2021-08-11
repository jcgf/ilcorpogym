/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Juan
 */
@Entity
@Table(name = "devoluciones_articulos")
@NamedQueries({
    @NamedQuery(name = "DevolucionesArticulos.findAll", query = "SELECT d FROM DevolucionesArticulos d")})
public class DevolucionesArticulos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "valorindividual")
    private float valorindividual;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @Column(name = "subtotal")
    private float subtotal;
    @Basic(optional = false)
    @Column(name = "valoriva")
    private float valoriva;
    @Basic(optional = false)
    @Column(name = "total")
    private float total;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @JoinColumn(name = "iddevolucion", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private VariedadesDevoluciones iddevolucion;
    @JoinColumn(name = "idarticulo", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private VariedadesArticulos idarticulo;

    public DevolucionesArticulos() {
    }

    public DevolucionesArticulos(Integer id) {
        this.id = id;
    }

    public DevolucionesArticulos(Integer id, float valorindividual, int cantidad, float subtotal, float valoriva, float total, int estado) {
        this.id = id;
        this.valorindividual = valorindividual;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.valoriva = valoriva;
        this.total = total;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getValorindividual() {
        return valorindividual;
    }

    public void setValorindividual(float valorindividual) {
        this.valorindividual = valorindividual;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getValoriva() {
        return valoriva;
    }

    public void setValoriva(float valoriva) {
        this.valoriva = valoriva;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public VariedadesDevoluciones getIddevolucion() {
        return iddevolucion;
    }

    public void setIddevolucion(VariedadesDevoluciones iddevolucion) {
        this.iddevolucion = iddevolucion;
    }

    public VariedadesArticulos getIdarticulo() {
        return idarticulo;
    }

    public void setIdarticulo(VariedadesArticulos idarticulo) {
        this.idarticulo = idarticulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DevolucionesArticulos)) {
            return false;
        }
        DevolucionesArticulos other = (DevolucionesArticulos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DevolucionesArticulos[ id=" + id + " ]";
    }
    
}
