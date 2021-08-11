/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.VariedadesIngresos;
import java.util.ArrayList;
import java.util.List;
import entity.VariedadesEgresos;
import entity.VariedadesSuministro;
import entity.UserInfo;
import entity.UserLog;
import entity.VariedadesVentas;
import entity.UserPermisos;
import entity.VariedadesDevoluciones;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class UserLogJpaController implements Serializable {

    public UserLogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserLog userLog) {
        if (userLog.getVariedadesIngresosList() == null) {
            userLog.setVariedadesIngresosList(new ArrayList<VariedadesIngresos>());
        }
        if (userLog.getVariedadesEgresosList() == null) {
            userLog.setVariedadesEgresosList(new ArrayList<VariedadesEgresos>());
        }
        if (userLog.getVariedadesSuministroList() == null) {
            userLog.setVariedadesSuministroList(new ArrayList<VariedadesSuministro>());
        }
        if (userLog.getUserInfoList() == null) {
            userLog.setUserInfoList(new ArrayList<UserInfo>());
        }
        if (userLog.getVariedadesVentasList() == null) {
            userLog.setVariedadesVentasList(new ArrayList<VariedadesVentas>());
        }
        if (userLog.getUserPermisosList() == null) {
            userLog.setUserPermisosList(new ArrayList<UserPermisos>());
        }
        if (userLog.getVariedadesDevolucionesList() == null) {
            userLog.setVariedadesDevolucionesList(new ArrayList<VariedadesDevoluciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<VariedadesIngresos> attachedVariedadesIngresosList = new ArrayList<VariedadesIngresos>();
            for (VariedadesIngresos variedadesIngresosListVariedadesIngresosToAttach : userLog.getVariedadesIngresosList()) {
                variedadesIngresosListVariedadesIngresosToAttach = em.getReference(variedadesIngresosListVariedadesIngresosToAttach.getClass(), variedadesIngresosListVariedadesIngresosToAttach.getId());
                attachedVariedadesIngresosList.add(variedadesIngresosListVariedadesIngresosToAttach);
            }
            userLog.setVariedadesIngresosList(attachedVariedadesIngresosList);
            List<VariedadesEgresos> attachedVariedadesEgresosList = new ArrayList<VariedadesEgresos>();
            for (VariedadesEgresos variedadesEgresosListVariedadesEgresosToAttach : userLog.getVariedadesEgresosList()) {
                variedadesEgresosListVariedadesEgresosToAttach = em.getReference(variedadesEgresosListVariedadesEgresosToAttach.getClass(), variedadesEgresosListVariedadesEgresosToAttach.getId());
                attachedVariedadesEgresosList.add(variedadesEgresosListVariedadesEgresosToAttach);
            }
            userLog.setVariedadesEgresosList(attachedVariedadesEgresosList);
            List<VariedadesSuministro> attachedVariedadesSuministroList = new ArrayList<VariedadesSuministro>();
            for (VariedadesSuministro variedadesSuministroListVariedadesSuministroToAttach : userLog.getVariedadesSuministroList()) {
                variedadesSuministroListVariedadesSuministroToAttach = em.getReference(variedadesSuministroListVariedadesSuministroToAttach.getClass(), variedadesSuministroListVariedadesSuministroToAttach.getId());
                attachedVariedadesSuministroList.add(variedadesSuministroListVariedadesSuministroToAttach);
            }
            userLog.setVariedadesSuministroList(attachedVariedadesSuministroList);
            List<UserInfo> attachedUserInfoList = new ArrayList<UserInfo>();
            for (UserInfo userInfoListUserInfoToAttach : userLog.getUserInfoList()) {
                userInfoListUserInfoToAttach = em.getReference(userInfoListUserInfoToAttach.getClass(), userInfoListUserInfoToAttach.getId());
                attachedUserInfoList.add(userInfoListUserInfoToAttach);
            }
            userLog.setUserInfoList(attachedUserInfoList);
            List<VariedadesVentas> attachedVariedadesVentasList = new ArrayList<VariedadesVentas>();
            for (VariedadesVentas variedadesVentasListVariedadesVentasToAttach : userLog.getVariedadesVentasList()) {
                variedadesVentasListVariedadesVentasToAttach = em.getReference(variedadesVentasListVariedadesVentasToAttach.getClass(), variedadesVentasListVariedadesVentasToAttach.getId());
                attachedVariedadesVentasList.add(variedadesVentasListVariedadesVentasToAttach);
            }
            userLog.setVariedadesVentasList(attachedVariedadesVentasList);
            List<UserPermisos> attachedUserPermisosList = new ArrayList<UserPermisos>();
            for (UserPermisos userPermisosListUserPermisosToAttach : userLog.getUserPermisosList()) {
                userPermisosListUserPermisosToAttach = em.getReference(userPermisosListUserPermisosToAttach.getClass(), userPermisosListUserPermisosToAttach.getId());
                attachedUserPermisosList.add(userPermisosListUserPermisosToAttach);
            }
            userLog.setUserPermisosList(attachedUserPermisosList);
            List<VariedadesDevoluciones> attachedVariedadesDevolucionesList = new ArrayList<VariedadesDevoluciones>();
            for (VariedadesDevoluciones variedadesDevolucionesListVariedadesDevolucionesToAttach : userLog.getVariedadesDevolucionesList()) {
                variedadesDevolucionesListVariedadesDevolucionesToAttach = em.getReference(variedadesDevolucionesListVariedadesDevolucionesToAttach.getClass(), variedadesDevolucionesListVariedadesDevolucionesToAttach.getId());
                attachedVariedadesDevolucionesList.add(variedadesDevolucionesListVariedadesDevolucionesToAttach);
            }
            userLog.setVariedadesDevolucionesList(attachedVariedadesDevolucionesList);
            em.persist(userLog);
            for (VariedadesIngresos variedadesIngresosListVariedadesIngresos : userLog.getVariedadesIngresosList()) {
                UserLog oldIdusuarioOfVariedadesIngresosListVariedadesIngresos = variedadesIngresosListVariedadesIngresos.getIdusuario();
                variedadesIngresosListVariedadesIngresos.setIdusuario(userLog);
                variedadesIngresosListVariedadesIngresos = em.merge(variedadesIngresosListVariedadesIngresos);
                if (oldIdusuarioOfVariedadesIngresosListVariedadesIngresos != null) {
                    oldIdusuarioOfVariedadesIngresosListVariedadesIngresos.getVariedadesIngresosList().remove(variedadesIngresosListVariedadesIngresos);
                    oldIdusuarioOfVariedadesIngresosListVariedadesIngresos = em.merge(oldIdusuarioOfVariedadesIngresosListVariedadesIngresos);
                }
            }
            for (VariedadesEgresos variedadesEgresosListVariedadesEgresos : userLog.getVariedadesEgresosList()) {
                UserLog oldIdusuarioOfVariedadesEgresosListVariedadesEgresos = variedadesEgresosListVariedadesEgresos.getIdusuario();
                variedadesEgresosListVariedadesEgresos.setIdusuario(userLog);
                variedadesEgresosListVariedadesEgresos = em.merge(variedadesEgresosListVariedadesEgresos);
                if (oldIdusuarioOfVariedadesEgresosListVariedadesEgresos != null) {
                    oldIdusuarioOfVariedadesEgresosListVariedadesEgresos.getVariedadesEgresosList().remove(variedadesEgresosListVariedadesEgresos);
                    oldIdusuarioOfVariedadesEgresosListVariedadesEgresos = em.merge(oldIdusuarioOfVariedadesEgresosListVariedadesEgresos);
                }
            }
            for (VariedadesSuministro variedadesSuministroListVariedadesSuministro : userLog.getVariedadesSuministroList()) {
                UserLog oldIdusuarioOfVariedadesSuministroListVariedadesSuministro = variedadesSuministroListVariedadesSuministro.getIdusuario();
                variedadesSuministroListVariedadesSuministro.setIdusuario(userLog);
                variedadesSuministroListVariedadesSuministro = em.merge(variedadesSuministroListVariedadesSuministro);
                if (oldIdusuarioOfVariedadesSuministroListVariedadesSuministro != null) {
                    oldIdusuarioOfVariedadesSuministroListVariedadesSuministro.getVariedadesSuministroList().remove(variedadesSuministroListVariedadesSuministro);
                    oldIdusuarioOfVariedadesSuministroListVariedadesSuministro = em.merge(oldIdusuarioOfVariedadesSuministroListVariedadesSuministro);
                }
            }
            for (UserInfo userInfoListUserInfo : userLog.getUserInfoList()) {
                UserLog oldIdlogOfUserInfoListUserInfo = userInfoListUserInfo.getIdlog();
                userInfoListUserInfo.setIdlog(userLog);
                userInfoListUserInfo = em.merge(userInfoListUserInfo);
                if (oldIdlogOfUserInfoListUserInfo != null) {
                    oldIdlogOfUserInfoListUserInfo.getUserInfoList().remove(userInfoListUserInfo);
                    oldIdlogOfUserInfoListUserInfo = em.merge(oldIdlogOfUserInfoListUserInfo);
                }
            }
            for (VariedadesVentas variedadesVentasListVariedadesVentas : userLog.getVariedadesVentasList()) {
                UserLog oldIdusuarioOfVariedadesVentasListVariedadesVentas = variedadesVentasListVariedadesVentas.getIdusuario();
                variedadesVentasListVariedadesVentas.setIdusuario(userLog);
                variedadesVentasListVariedadesVentas = em.merge(variedadesVentasListVariedadesVentas);
                if (oldIdusuarioOfVariedadesVentasListVariedadesVentas != null) {
                    oldIdusuarioOfVariedadesVentasListVariedadesVentas.getVariedadesVentasList().remove(variedadesVentasListVariedadesVentas);
                    oldIdusuarioOfVariedadesVentasListVariedadesVentas = em.merge(oldIdusuarioOfVariedadesVentasListVariedadesVentas);
                }
            }
            for (UserPermisos userPermisosListUserPermisos : userLog.getUserPermisosList()) {
                UserLog oldIduserOfUserPermisosListUserPermisos = userPermisosListUserPermisos.getIduser();
                userPermisosListUserPermisos.setIduser(userLog);
                userPermisosListUserPermisos = em.merge(userPermisosListUserPermisos);
                if (oldIduserOfUserPermisosListUserPermisos != null) {
                    oldIduserOfUserPermisosListUserPermisos.getUserPermisosList().remove(userPermisosListUserPermisos);
                    oldIduserOfUserPermisosListUserPermisos = em.merge(oldIduserOfUserPermisosListUserPermisos);
                }
            }
            for (VariedadesDevoluciones variedadesDevolucionesListVariedadesDevoluciones : userLog.getVariedadesDevolucionesList()) {
                UserLog oldIdusuarioOfVariedadesDevolucionesListVariedadesDevoluciones = variedadesDevolucionesListVariedadesDevoluciones.getIdusuario();
                variedadesDevolucionesListVariedadesDevoluciones.setIdusuario(userLog);
                variedadesDevolucionesListVariedadesDevoluciones = em.merge(variedadesDevolucionesListVariedadesDevoluciones);
                if (oldIdusuarioOfVariedadesDevolucionesListVariedadesDevoluciones != null) {
                    oldIdusuarioOfVariedadesDevolucionesListVariedadesDevoluciones.getVariedadesDevolucionesList().remove(variedadesDevolucionesListVariedadesDevoluciones);
                    oldIdusuarioOfVariedadesDevolucionesListVariedadesDevoluciones = em.merge(oldIdusuarioOfVariedadesDevolucionesListVariedadesDevoluciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserLog userLog) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog persistentUserLog = em.find(UserLog.class, userLog.getId());
            List<VariedadesIngresos> variedadesIngresosListOld = persistentUserLog.getVariedadesIngresosList();
            List<VariedadesIngresos> variedadesIngresosListNew = userLog.getVariedadesIngresosList();
            List<VariedadesEgresos> variedadesEgresosListOld = persistentUserLog.getVariedadesEgresosList();
            List<VariedadesEgresos> variedadesEgresosListNew = userLog.getVariedadesEgresosList();
            List<VariedadesSuministro> variedadesSuministroListOld = persistentUserLog.getVariedadesSuministroList();
            List<VariedadesSuministro> variedadesSuministroListNew = userLog.getVariedadesSuministroList();
            List<UserInfo> userInfoListOld = persistentUserLog.getUserInfoList();
            List<UserInfo> userInfoListNew = userLog.getUserInfoList();
            List<VariedadesVentas> variedadesVentasListOld = persistentUserLog.getVariedadesVentasList();
            List<VariedadesVentas> variedadesVentasListNew = userLog.getVariedadesVentasList();
            List<UserPermisos> userPermisosListOld = persistentUserLog.getUserPermisosList();
            List<UserPermisos> userPermisosListNew = userLog.getUserPermisosList();
            List<VariedadesDevoluciones> variedadesDevolucionesListOld = persistentUserLog.getVariedadesDevolucionesList();
            List<VariedadesDevoluciones> variedadesDevolucionesListNew = userLog.getVariedadesDevolucionesList();
            List<String> illegalOrphanMessages = null;
            for (VariedadesIngresos variedadesIngresosListOldVariedadesIngresos : variedadesIngresosListOld) {
                if (!variedadesIngresosListNew.contains(variedadesIngresosListOldVariedadesIngresos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VariedadesIngresos " + variedadesIngresosListOldVariedadesIngresos + " since its idusuario field is not nullable.");
                }
            }
            for (VariedadesEgresos variedadesEgresosListOldVariedadesEgresos : variedadesEgresosListOld) {
                if (!variedadesEgresosListNew.contains(variedadesEgresosListOldVariedadesEgresos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VariedadesEgresos " + variedadesEgresosListOldVariedadesEgresos + " since its idusuario field is not nullable.");
                }
            }
            for (VariedadesSuministro variedadesSuministroListOldVariedadesSuministro : variedadesSuministroListOld) {
                if (!variedadesSuministroListNew.contains(variedadesSuministroListOldVariedadesSuministro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VariedadesSuministro " + variedadesSuministroListOldVariedadesSuministro + " since its idusuario field is not nullable.");
                }
            }
            for (UserInfo userInfoListOldUserInfo : userInfoListOld) {
                if (!userInfoListNew.contains(userInfoListOldUserInfo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserInfo " + userInfoListOldUserInfo + " since its idlog field is not nullable.");
                }
            }
            for (VariedadesVentas variedadesVentasListOldVariedadesVentas : variedadesVentasListOld) {
                if (!variedadesVentasListNew.contains(variedadesVentasListOldVariedadesVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VariedadesVentas " + variedadesVentasListOldVariedadesVentas + " since its idusuario field is not nullable.");
                }
            }
            for (UserPermisos userPermisosListOldUserPermisos : userPermisosListOld) {
                if (!userPermisosListNew.contains(userPermisosListOldUserPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserPermisos " + userPermisosListOldUserPermisos + " since its iduser field is not nullable.");
                }
            }
            for (VariedadesDevoluciones variedadesDevolucionesListOldVariedadesDevoluciones : variedadesDevolucionesListOld) {
                if (!variedadesDevolucionesListNew.contains(variedadesDevolucionesListOldVariedadesDevoluciones)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VariedadesDevoluciones " + variedadesDevolucionesListOldVariedadesDevoluciones + " since its idusuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<VariedadesIngresos> attachedVariedadesIngresosListNew = new ArrayList<VariedadesIngresos>();
            for (VariedadesIngresos variedadesIngresosListNewVariedadesIngresosToAttach : variedadesIngresosListNew) {
                variedadesIngresosListNewVariedadesIngresosToAttach = em.getReference(variedadesIngresosListNewVariedadesIngresosToAttach.getClass(), variedadesIngresosListNewVariedadesIngresosToAttach.getId());
                attachedVariedadesIngresosListNew.add(variedadesIngresosListNewVariedadesIngresosToAttach);
            }
            variedadesIngresosListNew = attachedVariedadesIngresosListNew;
            userLog.setVariedadesIngresosList(variedadesIngresosListNew);
            List<VariedadesEgresos> attachedVariedadesEgresosListNew = new ArrayList<VariedadesEgresos>();
            for (VariedadesEgresos variedadesEgresosListNewVariedadesEgresosToAttach : variedadesEgresosListNew) {
                variedadesEgresosListNewVariedadesEgresosToAttach = em.getReference(variedadesEgresosListNewVariedadesEgresosToAttach.getClass(), variedadesEgresosListNewVariedadesEgresosToAttach.getId());
                attachedVariedadesEgresosListNew.add(variedadesEgresosListNewVariedadesEgresosToAttach);
            }
            variedadesEgresosListNew = attachedVariedadesEgresosListNew;
            userLog.setVariedadesEgresosList(variedadesEgresosListNew);
            List<VariedadesSuministro> attachedVariedadesSuministroListNew = new ArrayList<VariedadesSuministro>();
            for (VariedadesSuministro variedadesSuministroListNewVariedadesSuministroToAttach : variedadesSuministroListNew) {
                variedadesSuministroListNewVariedadesSuministroToAttach = em.getReference(variedadesSuministroListNewVariedadesSuministroToAttach.getClass(), variedadesSuministroListNewVariedadesSuministroToAttach.getId());
                attachedVariedadesSuministroListNew.add(variedadesSuministroListNewVariedadesSuministroToAttach);
            }
            variedadesSuministroListNew = attachedVariedadesSuministroListNew;
            userLog.setVariedadesSuministroList(variedadesSuministroListNew);
            List<UserInfo> attachedUserInfoListNew = new ArrayList<UserInfo>();
            for (UserInfo userInfoListNewUserInfoToAttach : userInfoListNew) {
                userInfoListNewUserInfoToAttach = em.getReference(userInfoListNewUserInfoToAttach.getClass(), userInfoListNewUserInfoToAttach.getId());
                attachedUserInfoListNew.add(userInfoListNewUserInfoToAttach);
            }
            userInfoListNew = attachedUserInfoListNew;
            userLog.setUserInfoList(userInfoListNew);
            List<VariedadesVentas> attachedVariedadesVentasListNew = new ArrayList<VariedadesVentas>();
            for (VariedadesVentas variedadesVentasListNewVariedadesVentasToAttach : variedadesVentasListNew) {
                variedadesVentasListNewVariedadesVentasToAttach = em.getReference(variedadesVentasListNewVariedadesVentasToAttach.getClass(), variedadesVentasListNewVariedadesVentasToAttach.getId());
                attachedVariedadesVentasListNew.add(variedadesVentasListNewVariedadesVentasToAttach);
            }
            variedadesVentasListNew = attachedVariedadesVentasListNew;
            userLog.setVariedadesVentasList(variedadesVentasListNew);
            List<UserPermisos> attachedUserPermisosListNew = new ArrayList<UserPermisos>();
            for (UserPermisos userPermisosListNewUserPermisosToAttach : userPermisosListNew) {
                userPermisosListNewUserPermisosToAttach = em.getReference(userPermisosListNewUserPermisosToAttach.getClass(), userPermisosListNewUserPermisosToAttach.getId());
                attachedUserPermisosListNew.add(userPermisosListNewUserPermisosToAttach);
            }
            userPermisosListNew = attachedUserPermisosListNew;
            userLog.setUserPermisosList(userPermisosListNew);
            List<VariedadesDevoluciones> attachedVariedadesDevolucionesListNew = new ArrayList<VariedadesDevoluciones>();
            for (VariedadesDevoluciones variedadesDevolucionesListNewVariedadesDevolucionesToAttach : variedadesDevolucionesListNew) {
                variedadesDevolucionesListNewVariedadesDevolucionesToAttach = em.getReference(variedadesDevolucionesListNewVariedadesDevolucionesToAttach.getClass(), variedadesDevolucionesListNewVariedadesDevolucionesToAttach.getId());
                attachedVariedadesDevolucionesListNew.add(variedadesDevolucionesListNewVariedadesDevolucionesToAttach);
            }
            variedadesDevolucionesListNew = attachedVariedadesDevolucionesListNew;
            userLog.setVariedadesDevolucionesList(variedadesDevolucionesListNew);
            userLog = em.merge(userLog);
            for (VariedadesIngresos variedadesIngresosListNewVariedadesIngresos : variedadesIngresosListNew) {
                if (!variedadesIngresosListOld.contains(variedadesIngresosListNewVariedadesIngresos)) {
                    UserLog oldIdusuarioOfVariedadesIngresosListNewVariedadesIngresos = variedadesIngresosListNewVariedadesIngresos.getIdusuario();
                    variedadesIngresosListNewVariedadesIngresos.setIdusuario(userLog);
                    variedadesIngresosListNewVariedadesIngresos = em.merge(variedadesIngresosListNewVariedadesIngresos);
                    if (oldIdusuarioOfVariedadesIngresosListNewVariedadesIngresos != null && !oldIdusuarioOfVariedadesIngresosListNewVariedadesIngresos.equals(userLog)) {
                        oldIdusuarioOfVariedadesIngresosListNewVariedadesIngresos.getVariedadesIngresosList().remove(variedadesIngresosListNewVariedadesIngresos);
                        oldIdusuarioOfVariedadesIngresosListNewVariedadesIngresos = em.merge(oldIdusuarioOfVariedadesIngresosListNewVariedadesIngresos);
                    }
                }
            }
            for (VariedadesEgresos variedadesEgresosListNewVariedadesEgresos : variedadesEgresosListNew) {
                if (!variedadesEgresosListOld.contains(variedadesEgresosListNewVariedadesEgresos)) {
                    UserLog oldIdusuarioOfVariedadesEgresosListNewVariedadesEgresos = variedadesEgresosListNewVariedadesEgresos.getIdusuario();
                    variedadesEgresosListNewVariedadesEgresos.setIdusuario(userLog);
                    variedadesEgresosListNewVariedadesEgresos = em.merge(variedadesEgresosListNewVariedadesEgresos);
                    if (oldIdusuarioOfVariedadesEgresosListNewVariedadesEgresos != null && !oldIdusuarioOfVariedadesEgresosListNewVariedadesEgresos.equals(userLog)) {
                        oldIdusuarioOfVariedadesEgresosListNewVariedadesEgresos.getVariedadesEgresosList().remove(variedadesEgresosListNewVariedadesEgresos);
                        oldIdusuarioOfVariedadesEgresosListNewVariedadesEgresos = em.merge(oldIdusuarioOfVariedadesEgresosListNewVariedadesEgresos);
                    }
                }
            }
            for (VariedadesSuministro variedadesSuministroListNewVariedadesSuministro : variedadesSuministroListNew) {
                if (!variedadesSuministroListOld.contains(variedadesSuministroListNewVariedadesSuministro)) {
                    UserLog oldIdusuarioOfVariedadesSuministroListNewVariedadesSuministro = variedadesSuministroListNewVariedadesSuministro.getIdusuario();
                    variedadesSuministroListNewVariedadesSuministro.setIdusuario(userLog);
                    variedadesSuministroListNewVariedadesSuministro = em.merge(variedadesSuministroListNewVariedadesSuministro);
                    if (oldIdusuarioOfVariedadesSuministroListNewVariedadesSuministro != null && !oldIdusuarioOfVariedadesSuministroListNewVariedadesSuministro.equals(userLog)) {
                        oldIdusuarioOfVariedadesSuministroListNewVariedadesSuministro.getVariedadesSuministroList().remove(variedadesSuministroListNewVariedadesSuministro);
                        oldIdusuarioOfVariedadesSuministroListNewVariedadesSuministro = em.merge(oldIdusuarioOfVariedadesSuministroListNewVariedadesSuministro);
                    }
                }
            }
            for (UserInfo userInfoListNewUserInfo : userInfoListNew) {
                if (!userInfoListOld.contains(userInfoListNewUserInfo)) {
                    UserLog oldIdlogOfUserInfoListNewUserInfo = userInfoListNewUserInfo.getIdlog();
                    userInfoListNewUserInfo.setIdlog(userLog);
                    userInfoListNewUserInfo = em.merge(userInfoListNewUserInfo);
                    if (oldIdlogOfUserInfoListNewUserInfo != null && !oldIdlogOfUserInfoListNewUserInfo.equals(userLog)) {
                        oldIdlogOfUserInfoListNewUserInfo.getUserInfoList().remove(userInfoListNewUserInfo);
                        oldIdlogOfUserInfoListNewUserInfo = em.merge(oldIdlogOfUserInfoListNewUserInfo);
                    }
                }
            }
            for (VariedadesVentas variedadesVentasListNewVariedadesVentas : variedadesVentasListNew) {
                if (!variedadesVentasListOld.contains(variedadesVentasListNewVariedadesVentas)) {
                    UserLog oldIdusuarioOfVariedadesVentasListNewVariedadesVentas = variedadesVentasListNewVariedadesVentas.getIdusuario();
                    variedadesVentasListNewVariedadesVentas.setIdusuario(userLog);
                    variedadesVentasListNewVariedadesVentas = em.merge(variedadesVentasListNewVariedadesVentas);
                    if (oldIdusuarioOfVariedadesVentasListNewVariedadesVentas != null && !oldIdusuarioOfVariedadesVentasListNewVariedadesVentas.equals(userLog)) {
                        oldIdusuarioOfVariedadesVentasListNewVariedadesVentas.getVariedadesVentasList().remove(variedadesVentasListNewVariedadesVentas);
                        oldIdusuarioOfVariedadesVentasListNewVariedadesVentas = em.merge(oldIdusuarioOfVariedadesVentasListNewVariedadesVentas);
                    }
                }
            }
            for (UserPermisos userPermisosListNewUserPermisos : userPermisosListNew) {
                if (!userPermisosListOld.contains(userPermisosListNewUserPermisos)) {
                    UserLog oldIduserOfUserPermisosListNewUserPermisos = userPermisosListNewUserPermisos.getIduser();
                    userPermisosListNewUserPermisos.setIduser(userLog);
                    userPermisosListNewUserPermisos = em.merge(userPermisosListNewUserPermisos);
                    if (oldIduserOfUserPermisosListNewUserPermisos != null && !oldIduserOfUserPermisosListNewUserPermisos.equals(userLog)) {
                        oldIduserOfUserPermisosListNewUserPermisos.getUserPermisosList().remove(userPermisosListNewUserPermisos);
                        oldIduserOfUserPermisosListNewUserPermisos = em.merge(oldIduserOfUserPermisosListNewUserPermisos);
                    }
                }
            }
            for (VariedadesDevoluciones variedadesDevolucionesListNewVariedadesDevoluciones : variedadesDevolucionesListNew) {
                if (!variedadesDevolucionesListOld.contains(variedadesDevolucionesListNewVariedadesDevoluciones)) {
                    UserLog oldIdusuarioOfVariedadesDevolucionesListNewVariedadesDevoluciones = variedadesDevolucionesListNewVariedadesDevoluciones.getIdusuario();
                    variedadesDevolucionesListNewVariedadesDevoluciones.setIdusuario(userLog);
                    variedadesDevolucionesListNewVariedadesDevoluciones = em.merge(variedadesDevolucionesListNewVariedadesDevoluciones);
                    if (oldIdusuarioOfVariedadesDevolucionesListNewVariedadesDevoluciones != null && !oldIdusuarioOfVariedadesDevolucionesListNewVariedadesDevoluciones.equals(userLog)) {
                        oldIdusuarioOfVariedadesDevolucionesListNewVariedadesDevoluciones.getVariedadesDevolucionesList().remove(variedadesDevolucionesListNewVariedadesDevoluciones);
                        oldIdusuarioOfVariedadesDevolucionesListNewVariedadesDevoluciones = em.merge(oldIdusuarioOfVariedadesDevolucionesListNewVariedadesDevoluciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userLog.getId();
                if (findUserLog(id) == null) {
                    throw new NonexistentEntityException("The userLog with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog userLog;
            try {
                userLog = em.getReference(UserLog.class, id);
                userLog.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userLog with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VariedadesIngresos> variedadesIngresosListOrphanCheck = userLog.getVariedadesIngresosList();
            for (VariedadesIngresos variedadesIngresosListOrphanCheckVariedadesIngresos : variedadesIngresosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserLog (" + userLog + ") cannot be destroyed since the VariedadesIngresos " + variedadesIngresosListOrphanCheckVariedadesIngresos + " in its variedadesIngresosList field has a non-nullable idusuario field.");
            }
            List<VariedadesEgresos> variedadesEgresosListOrphanCheck = userLog.getVariedadesEgresosList();
            for (VariedadesEgresos variedadesEgresosListOrphanCheckVariedadesEgresos : variedadesEgresosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserLog (" + userLog + ") cannot be destroyed since the VariedadesEgresos " + variedadesEgresosListOrphanCheckVariedadesEgresos + " in its variedadesEgresosList field has a non-nullable idusuario field.");
            }
            List<VariedadesSuministro> variedadesSuministroListOrphanCheck = userLog.getVariedadesSuministroList();
            for (VariedadesSuministro variedadesSuministroListOrphanCheckVariedadesSuministro : variedadesSuministroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserLog (" + userLog + ") cannot be destroyed since the VariedadesSuministro " + variedadesSuministroListOrphanCheckVariedadesSuministro + " in its variedadesSuministroList field has a non-nullable idusuario field.");
            }
            List<UserInfo> userInfoListOrphanCheck = userLog.getUserInfoList();
            for (UserInfo userInfoListOrphanCheckUserInfo : userInfoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserLog (" + userLog + ") cannot be destroyed since the UserInfo " + userInfoListOrphanCheckUserInfo + " in its userInfoList field has a non-nullable idlog field.");
            }
            List<VariedadesVentas> variedadesVentasListOrphanCheck = userLog.getVariedadesVentasList();
            for (VariedadesVentas variedadesVentasListOrphanCheckVariedadesVentas : variedadesVentasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserLog (" + userLog + ") cannot be destroyed since the VariedadesVentas " + variedadesVentasListOrphanCheckVariedadesVentas + " in its variedadesVentasList field has a non-nullable idusuario field.");
            }
            List<UserPermisos> userPermisosListOrphanCheck = userLog.getUserPermisosList();
            for (UserPermisos userPermisosListOrphanCheckUserPermisos : userPermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserLog (" + userLog + ") cannot be destroyed since the UserPermisos " + userPermisosListOrphanCheckUserPermisos + " in its userPermisosList field has a non-nullable iduser field.");
            }
            List<VariedadesDevoluciones> variedadesDevolucionesListOrphanCheck = userLog.getVariedadesDevolucionesList();
            for (VariedadesDevoluciones variedadesDevolucionesListOrphanCheckVariedadesDevoluciones : variedadesDevolucionesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UserLog (" + userLog + ") cannot be destroyed since the VariedadesDevoluciones " + variedadesDevolucionesListOrphanCheckVariedadesDevoluciones + " in its variedadesDevolucionesList field has a non-nullable idusuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(userLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserLog> findUserLogEntities() {
        return findUserLogEntities(true, -1, -1);
    }

    public List<UserLog> findUserLogEntities(int maxResults, int firstResult) {
        return findUserLogEntities(false, maxResults, firstResult);
    }

    private List<UserLog> findUserLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserLog.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public UserLog findUserLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserLog.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserLog> rt = cq.from(UserLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
