package entity;

import entity.ArticulosCantidades;
import entity.DevolucionesArticulos;
import entity.SuministroArticulos;
import entity.VentasArticulos;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VariedadesArticulos.class)
public class VariedadesArticulos_ { 

    public static volatile SingularAttribute<VariedadesArticulos, String> nombrearticulo;
    public static volatile SingularAttribute<VariedadesArticulos, Float> ivaventa;
    public static volatile SingularAttribute<VariedadesArticulos, String> descripcion;
    public static volatile SingularAttribute<VariedadesArticulos, Integer> viva;
    public static volatile SingularAttribute<VariedadesArticulos, Integer> cantidadinicial;
    public static volatile SingularAttribute<VariedadesArticulos, Integer> id;
    public static volatile ListAttribute<VariedadesArticulos, ArticulosCantidades> articulosCantidadesList;
    public static volatile SingularAttribute<VariedadesArticulos, Integer> estado;
    public static volatile SingularAttribute<VariedadesArticulos, Float> valorventa;
    public static volatile ListAttribute<VariedadesArticulos, SuministroArticulos> suministroArticulosList;
    public static volatile ListAttribute<VariedadesArticulos, VentasArticulos> ventasArticulosList;
    public static volatile SingularAttribute<VariedadesArticulos, Float> valorcompra;
    public static volatile SingularAttribute<VariedadesArticulos, Integer> civa;
    public static volatile SingularAttribute<VariedadesArticulos, String> codigoarticulo;
    public static volatile ListAttribute<VariedadesArticulos, DevolucionesArticulos> devolucionesArticulosList;
    public static volatile SingularAttribute<VariedadesArticulos, Float> ivacompra;

}