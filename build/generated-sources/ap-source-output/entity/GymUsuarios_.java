package entity;

import entity.GymAsignados;
import entity.GymContdias;
import entity.GymHuella;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(GymUsuarios.class)
public class GymUsuarios_ { 

    public static volatile SingularAttribute<GymUsuarios, Integer> id;
    public static volatile SingularAttribute<GymUsuarios, String> identificacion;
    public static volatile SingularAttribute<GymUsuarios, String> direccion;
    public static volatile SingularAttribute<GymUsuarios, String> nombres;
    public static volatile SingularAttribute<GymUsuarios, Integer> estado;
    public static volatile SingularAttribute<GymUsuarios, String> tipodoc;
    public static volatile SingularAttribute<GymUsuarios, String> apellidos;
    public static volatile SingularAttribute<GymUsuarios, String> email;
    public static volatile ListAttribute<GymUsuarios, GymContdias> gymContdiasList;
    public static volatile SingularAttribute<GymUsuarios, String> telefono;
    public static volatile ListAttribute<GymUsuarios, GymAsignados> gymAsignadosList;
    public static volatile SingularAttribute<GymUsuarios, Date> fechanacimiento;
    public static volatile ListAttribute<GymUsuarios, GymHuella> gymHuellaList;

}