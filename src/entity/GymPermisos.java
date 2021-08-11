/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Juan
 */
@Entity
@Table(name = "gym_permisos")
@NamedQueries({
    @NamedQuery(name = "GymPermisos.findAll", query = "SELECT g FROM GymPermisos g")})
public class GymPermisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "nombrepermiso")
    private String nombrepermiso;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idpermiso", fetch = FetchType.LAZY)
    private List<UserPermisos> userPermisosList;

    public GymPermisos() {
    }

    public GymPermisos(Integer id) {
        this.id = id;
    }

    public GymPermisos(Integer id, String nombrepermiso, int estado) {
        this.id = id;
        this.nombrepermiso = nombrepermiso;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombrepermiso() {
        return nombrepermiso;
    }

    public void setNombrepermiso(String nombrepermiso) {
        this.nombrepermiso = nombrepermiso;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<UserPermisos> getUserPermisosList() {
        return userPermisosList;
    }

    public void setUserPermisosList(List<UserPermisos> userPermisosList) {
        this.userPermisosList = userPermisosList;
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
        if (!(object instanceof GymPermisos)) {
            return false;
        }
        GymPermisos other = (GymPermisos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GymPermisos[ id=" + id + " ]";
    }
    
}
