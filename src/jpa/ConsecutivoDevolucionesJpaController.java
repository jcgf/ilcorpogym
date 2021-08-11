/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.ConsecutivoDevoluciones;
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
public class ConsecutivoDevolucionesJpaController implements Serializable {

    public ConsecutivoDevolucionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsecutivoDevoluciones consecutivoDevoluciones) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(consecutivoDevoluciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsecutivoDevoluciones consecutivoDevoluciones) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            consecutivoDevoluciones = em.merge(consecutivoDevoluciones);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consecutivoDevoluciones.getId();
                if (findConsecutivoDevoluciones(id) == null) {
                    throw new NonexistentEntityException("The consecutivoDevoluciones with id " + id + " no longer exists.");
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
            ConsecutivoDevoluciones consecutivoDevoluciones;
            try {
                consecutivoDevoluciones = em.getReference(ConsecutivoDevoluciones.class, id);
                consecutivoDevoluciones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consecutivoDevoluciones with id " + id + " no longer exists.", enfe);
            }
            em.remove(consecutivoDevoluciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConsecutivoDevoluciones> findConsecutivoDevolucionesEntities() {
        return findConsecutivoDevolucionesEntities(true, -1, -1);
    }

    public List<ConsecutivoDevoluciones> findConsecutivoDevolucionesEntities(int maxResults, int firstResult) {
        return findConsecutivoDevolucionesEntities(false, maxResults, firstResult);
    }

    private List<ConsecutivoDevoluciones> findConsecutivoDevolucionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsecutivoDevoluciones.class));
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

    public ConsecutivoDevoluciones findConsecutivoDevoluciones(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsecutivoDevoluciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsecutivoDevolucionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsecutivoDevoluciones> rt = cq.from(ConsecutivoDevoluciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
