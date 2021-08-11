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
import entity.VariedadesVentas;
import entity.VentasArticulos;
import java.util.ArrayList;
import java.util.List;
import entity.VentasServicios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class VariedadesVentasJpaController implements Serializable {

    public VariedadesVentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariedadesVentas variedadesVentas) {
        if (variedadesVentas.getVentasArticulosList() == null) {
            variedadesVentas.setVentasArticulosList(new ArrayList<VentasArticulos>());
        }
        if (variedadesVentas.getVentasServiciosList() == null) {
            variedadesVentas.setVentasServiciosList(new ArrayList<VentasServicios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog idusuario = variedadesVentas.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getId());
                variedadesVentas.setIdusuario(idusuario);
            }
            List<VentasArticulos> attachedVentasArticulosList = new ArrayList<VentasArticulos>();
            for (VentasArticulos ventasArticulosListVentasArticulosToAttach : variedadesVentas.getVentasArticulosList()) {
                ventasArticulosListVentasArticulosToAttach = em.getReference(ventasArticulosListVentasArticulosToAttach.getClass(), ventasArticulosListVentasArticulosToAttach.getId());
                attachedVentasArticulosList.add(ventasArticulosListVentasArticulosToAttach);
            }
            variedadesVentas.setVentasArticulosList(attachedVentasArticulosList);
            List<VentasServicios> attachedVentasServiciosList = new ArrayList<VentasServicios>();
            for (VentasServicios ventasServiciosListVentasServiciosToAttach : variedadesVentas.getVentasServiciosList()) {
                ventasServiciosListVentasServiciosToAttach = em.getReference(ventasServiciosListVentasServiciosToAttach.getClass(), ventasServiciosListVentasServiciosToAttach.getId());
                attachedVentasServiciosList.add(ventasServiciosListVentasServiciosToAttach);
            }
            variedadesVentas.setVentasServiciosList(attachedVentasServiciosList);
            em.persist(variedadesVentas);
            if (idusuario != null) {
                idusuario.getVariedadesVentasList().add(variedadesVentas);
                idusuario = em.merge(idusuario);
            }
            for (VentasArticulos ventasArticulosListVentasArticulos : variedadesVentas.getVentasArticulosList()) {
                VariedadesVentas oldIdventaOfVentasArticulosListVentasArticulos = ventasArticulosListVentasArticulos.getIdventa();
                ventasArticulosListVentasArticulos.setIdventa(variedadesVentas);
                ventasArticulosListVentasArticulos = em.merge(ventasArticulosListVentasArticulos);
                if (oldIdventaOfVentasArticulosListVentasArticulos != null) {
                    oldIdventaOfVentasArticulosListVentasArticulos.getVentasArticulosList().remove(ventasArticulosListVentasArticulos);
                    oldIdventaOfVentasArticulosListVentasArticulos = em.merge(oldIdventaOfVentasArticulosListVentasArticulos);
                }
            }
            for (VentasServicios ventasServiciosListVentasServicios : variedadesVentas.getVentasServiciosList()) {
                VariedadesVentas oldIdventaOfVentasServiciosListVentasServicios = ventasServiciosListVentasServicios.getIdventa();
                ventasServiciosListVentasServicios.setIdventa(variedadesVentas);
                ventasServiciosListVentasServicios = em.merge(ventasServiciosListVentasServicios);
                if (oldIdventaOfVentasServiciosListVentasServicios != null) {
                    oldIdventaOfVentasServiciosListVentasServicios.getVentasServiciosList().remove(ventasServiciosListVentasServicios);
                    oldIdventaOfVentasServiciosListVentasServicios = em.merge(oldIdventaOfVentasServiciosListVentasServicios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariedadesVentas variedadesVentas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesVentas persistentVariedadesVentas = em.find(VariedadesVentas.class, variedadesVentas.getId());
            UserLog idusuarioOld = persistentVariedadesVentas.getIdusuario();
            UserLog idusuarioNew = variedadesVentas.getIdusuario();
            List<VentasArticulos> ventasArticulosListOld = persistentVariedadesVentas.getVentasArticulosList();
            List<VentasArticulos> ventasArticulosListNew = variedadesVentas.getVentasArticulosList();
            List<VentasServicios> ventasServiciosListOld = persistentVariedadesVentas.getVentasServiciosList();
            List<VentasServicios> ventasServiciosListNew = variedadesVentas.getVentasServiciosList();
            List<String> illegalOrphanMessages = null;
            for (VentasArticulos ventasArticulosListOldVentasArticulos : ventasArticulosListOld) {
                if (!ventasArticulosListNew.contains(ventasArticulosListOldVentasArticulos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VentasArticulos " + ventasArticulosListOldVentasArticulos + " since its idventa field is not nullable.");
                }
            }
            for (VentasServicios ventasServiciosListOldVentasServicios : ventasServiciosListOld) {
                if (!ventasServiciosListNew.contains(ventasServiciosListOldVentasServicios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VentasServicios " + ventasServiciosListOldVentasServicios + " since its idventa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getId());
                variedadesVentas.setIdusuario(idusuarioNew);
            }
            List<VentasArticulos> attachedVentasArticulosListNew = new ArrayList<VentasArticulos>();
            for (VentasArticulos ventasArticulosListNewVentasArticulosToAttach : ventasArticulosListNew) {
                ventasArticulosListNewVentasArticulosToAttach = em.getReference(ventasArticulosListNewVentasArticulosToAttach.getClass(), ventasArticulosListNewVentasArticulosToAttach.getId());
                attachedVentasArticulosListNew.add(ventasArticulosListNewVentasArticulosToAttach);
            }
            ventasArticulosListNew = attachedVentasArticulosListNew;
            variedadesVentas.setVentasArticulosList(ventasArticulosListNew);
            List<VentasServicios> attachedVentasServiciosListNew = new ArrayList<VentasServicios>();
            for (VentasServicios ventasServiciosListNewVentasServiciosToAttach : ventasServiciosListNew) {
                ventasServiciosListNewVentasServiciosToAttach = em.getReference(ventasServiciosListNewVentasServiciosToAttach.getClass(), ventasServiciosListNewVentasServiciosToAttach.getId());
                attachedVentasServiciosListNew.add(ventasServiciosListNewVentasServiciosToAttach);
            }
            ventasServiciosListNew = attachedVentasServiciosListNew;
            variedadesVentas.setVentasServiciosList(ventasServiciosListNew);
            variedadesVentas = em.merge(variedadesVentas);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getVariedadesVentasList().remove(variedadesVentas);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getVariedadesVentasList().add(variedadesVentas);
                idusuarioNew = em.merge(idusuarioNew);
            }
            for (VentasArticulos ventasArticulosListNewVentasArticulos : ventasArticulosListNew) {
                if (!ventasArticulosListOld.contains(ventasArticulosListNewVentasArticulos)) {
                    VariedadesVentas oldIdventaOfVentasArticulosListNewVentasArticulos = ventasArticulosListNewVentasArticulos.getIdventa();
                    ventasArticulosListNewVentasArticulos.setIdventa(variedadesVentas);
                    ventasArticulosListNewVentasArticulos = em.merge(ventasArticulosListNewVentasArticulos);
                    if (oldIdventaOfVentasArticulosListNewVentasArticulos != null && !oldIdventaOfVentasArticulosListNewVentasArticulos.equals(variedadesVentas)) {
                        oldIdventaOfVentasArticulosListNewVentasArticulos.getVentasArticulosList().remove(ventasArticulosListNewVentasArticulos);
                        oldIdventaOfVentasArticulosListNewVentasArticulos = em.merge(oldIdventaOfVentasArticulosListNewVentasArticulos);
                    }
                }
            }
            for (VentasServicios ventasServiciosListNewVentasServicios : ventasServiciosListNew) {
                if (!ventasServiciosListOld.contains(ventasServiciosListNewVentasServicios)) {
                    VariedadesVentas oldIdventaOfVentasServiciosListNewVentasServicios = ventasServiciosListNewVentasServicios.getIdventa();
                    ventasServiciosListNewVentasServicios.setIdventa(variedadesVentas);
                    ventasServiciosListNewVentasServicios = em.merge(ventasServiciosListNewVentasServicios);
                    if (oldIdventaOfVentasServiciosListNewVentasServicios != null && !oldIdventaOfVentasServiciosListNewVentasServicios.equals(variedadesVentas)) {
                        oldIdventaOfVentasServiciosListNewVentasServicios.getVentasServiciosList().remove(ventasServiciosListNewVentasServicios);
                        oldIdventaOfVentasServiciosListNewVentasServicios = em.merge(oldIdventaOfVentasServiciosListNewVentasServicios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variedadesVentas.getId();
                if (findVariedadesVentas(id) == null) {
                    throw new NonexistentEntityException("The variedadesVentas with id " + id + " no longer exists.");
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
            VariedadesVentas variedadesVentas;
            try {
                variedadesVentas = em.getReference(VariedadesVentas.class, id);
                variedadesVentas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variedadesVentas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VentasArticulos> ventasArticulosListOrphanCheck = variedadesVentas.getVentasArticulosList();
            for (VentasArticulos ventasArticulosListOrphanCheckVentasArticulos : ventasArticulosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesVentas (" + variedadesVentas + ") cannot be destroyed since the VentasArticulos " + ventasArticulosListOrphanCheckVentasArticulos + " in its ventasArticulosList field has a non-nullable idventa field.");
            }
            List<VentasServicios> ventasServiciosListOrphanCheck = variedadesVentas.getVentasServiciosList();
            for (VentasServicios ventasServiciosListOrphanCheckVentasServicios : ventasServiciosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesVentas (" + variedadesVentas + ") cannot be destroyed since the VentasServicios " + ventasServiciosListOrphanCheckVentasServicios + " in its ventasServiciosList field has a non-nullable idventa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserLog idusuario = variedadesVentas.getIdusuario();
            if (idusuario != null) {
                idusuario.getVariedadesVentasList().remove(variedadesVentas);
                idusuario = em.merge(idusuario);
            }
            em.remove(variedadesVentas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariedadesVentas> findVariedadesVentasEntities() {
        return findVariedadesVentasEntities(true, -1, -1);
    }

    public List<VariedadesVentas> findVariedadesVentasEntities(int maxResults, int firstResult) {
        return findVariedadesVentasEntities(false, maxResults, firstResult);
    }

    private List<VariedadesVentas> findVariedadesVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariedadesVentas.class));
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

    public VariedadesVentas findVariedadesVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariedadesVentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariedadesVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariedadesVentas> rt = cq.from(VariedadesVentas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
