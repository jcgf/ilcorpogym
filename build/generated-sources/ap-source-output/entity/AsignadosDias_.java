package entity;

import entity.GymAsignados;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(AsignadosDias.class)
public class AsignadosDias_ { 

    public static volatile SingularAttribute<AsignadosDias, Integer> id;
    public static volatile SingularAttribute<AsignadosDias, GymAsignados> idasignacion;
    public static volatile SingularAttribute<AsignadosDias, Integer> estado;
    public static volatile SingularAttribute<AsignadosDias, Integer> dias;

}