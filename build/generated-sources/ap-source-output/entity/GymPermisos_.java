package entity;

import entity.UserPermisos;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(GymPermisos.class)
public class GymPermisos_ { 

    public static volatile SingularAttribute<GymPermisos, Integer> id;
    public static volatile ListAttribute<GymPermisos, UserPermisos> userPermisosList;
    public static volatile SingularAttribute<GymPermisos, Integer> estado;
    public static volatile SingularAttribute<GymPermisos, String> nombrepermiso;

}