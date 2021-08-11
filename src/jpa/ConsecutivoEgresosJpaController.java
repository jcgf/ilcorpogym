/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.ConsecutivoEgresos;
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
public class ConsecutivoEgresosJpaController implements Serializable {

    public ConsecutivoEgresosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsecutivoEgresos consecutivoEgresos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(consecutivoEgresos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsecutivoEgresos consecutivoEgresos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            consecutivoEgresos = em.merge(consecutivoEgresos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consecutivoEgresos.getId();
                if (findConsecutivoEgresos(id) == null) {
                    throw new NonexistentEntityException("The consecutivoEgresos with id " + id + " no longer exists.");
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
            ConsecutivoEgresos consecutivoEgresos;
            try {
                consecutivoEgresos = em.getReference(ConsecutivoEgresos.class, id);
                consecutivoEgresos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consecutivoEgresos with id " + id + " no longer exists.", enfe);
            }
            em.remove(consecutivoEgresos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConsecutivoEgresos> findConsecutivoEgresosEntities() {
        return findConsecutivoEgresosEntities(true, -1, -1);
    }

    public List<ConsecutivoEgresos> findConsecutivoEgresosEntities(int maxResults, int firstResult) {
        return findConsecutivoEgresosEntities(false, maxResults, firstResult);
    }

    private List<ConsecutivoEgresos> findConsecutivoEgresosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsecutivoEgresos.class));
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

    public ConsecutivoEgresos findConsecutivoEgresos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsecutivoEgresos.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsecutivoEgresosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsecutivoEgresos> rt = cq.from(ConsecutivoEgresos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
