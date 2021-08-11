/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.EgresoItem;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.VariedadesEgresos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class EgresoItemJpaController implements Serializable {

    public EgresoItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EgresoItem egresoItem) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesEgresos idegreso = egresoItem.getIdegreso();
            if (idegreso != null) {
                idegreso = em.getReference(idegreso.getClass(), idegreso.getId());
                egresoItem.setIdegreso(idegreso);
            }
            em.persist(egresoItem);
            if (idegreso != null) {
                idegreso.getEgresoItemList().add(egresoItem);
                idegreso = em.merge(idegreso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EgresoItem egresoItem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EgresoItem persistentEgresoItem = em.find(EgresoItem.class, egresoItem.getId());
            VariedadesEgresos idegresoOld = persistentEgresoItem.getIdegreso();
            VariedadesEgresos idegresoNew = egresoItem.getIdegreso();
            if (idegresoNew != null) {
                idegresoNew = em.getReference(idegresoNew.getClass(), idegresoNew.getId());
                egresoItem.setIdegreso(idegresoNew);
            }
            egresoItem = em.merge(egresoItem);
            if (idegresoOld != null && !idegresoOld.equals(idegresoNew)) {
                idegresoOld.getEgresoItemList().remove(egresoItem);
                idegresoOld = em.merge(idegresoOld);
            }
            if (idegresoNew != null && !idegresoNew.equals(idegresoOld)) {
                idegresoNew.getEgresoItemList().add(egresoItem);
                idegresoNew = em.merge(idegresoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = egresoItem.getId();
                if (findEgresoItem(id) == null) {
                    throw new NonexistentEntityException("The egresoItem with id " + id + " no longer exists.");
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
            EgresoItem egresoItem;
            try {
                egresoItem = em.getReference(EgresoItem.class, id);
                egresoItem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The egresoItem with id " + id + " no longer exists.", enfe);
            }
            VariedadesEgresos idegreso = egresoItem.getIdegreso();
            if (idegreso != null) {
                idegreso.getEgresoItemList().remove(egresoItem);
                idegreso = em.merge(idegreso);
            }
            em.remove(egresoItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EgresoItem> findEgresoItemEntities() {
        return findEgresoItemEntities(true, -1, -1);
    }

    public List<EgresoItem> findEgresoItemEntities(int maxResults, int firstResult) {
        return findEgresoItemEntities(false, maxResults, firstResult);
    }

    private List<EgresoItem> findEgresoItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EgresoItem.class));
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

    public EgresoItem findEgresoItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EgresoItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getEgresoItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EgresoItem> rt = cq.from(EgresoItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
