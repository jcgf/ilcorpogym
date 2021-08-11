/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.GymPermisos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.UserPermisos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class GymPermisosJpaController implements Serializable {

    public GymPermisosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GymPermisos gymPermisos) {
        if (gymPermisos.getUserPermisosList() == null) {
            gymPermisos.setUserPermisosList(new ArrayList<UserPermisos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UserPermisos> attachedUserPermisosList = new ArrayList<UserPermisos>();
            for (UserPermisos userPermisosListUserPermisosToAttach : gymPermisos.getUserPermisosList()) {
                userPermisosListUserPermisosToAttach = em.getReference(userPermisosListUserPermisosToAttach.getClass(), userPermisosListUserPermisosToAttach.getId());
                attachedUserPermisosList.add(userPermisosListUserPermisosToAttach);
            }
            gymPermisos.setUserPermisosList(attachedUserPermisosList);
            em.persist(gymPermisos);
            for (UserPermisos userPermisosListUserPermisos : gymPermisos.getUserPermisosList()) {
                GymPermisos oldIdpermisoOfUserPermisosListUserPermisos = userPermisosListUserPermisos.getIdpermiso();
                userPermisosListUserPermisos.setIdpermiso(gymPermisos);
                userPermisosListUserPermisos = em.merge(userPermisosListUserPermisos);
                if (oldIdpermisoOfUserPermisosListUserPermisos != null) {
                    oldIdpermisoOfUserPermisosListUserPermisos.getUserPermisosList().remove(userPermisosListUserPermisos);
                    oldIdpermisoOfUserPermisosListUserPermisos = em.merge(oldIdpermisoOfUserPermisosListUserPermisos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GymPermisos gymPermisos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymPermisos persistentGymPermisos = em.find(GymPermisos.class, gymPermisos.getId());
            List<UserPermisos> userPermisosListOld = persistentGymPermisos.getUserPermisosList();
            List<UserPermisos> userPermisosListNew = gymPermisos.getUserPermisosList();
            List<String> illegalOrphanMessages = null;
            for (UserPermisos userPermisosListOldUserPermisos : userPermisosListOld) {
                if (!userPermisosListNew.contains(userPermisosListOldUserPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserPermisos " + userPermisosListOldUserPermisos + " since its idpermiso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<UserPermisos> attachedUserPermisosListNew = new ArrayList<UserPermisos>();
            for (UserPermisos userPermisosListNewUserPermisosToAttach : userPermisosListNew) {
                userPermisosListNewUserPermisosToAttach = em.getReference(userPermisosListNewUserPermisosToAttach.getClass(), userPermisosListNewUserPermisosToAttach.getId());
                attachedUserPermisosListNew.add(userPermisosListNewUserPermisosToAttach);
            }
            userPermisosListNew = attachedUserPermisosListNew;
            gymPermisos.setUserPermisosList(userPermisosListNew);
            gymPermisos = em.merge(gymPermisos);
            for (UserPermisos userPermisosListNewUserPermisos : userPermisosListNew) {
                if (!userPermisosListOld.contains(userPermisosListNewUserPermisos)) {
                    GymPermisos oldIdpermisoOfUserPermisosListNewUserPermisos = userPermisosListNewUserPermisos.getIdpermiso();
                    userPermisosListNewUserPermisos.setIdpermiso(gymPermisos);
                    userPermisosListNewUserPermisos = em.merge(userPermisosListNewUserPermisos);
                    if (oldIdpermisoOfUserPermisosListNewUserPermisos != null && !oldIdpermisoOfUserPermisosListNewUserPermisos.equals(gymPermisos)) {
                        oldIdpermisoOfUserPermisosListNewUserPermisos.getUserPermisosList().remove(userPermisosListNewUserPermisos);
                        oldIdpermisoOfUserPermisosListNewUserPermisos = em.merge(oldIdpermisoOfUserPermisosListNewUserPermisos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gymPermisos.getId();
                if (findGymPermisos(id) == null) {
                    throw new NonexistentEntityException("The gymPermisos with id " + id + " no longer exists.");
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
            GymPermisos gymPermisos;
            try {
                gymPermisos = em.getReference(GymPermisos.class, id);
                gymPermisos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gymPermisos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UserPermisos> userPermisosListOrphanCheck = gymPermisos.getUserPermisosList();
            for (UserPermisos userPermisosListOrphanCheckUserPermisos : userPermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GymPermisos (" + gymPermisos + ") cannot be destroyed since the UserPermisos " + userPermisosListOrphanCheckUserPermisos + " in its userPermisosList field has a non-nullable idpermiso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gymPermisos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GymPermisos> findGymPermisosEntities() {
        return findGymPermisosEntities(true, -1, -1);
    }

    public List<GymPermisos> findGymPermisosEntities(int maxResults, int firstResult) {
        return findGymPermisosEntities(false, maxResults, firstResult);
    }

    private List<GymPermisos> findGymPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GymPermisos.class));
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

    public GymPermisos findGymPermisos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GymPermisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getGymPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GymPermisos> rt = cq.from(GymPermisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
