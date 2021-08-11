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
import entity.GymContdias;
import java.util.ArrayList;
import java.util.List;
import entity.GymHuella;
import entity.GymAsignados;
import entity.GymUsuarios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class GymUsuariosJpaController implements Serializable {

    public GymUsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GymUsuarios gymUsuarios) {
        if (gymUsuarios.getGymContdiasList() == null) {
            gymUsuarios.setGymContdiasList(new ArrayList<GymContdias>());
        }
        if (gymUsuarios.getGymHuellaList() == null) {
            gymUsuarios.setGymHuellaList(new ArrayList<GymHuella>());
        }
        if (gymUsuarios.getGymAsignadosList() == null) {
            gymUsuarios.setGymAsignadosList(new ArrayList<GymAsignados>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<GymContdias> attachedGymContdiasList = new ArrayList<GymContdias>();
            for (GymContdias gymContdiasListGymContdiasToAttach : gymUsuarios.getGymContdiasList()) {
                gymContdiasListGymContdiasToAttach = em.getReference(gymContdiasListGymContdiasToAttach.getClass(), gymContdiasListGymContdiasToAttach.getId());
                attachedGymContdiasList.add(gymContdiasListGymContdiasToAttach);
            }
            gymUsuarios.setGymContdiasList(attachedGymContdiasList);
            List<GymHuella> attachedGymHuellaList = new ArrayList<GymHuella>();
            for (GymHuella gymHuellaListGymHuellaToAttach : gymUsuarios.getGymHuellaList()) {
                gymHuellaListGymHuellaToAttach = em.getReference(gymHuellaListGymHuellaToAttach.getClass(), gymHuellaListGymHuellaToAttach.getId());
                attachedGymHuellaList.add(gymHuellaListGymHuellaToAttach);
            }
            gymUsuarios.setGymHuellaList(attachedGymHuellaList);
            List<GymAsignados> attachedGymAsignadosList = new ArrayList<GymAsignados>();
            for (GymAsignados gymAsignadosListGymAsignadosToAttach : gymUsuarios.getGymAsignadosList()) {
                gymAsignadosListGymAsignadosToAttach = em.getReference(gymAsignadosListGymAsignadosToAttach.getClass(), gymAsignadosListGymAsignadosToAttach.getId());
                attachedGymAsignadosList.add(gymAsignadosListGymAsignadosToAttach);
            }
            gymUsuarios.setGymAsignadosList(attachedGymAsignadosList);
            em.persist(gymUsuarios);
            for (GymContdias gymContdiasListGymContdias : gymUsuarios.getGymContdiasList()) {
                GymUsuarios oldIdusuarioOfGymContdiasListGymContdias = gymContdiasListGymContdias.getIdusuario();
                gymContdiasListGymContdias.setIdusuario(gymUsuarios);
                gymContdiasListGymContdias = em.merge(gymContdiasListGymContdias);
                if (oldIdusuarioOfGymContdiasListGymContdias != null) {
                    oldIdusuarioOfGymContdiasListGymContdias.getGymContdiasList().remove(gymContdiasListGymContdias);
                    oldIdusuarioOfGymContdiasListGymContdias = em.merge(oldIdusuarioOfGymContdiasListGymContdias);
                }
            }
            for (GymHuella gymHuellaListGymHuella : gymUsuarios.getGymHuellaList()) {
                GymUsuarios oldIduserOfGymHuellaListGymHuella = gymHuellaListGymHuella.getIduser();
                gymHuellaListGymHuella.setIduser(gymUsuarios);
                gymHuellaListGymHuella = em.merge(gymHuellaListGymHuella);
                if (oldIduserOfGymHuellaListGymHuella != null) {
                    oldIduserOfGymHuellaListGymHuella.getGymHuellaList().remove(gymHuellaListGymHuella);
                    oldIduserOfGymHuellaListGymHuella = em.merge(oldIduserOfGymHuellaListGymHuella);
                }
            }
            for (GymAsignados gymAsignadosListGymAsignados : gymUsuarios.getGymAsignadosList()) {
                GymUsuarios oldIduserOfGymAsignadosListGymAsignados = gymAsignadosListGymAsignados.getIduser();
                gymAsignadosListGymAsignados.setIduser(gymUsuarios);
                gymAsignadosListGymAsignados = em.merge(gymAsignadosListGymAsignados);
                if (oldIduserOfGymAsignadosListGymAsignados != null) {
                    oldIduserOfGymAsignadosListGymAsignados.getGymAsignadosList().remove(gymAsignadosListGymAsignados);
                    oldIduserOfGymAsignadosListGymAsignados = em.merge(oldIduserOfGymAsignadosListGymAsignados);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GymUsuarios gymUsuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GymUsuarios persistentGymUsuarios = em.find(GymUsuarios.class, gymUsuarios.getId());
            List<GymContdias> gymContdiasListOld = persistentGymUsuarios.getGymContdiasList();
            List<GymContdias> gymContdiasListNew = gymUsuarios.getGymContdiasList();
            List<GymHuella> gymHuellaListOld = persistentGymUsuarios.getGymHuellaList();
            List<GymHuella> gymHuellaListNew = gymUsuarios.getGymHuellaList();
            List<GymAsignados> gymAsignadosListOld = persistentGymUsuarios.getGymAsignadosList();
            List<GymAsignados> gymAsignadosListNew = gymUsuarios.getGymAsignadosList();
            List<String> illegalOrphanMessages = null;
            for (GymContdias gymContdiasListOldGymContdias : gymContdiasListOld) {
                if (!gymContdiasListNew.contains(gymContdiasListOldGymContdias)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GymContdias " + gymContdiasListOldGymContdias + " since its idusuario field is not nullable.");
                }
            }
            for (GymHuella gymHuellaListOldGymHuella : gymHuellaListOld) {
                if (!gymHuellaListNew.contains(gymHuellaListOldGymHuella)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GymHuella " + gymHuellaListOldGymHuella + " since its iduser field is not nullable.");
                }
            }
            for (GymAsignados gymAsignadosListOldGymAsignados : gymAsignadosListOld) {
                if (!gymAsignadosListNew.contains(gymAsignadosListOldGymAsignados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GymAsignados " + gymAsignadosListOldGymAsignados + " since its iduser field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GymContdias> attachedGymContdiasListNew = new ArrayList<GymContdias>();
            for (GymContdias gymContdiasListNewGymContdiasToAttach : gymContdiasListNew) {
                gymContdiasListNewGymContdiasToAttach = em.getReference(gymContdiasListNewGymContdiasToAttach.getClass(), gymContdiasListNewGymContdiasToAttach.getId());
                attachedGymContdiasListNew.add(gymContdiasListNewGymContdiasToAttach);
            }
            gymContdiasListNew = attachedGymContdiasListNew;
            gymUsuarios.setGymContdiasList(gymContdiasListNew);
            List<GymHuella> attachedGymHuellaListNew = new ArrayList<GymHuella>();
            for (GymHuella gymHuellaListNewGymHuellaToAttach : gymHuellaListNew) {
                gymHuellaListNewGymHuellaToAttach = em.getReference(gymHuellaListNewGymHuellaToAttach.getClass(), gymHuellaListNewGymHuellaToAttach.getId());
                attachedGymHuellaListNew.add(gymHuellaListNewGymHuellaToAttach);
            }
            gymHuellaListNew = attachedGymHuellaListNew;
            gymUsuarios.setGymHuellaList(gymHuellaListNew);
            List<GymAsignados> attachedGymAsignadosListNew = new ArrayList<GymAsignados>();
            for (GymAsignados gymAsignadosListNewGymAsignadosToAttach : gymAsignadosListNew) {
                gymAsignadosListNewGymAsignadosToAttach = em.getReference(gymAsignadosListNewGymAsignadosToAttach.getClass(), gymAsignadosListNewGymAsignadosToAttach.getId());
                attachedGymAsignadosListNew.add(gymAsignadosListNewGymAsignadosToAttach);
            }
            gymAsignadosListNew = attachedGymAsignadosListNew;
            gymUsuarios.setGymAsignadosList(gymAsignadosListNew);
            gymUsuarios = em.merge(gymUsuarios);
            for (GymContdias gymContdiasListNewGymContdias : gymContdiasListNew) {
                if (!gymContdiasListOld.contains(gymContdiasListNewGymContdias)) {
                    GymUsuarios oldIdusuarioOfGymContdiasListNewGymContdias = gymContdiasListNewGymContdias.getIdusuario();
                    gymContdiasListNewGymContdias.setIdusuario(gymUsuarios);
                    gymContdiasListNewGymContdias = em.merge(gymContdiasListNewGymContdias);
                    if (oldIdusuarioOfGymContdiasListNewGymContdias != null && !oldIdusuarioOfGymContdiasListNewGymContdias.equals(gymUsuarios)) {
                        oldIdusuarioOfGymContdiasListNewGymContdias.getGymContdiasList().remove(gymContdiasListNewGymContdias);
                        oldIdusuarioOfGymContdiasListNewGymContdias = em.merge(oldIdusuarioOfGymContdiasListNewGymContdias);
                    }
                }
            }
            for (GymHuella gymHuellaListNewGymHuella : gymHuellaListNew) {
                if (!gymHuellaListOld.contains(gymHuellaListNewGymHuella)) {
                    GymUsuarios oldIduserOfGymHuellaListNewGymHuella = gymHuellaListNewGymHuella.getIduser();
                    gymHuellaListNewGymHuella.setIduser(gymUsuarios);
                    gymHuellaListNewGymHuella = em.merge(gymHuellaListNewGymHuella);
                    if (oldIduserOfGymHuellaListNewGymHuella != null && !oldIduserOfGymHuellaListNewGymHuella.equals(gymUsuarios)) {
                        oldIduserOfGymHuellaListNewGymHuella.getGymHuellaList().remove(gymHuellaListNewGymHuella);
                        oldIduserOfGymHuellaListNewGymHuella = em.merge(oldIduserOfGymHuellaListNewGymHuella);
                    }
                }
            }
            for (GymAsignados gymAsignadosListNewGymAsignados : gymAsignadosListNew) {
                if (!gymAsignadosListOld.contains(gymAsignadosListNewGymAsignados)) {
                    GymUsuarios oldIduserOfGymAsignadosListNewGymAsignados = gymAsignadosListNewGymAsignados.getIduser();
                    gymAsignadosListNewGymAsignados.setIduser(gymUsuarios);
                    gymAsignadosListNewGymAsignados = em.merge(gymAsignadosListNewGymAsignados);
                    if (oldIduserOfGymAsignadosListNewGymAsignados != null && !oldIduserOfGymAsignadosListNewGymAsignados.equals(gymUsuarios)) {
                        oldIduserOfGymAsignadosListNewGymAsignados.getGymAsignadosList().remove(gymAsignadosListNewGymAsignados);
                        oldIduserOfGymAsignadosListNewGymAsignados = em.merge(oldIduserOfGymAsignadosListNewGymAsignados);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gymUsuarios.getId();
                if (findGymUsuarios(id) == null) {
                    throw new NonexistentEntityException("The gymUsuarios with id " + id + " no longer exists.");
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
            GymUsuarios gymUsuarios;
            try {
                gymUsuarios = em.getReference(GymUsuarios.class, id);
                gymUsuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gymUsuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GymContdias> gymContdiasListOrphanCheck = gymUsuarios.getGymContdiasList();
            for (GymContdias gymContdiasListOrphanCheckGymContdias : gymContdiasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GymUsuarios (" + gymUsuarios + ") cannot be destroyed since the GymContdias " + gymContdiasListOrphanCheckGymContdias + " in its gymContdiasList field has a non-nullable idusuario field.");
            }
            List<GymHuella> gymHuellaListOrphanCheck = gymUsuarios.getGymHuellaList();
            for (GymHuella gymHuellaListOrphanCheckGymHuella : gymHuellaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GymUsuarios (" + gymUsuarios + ") cannot be destroyed since the GymHuella " + gymHuellaListOrphanCheckGymHuella + " in its gymHuellaList field has a non-nullable iduser field.");
            }
            List<GymAsignados> gymAsignadosListOrphanCheck = gymUsuarios.getGymAsignadosList();
            for (GymAsignados gymAsignadosListOrphanCheckGymAsignados : gymAsignadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GymUsuarios (" + gymUsuarios + ") cannot be destroyed since the GymAsignados " + gymAsignadosListOrphanCheckGymAsignados + " in its gymAsignadosList field has a non-nullable iduser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(gymUsuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GymUsuarios> findGymUsuariosEntities() {
        return findGymUsuariosEntities(true, -1, -1);
    }

    public List<GymUsuarios> findGymUsuariosEntities(int maxResults, int firstResult) {
        return findGymUsuariosEntities(false, maxResults, firstResult);
    }

    private List<GymUsuarios> findGymUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GymUsuarios.class));
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

    public GymUsuarios findGymUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GymUsuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getGymUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GymUsuarios> rt = cq.from(GymUsuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
