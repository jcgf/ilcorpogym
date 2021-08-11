package entity;

import entity.DevolucionesArticulos;
import entity.UserLog;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VariedadesDevoluciones.class)
public class VariedadesDevoluciones_ { 

    public static volatile SingularAttribute<VariedadesDevoluciones, Integer> id;
    public static volatile SingularAttribute<VariedadesDevoluciones, UserLog> idusuario;
    public static volatile SingularAttribute<VariedadesDevoluciones, Date> fechaventa;
    public static volatile SingularAttribute<VariedadesDevoluciones, Date> fechaventatiempo;
    public static volatile SingularAttribute<VariedadesDevoluciones, Integer> estado;
    public static volatile SingularAttribute<VariedadesDevoluciones, Integer> codigodevolucion;
    public static volatile SingularAttribute<VariedadesDevoluciones, Float> valortotal;
    public static volatile SingularAttribute<VariedadesDevoluciones, String> codcliente;
    public static volatile ListAttribute<VariedadesDevoluciones, DevolucionesArticulos> devolucionesArticulosList;
    public static volatile SingularAttribute<VariedadesDevoluciones, Float> subtotal;
    public static volatile SingularAttribute<VariedadesDevoluciones, Float> valoriva;

}