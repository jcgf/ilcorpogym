/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.ConsecutivoIngresos;
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
public class ConsecutivoIngresosJpaController implements Serializable {

    public ConsecutivoIngresosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsecutivoIngresos consecutivoIngresos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(consecutivoIngresos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsecutivoIngresos consecutivoIngresos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            consecutivoIngresos = em.merge(consecutivoIngresos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consecutivoIngresos.getId();
                if (findConsecutivoIngresos(id) == null) {
                    throw new NonexistentEntityException("The consecutivoIngresos with id " + id + " no longer exists.");
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
            ConsecutivoIngresos consecutivoIngresos;
            try {
                consecutivoIngresos = em.getReference(ConsecutivoIngresos.class, id);
                consecutivoIngresos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consecutivoIngresos with id " + id + " no longer exists.", enfe);
            }
            em.remove(consecutivoIngresos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConsecutivoIngresos> findConsecutivoIngresosEntities() {
        return findConsecutivoIngresosEntities(true, -1, -1);
    }

    public List<ConsecutivoIngresos> findConsecutivoIngresosEntities(int maxResults, int firstResult) {
        return findConsecutivoIngresosEntities(false, maxResults, firstResult);
    }

    private List<ConsecutivoIngresos> findConsecutivoIngresosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsecutivoIngresos.class));
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

    public ConsecutivoIngresos findConsecutivoIngresos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsecutivoIngresos.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsecutivoIngresosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsecutivoIngresos> rt = cq.from(ConsecutivoIngresos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
