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
import entity.IngresoItem;
import entity.VariedadesIngresos;
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
public class VariedadesIngresosJpaController implements Serializable {

    public VariedadesIngresosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariedadesIngresos variedadesIngresos) {
        if (variedadesIngresos.getIngresoItemList() == null) {
            variedadesIngresos.setIngresoItemList(new ArrayList<IngresoItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog idusuario = variedadesIngresos.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getId());
                variedadesIngresos.setIdusuario(idusuario);
            }
            List<IngresoItem> attachedIngresoItemList = new ArrayList<IngresoItem>();
            for (IngresoItem ingresoItemListIngresoItemToAttach : variedadesIngresos.getIngresoItemList()) {
                ingresoItemListIngresoItemToAttach = em.getReference(ingresoItemListIngresoItemToAttach.getClass(), ingresoItemListIngresoItemToAttach.getId());
                attachedIngresoItemList.add(ingresoItemListIngresoItemToAttach);
            }
            variedadesIngresos.setIngresoItemList(attachedIngresoItemList);
            em.persist(variedadesIngresos);
            if (idusuario != null) {
                idusuario.getVariedadesIngresosList().add(variedadesIngresos);
                idusuario = em.merge(idusuario);
            }
            for (IngresoItem ingresoItemListIngresoItem : variedadesIngresos.getIngresoItemList()) {
                VariedadesIngresos oldIdingresoOfIngresoItemListIngresoItem = ingresoItemListIngresoItem.getIdingreso();
                ingresoItemListIngresoItem.setIdingreso(variedadesIngresos);
                ingresoItemListIngresoItem = em.merge(ingresoItemListIngresoItem);
                if (oldIdingresoOfIngresoItemListIngresoItem != null) {
                    oldIdingresoOfIngresoItemListIngresoItem.getIngresoItemList().remove(ingresoItemListIngresoItem);
                    oldIdingresoOfIngresoItemListIngresoItem = em.merge(oldIdingresoOfIngresoItemListIngresoItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariedadesIngresos variedadesIngresos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesIngresos persistentVariedadesIngresos = em.find(VariedadesIngresos.class, variedadesIngresos.getId());
            UserLog idusuarioOld = persistentVariedadesIngresos.getIdusuario();
            UserLog idusuarioNew = variedadesIngresos.getIdusuario();
            List<IngresoItem> ingresoItemListOld = persistentVariedadesIngresos.getIngresoItemList();
            List<IngresoItem> ingresoItemListNew = variedadesIngresos.getIngresoItemList();
            List<String> illegalOrphanMessages = null;
            for (IngresoItem ingresoItemListOldIngresoItem : ingresoItemListOld) {
                if (!ingresoItemListNew.contains(ingresoItemListOldIngresoItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain IngresoItem " + ingresoItemListOldIngresoItem + " since its idingreso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getId());
                variedadesIngresos.setIdusuario(idusuarioNew);
            }
            List<IngresoItem> attachedIngresoItemListNew = new ArrayList<IngresoItem>();
            for (IngresoItem ingresoItemListNewIngresoItemToAttach : ingresoItemListNew) {
                ingresoItemListNewIngresoItemToAttach = em.getReference(ingresoItemListNewIngresoItemToAttach.getClass(), ingresoItemListNewIngresoItemToAttach.getId());
                attachedIngresoItemListNew.add(ingresoItemListNewIngresoItemToAttach);
            }
            ingresoItemListNew = attachedIngresoItemListNew;
            variedadesIngresos.setIngresoItemList(ingresoItemListNew);
            variedadesIngresos = em.merge(variedadesIngresos);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getVariedadesIngresosList().remove(variedadesIngresos);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getVariedadesIngresosList().add(variedadesIngresos);
                idusuarioNew = em.merge(idusuarioNew);
            }
            for (IngresoItem ingresoItemListNewIngresoItem : ingresoItemListNew) {
                if (!ingresoItemListOld.contains(ingresoItemListNewIngresoItem)) {
                    VariedadesIngresos oldIdingresoOfIngresoItemListNewIngresoItem = ingresoItemListNewIngresoItem.getIdingreso();
                    ingresoItemListNewIngresoItem.setIdingreso(variedadesIngresos);
                    ingresoItemListNewIngresoItem = em.merge(ingresoItemListNewIngresoItem);
                    if (oldIdingresoOfIngresoItemListNewIngresoItem != null && !oldIdingresoOfIngresoItemListNewIngresoItem.equals(variedadesIngresos)) {
                        oldIdingresoOfIngresoItemListNewIngresoItem.getIngresoItemList().remove(ingresoItemListNewIngresoItem);
                        oldIdingresoOfIngresoItemListNewIngresoItem = em.merge(oldIdingresoOfIngresoItemListNewIngresoItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variedadesIngresos.getId();
                if (findVariedadesIngresos(id) == null) {
                    throw new NonexistentEntityException("The variedadesIngresos with id " + id + " no longer exists.");
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
            VariedadesIngresos variedadesIngresos;
            try {
                variedadesIngresos = em.getReference(VariedadesIngresos.class, id);
                variedadesIngresos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variedadesIngresos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<IngresoItem> ingresoItemListOrphanCheck = variedadesIngresos.getIngresoItemList();
            for (IngresoItem ingresoItemListOrphanCheckIngresoItem : ingresoItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesIngresos (" + variedadesIngresos + ") cannot be destroyed since the IngresoItem " + ingresoItemListOrphanCheckIngresoItem + " in its ingresoItemList field has a non-nullable idingreso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserLog idusuario = variedadesIngresos.getIdusuario();
            if (idusuario != null) {
                idusuario.getVariedadesIngresosList().remove(variedadesIngresos);
                idusuario = em.merge(idusuario);
            }
            em.remove(variedadesIngresos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariedadesIngresos> findVariedadesIngresosEntities() {
        return findVariedadesIngresosEntities(true, -1, -1);
    }

    public List<VariedadesIngresos> findVariedadesIngresosEntities(int maxResults, int firstResult) {
        return findVariedadesIngresosEntities(false, maxResults, firstResult);
    }

    private List<VariedadesIngresos> findVariedadesIngresosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariedadesIngresos.class));
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

    public VariedadesIngresos findVariedadesIngresos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariedadesIngresos.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariedadesIngresosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariedadesIngresos> rt = cq.from(VariedadesIngresos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
