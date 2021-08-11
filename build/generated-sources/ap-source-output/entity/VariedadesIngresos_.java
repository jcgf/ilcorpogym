package entity;

import entity.IngresoItem;
import entity.UserLog;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VariedadesIngresos.class)
public class VariedadesIngresos_ { 

    public static volatile SingularAttribute<VariedadesIngresos, Integer> id;
    public static volatile SingularAttribute<VariedadesIngresos, UserLog> idusuario;
    public static volatile SingularAttribute<VariedadesIngresos, Integer> estado;
    public static volatile ListAttribute<VariedadesIngresos, IngresoItem> ingresoItemList;
    public static volatile SingularAttribute<VariedadesIngresos, Integer> codigoingreso;
    public static volatile SingularAttribute<VariedadesIngresos, Float> valortotal;
    public static volatile SingularAttribute<VariedadesIngresos, String> codcliente;
    public static volatile SingularAttribute<VariedadesIngresos, Float> subtotal;
    public static volatile SingularAttribute<VariedadesIngresos, Float> valoriva;
    public static volatile SingularAttribute<VariedadesIngresos, Date> fechaingreso;
    public static volatile SingularAttribute<VariedadesIngresos, Date> fechaingresotiempo;

}