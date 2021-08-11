package entity;

import entity.UserLog;
import entity.VariedadesAbono;
import entity.VentasArticulos;
import entity.VentasServicios;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VariedadesVentas.class)
public class VariedadesVentas_ { 

    public static volatile SingularAttribute<VariedadesVentas, Integer> codigoventa;
    public static volatile SingularAttribute<VariedadesVentas, UserLog> idusuario;
    public static volatile ListAttribute<VariedadesVentas, VariedadesAbono> variedadesAbonoList;
    public static volatile SingularAttribute<VariedadesVentas, String> codcliente;
    public static volatile SingularAttribute<VariedadesVentas, Float> subtotal;
    public static volatile SingularAttribute<VariedadesVentas, Integer> efectivo;
    public static volatile SingularAttribute<VariedadesVentas, Integer> id;
    public static volatile ListAttribute<VariedadesVentas, VentasServicios> ventasServiciosList;
    public static volatile SingularAttribute<VariedadesVentas, Integer> estado;
    public static volatile SingularAttribute<VariedadesVentas, Date> fechaventatiempo;
    public static volatile SingularAttribute<VariedadesVentas, Date> fechaventa;
    public static volatile SingularAttribute<VariedadesVentas, Float> valortotal;
    public static volatile ListAttribute<VariedadesVentas, VentasArticulos> ventasArticulosList;
    public static volatile SingularAttribute<VariedadesVentas, Float> valoriva;

}