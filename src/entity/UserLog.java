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
@Table(name = "user_log")
@NamedQueries({
    @NamedQuery(name = "UserLog.findAll", query = "SELECT u FROM UserLog u")})
public class UserLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @Lob
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idusuario", fetch = FetchType.LAZY)
    private List<VariedadesIngresos> variedadesIngresosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idusuario", fetch = FetchType.LAZY)
    private List<VariedadesEgresos> variedadesEgresosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idusuario", fetch = FetchType.LAZY)
    private List<VariedadesSuministro> variedadesSuministroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idlog", fetch = FetchType.LAZY)
    private List<UserInfo> userInfoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idusuario", fetch = FetchType.LAZY)
    private List<VariedadesVentas> variedadesVentasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser", fetch = FetchType.LAZY)
    private List<UserPermisos> userPermisosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idusuario", fetch = FetchType.LAZY)
    private List<VariedadesDevoluciones> variedadesDevolucionesList;

    public UserLog() {
    }

    public UserLog(Integer id) {
        this.id = id;
    }

    public UserLog(Integer id, String usuario, String clave, int estado) {
        this.id = id;
        this.usuario = usuario;
        this.clave = clave;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<VariedadesIngresos> getVariedadesIngresosList() {
        return variedadesIngresosList;
    }

    public void setVariedadesIngresosList(List<VariedadesIngresos> variedadesIngresosList) {
        this.variedadesIngresosList = variedadesIngresosList;
    }

    public List<VariedadesEgresos> getVariedadesEgresosList() {
        return variedadesEgresosList;
    }

    public void setVariedadesEgresosList(List<VariedadesEgresos> variedadesEgresosList) {
        this.variedadesEgresosList = variedadesEgresosList;
    }

    public List<VariedadesSuministro> getVariedadesSuministroList() {
        return variedadesSuministroList;
    }

    public void setVariedadesSuministroList(List<VariedadesSuministro> variedadesSuministroList) {
        this.variedadesSuministroList = variedadesSuministroList;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public List<VariedadesVentas> getVariedadesVentasList() {
        return variedadesVentasList;
    }

    public void setVariedadesVentasList(List<VariedadesVentas> variedadesVentasList) {
        this.variedadesVentasList = variedadesVentasList;
    }

    public List<UserPermisos> getUserPermisosList() {
        return userPermisosList;
    }

    public void setUserPermisosList(List<UserPermisos> userPermisosList) {
        this.userPermisosList = userPermisosList;
    }

    public List<VariedadesDevoluciones> getVariedadesDevolucionesList() {
        return variedadesDevolucionesList;
    }

    public void setVariedadesDevolucionesList(List<VariedadesDevoluciones> variedadesDevolucionesList) {
        this.variedadesDevolucionesList = variedadesDevolucionesList;
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
        if (!(object instanceof UserLog)) {
            return false;
        }
        UserLog other = (UserLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UserLog[ id=" + id + " ]";
    }
    
}
