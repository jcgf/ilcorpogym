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
import entity.DevolucionesArticulos;
import entity.VariedadesDevoluciones;
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
public class VariedadesDevolucionesJpaController implements Serializable {

    public VariedadesDevolucionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariedadesDevoluciones variedadesDevoluciones) {
        if (variedadesDevoluciones.getDevolucionesArticulosList() == null) {
            variedadesDevoluciones.setDevolucionesArticulosList(new ArrayList<DevolucionesArticulos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserLog idusuario = variedadesDevoluciones.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getId());
                variedadesDevoluciones.setIdusuario(idusuario);
            }
            List<DevolucionesArticulos> attachedDevolucionesArticulosList = new ArrayList<DevolucionesArticulos>();
            for (DevolucionesArticulos devolucionesArticulosListDevolucionesArticulosToAttach : variedadesDevoluciones.getDevolucionesArticulosList()) {
                devolucionesArticulosListDevolucionesArticulosToAttach = em.getReference(devolucionesArticulosListDevolucionesArticulosToAttach.getClass(), devolucionesArticulosListDevolucionesArticulosToAttach.getId());
                attachedDevolucionesArticulosList.add(devolucionesArticulosListDevolucionesArticulosToAttach);
            }
            variedadesDevoluciones.setDevolucionesArticulosList(attachedDevolucionesArticulosList);
            em.persist(variedadesDevoluciones);
            if (idusuario != null) {
                idusuario.getVariedadesDevolucionesList().add(variedadesDevoluciones);
                idusuario = em.merge(idusuario);
            }
            for (DevolucionesArticulos devolucionesArticulosListDevolucionesArticulos : variedadesDevoluciones.getDevolucionesArticulosList()) {
                VariedadesDevoluciones oldIddevolucionOfDevolucionesArticulosListDevolucionesArticulos = devolucionesArticulosListDevolucionesArticulos.getIddevolucion();
                devolucionesArticulosListDevolucionesArticulos.setIddevolucion(variedadesDevoluciones);
                devolucionesArticulosListDevolucionesArticulos = em.merge(devolucionesArticulosListDevolucionesArticulos);
                if (oldIddevolucionOfDevolucionesArticulosListDevolucionesArticulos != null) {
                    oldIddevolucionOfDevolucionesArticulosListDevolucionesArticulos.getDevolucionesArticulosList().remove(devolucionesArticulosListDevolucionesArticulos);
                    oldIddevolucionOfDevolucionesArticulosListDevolucionesArticulos = em.merge(oldIddevolucionOfDevolucionesArticulosListDevolucionesArticulos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariedadesDevoluciones variedadesDevoluciones) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesDevoluciones persistentVariedadesDevoluciones = em.find(VariedadesDevoluciones.class, variedadesDevoluciones.getId());
            UserLog idusuarioOld = persistentVariedadesDevoluciones.getIdusuario();
            UserLog idusuarioNew = variedadesDevoluciones.getIdusuario();
            List<DevolucionesArticulos> devolucionesArticulosListOld = persistentVariedadesDevoluciones.getDevolucionesArticulosList();
            List<DevolucionesArticulos> devolucionesArticulosListNew = variedadesDevoluciones.getDevolucionesArticulosList();
            List<String> illegalOrphanMessages = null;
            for (DevolucionesArticulos devolucionesArticulosListOldDevolucionesArticulos : devolucionesArticulosListOld) {
                if (!devolucionesArticulosListNew.contains(devolucionesArticulosListOldDevolucionesArticulos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DevolucionesArticulos " + devolucionesArticulosListOldDevolucionesArticulos + " since its iddevolucion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getId());
                variedadesDevoluciones.setIdusuario(idusuarioNew);
            }
            List<DevolucionesArticulos> attachedDevolucionesArticulosListNew = new ArrayList<DevolucionesArticulos>();
            for (DevolucionesArticulos devolucionesArticulosListNewDevolucionesArticulosToAttach : devolucionesArticulosListNew) {
                devolucionesArticulosListNewDevolucionesArticulosToAttach = em.getReference(devolucionesArticulosListNewDevolucionesArticulosToAttach.getClass(), devolucionesArticulosListNewDevolucionesArticulosToAttach.getId());
                attachedDevolucionesArticulosListNew.add(devolucionesArticulosListNewDevolucionesArticulosToAttach);
            }
            devolucionesArticulosListNew = attachedDevolucionesArticulosListNew;
            variedadesDevoluciones.setDevolucionesArticulosList(devolucionesArticulosListNew);
            variedadesDevoluciones = em.merge(variedadesDevoluciones);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getVariedadesDevolucionesList().remove(variedadesDevoluciones);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getVariedadesDevolucionesList().add(variedadesDevoluciones);
                idusuarioNew = em.merge(idusuarioNew);
            }
            for (DevolucionesArticulos devolucionesArticulosListNewDevolucionesArticulos : devolucionesArticulosListNew) {
                if (!devolucionesArticulosListOld.contains(devolucionesArticulosListNewDevolucionesArticulos)) {
                    VariedadesDevoluciones oldIddevolucionOfDevolucionesArticulosListNewDevolucionesArticulos = devolucionesArticulosListNewDevolucionesArticulos.getIddevolucion();
                    devolucionesArticulosListNewDevolucionesArticulos.setIddevolucion(variedadesDevoluciones);
                    devolucionesArticulosListNewDevolucionesArticulos = em.merge(devolucionesArticulosListNewDevolucionesArticulos);
                    if (oldIddevolucionOfDevolucionesArticulosListNewDevolucionesArticulos != null && !oldIddevolucionOfDevolucionesArticulosListNewDevolucionesArticulos.equals(variedadesDevoluciones)) {
                        oldIddevolucionOfDevolucionesArticulosListNewDevolucionesArticulos.getDevolucionesArticulosList().remove(devolucionesArticulosListNewDevolucionesArticulos);
                        oldIddevolucionOfDevolucionesArticulosListNewDevolucionesArticulos = em.merge(oldIddevolucionOfDevolucionesArticulosListNewDevolucionesArticulos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variedadesDevoluciones.getId();
                if (findVariedadesDevoluciones(id) == null) {
                    throw new NonexistentEntityException("The variedadesDevoluciones with id " + id + " no longer exists.");
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
            VariedadesDevoluciones variedadesDevoluciones;
            try {
                variedadesDevoluciones = em.getReference(VariedadesDevoluciones.class, id);
                variedadesDevoluciones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variedadesDevoluciones with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DevolucionesArticulos> devolucionesArticulosListOrphanCheck = variedadesDevoluciones.getDevolucionesArticulosList();
            for (DevolucionesArticulos devolucionesArticulosListOrphanCheckDevolucionesArticulos : devolucionesArticulosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesDevoluciones (" + variedadesDevoluciones + ") cannot be destroyed since the DevolucionesArticulos " + devolucionesArticulosListOrphanCheckDevolucionesArticulos + " in its devolucionesArticulosList field has a non-nullable iddevolucion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UserLog idusuario = variedadesDevoluciones.getIdusuario();
            if (idusuario != null) {
                idusuario.getVariedadesDevolucionesList().remove(variedadesDevoluciones);
                idusuario = em.merge(idusuario);
            }
            em.remove(variedadesDevoluciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariedadesDevoluciones> findVariedadesDevolucionesEntities() {
        return findVariedadesDevolucionesEntities(true, -1, -1);
    }

    public List<VariedadesDevoluciones> findVariedadesDevolucionesEntities(int maxResults, int firstResult) {
        return findVariedadesDevolucionesEntities(false, maxResults, firstResult);
    }

    private List<VariedadesDevoluciones> findVariedadesDevolucionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariedadesDevoluciones.class));
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

    public VariedadesDevoluciones findVariedadesDevoluciones(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariedadesDevoluciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariedadesDevolucionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariedadesDevoluciones> rt = cq.from(VariedadesDevoluciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
