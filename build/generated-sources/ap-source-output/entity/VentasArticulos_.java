package entity;

import entity.VariedadesArticulos;
import entity.VariedadesVentas;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VentasArticulos.class)
public class VentasArticulos_ { 

    public static volatile SingularAttribute<VentasArticulos, Integer> id;
    public static volatile SingularAttribute<VentasArticulos, Float> total;
    public static volatile SingularAttribute<VentasArticulos, Integer> estado;
    public static volatile SingularAttribute<VentasArticulos, VariedadesVentas> idventa;
    public static volatile SingularAttribute<VentasArticulos, Integer> cantidad;
    public static volatile SingularAttribute<VentasArticulos, VariedadesArticulos> idarticulo;
    public static volatile SingularAttribute<VentasArticulos, Float> valorindividual;
    public static volatile SingularAttribute<VentasArticulos, Float> subtotal;
    public static volatile SingularAttribute<VentasArticulos, Float> valoriva;

}