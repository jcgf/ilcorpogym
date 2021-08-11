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
@Table(name = "gym_articulo")
@NamedQueries({
    @NamedQuery(name = "GymArticulo.findAll", query = "SELECT g FROM GymArticulo g")})
public class GymArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "codigoarticulo")
    private String codigoarticulo;
    @Basic(optional = false)
    @Lob
    @Column(name = "nombrearticulo")
    private String nombrearticulo;
    @Basic(optional = false)
    @Column(name = "valorcompra")
    private float valorcompra;
    @Basic(optional = false)
    @Column(name = "valorventa")
    private float valorventa;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;

    public GymArticulo() {
    }

    public GymArticulo(Integer id) {
        this.id = id;
    }

    public GymArticulo(Integer id, String codigoarticulo, String nombrearticulo, float valorcompra, float valorventa, int estado) {
        this.id = id;
        this.codigoarticulo = codigoarticulo;
        this.nombrearticulo = nombrearticulo;
        this.valorcompra = valorcompra;
        this.valorventa = valorventa;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoarticulo() {
        return codigoarticulo;
    }

    public void setCodigoarticulo(String codigoarticulo) {
        this.codigoarticulo = codigoarticulo;
    }

    public String getNombrearticulo() {
        return nombrearticulo;
    }

    public void setNombrearticulo(String nombrearticulo) {
        this.nombrearticulo = nombrearticulo;
    }

    public float getValorcompra() {
        return valorcompra;
    }

    public void setValorcompra(float valorcompra) {
        this.valorcompra = valorcompra;
    }

    public float getValorventa() {
        return valorventa;
    }

    public void setValorventa(float valorventa) {
        this.valorventa = valorventa;
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
        if (!(object instanceof GymArticulo)) {
            return false;
        }
        GymArticulo other = (GymArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GymArticulo[ id=" + id + " ]";
    }
    
}
