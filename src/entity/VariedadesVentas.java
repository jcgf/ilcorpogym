/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Juan
 */
@Entity
@Table(name = "variedades_ventas")
@NamedQueries({
    @NamedQuery(name = "VariedadesVentas.findAll", query = "SELECT v FROM VariedadesVentas v")})
public class VariedadesVentas implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idventa", fetch = FetchType.LAZY)
    private List<VariedadesAbono> variedadesAbonoList;

    @Basic(optional = false)
    @Column(name = "efectivo")
    private int efectivo;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "codigoventa")
    private int codigoventa;
    @Lob
    @Column(name = "codcliente")
    private String codcliente;
    @Basic(optional = false)
    @Column(name = "fechaventa")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaventa;
    @Basic(optional = false)
    @Column(name = "fechaventatiempo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaventatiempo;
    @Basic(optional = false)
    @Column(name = "subtotal")
    private float subtotal;
    @Basic(optional = false)
    @Column(name = "valoriva")
    private float valoriva;
    @Basic(optional = false)
    @Column(name = "valortotal")
    private float valortotal;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idventa"/*, fetch = FetchType.LAZY*/)
    private List<VentasArticulos> ventasArticulosList;
    @JoinColumn(name = "idusuario", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private UserLog idusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idventa"/*, fetch = FetchType.LAZY*/)
    private List<VentasServicios> ventasServiciosList;

    public VariedadesVentas() {
    }

    public VariedadesVentas(Integer id) {
        this.id = id;
    }

    public VariedadesVentas(Integer id, int codigoventa, Date fechaventa, Date fechaventatiempo, float subtotal, float valoriva, float valortotal, int estado) {
        this.id = id;
        this.codigoventa = codigoventa;
        this.fechaventa = fechaventa;
        this.fechaventatiempo = fechaventatiempo;
        this.subtotal = subtotal;
        this.valoriva = valoriva;
        this.valortotal = valortotal;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCodigoventa() {
        return codigoventa;
    }

    public void setCodigoventa(int codigoventa) {
        this.codigoventa = codigoventa;
    }

    public String getCodcliente() {
        return codcliente;
    }

    public void setCodcliente(String codcliente) {
        this.codcliente = codcliente;
    }

    public Date getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(Date fechaventa) {
        this.fechaventa = fechaventa;
    }

    public Date getFechaventatiempo() {
        return fechaventatiempo;
    }

    public void setFechaventatiempo(Date fechaventatiempo) {
        this.fechaventatiempo = fechaventatiempo;
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

    public float getValortotal() {
        return valortotal;
    }

    public void setValortotal(float valortotal) {
        this.valortotal = valortotal;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<VentasArticulos> getVentasArticulosList() {
        return ventasArticulosList;
    }

    public void setVentasArticulosList(List<VentasArticulos> ventasArticulosList) {
        this.ventasArticulosList = ventasArticulosList;
    }

    public UserLog getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(UserLog idusuario) {
        this.idusuario = idusuario;
    }

    public List<VentasServicios> getVentasServiciosList() {
        return ventasServiciosList;
    }

    public void setVentasServiciosList(List<VentasServicios> ventasServiciosList) {
        this.ventasServiciosList = ventasServiciosList;
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
        if (!(object instanceof VariedadesVentas)) {
            return false;
        }
        VariedadesVentas other = (VariedadesVentas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VariedadesVentas[ id=" + id + " ]";
    }

    public int getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(int efectivo) {
        this.efectivo = efectivo;
    }

    public List<VariedadesAbono> getVariedadesAbonoList() {
        return variedadesAbonoList;
    }

    public void setVariedadesAbonoList(List<VariedadesAbono> variedadesAbonoList) {
        this.variedadesAbonoList = variedadesAbonoList;
    }
    
}
