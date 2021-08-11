/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Juan
 */
@Entity
@Table(name = "variedades_caja")
@NamedQueries({
    @NamedQuery(name = "VariedadesCaja.findAll", query = "SELECT v FROM VariedadesCaja v")})
public class VariedadesCaja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "valorinicial")
    private float valorinicial;
    @Basic(optional = false)
    @Column(name = "estadofinal")
    private float estadofinal;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;

    public VariedadesCaja() {
    }

    public VariedadesCaja(Integer id) {
        this.id = id;
    }

    public VariedadesCaja(Integer id, Date fecha, float valorinicial, float estadofinal, int estado) {
        this.id = id;
        this.fecha = fecha;
        this.valorinicial = valorinicial;
        this.estadofinal = estadofinal;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getValorinicial() {
        return valorinicial;
    }

    public void setValorinicial(float valorinicial) {
        this.valorinicial = valorinicial;
    }

    public float getEstadofinal() {
        return estadofinal;
    }

    public void setEstadofinal(float estadofinal) {
        this.estadofinal = estadofinal;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
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
        if (!(object instanceof VariedadesCaja)) {
            return false;
        }
        VariedadesCaja other = (VariedadesCaja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VariedadesCaja[ id=" + id + " ]";
    }
    
}
