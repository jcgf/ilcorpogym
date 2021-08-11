/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.GymTerceros;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class GymTercerosJpaController implements Serializable {

    public GymTercerosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GymTerceros gymTerceros) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(gymTerceros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GymTerceros gymTerceros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            gymTerceros = em.merge(gymTerceros);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gymTerceros.getId();
                if (findGymTerceros(id) == null) {
                    throw new NonexistentEntityException("The gymTerceros with id " + id + " no longer exists.");
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
            GymTerceros gymTerceros;
            try {
                gymTerceros = em.getReference(GymTerceros.class, id);
                gymTerceros.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gymTerceros with id " + id + " no longer exists.", enfe);
            }
            em.remove(gymTerceros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GymTerceros> findGymTercerosEntities() {
        return findGymTercerosEntities(true, -1, -1);
    }

    public List<GymTerceros> findGymTercerosEntities(int maxResults, int firstResult) {
        return findGymTercerosEntities(false, maxResults, firstResult);
    }

    private List<GymTerceros> findGymTercerosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GymTerceros.class));
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

    public GymTerceros findGymTerceros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GymTerceros.class, id);
        } finally {
            em.close();
        }
    }

    public int getGymTercerosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GymTerceros> rt = cq.from(GymTerceros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<GymTerceros> obtenerTerceroByNit(String nit) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM GymTerceros t WHERE t.nit=:nit AND t.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter("nit", nit)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<GymTerceros> obtenerTercerosActivos() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM GymTerceros t WHERE t.estado='1'")
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
}
