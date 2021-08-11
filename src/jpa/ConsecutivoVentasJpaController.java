/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.ConsecutivoVentas;
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
public class ConsecutivoVentasJpaController implements Serializable {

    public ConsecutivoVentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsecutivoVentas consecutivoVentas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(consecutivoVentas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsecutivoVentas consecutivoVentas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            consecutivoVentas = em.merge(consecutivoVentas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consecutivoVentas.getId();
                if (findConsecutivoVentas(id) == null) {
                    throw new NonexistentEntityException("The consecutivoVentas with id " + id + " no longer exists.");
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
            ConsecutivoVentas consecutivoVentas;
            try {
                consecutivoVentas = em.getReference(ConsecutivoVentas.class, id);
                consecutivoVentas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consecutivoVentas with id " + id + " no longer exists.", enfe);
            }
            em.remove(consecutivoVentas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConsecutivoVentas> findConsecutivoVentasEntities() {
        return findConsecutivoVentasEntities(true, -1, -1);
    }

    public List<ConsecutivoVentas> findConsecutivoVentasEntities(int maxResults, int firstResult) {
        return findConsecutivoVentasEntities(false, maxResults, firstResult);
    }

    private List<ConsecutivoVentas> findConsecutivoVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsecutivoVentas.class));
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

    public ConsecutivoVentas findConsecutivoVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsecutivoVentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsecutivoVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsecutivoVentas> rt = cq.from(ConsecutivoVentas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
