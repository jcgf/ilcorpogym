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
import entity.SuministroArticulos;
import entity.VariedadesSuministro;
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
public class VariedadesSuministroJpaController implements Serializable {

    public VariedadesSuministroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariedadesSuministro variedadesSuministro) {
        if (variedadesSuministro.getSuministroArticulosList() == null) {
            variedadesSuministro.setSuministroArticulosList(new ArrayList<SuministroArticulos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog idusuario = variedadesSuministro.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getId());
                variedadesSuministro.setIdusuario(idusuario);
            }
            List<SuministroArticulos> attachedSuministroArticulosList = new ArrayList<SuministroArticulos>();
            for (SuministroArticulos suministroArticulosListSuministroArticulosToAttach : variedadesSuministro.getSuministroArticulosList()) {
                suministroArticulosListSuministroArticulosToAttach = em.getReference(suministroArticulosListSuministroArticulosToAttach.getClass(), suministroArticulosListSuministroArticulosToAttach.getId());
                attachedSuministroArticulosList.add(suministroArticulosListSuministroArticulosToAttach);
            }
            variedadesSuministro.setSuministroArticulosList(attachedSuministroArticulosList);
            em.persist(variedadesSuministro);
            if (idusuario != null) {
                idusuario.getVariedadesSuministroList().add(variedadesSuministro);
                idusuario = em.merge(idusuario);
            }
            for (SuministroArticulos suministroArticulosListSuministroArticulos : variedadesSuministro.getSuministroArticulosList()) {
                VariedadesSuministro oldIdsuministroOfSuministroArticulosListSuministroArticulos = suministroArticulosListSuministroArticulos.getIdsuministro();
                suministroArticulosListSuministroArticulos.setIdsuministro(variedadesSuministro);
                suministroArticulosListSuministroArticulos = em.merge(suministroArticulosListSuministroArticulos);
                if (oldIdsuministroOfSuministroArticulosListSuministroArticulos != null) {
                    oldIdsuministroOfSuministroArticulosListSuministroArticulos.getSuministroArticulosList().remove(suministroArticulosListSuministroArticulos);
                    oldIdsuministroOfSuministroArticulosListSuministroArticulos = em.merge(oldIdsuministroOfSuministroArticulosListSuministroArticulos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariedadesSuministro variedadesSuministro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesSuministro persistentVariedadesSuministro = em.find(VariedadesSuministro.class, variedadesSuministro.getId());
            UserLog idusuarioOld = persistentVariedadesSuministro.getIdusuario();
            UserLog idusuarioNew = variedadesSuministro.getIdusuario();
            List<SuministroArticulos> suministroArticulosListOld = persistentVariedadesSuministro.getSuministroArticulosList();
            List<SuministroArticulos> suministroArticulosListNew = variedadesSuministro.getSuministroArticulosList();
            List<String> illegalOrphanMessages = null;
            for (SuministroArticulos suministroArticulosListOldSuministroArticulos : suministroArticulosListOld) {
                if (!suministroArticulosListNew.contains(suministroArticulosListOldSuministroArticulos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SuministroArticulos " + suministroArticulosListOldSuministroArticulos + " since its idsuministro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getId());
                variedadesSuministro.setIdusuario(idusuarioNew);
            }
            List<SuministroArticulos> attachedSuministroArticulosListNew = new ArrayList<SuministroArticulos>();
            for (SuministroArticulos suministroArticulosListNewSuministroArticulosToAttach : suministroArticulosListNew) {
                suministroArticulosListNewSuministroArticulosToAttach = em.getReference(suministroArticulosListNewSuministroArticulosToAttach.getClass(), suministroArticulosListNewSuministroArticulosToAttach.getId());
                attachedSuministroArticulosListNew.add(suministroArticulosListNewSuministroArticulosToAttach);
            }
            suministroArticulosListNew = attachedSuministroArticulosListNew;
            variedadesSuministro.setSuministroArticulosList(suministroArticulosListNew);
            variedadesSuministro = em.merge(variedadesSuministro);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getVariedadesSuministroList().remove(variedadesSuministro);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getVariedadesSuministroList().add(variedadesSuministro);
                idusuarioNew = em.merge(idusuarioNew);
            }
            for (SuministroArticulos suministroArticulosListNewSuministroArticulos : suministroArticulosListNew) {
                if (!suministroArticulosListOld.contains(suministroArticulosListNewSuministroArticulos)) {
                    VariedadesSuministro oldIdsuministroOfSuministroArticulosListNewSuministroArticulos = suministroArticulosListNewSuministroArticulos.getIdsuministro();
                    suministroArticulosListNewSuministroArticulos.setIdsuministro(variedadesSuministro);
                    suministroArticulosListNewSuministroArticulos = em.merge(suministroArticulosListNewSuministroArticulos);
                    if (oldIdsuministroOfSuministroArticulosListNewSuministroArticulos != null && !oldIdsuministroOfSuministroArticulosListNewSuministroArticulos.equals(variedadesSuministro)) {
                        oldIdsuministroOfSuministroArticulosListNewSuministroArticulos.getSuministroArticulosList().remove(suministroArticulosListNewSuministroArticulos);
                        oldIdsuministroOfSuministroArticulosListNewSuministroArticulos = em.merge(oldIdsuministroOfSuministroArticulosListNewSuministroArticulos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variedadesSuministro.getId();
                if (findVariedadesSuministro(id) == null) {
                    throw new NonexistentEntityException("The variedadesSuministro with id " + id + " no longer exists.");
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
            VariedadesSuministro variedadesSuministro;
            try {
                variedadesSuministro = em.getReference(VariedadesSuministro.class, id);
                variedadesSuministro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variedadesSuministro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SuministroArticulos> suministroArticulosListOrphanCheck = variedadesSuministro.getSuministroArticulosList();
            for (SuministroArticulos suministroArticulosListOrphanCheckSuministroArticulos : suministroArticulosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesSuministro (" + variedadesSuministro + ") cannot be destroyed since the SuministroArticulos " + suministroArticulosListOrphanCheckSuministroArticulos + " in its suministroArticulosList field has a non-nullable idsuministro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserLog idusuario = variedadesSuministro.getIdusuario();
            if (idusuario != null) {
                idusuario.getVariedadesSuministroList().remove(variedadesSuministro);
                idusuario = em.merge(idusuario);
            }
            em.remove(variedadesSuministro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariedadesSuministro> findVariedadesSuministroEntities() {
        return findVariedadesSuministroEntities(true, -1, -1);
    }

    public List<VariedadesSuministro> findVariedadesSuministroEntities(int maxResults, int firstResult) {
        return findVariedadesSuministroEntities(false, maxResults, firstResult);
    }

    private List<VariedadesSuministro> findVariedadesSuministroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariedadesSuministro.class));
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

    public VariedadesSuministro findVariedadesSuministro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariedadesSuministro.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariedadesSuministroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariedadesSuministro> rt = cq.from(VariedadesSuministro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
