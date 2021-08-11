package entity;

import entity.GymAsignados;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(GymCongelado.class)
public class GymCongelado_ { 

    public static volatile SingularAttribute<GymCongelado, Integer> id;
    public static volatile SingularAttribute<GymCongelado, GymAsignados> idasignacion;
    public static volatile SingularAttribute<GymCongelado, Integer> estado;
    public static volatile SingularAttribute<GymCongelado, Date> fechacongela;

}