package entity;

import entity.UserInfo;
import entity.UserPermisos;
import entity.VariedadesDevoluciones;
import entity.VariedadesEgresos;
import entity.VariedadesIngresos;
import entity.VariedadesSuministro;
import entity.VariedadesVentas;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(UserLog.class)
public class UserLog_ { 

    public static volatile SingularAttribute<UserLog, Integer> id;
    public static volatile ListAttribute<UserLog, UserInfo> userInfoList;
    public static volatile ListAttribute<UserLog, UserPermisos> userPermisosList;
    public static volatile ListAttribute<UserLog, VariedadesDevoluciones> variedadesDevolucionesList;
    public static volatile SingularAttribute<UserLog, Integer> estado;
    public static volatile ListAttribute<UserLog, VariedadesVentas> variedadesVentasList;
    public static volatile ListAttribute<UserLog, VariedadesSuministro> variedadesSuministroList;
    public static volatile SingularAttribute<UserLog, String> usuario;
    public static volatile ListAttribute<UserLog, VariedadesEgresos> variedadesEgresosList;
    public static volatile ListAttribute<UserLog, VariedadesIngresos> variedadesIngresosList;
    public static volatile SingularAttribute<UserLog, String> clave;

}