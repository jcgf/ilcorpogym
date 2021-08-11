/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.IngresoItem;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.VariedadesIngresos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class IngresoItemJpaController implements Serializable {

    public IngresoItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(IngresoItem ingresoItem) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesIngresos idingreso = ingresoItem.getIdingreso();
            if (idingreso != null) {
                idingreso = em.getReference(idingreso.getClass(), idingreso.getId());
                ingresoItem.setIdingreso(idingreso);
            }
            em.persist(ingresoItem);
            if (idingreso != null) {
                idingreso.getIngresoItemList().add(ingresoItem);
                idingreso = em.merge(idingreso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(IngresoItem ingresoItem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IngresoItem persistentIngresoItem = em.find(IngresoItem.class, ingresoItem.getId());
            VariedadesIngresos idingresoOld = persistentIngresoItem.getIdingreso();
            VariedadesIngresos idingresoNew = ingresoItem.getIdingreso();
            if (idingresoNew != null) {
                idingresoNew = em.getReference(idingresoNew.getClass(), idingresoNew.getId());
                ingresoItem.setIdingreso(idingresoNew);
            }
            ingresoItem = em.merge(ingresoItem);
            if (idingresoOld != null && !idingresoOld.equals(idingresoNew)) {
                idingresoOld.getIngresoItemList().remove(ingresoItem);
                idingresoOld = em.merge(idingresoOld);
            }
            if (idingresoNew != null && !idingresoNew.equals(idingresoOld)) {
                idingresoNew.getIngresoItemList().add(ingresoItem);
                idingresoNew = em.merge(idingresoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingresoItem.getId();
                if (findIngresoItem(id) == null) {
                    throw new NonexistentEntityException("The ingresoItem with id " + id + " no longer exists.");
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
            IngresoItem ingresoItem;
            try {
                ingresoItem = em.getReference(IngresoItem.class, id);
                ingresoItem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingresoItem with id " + id + " no longer exists.", enfe);
            }
            VariedadesIngresos idingreso = ingresoItem.getIdingreso();
            if (idingreso != null) {
                idingreso.getIngresoItemList().remove(ingresoItem);
                idingreso = em.merge(idingreso);
            }
            em.remove(ingresoItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<IngresoItem> findIngresoItemEntities() {
        return findIngresoItemEntities(true, -1, -1);
    }

    public List<IngresoItem> findIngresoItemEntities(int maxResults, int firstResult) {
        return findIngresoItemEntities(false, maxResults, firstResult);
    }

    private List<IngresoItem> findIngresoItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(IngresoItem.class));
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

    public IngresoItem findIngresoItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(IngresoItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngresoItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<IngresoItem> rt = cq.from(IngresoItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
