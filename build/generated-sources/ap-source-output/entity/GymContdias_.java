package entity;

import entity.GymAsignados;
import entity.GymUsuarios;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(GymContdias.class)
public class GymContdias_ { 

    public static volatile SingularAttribute<GymContdias, Integer> id;
    public static volatile SingularAttribute<GymContdias, GymAsignados> idasignacion;
    public static volatile SingularAttribute<GymContdias, GymUsuarios> idusuario;
    public static volatile SingularAttribute<GymContdias, Integer> estado;
    public static volatile SingularAttribute<GymContdias, Date> fechadia;

}