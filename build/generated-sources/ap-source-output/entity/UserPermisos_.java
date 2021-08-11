package entity;

import entity.GymPermisos;
import entity.UserLog;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(UserPermisos.class)
public class UserPermisos_ { 

    public static volatile SingularAttribute<UserPermisos, Integer> id;
    public static volatile SingularAttribute<UserPermisos, UserLog> iduser;
    public static volatile SingularAttribute<UserPermisos, Integer> estado;
    public static volatile SingularAttribute<UserPermisos, GymPermisos> idpermiso;

}