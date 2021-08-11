package entity;

import entity.AsignadosDias;
import entity.GymCongelado;
import entity.GymContdias;
import entity.GymPlanes;
import entity.GymUsuarios;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(GymAsignados.class)
public class GymAsignados_ { 

    public static volatile SingularAttribute<GymAsignados, Integer> id;
    public static volatile SingularAttribute<GymAsignados, Integer> pagocompleto;
    public static volatile SingularAttribute<GymAsignados, GymUsuarios> iduser;
    public static volatile SingularAttribute<GymAsignados, Integer> estado;
    public static volatile SingularAttribute<GymAsignados, Date> fechafin;
    public static volatile ListAttribute<GymAsignados, GymCongelado> gymCongeladoList;
    public static volatile ListAttribute<GymAsignados, GymContdias> gymContdiasList;
    public static volatile SingularAttribute<GymAsignados, Float> abono;
    public static volatile SingularAttribute<GymAsignados, GymPlanes> idplan;
    public static volatile ListAttribute<GymAsignados, AsignadosDias> asignadosDiasList;
    public static volatile SingularAttribute<GymAsignados, Date> fechainicio;

}