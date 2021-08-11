package entity;

import entity.EgresoItem;
import entity.UserLog;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VariedadesEgresos.class)
public class VariedadesEgresos_ { 

    public static volatile SingularAttribute<VariedadesEgresos, Integer> id;
    public static volatile SingularAttribute<VariedadesEgresos, Date> fechaegreso;
    public static volatile ListAttribute<VariedadesEgresos, EgresoItem> egresoItemList;
    public static volatile SingularAttribute<VariedadesEgresos, UserLog> idusuario;
    public static volatile SingularAttribute<VariedadesEgresos, Integer> estado;
    public static volatile SingularAttribute<VariedadesEgresos, Integer> codigoegreso;
    public static volatile SingularAttribute<VariedadesEgresos, Date> fechaegresotiempo;
    public static volatile SingularAttribute<VariedadesEgresos, Float> valortotal;
    public static volatile SingularAttribute<VariedadesEgresos, String> codcliente;
    public static volatile SingularAttribute<VariedadesEgresos, Float> subtotal;
    public static volatile SingularAttribute<VariedadesEgresos, Float> valoriva;

}