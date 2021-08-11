/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.VariedadesAbono;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.VariedadesVentas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class VariedadesAbonoJpaController implements Serializable {

    public VariedadesAbonoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariedadesAbono variedadesAbono) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesVentas idventa = variedadesAbono.getIdventa();
            if (idventa != null) {
                idventa = em.getReference(idventa.getClass(), idventa.getId());
                variedadesAbono.setIdventa(idventa);
            }
            em.persist(variedadesAbono);
            if (idventa != null) {
                idventa.getVariedadesAbonoList().add(variedadesAbono);
                idventa = em.merge(idventa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariedadesAbono variedadesAbono) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesAbono persistentVariedadesAbono = em.find(VariedadesAbono.class, variedadesAbono.getId());
            VariedadesVentas idventaOld = persistentVariedadesAbono.getIdventa();
            VariedadesVentas idventaNew = variedadesAbono.getIdventa();
            if (idventaNew != null) {
                idventaNew = em.getReference(idventaNew.getClass(), idventaNew.getId());
                variedadesAbono.setIdventa(idventaNew);
            }
            variedadesAbono = em.merge(variedadesAbono);
            if (idventaOld != null && !idventaOld.equals(idventaNew)) {
                idventaOld.getVariedadesAbonoList().remove(variedadesAbono);
                idventaOld = em.merge(idventaOld);
            }
            if (idventaNew != null && !idventaNew.equals(idventaOld)) {
                idventaNew.getVariedadesAbonoList().add(variedadesAbono);
                idventaNew = em.merge(idventaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variedadesAbono.getId();
                if (findVariedadesAbono(id) == null) {
                    throw new NonexistentEntityException("The variedadesAbono with id " + id + " no longer exists.");
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
            VariedadesAbono variedadesAbono;
            try {
                variedadesAbono = em.getReference(VariedadesAbono.class, id);
                variedadesAbono.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variedadesAbono with id " + id + " no longer exists.", enfe);
            }
            VariedadesVentas idventa = variedadesAbono.getIdventa();
            if (idventa != null) {
                idventa.getVariedadesAbonoList().remove(variedadesAbono);
                idventa = em.merge(idventa);
            }
            em.remove(variedadesAbono);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariedadesAbono> findVariedadesAbonoEntities() {
        return findVariedadesAbonoEntities(true, -1, -1);
    }

    public List<VariedadesAbono> findVariedadesAbonoEntities(int maxResults, int firstResult) {
        return findVariedadesAbonoEntities(false, maxResults, firstResult);
    }

    private List<VariedadesAbono> findVariedadesAbonoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariedadesAbono.class));
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

    public VariedadesAbono findVariedadesAbono(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariedadesAbono.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariedadesAbonoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariedadesAbono> rt = cq.from(VariedadesAbono.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
