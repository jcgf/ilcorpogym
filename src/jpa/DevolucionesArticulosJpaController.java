/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import entity.DevolucionesArticulos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.VariedadesDevoluciones;
import entity.VariedadesArticulos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class DevolucionesArticulosJpaController implements Serializable {

    public DevolucionesArticulosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DevolucionesArticulos devolucionesArticulos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesDevoluciones iddevolucion = devolucionesArticulos.getIddevolucion();
            if (iddevolucion != null) {
                iddevolucion = em.getReference(iddevolucion.getClass(), iddevolucion.getId());
                devolucionesArticulos.setIddevolucion(iddevolucion);
            }
            VariedadesArticulos idarticulo = devolucionesArticulos.getIdarticulo();
            if (idarticulo != null) {
                idarticulo = em.getReference(idarticulo.getClass(), idarticulo.getId());
                devolucionesArticulos.setIdarticulo(idarticulo);
            }
            em.persist(devolucionesArticulos);
            if (iddevolucion != null) {
                iddevolucion.getDevolucionesArticulosList().add(devolucionesArticulos);
                iddevolucion = em.merge(iddevolucion);
            }
            if (idarticulo != null) {
                idarticulo.getDevolucionesArticulosList().add(devolucionesArticulos);
                idarticulo = em.merge(idarticulo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DevolucionesArticulos devolucionesArticulos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DevolucionesArticulos persistentDevolucionesArticulos = em.find(DevolucionesArticulos.class, devolucionesArticulos.getId());
            VariedadesDevoluciones iddevolucionOld = persistentDevolucionesArticulos.getIddevolucion();
            VariedadesDevoluciones iddevolucionNew = devolucionesArticulos.getIddevolucion();
            VariedadesArticulos idarticuloOld = persistentDevolucionesArticulos.getIdarticulo();
            VariedadesArticulos idarticuloNew = devolucionesArticulos.getIdarticulo();
            if (iddevolucionNew != null) {
                iddevolucionNew = em.getReference(iddevolucionNew.getClass(), iddevolucionNew.getId());
                devolucionesArticulos.setIddevolucion(iddevolucionNew);
            }
            if (idarticuloNew != null) {
                idarticuloNew = em.getReference(idarticuloNew.getClass(), idarticuloNew.getId());
                devolucionesArticulos.setIdarticulo(idarticuloNew);
            }
            devolucionesArticulos = em.merge(devolucionesArticulos);
            if (iddevolucionOld != null && !iddevolucionOld.equals(iddevolucionNew)) {
                iddevolucionOld.getDevolucionesArticulosList().remove(devolucionesArticulos);
                iddevolucionOld = em.merge(iddevolucionOld);
            }
            if (iddevolucionNew != null && !iddevolucionNew.equals(iddevolucionOld)) {
                iddevolucionNew.getDevolucionesArticulosList().add(devolucionesArticulos);
                iddevolucionNew = em.merge(iddevolucionNew);
            }
            if (idarticuloOld != null && !idarticuloOld.equals(idarticuloNew)) {
                idarticuloOld.getDevolucionesArticulosList().remove(devolucionesArticulos);
                idarticuloOld = em.merge(idarticuloOld);
            }
            if (idarticuloNew != null && !idarticuloNew.equals(idarticuloOld)) {
                idarticuloNew.getDevolucionesArticulosList().add(devolucionesArticulos);
                idarticuloNew = em.merge(idarticuloNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = devolucionesArticulos.getId();
                if (findDevolucionesArticulos(id) == null) {
                    throw new NonexistentEntityException("The devolucionesArticulos with id " + id + " no longer exists.");
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
            DevolucionesArticulos devolucionesArticulos;
            try {
                devolucionesArticulos = em.getReference(DevolucionesArticulos.class, id);
                devolucionesArticulos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The devolucionesArticulos with id " + id + " no longer exists.", enfe);
            }
            VariedadesDevoluciones iddevolucion = devolucionesArticulos.getIddevolucion();
            if (iddevolucion != null) {
                iddevolucion.getDevolucionesArticulosList().remove(devolucionesArticulos);
                iddevolucion = em.merge(iddevolucion);
            }
            VariedadesArticulos idarticulo = devolucionesArticulos.getIdarticulo();
            if (idarticulo != null) {
                idarticulo.getDevolucionesArticulosList().remove(devolucionesArticulos);
                idarticulo = em.merge(idarticulo);
            }
            em.remove(devolucionesArticulos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DevolucionesArticulos> findDevolucionesArticulosEntities() {
        return findDevolucionesArticulosEntities(true, -1, -1);
    }

    public List<DevolucionesArticulos> findDevolucionesArticulosEntities(int maxResults, int firstResult) {
        return findDevolucionesArticulosEntities(false, maxResults, firstResult);
    }

    private List<DevolucionesArticulos> findDevolucionesArticulosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DevolucionesArticulos.class));
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

    public DevolucionesArticulos findDevolucionesArticulos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DevolucionesArticulos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDevolucionesArticulosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DevolucionesArticulos> rt = cq.from(DevolucionesArticulos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
