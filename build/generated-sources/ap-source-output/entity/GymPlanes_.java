package entity;

import entity.GymAsignados;
import entity.VentasServicios;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(GymPlanes.class)
public class GymPlanes_ { 

    public static volatile SingularAttribute<GymPlanes, Integer> id;
    public static volatile SingularAttribute<GymPlanes, String> codigoservicio;
    public static volatile ListAttribute<GymPlanes, VentasServicios> ventasServiciosList;
    public static volatile SingularAttribute<GymPlanes, String> nombreservicio;
    public static volatile SingularAttribute<GymPlanes, Integer> estado;
    public static volatile SingularAttribute<GymPlanes, Float> valor;
    public static volatile ListAttribute<GymPlanes, GymAsignados> gymAsignadosList;
    public static volatile SingularAttribute<GymPlanes, Integer> dias;

}