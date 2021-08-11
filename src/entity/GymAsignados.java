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
@Table(name = "gym_asignados")
@NamedQueries({
    @NamedQuery(name = "GymAsignados.findAll", query = "SELECT g FROM GymAsignados g")})
public class GymAsignados implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idasignacion", fetch = FetchType.LAZY)
    private List<GymCongelado> gymCongeladoList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fechainicio")
    @Temporal(TemporalType.DATE)
    private Date fechainicio;
    @Basic(optional = false)
    @Column(name = "fechafin")
    @Temporal(TemporalType.DATE)
    private Date fechafin;
    @Basic(optional = false)
    @Column(name = "pagocompleto")
    private int pagocompleto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "abono")
    private Float abono;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idasignacion"/*, fetch = FetchType.LAZY*/)
    private List<GymContdias> gymContdiasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idasignacion"/*, fetch = FetchType.LAZY*/)
    private List<AsignadosDias> asignadosDiasList;
    @JoinColumn(name = "iduser", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private GymUsuarios iduser;
    @JoinColumn(name = "idplan", referencedColumnName = "id")
    @ManyToOne(optional = false/*, fetch = FetchType.LAZY*/)
    private GymPlanes idplan;

    public GymAsignados() {
    }

    public GymAsignados(Integer id) {
        this.id = id;
    }

    public GymAsignados(Integer id, Date fechainicio, Date fechafin, int pagocompleto, int estado) {
        this.id = id;
        this.fechainicio = fechainicio;
        this.fechafin = fechafin;
        this.pagocompleto = pagocompleto;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public int getPagocompleto() {
        return pagocompleto;
    }

    public void setPagocompleto(int pagocompleto) {
        this.pagocompleto = pagocompleto;
    }

    public Float getAbono() {
        return abono;
    }

    public void setAbono(Float abono) {
        this.abono = abono;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<GymContdias> getGymContdiasList() {
        return gymContdiasList;
    }

    public void setGymContdiasList(List<GymContdias> gymContdiasList) {
        this.gymContdiasList = gymContdiasList;
    }

    public List<AsignadosDias> getAsignadosDiasList() {
        return asignadosDiasList;
    }

    public void setAsignadosDiasList(List<AsignadosDias> asignadosDiasList) {
        this.asignadosDiasList = asignadosDiasList;
    }

    public GymUsuarios getIduser() {
        return iduser;
    }

    public void setIduser(GymUsuarios iduser) {
        this.iduser = iduser;
    }

    public GymPlanes getIdplan() {
        return idplan;
    }

    public void setIdplan(GymPlanes idplan) {
        this.idplan = idplan;
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
        if (!(object instanceof GymAsignados)) {
            return false;
        }
        GymAsignados other = (GymAsignados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GymAsignados[ id=" + id + " ]";
    }

    public List<GymCongelado> getGymCongeladoList() {
        return gymCongeladoList;
    }

    public void setGymCongeladoList(List<GymCongelado> gymCongeladoList) {
        this.gymCongeladoList = gymCongeladoList;
    }
    
}
