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
import entity.GymAsignados;
import entity.GymContdias;
import entity.GymUsuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class GymContdiasJpaController implements Serializable {

    public GymContdiasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GymContdias gymContdias) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymAsignados idasignacion = gymContdias.getIdasignacion();
            if (idasignacion != null) {
                idasignacion = em.getReference(idasignacion.getClass(), idasignacion.getId());
                gymContdias.setIdasignacion(idasignacion);
            }
            GymUsuarios idusuario = gymContdias.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getId());
                gymContdias.setIdusuario(idusuario);
            }
            em.persist(gymContdias);
            if (idasignacion != null) {
                idasignacion.getGymContdiasList().add(gymContdias);
                idasignacion = em.merge(idasignacion);
            }
            if (idusuario != null) {
                idusuario.getGymContdiasList().add(gymContdias);
                idusuario = em.merge(idusuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GymContdias gymContdias) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymContdias persistentGymContdias = em.find(GymContdias.class, gymContdias.getId());
            GymAsignados idasignacionOld = persistentGymContdias.getIdasignacion();
            GymAsignados idasignacionNew = gymContdias.getIdasignacion();
            GymUsuarios idusuarioOld = persistentGymContdias.getIdusuario();
            GymUsuarios idusuarioNew = gymContdias.getIdusuario();
            if (idasignacionNew != null) {
                idasignacionNew = em.getReference(idasignacionNew.getClass(), idasignacionNew.getId());
                gymContdias.setIdasignacion(idasignacionNew);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getId());
                gymContdias.setIdusuario(idusuarioNew);
            }
            gymContdias = em.merge(gymContdias);
            if (idasignacionOld != null && !idasignacionOld.equals(idasignacionNew)) {
                idasignacionOld.getGymContdiasList().remove(gymContdias);
                idasignacionOld = em.merge(idasignacionOld);
            }
            if (idasignacionNew != null && !idasignacionNew.equals(idasignacionOld)) {
                idasignacionNew.getGymContdiasList().add(gymContdias);
                idasignacionNew = em.merge(idasignacionNew);
            }
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getGymContdiasList().remove(gymContdias);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getGymContdiasList().add(gymContdias);
                idusuarioNew = em.merge(idusuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gymContdias.getId();
                if (findGymContdias(id) == null) {
                    throw new NonexistentEntityException("The gymContdias with id " + id + " no longer exists.");
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
            GymContdias gymContdias;
            try {
                gymContdias = em.getReference(GymContdias.class, id);
                gymContdias.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gymContdias with id " + id + " no longer exists.", enfe);
            }
            GymAsignados idasignacion = gymContdias.getIdasignacion();
            if (idasignacion != null) {
                idasignacion.getGymContdiasList().remove(gymContdias);
                idasignacion = em.merge(idasignacion);
            }
            GymUsuarios idusuario = gymContdias.getIdusuario();
            if (idusuario != null) {
                idusuario.getGymContdiasList().remove(gymContdias);
                idusuario = em.merge(idusuario);
            }
            em.remove(gymContdias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GymContdias> findGymContdiasEntities() {
        return findGymContdiasEntities(true, -1, -1);
    }

    public List<GymContdias> findGymContdiasEntities(int maxResults, int firstResult) {
        return findGymContdiasEntities(false, maxResults, firstResult);
    }

    private List<GymContdias> findGymContdiasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GymContdias.class));
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

    public GymContdias findGymContdias(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GymContdias.class, id);
        } finally {
            em.close();
        }
    }

    public int getGymContdiasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GymContdias> rt = cq.from(GymContdias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
