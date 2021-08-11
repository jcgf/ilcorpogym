/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.UserInfo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.UserLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class UserInfoJpaController implements Serializable {

    public UserInfoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserInfo userInfo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog idlog = userInfo.getIdlog();
            if (idlog != null) {
                idlog = em.getReference(idlog.getClass(), idlog.getId());
                userInfo.setIdlog(idlog);
            }
            em.persist(userInfo);
            if (idlog != null) {
                idlog.getUserInfoList().add(userInfo);
                idlog = em.merge(idlog);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserInfo userInfo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserInfo persistentUserInfo = em.find(UserInfo.class, userInfo.getId());
            UserLog idlogOld = persistentUserInfo.getIdlog();
            UserLog idlogNew = userInfo.getIdlog();
            if (idlogNew != null) {
                idlogNew = em.getReference(idlogNew.getClass(), idlogNew.getId());
                userInfo.setIdlog(idlogNew);
            }
            userInfo = em.merge(userInfo);
            if (idlogOld != null && !idlogOld.equals(idlogNew)) {
                idlogOld.getUserInfoList().remove(userInfo);
                idlogOld = em.merge(idlogOld);
            }
            if (idlogNew != null && !idlogNew.equals(idlogOld)) {
                idlogNew.getUserInfoList().add(userInfo);
                idlogNew = em.merge(idlogNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userInfo.getId();
                if (findUserInfo(id) == null) {
                    throw new NonexistentEntityException("The userInfo with id " + id + " no longer exists.");
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
            UserInfo userInfo;
            try {
                userInfo = em.getReference(UserInfo.class, id);
                userInfo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userInfo with id " + id + " no longer exists.", enfe);
            }
            UserLog idlog = userInfo.getIdlog();
            if (idlog != null) {
                idlog.getUserInfoList().remove(userInfo);
                idlog = em.merge(idlog);
            }
            em.remove(userInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserInfo> findUserInfoEntities() {
        return findUserInfoEntities(true, -1, -1);
    }

    public List<UserInfo> findUserInfoEntities(int maxResults, int firstResult) {
        return findUserInfoEntities(false, maxResults, firstResult);
    }

    private List<UserInfo> findUserInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserInfo.class));
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

    public UserInfo findUserInfo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserInfo> rt = cq.from(UserInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
