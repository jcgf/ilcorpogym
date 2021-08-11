/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.SuministroArticulos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.VariedadesSuministro;
import entity.VariedadesArticulos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class SuministroArticulosJpaController implements Serializable {

    public SuministroArticulosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SuministroArticulos suministroArticulos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesSuministro idsuministro = suministroArticulos.getIdsuministro();
            if (idsuministro != null) {
                idsuministro = em.getReference(idsuministro.getClass(), idsuministro.getId());
                suministroArticulos.setIdsuministro(idsuministro);
            }
            VariedadesArticulos idarticulo = suministroArticulos.getIdarticulo();
            if (idarticulo != null) {
                idarticulo = em.getReference(idarticulo.getClass(), idarticulo.getId());
                suministroArticulos.setIdarticulo(idarticulo);
            }
            em.persist(suministroArticulos);
            if (idsuministro != null) {
                idsuministro.getSuministroArticulosList().add(suministroArticulos);
                idsuministro = em.merge(idsuministro);
            }
            if (idarticulo != null) {
                idarticulo.getSuministroArticulosList().add(suministroArticulos);
                idarticulo = em.merge(idarticulo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SuministroArticulos suministroArticulos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SuministroArticulos persistentSuministroArticulos = em.find(SuministroArticulos.class, suministroArticulos.getId());
            VariedadesSuministro idsuministroOld = persistentSuministroArticulos.getIdsuministro();
            VariedadesSuministro idsuministroNew = suministroArticulos.getIdsuministro();
            VariedadesArticulos idarticuloOld = persistentSuministroArticulos.getIdarticulo();
            VariedadesArticulos idarticuloNew = suministroArticulos.getIdarticulo();
            if (idsuministroNew != null) {
                idsuministroNew = em.getReference(idsuministroNew.getClass(), idsuministroNew.getId());
                suministroArticulos.setIdsuministro(idsuministroNew);
            }
            if (idarticuloNew != null) {
                idarticuloNew = em.getReference(idarticuloNew.getClass(), idarticuloNew.getId());
                suministroArticulos.setIdarticulo(idarticuloNew);
            }
            suministroArticulos = em.merge(suministroArticulos);
            if (idsuministroOld != null && !idsuministroOld.equals(idsuministroNew)) {
                idsuministroOld.getSuministroArticulosList().remove(suministroArticulos);
                idsuministroOld = em.merge(idsuministroOld);
            }
            if (idsuministroNew != null && !idsuministroNew.equals(idsuministroOld)) {
                idsuministroNew.getSuministroArticulosList().add(suministroArticulos);
                idsuministroNew = em.merge(idsuministroNew);
            }
            if (idarticuloOld != null && !idarticuloOld.equals(idarticuloNew)) {
                idarticuloOld.getSuministroArticulosList().remove(suministroArticulos);
                idarticuloOld = em.merge(idarticuloOld);
            }
            if (idarticuloNew != null && !idarticuloNew.equals(idarticuloOld)) {
                idarticuloNew.getSuministroArticulosList().add(suministroArticulos);
                idarticuloNew = em.merge(idarticuloNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = suministroArticulos.getId();
                if (findSuministroArticulos(id) == null) {
                    throw new NonexistentEntityException("The suministroArticulos with id " + id + " no longer exists.");
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
            SuministroArticulos suministroArticulos;
            try {
                suministroArticulos = em.getReference(SuministroArticulos.class, id);
                suministroArticulos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The suministroArticulos with id " + id + " no longer exists.", enfe);
            }
            VariedadesSuministro idsuministro = suministroArticulos.getIdsuministro();
            if (idsuministro != null) {
                idsuministro.getSuministroArticulosList().remove(suministroArticulos);
                idsuministro = em.merge(idsuministro);
            }
            VariedadesArticulos idarticulo = suministroArticulos.getIdarticulo();
            if (idarticulo != null) {
                idarticulo.getSuministroArticulosList().remove(suministroArticulos);
                idarticulo = em.merge(idarticulo);
            }
            em.remove(suministroArticulos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SuministroArticulos> findSuministroArticulosEntities() {
        return findSuministroArticulosEntities(true, -1, -1);
    }

    public List<SuministroArticulos> findSuministroArticulosEntities(int maxResults, int firstResult) {
        return findSuministroArticulosEntities(false, maxResults, firstResult);
    }

    private List<SuministroArticulos> findSuministroArticulosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SuministroArticulos.class));
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

    public SuministroArticulos findSuministroArticulos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SuministroArticulos.class, id);
        } finally {
            em.close();
        }
    }

    public int getSuministroArticulosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SuministroArticulos> rt = cq.from(SuministroArticulos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
