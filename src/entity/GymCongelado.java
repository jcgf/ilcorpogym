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
@Table(name = "gym_congelado")
@NamedQueries({
    @NamedQuery(name = "GymCongelado.findAll", query = "SELECT g FROM GymCongelado g")})
public class GymCongelado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fechacongela")
    @Temporal(TemporalType.DATE)
    private Date fechacongela;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @JoinColumn(name = "idasignacion", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GymAsignados idasignacion;

    public GymCongelado() {
    }

    public GymCongelado(Integer id) {
        this.id = id;
    }

    public GymCongelado(Integer id, Date fechacongela, int estado) {
        this.id = id;
        this.fechacongela = fechacongela;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechacongela() {
        return fechacongela;
    }

    public void setFechacongela(Date fechacongela) {
        this.fechacongela = fechacongela;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GymCongelado)) {
            return false;
        }
        GymCongelado other = (GymCongelado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GymCongelado[ id=" + id + " ]";
    }
    
}
