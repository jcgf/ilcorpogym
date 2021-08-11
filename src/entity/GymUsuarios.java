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
import javax.persistence.Lob;
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
@Table(name = "gym_usuarios")
@NamedQueries({
    @NamedQuery(name = "GymUsuarios.findAll", query = "SELECT g FROM GymUsuarios g")})
public class GymUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipodoc")
    private String tipodoc;
    @Basic(optional = false)
    @Column(name = "identificacion")
    private String identificacion;
    @Basic(optional = false)
    @Lob
    @Column(name = "nombres")
    private String nombres;
    @Basic(optional = false)
    @Lob
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "fechanacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechanacimiento;
    @Lob
    @Column(name = "telefono")
    private String telefono;
    @Lob
    @Column(name = "email")
    private String email;
    @Lob
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idusuario", fetch = FetchType.LAZY)
    private List<GymContdias> gymContdiasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser", fetch = FetchType.LAZY)
    private List<GymHuella> gymHuellaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iduser", fetch = FetchType.LAZY)
    private List<GymAsignados> gymAsignadosList;

    public GymUsuarios() {
    }

    public GymUsuarios(Integer id) {
        this.id = id;
    }

    public GymUsuarios(Integer id, String tipodoc, String identificacion, String nombres, String apellidos, Date fechanacimiento, int estado) {
        this.id = id;
        this.tipodoc = tipodoc;
        this.identificacion = identificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechanacimiento = fechanacimiento;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipodoc() {
        return tipodoc;
    }

    public void setTipodoc(String tipodoc) {
        this.tipodoc = tipodoc;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public List<GymHuella> getGymHuellaList() {
        return gymHuellaList;
    }

    public void setGymHuellaList(List<GymHuella> gymHuellaList) {
        this.gymHuellaList = gymHuellaList;
    }

    public List<GymAsignados> getGymAsignadosList() {
        return gymAsignadosList;
    }

    public void setGymAsignadosList(List<GymAsignados> gymAsignadosList) {
        this.gymAsignadosList = gymAsignadosList;
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
        if (!(object instanceof GymUsuarios)) {
            return false;
        }
        GymUsuarios other = (GymUsuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GymUsuarios[ id=" + id + " ]";
    }
    
}
