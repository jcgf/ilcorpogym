package entity;

import entity.VariedadesArticulos;
import entity.VariedadesDevoluciones;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(DevolucionesArticulos.class)
public class DevolucionesArticulos_ { 

    public static volatile SingularAttribute<DevolucionesArticulos, Integer> id;
    public static volatile SingularAttribute<DevolucionesArticulos, Float> total;
    public static volatile SingularAttribute<DevolucionesArticulos, Integer> estado;
    public static volatile SingularAttribute<DevolucionesArticulos, Integer> cantidad;
    public static volatile SingularAttribute<DevolucionesArticulos, VariedadesArticulos> idarticulo;
    public static volatile SingularAttribute<DevolucionesArticulos, Float> valorindividual;
    public static volatile SingularAttribute<DevolucionesArticulos, Float> subtotal;
    public static volatile SingularAttribute<DevolucionesArticulos, Float> valoriva;
    public static volatile SingularAttribute<DevolucionesArticulos, VariedadesDevoluciones> iddevolucion;

}