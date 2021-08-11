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
import entity.GymAsignados;
import entity.GymPlanes;
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
public class GymPlanesJpaController implements Serializable {

    public GymPlanesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GymPlanes gymPlanes) {
        if (gymPlanes.getGymAsignadosList() == null) {
            gymPlanes.setGymAsignadosList(new ArrayList<GymAsignados>());
        }
        if (gymPlanes.getVentasServiciosList() == null) {
            gymPlanes.setVentasServiciosList(new ArrayList<VentasServicios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<GymAsignados> attachedGymAsignadosList = new ArrayList<GymAsignados>();
            for (GymAsignados gymAsignadosListGymAsignadosToAttach : gymPlanes.getGymAsignadosList()) {
                gymAsignadosListGymAsignadosToAttach = em.getReference(gymAsignadosListGymAsignadosToAttach.getClass(), gymAsignadosListGymAsignadosToAttach.getId());
                attachedGymAsignadosList.add(gymAsignadosListGymAsignadosToAttach);
            }
            gymPlanes.setGymAsignadosList(attachedGymAsignadosList);
            List<VentasServicios> attachedVentasServiciosList = new ArrayList<VentasServicios>();
            for (VentasServicios ventasServiciosListVentasServiciosToAttach : gymPlanes.getVentasServiciosList()) {
                ventasServiciosListVentasServiciosToAttach = em.getReference(ventasServiciosListVentasServiciosToAttach.getClass(), ventasServiciosListVentasServiciosToAttach.getId());
                attachedVentasServiciosList.add(ventasServiciosListVentasServiciosToAttach);
            }
            gymPlanes.setVentasServiciosList(attachedVentasServiciosList);
            em.persist(gymPlanes);
            for (GymAsignados gymAsignadosListGymAsignados : gymPlanes.getGymAsignadosList()) {
                GymPlanes oldIdplanOfGymAsignadosListGymAsignados = gymAsignadosListGymAsignados.getIdplan();
                gymAsignadosListGymAsignados.setIdplan(gymPlanes);
                gymAsignadosListGymAsignados = em.merge(gymAsignadosListGymAsignados);
                if (oldIdplanOfGymAsignadosListGymAsignados != null) {
                    oldIdplanOfGymAsignadosListGymAsignados.getGymAsignadosList().remove(gymAsignadosListGymAsignados);
                    oldIdplanOfGymAsignadosListGymAsignados = em.merge(oldIdplanOfGymAsignadosListGymAsignados);
                }
            }
            for (VentasServicios ventasServiciosListVentasServicios : gymPlanes.getVentasServiciosList()) {
                GymPlanes oldIdservicioOfVentasServiciosListVentasServicios = ventasServiciosListVentasServicios.getIdservicio();
                ventasServiciosListVentasServicios.setIdservicio(gymPlanes);
                ventasServiciosListVentasServicios = em.merge(ventasServiciosListVentasServicios);
                if (oldIdservicioOfVentasServiciosListVentasServicios != null) {
                    oldIdservicioOfVentasServiciosListVentasServicios.getVentasServiciosList().remove(ventasServiciosListVentasServicios);
                    oldIdservicioOfVentasServiciosListVentasServicios = em.merge(oldIdservicioOfVentasServiciosListVentasServicios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GymPlanes gymPlanes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymPlanes persistentGymPlanes = em.find(GymPlanes.class, gymPlanes.getId());
            List<GymAsignados> gymAsignadosListOld = persistentGymPlanes.getGymAsignadosList();
            List<GymAsignados> gymAsignadosListNew = gymPlanes.getGymAsignadosList();
            List<VentasServicios> ventasServiciosListOld = persistentGymPlanes.getVentasServiciosList();
            List<VentasServicios> ventasServiciosListNew = gymPlanes.getVentasServiciosList();
            List<String> illegalOrphanMessages = null;
            for (GymAsignados gymAsignadosListOldGymAsignados : gymAsignadosListOld) {
                if (!gymAsignadosListNew.contains(gymAsignadosListOldGymAsignados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GymAsignados " + gymAsignadosListOldGymAsignados + " since its idplan field is not nullable.");
                }
            }
            for (VentasServicios ventasServiciosListOldVentasServicios : ventasServiciosListOld) {
                if (!ventasServiciosListNew.contains(ventasServiciosListOldVentasServicios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VentasServicios " + ventasServiciosListOldVentasServicios + " since its idservicio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GymAsignados> attachedGymAsignadosListNew = new ArrayList<GymAsignados>();
            for (GymAsignados gymAsignadosListNewGymAsignadosToAttach : gymAsignadosListNew) {
                gymAsignadosListNewGymAsignadosToAttach = em.getReference(gymAsignadosListNewGymAsignadosToAttach.getClass(), gymAsignadosListNewGymAsignadosToAttach.getId());
                attachedGymAsignadosListNew.add(gymAsignadosListNewGymAsignadosToAttach);
            }
            gymAsignadosListNew = attachedGymAsignadosListNew;
            gymPlanes.setGymAsignadosList(gymAsignadosListNew);
            List<VentasServicios> attachedVentasServiciosListNew = new ArrayList<VentasServicios>();
            for (VentasServicios ventasServiciosListNewVentasServiciosToAttach : ventasServiciosListNew) {
                ventasServiciosListNewVentasServiciosToAttach = em.getReference(ventasServiciosListNewVentasServiciosToAttach.getClass(), ventasServiciosListNewVentasServiciosToAttach.getId());
                attachedVentasServiciosListNew.add(ventasServiciosListNewVentasServiciosToAttach);
            }
            ventasServiciosListNew = attachedVentasServiciosListNew;
            gymPlanes.setVentasServiciosList(ventasServiciosListNew);
            gymPlanes = em.merge(gymPlanes);
            for (GymAsignados gymAsignadosListNewGymAsignados : gymAsignadosListNew) {
                if (!gymAsignadosListOld.contains(gymAsignadosListNewGymAsignados)) {
                    GymPlanes oldIdplanOfGymAsignadosListNewGymAsignados = gymAsignadosListNewGymAsignados.getIdplan();
                    gymAsignadosListNewGymAsignados.setIdplan(gymPlanes);
                    gymAsignadosListNewGymAsignados = em.merge(gymAsignadosListNewGymAsignados);
                    if (oldIdplanOfGymAsignadosListNewGymAsignados != null && !oldIdplanOfGymAsignadosListNewGymAsignados.equals(gymPlanes)) {
                        oldIdplanOfGymAsignadosListNewGymAsignados.getGymAsignadosList().remove(gymAsignadosListNewGymAsignados);
                        oldIdplanOfGymAsignadosListNewGymAsignados = em.merge(oldIdplanOfGymAsignadosListNewGymAsignados);
                    }
                }
            }
            for (VentasServicios ventasServiciosListNewVentasServicios : ventasServiciosListNew) {
                if (!ventasServiciosListOld.contains(ventasServiciosListNewVentasServicios)) {
                    GymPlanes oldIdservicioOfVentasServiciosListNewVentasServicios = ventasServiciosListNewVentasServicios.getIdservicio();
                    ventasServiciosListNewVentasServicios.setIdservicio(gymPlanes);
                    ventasServiciosListNewVentasServicios = em.merge(ventasServiciosListNewVentasServicios);
                    if (oldIdservicioOfVentasServiciosListNewVentasServicios != null && !oldIdservicioOfVentasServiciosListNewVentasServicios.equals(gymPlanes)) {
                        oldIdservicioOfVentasServiciosListNewVentasServicios.getVentasServiciosList().remove(ventasServiciosListNewVentasServicios);
                        oldIdservicioOfVentasServiciosListNewVentasServicios = em.merge(oldIdservicioOfVentasServiciosListNewVentasServicios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gymPlanes.getId();
                if (findGymPlanes(id) == null) {
                    throw new NonexistentEntityException("The gymPlanes with id " + id + " no longer exists.");
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
            GymPlanes gymPlanes;
            try {
                gymPlanes = em.getReference(GymPlanes.class, id);
                gymPlanes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gymPlanes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GymAsignados> gymAsignadosListOrphanCheck = gymPlanes.getGymAsignadosList();
            for (GymAsignados gymAsignadosListOrphanCheckGymAsignados : gymAsignadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GymPlanes (" + gymPlanes + ") cannot be destroyed since the GymAsignados " + gymAsignadosListOrphanCheckGymAsignados + " in its gymAsignadosList field has a non-nullable idplan field.");
            }
            List<VentasServicios> ventasServiciosListOrphanCheck = gymPlanes.getVentasServiciosList();
            for (VentasServicios ventasServiciosListOrphanCheckVentasServicios : ventasServiciosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GymPlanes (" + gymPlanes + ") cannot be destroyed since the VentasServicios " + ventasServiciosListOrphanCheckVentasServicios + " in its ventasServiciosList field has a non-nullable idservicio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gymPlanes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GymPlanes> findGymPlanesEntities() {
        return findGymPlanesEntities(true, -1, -1);
    }

    public List<GymPlanes> findGymPlanesEntities(int maxResults, int firstResult) {
        return findGymPlanesEntities(false, maxResults, firstResult);
    }

    private List<GymPlanes> findGymPlanesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GymPlanes.class));
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

    public GymPlanes findGymPlanes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GymPlanes.class, id);
        } finally {
            em.close();
        }
    }

    public int getGymPlanesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GymPlanes> rt = cq.from(GymPlanes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
