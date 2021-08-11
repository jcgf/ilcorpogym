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
import entity.VariedadesVentas;
import entity.VariedadesArticulos;
import entity.VentasArticulos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class VentasArticulosJpaController implements Serializable {

    public VentasArticulosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VentasArticulos ventasArticulos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesVentas idventa = ventasArticulos.getIdventa();
            if (idventa != null) {
                idventa = em.getReference(idventa.getClass(), idventa.getId());
                ventasArticulos.setIdventa(idventa);
            }
            VariedadesArticulos idarticulo = ventasArticulos.getIdarticulo();
            if (idarticulo != null) {
                idarticulo = em.getReference(idarticulo.getClass(), idarticulo.getId());
                ventasArticulos.setIdarticulo(idarticulo);
            }
            em.persist(ventasArticulos);
            if (idventa != null) {
                idventa.getVentasArticulosList().add(ventasArticulos);
                idventa = em.merge(idventa);
            }
            if (idarticulo != null) {
                idarticulo.getVentasArticulosList().add(ventasArticulos);
                idarticulo = em.merge(idarticulo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VentasArticulos ventasArticulos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VentasArticulos persistentVentasArticulos = em.find(VentasArticulos.class, ventasArticulos.getId());
            VariedadesVentas idventaOld = persistentVentasArticulos.getIdventa();
            VariedadesVentas idventaNew = ventasArticulos.getIdventa();
            VariedadesArticulos idarticuloOld = persistentVentasArticulos.getIdarticulo();
            VariedadesArticulos idarticuloNew = ventasArticulos.getIdarticulo();
            if (idventaNew != null) {
                idventaNew = em.getReference(idventaNew.getClass(), idventaNew.getId());
                ventasArticulos.setIdventa(idventaNew);
            }
            if (idarticuloNew != null) {
                idarticuloNew = em.getReference(idarticuloNew.getClass(), idarticuloNew.getId());
                ventasArticulos.setIdarticulo(idarticuloNew);
            }
            ventasArticulos = em.merge(ventasArticulos);
            if (idventaOld != null && !idventaOld.equals(idventaNew)) {
                idventaOld.getVentasArticulosList().remove(ventasArticulos);
                idventaOld = em.merge(idventaOld);
            }
            if (idventaNew != null && !idventaNew.equals(idventaOld)) {
                idventaNew.getVentasArticulosList().add(ventasArticulos);
                idventaNew = em.merge(idventaNew);
            }
            if (idarticuloOld != null && !idarticuloOld.equals(idarticuloNew)) {
                idarticuloOld.getVentasArticulosList().remove(ventasArticulos);
                idarticuloOld = em.merge(idarticuloOld);
            }
            if (idarticuloNew != null && !idarticuloNew.equals(idarticuloOld)) {
                idarticuloNew.getVentasArticulosList().add(ventasArticulos);
                idarticuloNew = em.merge(idarticuloNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ventasArticulos.getId();
                if (findVentasArticulos(id) == null) {
                    throw new NonexistentEntityException("The ventasArticulos with id " + id + " no longer exists.");
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
            VentasArticulos ventasArticulos;
            try {
                ventasArticulos = em.getReference(VentasArticulos.class, id);
                ventasArticulos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventasArticulos with id " + id + " no longer exists.", enfe);
            }
            VariedadesVentas idventa = ventasArticulos.getIdventa();
            if (idventa != null) {
                idventa.getVentasArticulosList().remove(ventasArticulos);
                idventa = em.merge(idventa);
            }
            VariedadesArticulos idarticulo = ventasArticulos.getIdarticulo();
            if (idarticulo != null) {
                idarticulo.getVentasArticulosList().remove(ventasArticulos);
                idarticulo = em.merge(idarticulo);
            }
            em.remove(ventasArticulos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VentasArticulos> findVentasArticulosEntities() {
        return findVentasArticulosEntities(true, -1, -1);
    }

    public List<VentasArticulos> findVentasArticulosEntities(int maxResults, int firstResult) {
        return findVentasArticulosEntities(false, maxResults, firstResult);
    }

    private List<VentasArticulos> findVentasArticulosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VentasArticulos.class));
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

    public VentasArticulos findVentasArticulos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VentasArticulos.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasArticulosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VentasArticulos> rt = cq.from(VentasArticulos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
