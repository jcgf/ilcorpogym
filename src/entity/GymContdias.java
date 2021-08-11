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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "gym_contdias")
@NamedQueries({
    @NamedQuery(name = "GymContdias.findAll", query = "SELECT g FROM GymContdias g")})
public class GymContdias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fechadia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadia;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @JoinColumn(name = "idasignacion", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private GymAsignados idasignacion;
    @JoinColumn(name = "idusuario", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private GymUsuarios idusuario;

    public GymContdias() {
    }

    public GymContdias(Integer id) {
        this.id = id;
    }

    public GymContdias(Integer id, Date fechadia, int estado) {
        this.id = id;
        this.fechadia = fechadia;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechadia() {
        return fechadia;
    }

    public void setFechadia(Date fechadia) {
        this.fechadia = fechadia;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public GymAsignados getIdasignacion() {
        return idasignacion;
    }

    public void setIdasignacion(GymAsignados idasignacion) {
        this.idasignacion = idasignacion;
    }

    public GymUsuarios getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(GymUsuarios idusuario) {
        this.idusuario = idusuario;
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
        if (!(object instanceof GymContdias)) {
            return false;
        }
        GymContdias other = (GymContdias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GymContdias[ id=" + id + " ]";
    }
    
}
