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
@Table(name = "gym_planes")
@NamedQueries({
    @NamedQuery(name = "GymPlanes.findAll", query = "SELECT g FROM GymPlanes g")})
public class GymPlanes implements Serializable {

    @Basic(optional = false)
    @Column(name = "dias")
    private int dias;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "codigoservicio")
    private String codigoservicio;
    @Basic(optional = false)
    @Lob
    @Column(name = "nombreservicio")
    private String nombreservicio;
    @Basic(optional = false)
    @Column(name = "valor")
    private float valor;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idplan"/*, fetch = FetchType.LAZY*/)
    private List<GymAsignados> gymAsignadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idservicio"/*, fetch = FetchType.LAZY*/)
    private List<VentasServicios> ventasServiciosList;

    public GymPlanes() {
    }

    public GymPlanes(Integer id) {
        this.id = id;
    }

    public GymPlanes(Integer id, String codigoservicio, String nombreservicio, float valor, int estado) {
        this.id = id;
        this.codigoservicio = codigoservicio;
        this.nombreservicio = nombreservicio;
        this.valor = valor;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoservicio() {
        return codigoservicio;
    }

    public void setCodigoservicio(String codigoservicio) {
        this.codigoservicio = codigoservicio;
    }

    public String getNombreservicio() {
        return nombreservicio;
    }

    public void setNombreservicio(String nombreservicio) {
        this.nombreservicio = nombreservicio;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<GymAsignados> getGymAsignadosList() {
        return gymAsignadosList;
    }

    public void setGymAsignadosList(List<GymAsignados> gymAsignadosList) {
        this.gymAsignadosList = gymAsignadosList;
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
        if (!(object instanceof GymPlanes)) {
            return false;
        }
        GymPlanes other = (GymPlanes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GymPlanes[ id=" + id + " ]";
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }
    
}
