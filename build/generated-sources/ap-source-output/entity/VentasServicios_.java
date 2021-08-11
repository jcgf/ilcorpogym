package entity;

import entity.GymPlanes;
import entity.VariedadesVentas;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VentasServicios.class)
public class VentasServicios_ { 

    public static volatile SingularAttribute<VentasServicios, Integer> id;
    public static volatile SingularAttribute<VentasServicios, Float> total;
    public static volatile SingularAttribute<VentasServicios, Integer> estado;
    public static volatile SingularAttribute<VentasServicios, VariedadesVentas> idventa;
    public static volatile SingularAttribute<VentasServicios, Float> valor;
    public static volatile SingularAttribute<VentasServicios, Integer> cantidad;
    public static volatile SingularAttribute<VentasServicios, GymPlanes> idservicio;
    public static volatile SingularAttribute<VentasServicios, Float> subtotal;
    public static volatile SingularAttribute<VentasServicios, Float> valoriva;

}