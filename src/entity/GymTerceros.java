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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Juan
 */
@Entity
@Table(name = "gym_terceros")
@NamedQueries({
    @NamedQuery(name = "GymTerceros.findAll", query = "SELECT g FROM GymTerceros g")})
public class GymTerceros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "nit")
    private String nit;
    @Basic(optional = false)
    @Lob
    @Column(name = "razonsocial")
    private String razonsocial;
    @Lob
    @Column(name = "direccion")
    private String direccion;
    @Lob
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @Lob
    @Column(name = "regimen")
    private String regimen;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;

    public GymTerceros() {
    }

    public GymTerceros(Integer id) {
        this.id = id;
    }

    public GymTerceros(Integer id, String nit, String razonsocial, String regimen, int estado) {
        this.id = id;
        this.nit = nit;
        this.razonsocial = razonsocial;
        this.regimen = regimen;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
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
        if (!(object instanceof GymTerceros)) {
            return false;
        }
        GymTerceros other = (GymTerceros) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GymTerceros[ id=" + id + " ]";
    }
    
}
