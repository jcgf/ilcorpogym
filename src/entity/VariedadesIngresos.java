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
@Table(name = "variedades_ingresos")
@NamedQueries({
    @NamedQuery(name = "VariedadesIngresos.findAll", query = "SELECT v FROM VariedadesIngresos v")})
public class VariedadesIngresos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "codigoingreso")
    private int codigoingreso;
    @Lob
    @Column(name = "codcliente")
    private String codcliente;
    @Basic(optional = false)
    @Column(name = "fechaingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaingreso;
    @Basic(optional = false)
    @Column(name = "fechaingresotiempo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaingresotiempo;
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
    @JoinColumn(name = "idusuario", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private UserLog idusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idingreso"/*, fetch = FetchType.LAZY*/)
    private List<IngresoItem> ingresoItemList;

    public VariedadesIngresos() {
    }

    public VariedadesIngresos(Integer id) {
        this.id = id;
    }

    public VariedadesIngresos(Integer id, int codigoingreso, Date fechaingreso, Date fechaingresotiempo, float subtotal, float valoriva, float valortotal, int estado) {
        this.id = id;
        this.codigoingreso = codigoingreso;
        this.fechaingreso = fechaingreso;
        this.fechaingresotiempo = fechaingresotiempo;
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

    public int getCodigoingreso() {
        return codigoingreso;
    }

    public void setCodigoingreso(int codigoingreso) {
        this.codigoingreso = codigoingreso;
    }

    public String getCodcliente() {
        return codcliente;
    }

    public void setCodcliente(String codcliente) {
        this.codcliente = codcliente;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Date getFechaingresotiempo() {
        return fechaingresotiempo;
    }

    public void setFechaingresotiempo(Date fechaingresotiempo) {
        this.fechaingresotiempo = fechaingresotiempo;
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

    public UserLog getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(UserLog idusuario) {
        this.idusuario = idusuario;
    }

    public List<IngresoItem> getIngresoItemList() {
        return ingresoItemList;
    }

    public void setIngresoItemList(List<IngresoItem> ingresoItemList) {
        this.ingresoItemList = ingresoItemList;
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
        if (!(object instanceof VariedadesIngresos)) {
            return false;
        }
        VariedadesIngresos other = (VariedadesIngresos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VariedadesIngresos[ id=" + id + " ]";
    }
    
}
