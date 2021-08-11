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
import entity.UserLog;
import entity.GymPermisos;
import entity.UserPermisos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class UserPermisosJpaController implements Serializable {

    public UserPermisosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserPermisos userPermisos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog iduser = userPermisos.getIduser();
            if (iduser != null) {
                iduser = em.getReference(iduser.getClass(), iduser.getId());
                userPermisos.setIduser(iduser);
            }
            GymPermisos idpermiso = userPermisos.getIdpermiso();
            if (idpermiso != null) {
                idpermiso = em.getReference(idpermiso.getClass(), idpermiso.getId());
                userPermisos.setIdpermiso(idpermiso);
            }
            em.persist(userPermisos);
            if (iduser != null) {
                iduser.getUserPermisosList().add(userPermisos);
                iduser = em.merge(iduser);
            }
            if (idpermiso != null) {
                idpermiso.getUserPermisosList().add(userPermisos);
                idpermiso = em.merge(idpermiso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserPermisos userPermisos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserPermisos persistentUserPermisos = em.find(UserPermisos.class, userPermisos.getId());
            UserLog iduserOld = persistentUserPermisos.getIduser();
            UserLog iduserNew = userPermisos.getIduser();
            GymPermisos idpermisoOld = persistentUserPermisos.getIdpermiso();
            GymPermisos idpermisoNew = userPermisos.getIdpermiso();
            if (iduserNew != null) {
                iduserNew = em.getReference(iduserNew.getClass(), iduserNew.getId());
                userPermisos.setIduser(iduserNew);
            }
            if (idpermisoNew != null) {
                idpermisoNew = em.getReference(idpermisoNew.getClass(), idpermisoNew.getId());
                userPermisos.setIdpermiso(idpermisoNew);
            }
            userPermisos = em.merge(userPermisos);
            if (iduserOld != null && !iduserOld.equals(iduserNew)) {
                iduserOld.getUserPermisosList().remove(userPermisos);
                iduserOld = em.merge(iduserOld);
            }
            if (iduserNew != null && !iduserNew.equals(iduserOld)) {
                iduserNew.getUserPermisosList().add(userPermisos);
                iduserNew = em.merge(iduserNew);
            }
            if (idpermisoOld != null && !idpermisoOld.equals(idpermisoNew)) {
                idpermisoOld.getUserPermisosList().remove(userPermisos);
                idpermisoOld = em.merge(idpermisoOld);
            }
            if (idpermisoNew != null && !idpermisoNew.equals(idpermisoOld)) {
                idpermisoNew.getUserPermisosList().add(userPermisos);
                idpermisoNew = em.merge(idpermisoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userPermisos.getId();
                if (findUserPermisos(id) == null) {
                    throw new NonexistentEntityException("The userPermisos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserPermisos userPermisos;
            try {
                userPermisos = em.getReference(UserPermisos.class, id);
                userPermisos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userPermisos with id " + id + " no longer exists.", enfe);
            }
            UserLog iduser = userPermisos.getIduser();
            if (iduser != null) {
                iduser.getUserPermisosList().remove(userPermisos);
                iduser = em.merge(iduser);
            }
            GymPermisos idpermiso = userPermisos.getIdpermiso();
            if (idpermiso != null) {
                idpermiso.getUserPermisosList().remove(userPermisos);
                idpermiso = em.merge(idpermiso);
            }
            em.remove(userPermisos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserPermisos> findUserPermisosEntities() {
        return findUserPermisosEntities(true, -1, -1);
    }

    public List<UserPermisos> findUserPermisosEntities(int maxResults, int firstResult) {
        return findUserPermisosEntities(false, maxResults, firstResult);
    }

    private List<UserPermisos> findUserPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserPermisos.class));
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

    public UserPermisos findUserPermisos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserPermisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserPermisos> rt = cq.from(UserPermisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
