/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.GymAsignados;
import entity.GymCongelado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class GymCongeladoJpaController implements Serializable {

    public GymCongeladoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GymCongelado gymCongelado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymAsignados idasignacion = gymCongelado.getIdasignacion();
            if (idasignacion != null) {
                idasignacion = em.getReference(idasignacion.getClass(), idasignacion.getId());
                gymCongelado.setIdasignacion(idasignacion);
            }
            em.persist(gymCongelado);
            if (idasignacion != null) {
                idasignacion.getGymCongeladoList().add(gymCongelado);
                idasignacion = em.merge(idasignacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GymCongelado gymCongelado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymCongelado persistentGymCongelado = em.find(GymCongelado.class, gymCongelado.getId());
            GymAsignados idasignacionOld = persistentGymCongelado.getIdasignacion();
            GymAsignados idasignacionNew = gymCongelado.getIdasignacion();
            if (idasignacionNew != null) {
                idasignacionNew = em.getReference(idasignacionNew.getClass(), idasignacionNew.getId());
                gymCongelado.setIdasignacion(idasignacionNew);
            }
            gymCongelado = em.merge(gymCongelado);
            if (idasignacionOld != null && !idasignacionOld.equals(idasignacionNew)) {
                idasignacionOld.getGymCongeladoList().remove(gymCongelado);
                idasignacionOld = em.merge(idasignacionOld);
            }
            if (idasignacionNew != null && !idasignacionNew.equals(idasignacionOld)) {
                idasignacionNew.getGymCongeladoList().add(gymCongelado);
                idasignacionNew = em.merge(idasignacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gymCongelado.getId();
                if (findGymCongelado(id) == null) {
                    throw new NonexistentEntityException("The gymCongelado with id " + id + " no longer exists.");
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
            GymCongelado gymCongelado;
            try {
                gymCongelado = em.getReference(GymCongelado.class, id);
                gymCongelado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gymCongelado with id " + id + " no longer exists.", enfe);
            }
            GymAsignados idasignacion = gymCongelado.getIdasignacion();
            if (idasignacion != null) {
                idasignacion.getGymCongeladoList().remove(gymCongelado);
                idasignacion = em.merge(idasignacion);
            }
            em.remove(gymCongelado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GymCongelado> findGymCongeladoEntities() {
        return findGymCongeladoEntities(true, -1, -1);
    }

    public List<GymCongelado> findGymCongeladoEntities(int maxResults, int firstResult) {
        return findGymCongeladoEntities(false, maxResults, firstResult);
    }

    private List<GymCongelado> findGymCongeladoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GymCongelado.class));
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

    public GymCongelado findGymCongelado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GymCongelado.class, id);
        } finally {
            em.close();
        }
    }

    public int getGymCongeladoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GymCongelado> rt = cq.from(GymCongelado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
