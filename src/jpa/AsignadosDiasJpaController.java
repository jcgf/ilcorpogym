/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.AsignadosDias;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.GymAsignados;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class AsignadosDiasJpaController implements Serializable {

    public AsignadosDiasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AsignadosDias asignadosDias) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymAsignados idasignacion = asignadosDias.getIdasignacion();
            if (idasignacion != null) {
                idasignacion = em.getReference(idasignacion.getClass(), idasignacion.getId());
                asignadosDias.setIdasignacion(idasignacion);
            }
            em.persist(asignadosDias);
            if (idasignacion != null) {
                idasignacion.getAsignadosDiasList().add(asignadosDias);
                idasignacion = em.merge(idasignacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AsignadosDias asignadosDias) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AsignadosDias persistentAsignadosDias = em.find(AsignadosDias.class, asignadosDias.getId());
            GymAsignados idasignacionOld = persistentAsignadosDias.getIdasignacion();
            GymAsignados idasignacionNew = asignadosDias.getIdasignacion();
            if (idasignacionNew != null) {
                idasignacionNew = em.getReference(idasignacionNew.getClass(), idasignacionNew.getId());
                asignadosDias.setIdasignacion(idasignacionNew);
            }
            asignadosDias = em.merge(asignadosDias);
            if (idasignacionOld != null && !idasignacionOld.equals(idasignacionNew)) {
                idasignacionOld.getAsignadosDiasList().remove(asignadosDias);
                idasignacionOld = em.merge(idasignacionOld);
            }
            if (idasignacionNew != null && !idasignacionNew.equals(idasignacionOld)) {
                idasignacionNew.getAsignadosDiasList().add(asignadosDias);
                idasignacionNew = em.merge(idasignacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = asignadosDias.getId();
                if (findAsignadosDias(id) == null) {
                    throw new NonexistentEntityException("The asignadosDias with id " + id + " no longer exists.");
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
            AsignadosDias asignadosDias;
            try {
                asignadosDias = em.getReference(AsignadosDias.class, id);
                asignadosDias.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignadosDias with id " + id + " no longer exists.", enfe);
            }
            GymAsignados idasignacion = asignadosDias.getIdasignacion();
            if (idasignacion != null) {
                idasignacion.getAsignadosDiasList().remove(asignadosDias);
                idasignacion = em.merge(idasignacion);
            }
            em.remove(asignadosDias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AsignadosDias> findAsignadosDiasEntities() {
        return findAsignadosDiasEntities(true, -1, -1);
    }

    public List<AsignadosDias> findAsignadosDiasEntities(int maxResults, int firstResult) {
        return findAsignadosDiasEntities(false, maxResults, firstResult);
    }

    private List<AsignadosDias> findAsignadosDiasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AsignadosDias.class));
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

    public AsignadosDias findAsignadosDias(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AsignadosDias.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignadosDiasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AsignadosDias> rt = cq.from(AsignadosDias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
