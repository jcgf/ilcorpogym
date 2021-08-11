package entity;

import entity.VariedadesVentas;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(VariedadesAbono.class)
public class VariedadesAbono_ { 

    public static volatile SingularAttribute<VariedadesAbono, Integer> id;
    public static volatile SingularAttribute<VariedadesAbono, Integer> estado;
    public static volatile SingularAttribute<VariedadesAbono, VariedadesVentas> idventa;
    public static volatile SingularAttribute<VariedadesAbono, Date> fecha;
    public static volatile SingularAttribute<VariedadesAbono, Float> abono;

}