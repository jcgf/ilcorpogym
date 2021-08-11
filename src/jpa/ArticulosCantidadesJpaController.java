/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.ArticulosCantidades;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.VariedadesArticulos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class ArticulosCantidadesJpaController implements Serializable {

    public ArticulosCantidadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ArticulosCantidades articulosCantidades) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesArticulos idarticulo = articulosCantidades.getIdarticulo();
            if (idarticulo != null) {
                idarticulo = em.getReference(idarticulo.getClass(), idarticulo.getId());
                articulosCantidades.setIdarticulo(idarticulo);
            }
            em.persist(articulosCantidades);
            if (idarticulo != null) {
                idarticulo.getArticulosCantidadesList().add(articulosCantidades);
                idarticulo = em.merge(idarticulo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ArticulosCantidades articulosCantidades) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ArticulosCantidades persistentArticulosCantidades = em.find(ArticulosCantidades.class, articulosCantidades.getId());
            VariedadesArticulos idarticuloOld = persistentArticulosCantidades.getIdarticulo();
            VariedadesArticulos idarticuloNew = articulosCantidades.getIdarticulo();
            if (idarticuloNew != null) {
                idarticuloNew = em.getReference(idarticuloNew.getClass(), idarticuloNew.getId());
                articulosCantidades.setIdarticulo(idarticuloNew);
            }
            articulosCantidades = em.merge(articulosCantidades);
            if (idarticuloOld != null && !idarticuloOld.equals(idarticuloNew)) {
                idarticuloOld.getArticulosCantidadesList().remove(articulosCantidades);
                idarticuloOld = em.merge(idarticuloOld);
            }
            if (idarticuloNew != null && !idarticuloNew.equals(idarticuloOld)) {
                idarticuloNew.getArticulosCantidadesList().add(articulosCantidades);
                idarticuloNew = em.merge(idarticuloNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = articulosCantidades.getId();
                if (findArticulosCantidades(id) == null) {
                    throw new NonexistentEntityException("The articulosCantidades with id " + id + " no longer exists.");
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
            ArticulosCantidades articulosCantidades;
            try {
                articulosCantidades = em.getReference(ArticulosCantidades.class, id);
                articulosCantidades.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articulosCantidades with id " + id + " no longer exists.", enfe);
            }
            VariedadesArticulos idarticulo = articulosCantidades.getIdarticulo();
            if (idarticulo != null) {
                idarticulo.getArticulosCantidadesList().remove(articulosCantidades);
                idarticulo = em.merge(idarticulo);
            }
            em.remove(articulosCantidades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ArticulosCantidades> findArticulosCantidadesEntities() {
        return findArticulosCantidadesEntities(true, -1, -1);
    }

    public List<ArticulosCantidades> findArticulosCantidadesEntities(int maxResults, int firstResult) {
        return findArticulosCantidadesEntities(false, maxResults, firstResult);
    }

    private List<ArticulosCantidades> findArticulosCantidadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ArticulosCantidades.class));
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

    public ArticulosCantidades findArticulosCantidades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ArticulosCantidades.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticulosCantidadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ArticulosCantidades> rt = cq.from(ArticulosCantidades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
