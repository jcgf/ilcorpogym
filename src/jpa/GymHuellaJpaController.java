/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.GymHuella;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.GymUsuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class GymHuellaJpaController implements Serializable {

    public GymHuellaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GymHuella gymHuella) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymUsuarios iduser = gymHuella.getIduser();
            if (iduser != null) {
                iduser = em.getReference(iduser.getClass(), iduser.getId());
                gymHuella.setIduser(iduser);
            }
            em.persist(gymHuella);
            if (iduser != null) {
                iduser.getGymHuellaList().add(gymHuella);
                iduser = em.merge(iduser);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GymHuella gymHuella) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymHuella persistentGymHuella = em.find(GymHuella.class, gymHuella.getId());
            GymUsuarios iduserOld = persistentGymHuella.getIduser();
            GymUsuarios iduserNew = gymHuella.getIduser();
            if (iduserNew != null) {
                iduserNew = em.getReference(iduserNew.getClass(), iduserNew.getId());
                gymHuella.setIduser(iduserNew);
            }
            gymHuella = em.merge(gymHuella);
            if (iduserOld != null && !iduserOld.equals(iduserNew)) {
                iduserOld.getGymHuellaList().remove(gymHuella);
                iduserOld = em.merge(iduserOld);
            }
            if (iduserNew != null && !iduserNew.equals(iduserOld)) {
                iduserNew.getGymHuellaList().add(gymHuella);
                iduserNew = em.merge(iduserNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gymHuella.getId();
                if (findGymHuella(id) == null) {
                    throw new NonexistentEntityException("The gymHuella with id " + id + " no longer exists.");
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
            GymHuella gymHuella;
            try {
                gymHuella = em.getReference(GymHuella.class, id);
                gymHuella.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gymHuella with id " + id + " no longer exists.", enfe);
            }
            GymUsuarios iduser = gymHuella.getIduser();
            if (iduser != null) {
                iduser.getGymHuellaList().remove(gymHuella);
                iduser = em.merge(iduser);
            }
            em.remove(gymHuella);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GymHuella> findGymHuellaEntities() {
        return findGymHuellaEntities(true, -1, -1);
    }

    public List<GymHuella> findGymHuellaEntities(int maxResults, int firstResult) {
        return findGymHuellaEntities(false, maxResults, firstResult);
    }

    private List<GymHuella> findGymHuellaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GymHuella.class));
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

    public GymHuella findGymHuella(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GymHuella.class, id);
        } finally {
            em.close();
        }
    }

    public int getGymHuellaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GymHuella> rt = cq.from(GymHuella.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
