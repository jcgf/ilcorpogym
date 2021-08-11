package entity;

import entity.GymUsuarios;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(GymHuella.class)
public class GymHuella_ { 

    public static volatile SingularAttribute<GymHuella, Integer> id;
    public static volatile SingularAttribute<GymHuella, GymUsuarios> iduser;
    public static volatile SingularAttribute<GymHuella, byte[]> huella;
    public static volatile SingularAttribute<GymHuella, Integer> estado;

}