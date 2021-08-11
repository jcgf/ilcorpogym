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
import entity.UserLog;
import entity.EgresoItem;
import entity.VariedadesEgresos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class VariedadesEgresosJpaController implements Serializable {

    public VariedadesEgresosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariedadesEgresos variedadesEgresos) {
        if (variedadesEgresos.getEgresoItemList() == null) {
            variedadesEgresos.setEgresoItemList(new ArrayList<EgresoItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog idusuario = variedadesEgresos.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getId());
                variedadesEgresos.setIdusuario(idusuario);
            }
            List<EgresoItem> attachedEgresoItemList = new ArrayList<EgresoItem>();
            for (EgresoItem egresoItemListEgresoItemToAttach : variedadesEgresos.getEgresoItemList()) {
                egresoItemListEgresoItemToAttach = em.getReference(egresoItemListEgresoItemToAttach.getClass(), egresoItemListEgresoItemToAttach.getId());
                attachedEgresoItemList.add(egresoItemListEgresoItemToAttach);
            }
            variedadesEgresos.setEgresoItemList(attachedEgresoItemList);
            em.persist(variedadesEgresos);
            if (idusuario != null) {
                idusuario.getVariedadesEgresosList().add(variedadesEgresos);
                idusuario = em.merge(idusuario);
            }
            for (EgresoItem egresoItemListEgresoItem : variedadesEgresos.getEgresoItemList()) {
                VariedadesEgresos oldIdegresoOfEgresoItemListEgresoItem = egresoItemListEgresoItem.getIdegreso();
                egresoItemListEgresoItem.setIdegreso(variedadesEgresos);
                egresoItemListEgresoItem = em.merge(egresoItemListEgresoItem);
                if (oldIdegresoOfEgresoItemListEgresoItem != null) {
                    oldIdegresoOfEgresoItemListEgresoItem.getEgresoItemList().remove(egresoItemListEgresoItem);
                    oldIdegresoOfEgresoItemListEgresoItem = em.merge(oldIdegresoOfEgresoItemListEgresoItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariedadesEgresos variedadesEgresos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesEgresos persistentVariedadesEgresos = em.find(VariedadesEgresos.class, variedadesEgresos.getId());
            UserLog idusuarioOld = persistentVariedadesEgresos.getIdusuario();
            UserLog idusuarioNew = variedadesEgresos.getIdusuario();
            List<EgresoItem> egresoItemListOld = persistentVariedadesEgresos.getEgresoItemList();
            List<EgresoItem> egresoItemListNew = variedadesEgresos.getEgresoItemList();
            List<String> illegalOrphanMessages = null;
            for (EgresoItem egresoItemListOldEgresoItem : egresoItemListOld) {
                if (!egresoItemListNew.contains(egresoItemListOldEgresoItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EgresoItem " + egresoItemListOldEgresoItem + " since its idegreso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getId());
                variedadesEgresos.setIdusuario(idusuarioNew);
            }
            List<EgresoItem> attachedEgresoItemListNew = new ArrayList<EgresoItem>();
            for (EgresoItem egresoItemListNewEgresoItemToAttach : egresoItemListNew) {
                egresoItemListNewEgresoItemToAttach = em.getReference(egresoItemListNewEgresoItemToAttach.getClass(), egresoItemListNewEgresoItemToAttach.getId());
                attachedEgresoItemListNew.add(egresoItemListNewEgresoItemToAttach);
            }
            egresoItemListNew = attachedEgresoItemListNew;
            variedadesEgresos.setEgresoItemList(egresoItemListNew);
            variedadesEgresos = em.merge(variedadesEgresos);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getVariedadesEgresosList().remove(variedadesEgresos);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getVariedadesEgresosList().add(variedadesEgresos);
                idusuarioNew = em.merge(idusuarioNew);
            }
            for (EgresoItem egresoItemListNewEgresoItem : egresoItemListNew) {
                if (!egresoItemListOld.contains(egresoItemListNewEgresoItem)) {
                    VariedadesEgresos oldIdegresoOfEgresoItemListNewEgresoItem = egresoItemListNewEgresoItem.getIdegreso();
                    egresoItemListNewEgresoItem.setIdegreso(variedadesEgresos);
                    egresoItemListNewEgresoItem = em.merge(egresoItemListNewEgresoItem);
                    if (oldIdegresoOfEgresoItemListNewEgresoItem != null && !oldIdegresoOfEgresoItemListNewEgresoItem.equals(variedadesEgresos)) {
                        oldIdegresoOfEgresoItemListNewEgresoItem.getEgresoItemList().remove(egresoItemListNewEgresoItem);
                        oldIdegresoOfEgresoItemListNewEgresoItem = em.merge(oldIdegresoOfEgresoItemListNewEgresoItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variedadesEgresos.getId();
                if (findVariedadesEgresos(id) == null) {
                    throw new NonexistentEntityException("The variedadesEgresos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesEgresos variedadesEgresos;
            try {
                variedadesEgresos = em.getReference(VariedadesEgresos.class, id);
                variedadesEgresos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variedadesEgresos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<EgresoItem> egresoItemListOrphanCheck = variedadesEgresos.getEgresoItemList();
            for (EgresoItem egresoItemListOrphanCheckEgresoItem : egresoItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesEgresos (" + variedadesEgresos + ") cannot be destroyed since the EgresoItem " + egresoItemListOrphanCheckEgresoItem + " in its egresoItemList field has a non-nullable idegreso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserLog idusuario = variedadesEgresos.getIdusuario();
            if (idusuario != null) {
                idusuario.getVariedadesEgresosList().remove(variedadesEgresos);
                idusuario = em.merge(idusuario);
            }
            em.remove(variedadesEgresos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariedadesEgresos> findVariedadesEgresosEntities() {
        return findVariedadesEgresosEntities(true, -1, -1);
    }

    public List<VariedadesEgresos> findVariedadesEgresosEntities(int maxResults, int firstResult) {
        return findVariedadesEgresosEntities(false, maxResults, firstResult);
    }

    private List<VariedadesEgresos> findVariedadesEgresosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariedadesEgresos.class));
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

    public VariedadesEgresos findVariedadesEgresos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariedadesEgresos.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariedadesEgresosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariedadesEgresos> rt = cq.from(VariedadesEgresos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
