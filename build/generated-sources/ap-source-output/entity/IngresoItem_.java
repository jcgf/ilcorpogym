package entity;

import entity.VariedadesIngresos;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(IngresoItem.class)
public class IngresoItem_ { 

    public static volatile SingularAttribute<IngresoItem, Integer> id;
    public static volatile SingularAttribute<IngresoItem, Float> iva;
    public static volatile SingularAttribute<IngresoItem, Float> total;
    public static volatile SingularAttribute<IngresoItem, Integer> estado;
    public static volatile SingularAttribute<IngresoItem, Integer> cantidad;
    public static volatile SingularAttribute<IngresoItem, String> descripcion;
    public static volatile SingularAttribute<IngresoItem, Float> valorindividual;
    public static volatile SingularAttribute<IngresoItem, VariedadesIngresos> idingreso;
    public static volatile SingularAttribute<IngresoItem, Float> subtotal;
    public static volatile SingularAttribute<IngresoItem, Float> valoriva;

}