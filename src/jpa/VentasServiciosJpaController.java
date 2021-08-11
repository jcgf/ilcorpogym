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
import entity.GymPlanes;
import entity.VentasServicios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class VentasServiciosJpaController implements Serializable {

    public VentasServiciosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VentasServicios ventasServicios) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesVentas idventa = ventasServicios.getIdventa();
            if (idventa != null) {
                idventa = em.getReference(idventa.getClass(), idventa.getId());
                ventasServicios.setIdventa(idventa);
            }
            GymPlanes idservicio = ventasServicios.getIdservicio();
            if (idservicio != null) {
                idservicio = em.getReference(idservicio.getClass(), idservicio.getId());
                ventasServicios.setIdservicio(idservicio);
            }
            em.persist(ventasServicios);
            if (idventa != null) {
                idventa.getVentasServiciosList().add(ventasServicios);
                idventa = em.merge(idventa);
            }
            if (idservicio != null) {
                idservicio.getVentasServiciosList().add(ventasServicios);
                idservicio = em.merge(idservicio);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VentasServicios ventasServicios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VentasServicios persistentVentasServicios = em.find(VentasServicios.class, ventasServicios.getId());
            VariedadesVentas idventaOld = persistentVentasServicios.getIdventa();
            VariedadesVentas idventaNew = ventasServicios.getIdventa();
            GymPlanes idservicioOld = persistentVentasServicios.getIdservicio();
            GymPlanes idservicioNew = ventasServicios.getIdservicio();
            if (idventaNew != null) {
                idventaNew = em.getReference(idventaNew.getClass(), idventaNew.getId());
                ventasServicios.setIdventa(idventaNew);
            }
            if (idservicioNew != null) {
                idservicioNew = em.getReference(idservicioNew.getClass(), idservicioNew.getId());
                ventasServicios.setIdservicio(idservicioNew);
            }
            ventasServicios = em.merge(ventasServicios);
            if (idventaOld != null && !idventaOld.equals(idventaNew)) {
                idventaOld.getVentasServiciosList().remove(ventasServicios);
                idventaOld = em.merge(idventaOld);
            }
            if (idventaNew != null && !idventaNew.equals(idventaOld)) {
                idventaNew.getVentasServiciosList().add(ventasServicios);
                idventaNew = em.merge(idventaNew);
            }
            if (idservicioOld != null && !idservicioOld.equals(idservicioNew)) {
                idservicioOld.getVentasServiciosList().remove(ventasServicios);
                idservicioOld = em.merge(idservicioOld);
            }
            if (idservicioNew != null && !idservicioNew.equals(idservicioOld)) {
                idservicioNew.getVentasServiciosList().add(ventasServicios);
                idservicioNew = em.merge(idservicioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ventasServicios.getId();
                if (findVentasServicios(id) == null) {
                    throw new NonexistentEntityException("The ventasServicios with id " + id + " no longer exists.");
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
            VentasServicios ventasServicios;
            try {
                ventasServicios = em.getReference(VentasServicios.class, id);
                ventasServicios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventasServicios with id " + id + " no longer exists.", enfe);
            }
            VariedadesVentas idventa = ventasServicios.getIdventa();
            if (idventa != null) {
                idventa.getVentasServiciosList().remove(ventasServicios);
                idventa = em.merge(idventa);
            }
            GymPlanes idservicio = ventasServicios.getIdservicio();
            if (idservicio != null) {
                idservicio.getVentasServiciosList().remove(ventasServicios);
                idservicio = em.merge(idservicio);
            }
            em.remove(ventasServicios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VentasServicios> findVentasServiciosEntities() {
        return findVentasServiciosEntities(true, -1, -1);
    }

    public List<VentasServicios> findVentasServiciosEntities(int maxResults, int firstResult) {
        return findVentasServiciosEntities(false, maxResults, firstResult);
    }

    private List<VentasServicios> findVentasServiciosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VentasServicios.class));
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

    public VentasServicios findVentasServicios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VentasServicios.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasServiciosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VentasServicios> rt = cq.from(VentasServicios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
