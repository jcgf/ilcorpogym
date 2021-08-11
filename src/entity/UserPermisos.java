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
@Table(name = "user_permisos")
@NamedQueries({
    @NamedQuery(name = "UserPermisos.findAll", query = "SELECT u FROM UserPermisos u")})
public class UserPermisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @JoinColumn(name = "iduser", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private UserLog iduser;
    @JoinColumn(name = "idpermiso", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private GymPermisos idpermiso;

    public UserPermisos() {
    }

    public UserPermisos(Integer id) {
        this.id = id;
    }

    public UserPermisos(Integer id, int estado) {
        this.id = id;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public UserLog getIduser() {
        return iduser;
    }

    public void setIduser(UserLog iduser) {
        this.iduser = iduser;
    }

    public GymPermisos getIdpermiso() {
        return idpermiso;
    }

    public void setIdpermiso(GymPermisos idpermiso) {
        this.idpermiso = idpermiso;
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
        if (!(object instanceof UserPermisos)) {
            return false;
        }
        UserPermisos other = (UserPermisos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UserPermisos[ id=" + id + " ]";
    }
    
}
