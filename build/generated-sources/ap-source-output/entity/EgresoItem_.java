package entity;

import entity.VariedadesEgresos;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(EgresoItem.class)
public class EgresoItem_ { 

    public static volatile SingularAttribute<EgresoItem, Integer> id;
    public static volatile SingularAttribute<EgresoItem, Float> iva;
    public static volatile SingularAttribute<EgresoItem, Float> total;
    public static volatile SingularAttribute<EgresoItem, Integer> estado;
    public static volatile SingularAttribute<EgresoItem, Integer> cantidad;
    public static volatile SingularAttribute<EgresoItem, String> descripcion;
    public static volatile SingularAttribute<EgresoItem, VariedadesEgresos> idegreso;
    public static volatile SingularAttribute<EgresoItem, Float> valorindividual;
    public static volatile SingularAttribute<EgresoItem, Float> subtotal;
    public static volatile SingularAttribute<EgresoItem, Float> valoriva;

}