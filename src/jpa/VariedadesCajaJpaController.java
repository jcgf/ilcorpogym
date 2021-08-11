/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.VariedadesCaja;
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
public class VariedadesCajaJpaController implements Serializable {

    public VariedadesCajaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariedadesCaja variedadesCaja) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(variedadesCaja);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariedadesCaja variedadesCaja) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            variedadesCaja = em.merge(variedadesCaja);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variedadesCaja.getId();
                if (findVariedadesCaja(id) == null) {
                    throw new NonexistentEntityException("The variedadesCaja with id " + id + " no longer exists.");
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
            VariedadesCaja variedadesCaja;
            try {
                variedadesCaja = em.getReference(VariedadesCaja.class, id);
                variedadesCaja.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variedadesCaja with id " + id + " no longer exists.", enfe);
            }
            em.remove(variedadesCaja);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariedadesCaja> findVariedadesCajaEntities() {
        return findVariedadesCajaEntities(true, -1, -1);
    }

    public List<VariedadesCaja> findVariedadesCajaEntities(int maxResults, int firstResult) {
        return findVariedadesCajaEntities(false, maxResults, firstResult);
    }

    private List<VariedadesCaja> findVariedadesCajaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariedadesCaja.class));
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

    public VariedadesCaja findVariedadesCaja(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariedadesCaja.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariedadesCajaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariedadesCaja> rt = cq.from(VariedadesCaja.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
