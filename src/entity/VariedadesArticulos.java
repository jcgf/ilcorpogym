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
@Table(name = "variedades_articulos")
@NamedQueries({
    @NamedQuery(name = "VariedadesArticulos.findAll", query = "SELECT v FROM VariedadesArticulos v")})
public class VariedadesArticulos implements Serializable {

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
    @Column(name = "civa")
    private int civa;
    @Basic(optional = false)
    @Column(name = "ivacompra")
    private float ivacompra;
    @Basic(optional = false)
    @Column(name = "valorventa")
    private float valorventa;
    @Basic(optional = false)
    @Column(name = "viva")
    private int viva;
    @Basic(optional = false)
    @Column(name = "ivaventa")
    private float ivaventa;
    @Basic(optional = false)
    @Column(name = "cantidadinicial")
    private int cantidadinicial;
    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "estado")
    private int estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idarticulo", fetch = FetchType.LAZY)
    private List<VentasArticulos> ventasArticulosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idarticulo", fetch = FetchType.LAZY)
    private List<ArticulosCantidades> articulosCantidadesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idarticulo", fetch = FetchType.LAZY)
    private List<DevolucionesArticulos> devolucionesArticulosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idarticulo", fetch = FetchType.LAZY)
    private List<SuministroArticulos> suministroArticulosList;

    public VariedadesArticulos() {
    }

    public VariedadesArticulos(Integer id) {
        this.id = id;
    }

    public VariedadesArticulos(Integer id, String codigoarticulo, String nombrearticulo, float valorcompra, int civa, float ivacompra, float valorventa, int viva, float ivaventa, int cantidadinicial, int estado) {
        this.id = id;
        this.codigoarticulo = codigoarticulo;
        this.nombrearticulo = nombrearticulo;
        this.valorcompra = valorcompra;
        this.civa = civa;
        this.ivacompra = ivacompra;
        this.valorventa = valorventa;
        this.viva = viva;
        this.ivaventa = ivaventa;
        this.cantidadinicial = cantidadinicial;
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

    public int getCiva() {
        return civa;
    }

    public void setCiva(int civa) {
        this.civa = civa;
    }

    public float getIvacompra() {
        return ivacompra;
    }

    public void setIvacompra(float ivacompra) {
        this.ivacompra = ivacompra;
    }

    public float getValorventa() {
        return valorventa;
    }

    public void setValorventa(float valorventa) {
        this.valorventa = valorventa;
    }

    public int getViva() {
        return viva;
    }

    public void setViva(int viva) {
        this.viva = viva;
    }

    public float getIvaventa() {
        return ivaventa;
    }

    public void setIvaventa(float ivaventa) {
        this.ivaventa = ivaventa;
    }

    public int getCantidadinicial() {
        return cantidadinicial;
    }

    public void setCantidadinicial(int cantidadinicial) {
        this.cantidadinicial = cantidadinicial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public List<VentasArticulos> getVentasArticulosList() {
        return ventasArticulosList;
    }

    public void setVentasArticulosList(List<VentasArticulos> ventasArticulosList) {
        this.ventasArticulosList = ventasArticulosList;
    }

    public List<ArticulosCantidades> getArticulosCantidadesList() {
        return articulosCantidadesList;
    }

    public void setArticulosCantidadesList(List<ArticulosCantidades> articulosCantidadesList) {
        this.articulosCantidadesList = articulosCantidadesList;
    }

    public List<DevolucionesArticulos> getDevolucionesArticulosList() {
        return devolucionesArticulosList;
    }

    public void setDevolucionesArticulosList(List<DevolucionesArticulos> devolucionesArticulosList) {
        this.devolucionesArticulosList = devolucionesArticulosList;
    }

    public List<SuministroArticulos> getSuministroArticulosList() {
        return suministroArticulosList;
    }

    public void setSuministroArticulosList(List<SuministroArticulos> suministroArticulosList) {
        this.suministroArticulosList = suministroArticulosList;
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
        if (!(object instanceof VariedadesArticulos)) {
            return false;
        }
        VariedadesArticulos other = (VariedadesArticulos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.VariedadesArticulos[ id=" + id + " ]";
    }
    
}
