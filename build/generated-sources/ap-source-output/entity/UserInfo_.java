package entity;

import entity.UserLog;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-02-19T16:47:58")
@StaticMetamodel(UserInfo.class)
public class UserInfo_ { 

    public static volatile SingularAttribute<UserInfo, Integer> id;
    public static volatile SingularAttribute<UserInfo, String> identificacion;
    public static volatile SingularAttribute<UserInfo, String> nombres;
    public static volatile SingularAttribute<UserInfo, Integer> estado;
    public static volatile SingularAttribute<UserInfo, String> apellidos;
    public static volatile SingularAttribute<UserInfo, UserLog> idlog;

}