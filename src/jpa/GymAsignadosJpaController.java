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
import entity.GymUsuarios;
import entity.GymPlanes;
import entity.GymContdias;
import java.util.ArrayList;
import java.util.List;
import entity.AsignadosDias;
import entity.GymAsignados;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class GymAsignadosJpaController implements Serializable {

    public GymAsignadosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GymAsignados gymAsignados) {
        if (gymAsignados.getGymContdiasList() == null) {
            gymAsignados.setGymContdiasList(new ArrayList<GymContdias>());
        }
        if (gymAsignados.getAsignadosDiasList() == null) {
            gymAsignados.setAsignadosDiasList(new ArrayList<AsignadosDias>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymUsuarios iduser = gymAsignados.getIduser();
            if (iduser != null) {
                iduser = em.getReference(iduser.getClass(), iduser.getId());
                gymAsignados.setIduser(iduser);
            }
            GymPlanes idplan = gymAsignados.getIdplan();
            if (idplan != null) {
                idplan = em.getReference(idplan.getClass(), idplan.getId());
                gymAsignados.setIdplan(idplan);
            }
            List<GymContdias> attachedGymContdiasList = new ArrayList<GymContdias>();
            for (GymContdias gymContdiasListGymContdiasToAttach : gymAsignados.getGymContdiasList()) {
                gymContdiasListGymContdiasToAttach = em.getReference(gymContdiasListGymContdiasToAttach.getClass(), gymContdiasListGymContdiasToAttach.getId());
                attachedGymContdiasList.add(gymContdiasListGymContdiasToAttach);
            }
            gymAsignados.setGymContdiasList(attachedGymContdiasList);
            List<AsignadosDias> attachedAsignadosDiasList = new ArrayList<AsignadosDias>();
            for (AsignadosDias asignadosDiasListAsignadosDiasToAttach : gymAsignados.getAsignadosDiasList()) {
                asignadosDiasListAsignadosDiasToAttach = em.getReference(asignadosDiasListAsignadosDiasToAttach.getClass(), asignadosDiasListAsignadosDiasToAttach.getId());
                attachedAsignadosDiasList.add(asignadosDiasListAsignadosDiasToAttach);
            }
            gymAsignados.setAsignadosDiasList(attachedAsignadosDiasList);
            em.persist(gymAsignados);
            if (iduser != null) {
                iduser.getGymAsignadosList().add(gymAsignados);
                iduser = em.merge(iduser);
            }
            if (idplan != null) {
                idplan.getGymAsignadosList().add(gymAsignados);
                idplan = em.merge(idplan);
            }
            for (GymContdias gymContdiasListGymContdias : gymAsignados.getGymContdiasList()) {
                GymAsignados oldIdasignacionOfGymContdiasListGymContdias = gymContdiasListGymContdias.getIdasignacion();
                gymContdiasListGymContdias.setIdasignacion(gymAsignados);
                gymContdiasListGymContdias = em.merge(gymContdiasListGymContdias);
                if (oldIdasignacionOfGymContdiasListGymContdias != null) {
                    oldIdasignacionOfGymContdiasListGymContdias.getGymContdiasList().remove(gymContdiasListGymContdias);
                    oldIdasignacionOfGymContdiasListGymContdias = em.merge(oldIdasignacionOfGymContdiasListGymContdias);
                }
            }
            for (AsignadosDias asignadosDiasListAsignadosDias : gymAsignados.getAsignadosDiasList()) {
                GymAsignados oldIdasignacionOfAsignadosDiasListAsignadosDias = asignadosDiasListAsignadosDias.getIdasignacion();
                asignadosDiasListAsignadosDias.setIdasignacion(gymAsignados);
                asignadosDiasListAsignadosDias = em.merge(asignadosDiasListAsignadosDias);
                if (oldIdasignacionOfAsignadosDiasListAsignadosDias != null) {
                    oldIdasignacionOfAsignadosDiasListAsignadosDias.getAsignadosDiasList().remove(asignadosDiasListAsignadosDias);
                    oldIdasignacionOfAsignadosDiasListAsignadosDias = em.merge(oldIdasignacionOfAsignadosDiasListAsignadosDias);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GymAsignados gymAsignados) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymAsignados persistentGymAsignados = em.find(GymAsignados.class, gymAsignados.getId());
            GymUsuarios iduserOld = persistentGymAsignados.getIduser();
            GymUsuarios iduserNew = gymAsignados.getIduser();
            GymPlanes idplanOld = persistentGymAsignados.getIdplan();
            GymPlanes idplanNew = gymAsignados.getIdplan();
            List<GymContdias> gymContdiasListOld = persistentGymAsignados.getGymContdiasList();
            List<GymContdias> gymContdiasListNew = gymAsignados.getGymContdiasList();
            List<AsignadosDias> asignadosDiasListOld = persistentGymAsignados.getAsignadosDiasList();
            List<AsignadosDias> asignadosDiasListNew = gymAsignados.getAsignadosDiasList();
            List<String> illegalOrphanMessages = null;
            for (GymContdias gymContdiasListOldGymContdias : gymContdiasListOld) {
                if (!gymContdiasListNew.contains(gymContdiasListOldGymContdias)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GymContdias " + gymContdiasListOldGymContdias + " since its idasignacion field is not nullable.");
                }
            }
            for (AsignadosDias asignadosDiasListOldAsignadosDias : asignadosDiasListOld) {
                if (!asignadosDiasListNew.contains(asignadosDiasListOldAsignadosDias)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AsignadosDias " + asignadosDiasListOldAsignadosDias + " since its idasignacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (iduserNew != null) {
                iduserNew = em.getReference(iduserNew.getClass(), iduserNew.getId());
                gymAsignados.setIduser(iduserNew);
            }
            if (idplanNew != null) {
                idplanNew = em.getReference(idplanNew.getClass(), idplanNew.getId());
                gymAsignados.setIdplan(idplanNew);
            }
            List<GymContdias> attachedGymContdiasListNew = new ArrayList<GymContdias>();
            for (GymContdias gymContdiasListNewGymContdiasToAttach : gymContdiasListNew) {
                gymContdiasListNewGymContdiasToAttach = em.getReference(gymContdiasListNewGymContdiasToAttach.getClass(), gymContdiasListNewGymContdiasToAttach.getId());
                attachedGymContdiasListNew.add(gymContdiasListNewGymContdiasToAttach);
            }
            gymContdiasListNew = attachedGymContdiasListNew;
            gymAsignados.setGymContdiasList(gymContdiasListNew);
            List<AsignadosDias> attachedAsignadosDiasListNew = new ArrayList<AsignadosDias>();
            for (AsignadosDias asignadosDiasListNewAsignadosDiasToAttach : asignadosDiasListNew) {
                asignadosDiasListNewAsignadosDiasToAttach = em.getReference(asignadosDiasListNewAsignadosDiasToAttach.getClass(), asignadosDiasListNewAsignadosDiasToAttach.getId());
                attachedAsignadosDiasListNew.add(asignadosDiasListNewAsignadosDiasToAttach);
            }
            asignadosDiasListNew = attachedAsignadosDiasListNew;
            gymAsignados.setAsignadosDiasList(asignadosDiasListNew);
            gymAsignados = em.merge(gymAsignados);
            if (iduserOld != null && !iduserOld.equals(iduserNew)) {
                iduserOld.getGymAsignadosList().remove(gymAsignados);
                iduserOld = em.merge(iduserOld);
            }
            if (iduserNew != null && !iduserNew.equals(iduserOld)) {
                iduserNew.getGymAsignadosList().add(gymAsignados);
                iduserNew = em.merge(iduserNew);
            }
            if (idplanOld != null && !idplanOld.equals(idplanNew)) {
                idplanOld.getGymAsignadosList().remove(gymAsignados);
                idplanOld = em.merge(idplanOld);
            }
            if (idplanNew != null && !idplanNew.equals(idplanOld)) {
                idplanNew.getGymAsignadosList().add(gymAsignados);
                idplanNew = em.merge(idplanNew);
            }
            for (GymContdias gymContdiasListNewGymContdias : gymContdiasListNew) {
                if (!gymContdiasListOld.contains(gymContdiasListNewGymContdias)) {
                    GymAsignados oldIdasignacionOfGymContdiasListNewGymContdias = gymContdiasListNewGymContdias.getIdasignacion();
                    gymContdiasListNewGymContdias.setIdasignacion(gymAsignados);
                    gymContdiasListNewGymContdias = em.merge(gymContdiasListNewGymContdias);
                    if (oldIdasignacionOfGymContdiasListNewGymContdias != null && !oldIdasignacionOfGymContdiasListNewGymContdias.equals(gymAsignados)) {
                        oldIdasignacionOfGymContdiasListNewGymContdias.getGymContdiasList().remove(gymContdiasListNewGymContdias);
                        oldIdasignacionOfGymContdiasListNewGymContdias = em.merge(oldIdasignacionOfGymContdiasListNewGymContdias);
                    }
                }
            }
            for (AsignadosDias asignadosDiasListNewAsignadosDias : asignadosDiasListNew) {
                if (!asignadosDiasListOld.contains(asignadosDiasListNewAsignadosDias)) {
                    GymAsignados oldIdasignacionOfAsignadosDiasListNewAsignadosDias = asignadosDiasListNewAsignadosDias.getIdasignacion();
                    asignadosDiasListNewAsignadosDias.setIdasignacion(gymAsignados);
                    asignadosDiasListNewAsignadosDias = em.merge(asignadosDiasListNewAsignadosDias);
                    if (oldIdasignacionOfAsignadosDiasListNewAsignadosDias != null && !oldIdasignacionOfAsignadosDiasListNewAsignadosDias.equals(gymAsignados)) {
                        oldIdasignacionOfAsignadosDiasListNewAsignadosDias.getAsignadosDiasList().remove(asignadosDiasListNewAsignadosDias);
                        oldIdasignacionOfAsignadosDiasListNewAsignadosDias = em.merge(oldIdasignacionOfAsignadosDiasListNewAsignadosDias);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gymAsignados.getId();
                if (findGymAsignados(id) == null) {
                    throw new NonexistentEntityException("The gymAsignados with id " + id + " no longer exists.");
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
            GymAsignados gymAsignados;
            try {
                gymAsignados = em.getReference(GymAsignados.class, id);
                gymAsignados.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gymAsignados with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GymContdias> gymContdiasListOrphanCheck = gymAsignados.getGymContdiasList();
            for (GymContdias gymContdiasListOrphanCheckGymContdias : gymContdiasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GymAsignados (" + gymAsignados + ") cannot be destroyed since the GymContdias " + gymContdiasListOrphanCheckGymContdias + " in its gymContdiasList field has a non-nullable idasignacion field.");
            }
            List<AsignadosDias> asignadosDiasListOrphanCheck = gymAsignados.getAsignadosDiasList();
            for (AsignadosDias asignadosDiasListOrphanCheckAsignadosDias : asignadosDiasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GymAsignados (" + gymAsignados + ") cannot be destroyed since the AsignadosDias " + asignadosDiasListOrphanCheckAsignadosDias + " in its asignadosDiasList field has a non-nullable idasignacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            GymUsuarios iduser = gymAsignados.getIduser();
            if (iduser != null) {
                iduser.getGymAsignadosList().remove(gymAsignados);
                iduser = em.merge(iduser);
            }
            GymPlanes idplan = gymAsignados.getIdplan();
            if (idplan != null) {
                idplan.getGymAsignadosList().remove(gymAsignados);
                idplan = em.merge(idplan);
            }
            em.remove(gymAsignados);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GymAsignados> findGymAsignadosEntities() {
        return findGymAsignadosEntities(true, -1, -1);
    }

    public List<GymAsignados> findGymAsignadosEntities(int maxResults, int firstResult) {
        return findGymAsignadosEntities(false, maxResults, firstResult);
    }

    private List<GymAsignados> findGymAsignadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GymAsignados.class));
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

    public GymAsignados findGymAsignados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GymAsignados.class, id);
        } finally {
            em.close();
        }
    }

    public int getGymAsignadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GymAsignados> rt = cq.from(GymAsignados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
