/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.ConsecutivoSuministros;
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
public class ConsecutivoSuministrosJpaController implements Serializable {

    public ConsecutivoSuministrosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsecutivoSuministros consecutivoSuministros) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(consecutivoSuministros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsecutivoSuministros consecutivoSuministros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            consecutivoSuministros = em.merge(consecutivoSuministros);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consecutivoSuministros.getId();
                if (findConsecutivoSuministros(id) == null) {
                    throw new NonexistentEntityException("The consecutivoSuministros with id " + id + " no longer exists.");
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
            ConsecutivoSuministros consecutivoSuministros;
            try {
                consecutivoSuministros = em.getReference(ConsecutivoSuministros.class, id);
                consecutivoSuministros.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consecutivoSuministros with id " + id + " no longer exists.", enfe);
            }
            em.remove(consecutivoSuministros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConsecutivoSuministros> findConsecutivoSuministrosEntities() {
        return findConsecutivoSuministrosEntities(true, -1, -1);
    }

    public List<ConsecutivoSuministros> findConsecutivoSuministrosEntities(int maxResults, int firstResult) {
        return findConsecutivoSuministrosEntities(false, maxResults, firstResult);
    }

    private List<ConsecutivoSuministros> findConsecutivoSuministrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsecutivoSuministros.class));
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

    public ConsecutivoSuministros findConsecutivoSuministros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsecutivoSuministros.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsecutivoSuministrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsecutivoSuministros> rt = cq.from(ConsecutivoSuministros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
