package entity;

import entity.SuministroArticulos;
import entity.UserLog;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VariedadesSuministro.class)
public class VariedadesSuministro_ { 

    public static volatile SingularAttribute<VariedadesSuministro, Integer> id;
    public static volatile SingularAttribute<VariedadesSuministro, Integer> codigosuministro;
    public static volatile SingularAttribute<VariedadesSuministro, UserLog> idusuario;
    public static volatile SingularAttribute<VariedadesSuministro, Date> fechaventa;
    public static volatile SingularAttribute<VariedadesSuministro, Date> fechaventatiempo;
    public static volatile SingularAttribute<VariedadesSuministro, Integer> estado;
    public static volatile SingularAttribute<VariedadesSuministro, Float> valortotal;
    public static volatile ListAttribute<VariedadesSuministro, SuministroArticulos> suministroArticulosList;
    public static volatile SingularAttribute<VariedadesSuministro, String> codcliente;
    public static volatile SingularAttribute<VariedadesSuministro, Float> subtotal;
    public static volatile SingularAttribute<VariedadesSuministro, Float> valoriva;

}