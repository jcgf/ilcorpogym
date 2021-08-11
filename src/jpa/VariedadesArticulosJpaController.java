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
import entity.VentasArticulos;
import java.util.ArrayList;
import java.util.List;
import entity.ArticulosCantidades;
import entity.DevolucionesArticulos;
import entity.SuministroArticulos;
import entity.VariedadesArticulos;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.IllegalOrphanException;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Juan
 */
public class VariedadesArticulosJpaController implements Serializable {

    public VariedadesArticulosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VariedadesArticulos variedadesArticulos) {
        if (variedadesArticulos.getVentasArticulosList() == null) {
            variedadesArticulos.setVentasArticulosList(new ArrayList<VentasArticulos>());
        }
        if (variedadesArticulos.getArticulosCantidadesList() == null) {
            variedadesArticulos.setArticulosCantidadesList(new ArrayList<ArticulosCantidades>());
        }
        if (variedadesArticulos.getDevolucionesArticulosList() == null) {
            variedadesArticulos.setDevolucionesArticulosList(new ArrayList<DevolucionesArticulos>());
        }
        if (variedadesArticulos.getSuministroArticulosList() == null) {
            variedadesArticulos.setSuministroArticulosList(new ArrayList<SuministroArticulos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<VentasArticulos> attachedVentasArticulosList = new ArrayList<VentasArticulos>();
            for (VentasArticulos ventasArticulosListVentasArticulosToAttach : variedadesArticulos.getVentasArticulosList()) {
                ventasArticulosListVentasArticulosToAttach = em.getReference(ventasArticulosListVentasArticulosToAttach.getClass(), ventasArticulosListVentasArticulosToAttach.getId());
                attachedVentasArticulosList.add(ventasArticulosListVentasArticulosToAttach);
            }
            variedadesArticulos.setVentasArticulosList(attachedVentasArticulosList);
            List<ArticulosCantidades> attachedArticulosCantidadesList = new ArrayList<ArticulosCantidades>();
            for (ArticulosCantidades articulosCantidadesListArticulosCantidadesToAttach : variedadesArticulos.getArticulosCantidadesList()) {
                articulosCantidadesListArticulosCantidadesToAttach = em.getReference(articulosCantidadesListArticulosCantidadesToAttach.getClass(), articulosCantidadesListArticulosCantidadesToAttach.getId());
                attachedArticulosCantidadesList.add(articulosCantidadesListArticulosCantidadesToAttach);
            }
            variedadesArticulos.setArticulosCantidadesList(attachedArticulosCantidadesList);
            List<DevolucionesArticulos> attachedDevolucionesArticulosList = new ArrayList<DevolucionesArticulos>();
            for (DevolucionesArticulos devolucionesArticulosListDevolucionesArticulosToAttach : variedadesArticulos.getDevolucionesArticulosList()) {
                devolucionesArticulosListDevolucionesArticulosToAttach = em.getReference(devolucionesArticulosListDevolucionesArticulosToAttach.getClass(), devolucionesArticulosListDevolucionesArticulosToAttach.getId());
                attachedDevolucionesArticulosList.add(devolucionesArticulosListDevolucionesArticulosToAttach);
            }
            variedadesArticulos.setDevolucionesArticulosList(attachedDevolucionesArticulosList);
            List<SuministroArticulos> attachedSuministroArticulosList = new ArrayList<SuministroArticulos>();
            for (SuministroArticulos suministroArticulosListSuministroArticulosToAttach : variedadesArticulos.getSuministroArticulosList()) {
                suministroArticulosListSuministroArticulosToAttach = em.getReference(suministroArticulosListSuministroArticulosToAttach.getClass(), suministroArticulosListSuministroArticulosToAttach.getId());
                attachedSuministroArticulosList.add(suministroArticulosListSuministroArticulosToAttach);
            }
            variedadesArticulos.setSuministroArticulosList(attachedSuministroArticulosList);
            em.persist(variedadesArticulos);
            for (VentasArticulos ventasArticulosListVentasArticulos : variedadesArticulos.getVentasArticulosList()) {
                VariedadesArticulos oldIdarticuloOfVentasArticulosListVentasArticulos = ventasArticulosListVentasArticulos.getIdarticulo();
                ventasArticulosListVentasArticulos.setIdarticulo(variedadesArticulos);
                ventasArticulosListVentasArticulos = em.merge(ventasArticulosListVentasArticulos);
                if (oldIdarticuloOfVentasArticulosListVentasArticulos != null) {
                    oldIdarticuloOfVentasArticulosListVentasArticulos.getVentasArticulosList().remove(ventasArticulosListVentasArticulos);
                    oldIdarticuloOfVentasArticulosListVentasArticulos = em.merge(oldIdarticuloOfVentasArticulosListVentasArticulos);
                }
            }
            for (ArticulosCantidades articulosCantidadesListArticulosCantidades : variedadesArticulos.getArticulosCantidadesList()) {
                VariedadesArticulos oldIdarticuloOfArticulosCantidadesListArticulosCantidades = articulosCantidadesListArticulosCantidades.getIdarticulo();
                articulosCantidadesListArticulosCantidades.setIdarticulo(variedadesArticulos);
                articulosCantidadesListArticulosCantidades = em.merge(articulosCantidadesListArticulosCantidades);
                if (oldIdarticuloOfArticulosCantidadesListArticulosCantidades != null) {
                    oldIdarticuloOfArticulosCantidadesListArticulosCantidades.getArticulosCantidadesList().remove(articulosCantidadesListArticulosCantidades);
                    oldIdarticuloOfArticulosCantidadesListArticulosCantidades = em.merge(oldIdarticuloOfArticulosCantidadesListArticulosCantidades);
                }
            }
            for (DevolucionesArticulos devolucionesArticulosListDevolucionesArticulos : variedadesArticulos.getDevolucionesArticulosList()) {
                VariedadesArticulos oldIdarticuloOfDevolucionesArticulosListDevolucionesArticulos = devolucionesArticulosListDevolucionesArticulos.getIdarticulo();
                devolucionesArticulosListDevolucionesArticulos.setIdarticulo(variedadesArticulos);
                devolucionesArticulosListDevolucionesArticulos = em.merge(devolucionesArticulosListDevolucionesArticulos);
                if (oldIdarticuloOfDevolucionesArticulosListDevolucionesArticulos != null) {
                    oldIdarticuloOfDevolucionesArticulosListDevolucionesArticulos.getDevolucionesArticulosList().remove(devolucionesArticulosListDevolucionesArticulos);
                    oldIdarticuloOfDevolucionesArticulosListDevolucionesArticulos = em.merge(oldIdarticuloOfDevolucionesArticulosListDevolucionesArticulos);
                }
            }
            for (SuministroArticulos suministroArticulosListSuministroArticulos : variedadesArticulos.getSuministroArticulosList()) {
                VariedadesArticulos oldIdarticuloOfSuministroArticulosListSuministroArticulos = suministroArticulosListSuministroArticulos.getIdarticulo();
                suministroArticulosListSuministroArticulos.setIdarticulo(variedadesArticulos);
                suministroArticulosListSuministroArticulos = em.merge(suministroArticulosListSuministroArticulos);
                if (oldIdarticuloOfSuministroArticulosListSuministroArticulos != null) {
                    oldIdarticuloOfSuministroArticulosListSuministroArticulos.getSuministroArticulosList().remove(suministroArticulosListSuministroArticulos);
                    oldIdarticuloOfSuministroArticulosListSuministroArticulos = em.merge(oldIdarticuloOfSuministroArticulosListSuministroArticulos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VariedadesArticulos variedadesArticulos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VariedadesArticulos persistentVariedadesArticulos = em.find(VariedadesArticulos.class, variedadesArticulos.getId());
            List<VentasArticulos> ventasArticulosListOld = persistentVariedadesArticulos.getVentasArticulosList();
            List<VentasArticulos> ventasArticulosListNew = variedadesArticulos.getVentasArticulosList();
            List<ArticulosCantidades> articulosCantidadesListOld = persistentVariedadesArticulos.getArticulosCantidadesList();
            List<ArticulosCantidades> articulosCantidadesListNew = variedadesArticulos.getArticulosCantidadesList();
            List<DevolucionesArticulos> devolucionesArticulosListOld = persistentVariedadesArticulos.getDevolucionesArticulosList();
            List<DevolucionesArticulos> devolucionesArticulosListNew = variedadesArticulos.getDevolucionesArticulosList();
            List<SuministroArticulos> suministroArticulosListOld = persistentVariedadesArticulos.getSuministroArticulosList();
            List<SuministroArticulos> suministroArticulosListNew = variedadesArticulos.getSuministroArticulosList();
            List<String> illegalOrphanMessages = null;
            for (VentasArticulos ventasArticulosListOldVentasArticulos : ventasArticulosListOld) {
                if (!ventasArticulosListNew.contains(ventasArticulosListOldVentasArticulos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain VentasArticulos " + ventasArticulosListOldVentasArticulos + " since its idarticulo field is not nullable.");
                }
            }
            for (ArticulosCantidades articulosCantidadesListOldArticulosCantidades : articulosCantidadesListOld) {
                if (!articulosCantidadesListNew.contains(articulosCantidadesListOldArticulosCantidades)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArticulosCantidades " + articulosCantidadesListOldArticulosCantidades + " since its idarticulo field is not nullable.");
                }
            }
            for (DevolucionesArticulos devolucionesArticulosListOldDevolucionesArticulos : devolucionesArticulosListOld) {
                if (!devolucionesArticulosListNew.contains(devolucionesArticulosListOldDevolucionesArticulos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DevolucionesArticulos " + devolucionesArticulosListOldDevolucionesArticulos + " since its idarticulo field is not nullable.");
                }
            }
            for (SuministroArticulos suministroArticulosListOldSuministroArticulos : suministroArticulosListOld) {
                if (!suministroArticulosListNew.contains(suministroArticulosListOldSuministroArticulos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SuministroArticulos " + suministroArticulosListOldSuministroArticulos + " since its idarticulo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<VentasArticulos> attachedVentasArticulosListNew = new ArrayList<VentasArticulos>();
            for (VentasArticulos ventasArticulosListNewVentasArticulosToAttach : ventasArticulosListNew) {
                ventasArticulosListNewVentasArticulosToAttach = em.getReference(ventasArticulosListNewVentasArticulosToAttach.getClass(), ventasArticulosListNewVentasArticulosToAttach.getId());
                attachedVentasArticulosListNew.add(ventasArticulosListNewVentasArticulosToAttach);
            }
            ventasArticulosListNew = attachedVentasArticulosListNew;
            variedadesArticulos.setVentasArticulosList(ventasArticulosListNew);
            List<ArticulosCantidades> attachedArticulosCantidadesListNew = new ArrayList<ArticulosCantidades>();
            for (ArticulosCantidades articulosCantidadesListNewArticulosCantidadesToAttach : articulosCantidadesListNew) {
                articulosCantidadesListNewArticulosCantidadesToAttach = em.getReference(articulosCantidadesListNewArticulosCantidadesToAttach.getClass(), articulosCantidadesListNewArticulosCantidadesToAttach.getId());
                attachedArticulosCantidadesListNew.add(articulosCantidadesListNewArticulosCantidadesToAttach);
            }
            articulosCantidadesListNew = attachedArticulosCantidadesListNew;
            variedadesArticulos.setArticulosCantidadesList(articulosCantidadesListNew);
            List<DevolucionesArticulos> attachedDevolucionesArticulosListNew = new ArrayList<DevolucionesArticulos>();
            for (DevolucionesArticulos devolucionesArticulosListNewDevolucionesArticulosToAttach : devolucionesArticulosListNew) {
                devolucionesArticulosListNewDevolucionesArticulosToAttach = em.getReference(devolucionesArticulosListNewDevolucionesArticulosToAttach.getClass(), devolucionesArticulosListNewDevolucionesArticulosToAttach.getId());
                attachedDevolucionesArticulosListNew.add(devolucionesArticulosListNewDevolucionesArticulosToAttach);
            }
            devolucionesArticulosListNew = attachedDevolucionesArticulosListNew;
            variedadesArticulos.setDevolucionesArticulosList(devolucionesArticulosListNew);
            List<SuministroArticulos> attachedSuministroArticulosListNew = new ArrayList<SuministroArticulos>();
            for (SuministroArticulos suministroArticulosListNewSuministroArticulosToAttach : suministroArticulosListNew) {
                suministroArticulosListNewSuministroArticulosToAttach = em.getReference(suministroArticulosListNewSuministroArticulosToAttach.getClass(), suministroArticulosListNewSuministroArticulosToAttach.getId());
                attachedSuministroArticulosListNew.add(suministroArticulosListNewSuministroArticulosToAttach);
            }
            suministroArticulosListNew = attachedSuministroArticulosListNew;
            variedadesArticulos.setSuministroArticulosList(suministroArticulosListNew);
            variedadesArticulos = em.merge(variedadesArticulos);
            for (VentasArticulos ventasArticulosListNewVentasArticulos : ventasArticulosListNew) {
                if (!ventasArticulosListOld.contains(ventasArticulosListNewVentasArticulos)) {
                    VariedadesArticulos oldIdarticuloOfVentasArticulosListNewVentasArticulos = ventasArticulosListNewVentasArticulos.getIdarticulo();
                    ventasArticulosListNewVentasArticulos.setIdarticulo(variedadesArticulos);
                    ventasArticulosListNewVentasArticulos = em.merge(ventasArticulosListNewVentasArticulos);
                    if (oldIdarticuloOfVentasArticulosListNewVentasArticulos != null && !oldIdarticuloOfVentasArticulosListNewVentasArticulos.equals(variedadesArticulos)) {
                        oldIdarticuloOfVentasArticulosListNewVentasArticulos.getVentasArticulosList().remove(ventasArticulosListNewVentasArticulos);
                        oldIdarticuloOfVentasArticulosListNewVentasArticulos = em.merge(oldIdarticuloOfVentasArticulosListNewVentasArticulos);
                    }
                }
            }
            for (ArticulosCantidades articulosCantidadesListNewArticulosCantidades : articulosCantidadesListNew) {
                if (!articulosCantidadesListOld.contains(articulosCantidadesListNewArticulosCantidades)) {
                    VariedadesArticulos oldIdarticuloOfArticulosCantidadesListNewArticulosCantidades = articulosCantidadesListNewArticulosCantidades.getIdarticulo();
                    articulosCantidadesListNewArticulosCantidades.setIdarticulo(variedadesArticulos);
                    articulosCantidadesListNewArticulosCantidades = em.merge(articulosCantidadesListNewArticulosCantidades);
                    if (oldIdarticuloOfArticulosCantidadesListNewArticulosCantidades != null && !oldIdarticuloOfArticulosCantidadesListNewArticulosCantidades.equals(variedadesArticulos)) {
                        oldIdarticuloOfArticulosCantidadesListNewArticulosCantidades.getArticulosCantidadesList().remove(articulosCantidadesListNewArticulosCantidades);
                        oldIdarticuloOfArticulosCantidadesListNewArticulosCantidades = em.merge(oldIdarticuloOfArticulosCantidadesListNewArticulosCantidades);
                    }
                }
            }
            for (DevolucionesArticulos devolucionesArticulosListNewDevolucionesArticulos : devolucionesArticulosListNew) {
                if (!devolucionesArticulosListOld.contains(devolucionesArticulosListNewDevolucionesArticulos)) {
                    VariedadesArticulos oldIdarticuloOfDevolucionesArticulosListNewDevolucionesArticulos = devolucionesArticulosListNewDevolucionesArticulos.getIdarticulo();
                    devolucionesArticulosListNewDevolucionesArticulos.setIdarticulo(variedadesArticulos);
                    devolucionesArticulosListNewDevolucionesArticulos = em.merge(devolucionesArticulosListNewDevolucionesArticulos);
                    if (oldIdarticuloOfDevolucionesArticulosListNewDevolucionesArticulos != null && !oldIdarticuloOfDevolucionesArticulosListNewDevolucionesArticulos.equals(variedadesArticulos)) {
                        oldIdarticuloOfDevolucionesArticulosListNewDevolucionesArticulos.getDevolucionesArticulosList().remove(devolucionesArticulosListNewDevolucionesArticulos);
                        oldIdarticuloOfDevolucionesArticulosListNewDevolucionesArticulos = em.merge(oldIdarticuloOfDevolucionesArticulosListNewDevolucionesArticulos);
                    }
                }
            }
            for (SuministroArticulos suministroArticulosListNewSuministroArticulos : suministroArticulosListNew) {
                if (!suministroArticulosListOld.contains(suministroArticulosListNewSuministroArticulos)) {
                    VariedadesArticulos oldIdarticuloOfSuministroArticulosListNewSuministroArticulos = suministroArticulosListNewSuministroArticulos.getIdarticulo();
                    suministroArticulosListNewSuministroArticulos.setIdarticulo(variedadesArticulos);
                    suministroArticulosListNewSuministroArticulos = em.merge(suministroArticulosListNewSuministroArticulos);
                    if (oldIdarticuloOfSuministroArticulosListNewSuministroArticulos != null && !oldIdarticuloOfSuministroArticulosListNewSuministroArticulos.equals(variedadesArticulos)) {
                        oldIdarticuloOfSuministroArticulosListNewSuministroArticulos.getSuministroArticulosList().remove(suministroArticulosListNewSuministroArticulos);
                        oldIdarticuloOfSuministroArticulosListNewSuministroArticulos = em.merge(oldIdarticuloOfSuministroArticulosListNewSuministroArticulos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = variedadesArticulos.getId();
                if (findVariedadesArticulos(id) == null) {
                    throw new NonexistentEntityException("The variedadesArticulos with id " + id + " no longer exists.");
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
            VariedadesArticulos variedadesArticulos;
            try {
                variedadesArticulos = em.getReference(VariedadesArticulos.class, id);
                variedadesArticulos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The variedadesArticulos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<VentasArticulos> ventasArticulosListOrphanCheck = variedadesArticulos.getVentasArticulosList();
            for (VentasArticulos ventasArticulosListOrphanCheckVentasArticulos : ventasArticulosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesArticulos (" + variedadesArticulos + ") cannot be destroyed since the VentasArticulos " + ventasArticulosListOrphanCheckVentasArticulos + " in its ventasArticulosList field has a non-nullable idarticulo field.");
            }
            List<ArticulosCantidades> articulosCantidadesListOrphanCheck = variedadesArticulos.getArticulosCantidadesList();
            for (ArticulosCantidades articulosCantidadesListOrphanCheckArticulosCantidades : articulosCantidadesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesArticulos (" + variedadesArticulos + ") cannot be destroyed since the ArticulosCantidades " + articulosCantidadesListOrphanCheckArticulosCantidades + " in its articulosCantidadesList field has a non-nullable idarticulo field.");
            }
            List<DevolucionesArticulos> devolucionesArticulosListOrphanCheck = variedadesArticulos.getDevolucionesArticulosList();
            for (DevolucionesArticulos devolucionesArticulosListOrphanCheckDevolucionesArticulos : devolucionesArticulosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesArticulos (" + variedadesArticulos + ") cannot be destroyed since the DevolucionesArticulos " + devolucionesArticulosListOrphanCheckDevolucionesArticulos + " in its devolucionesArticulosList field has a non-nullable idarticulo field.");
            }
            List<SuministroArticulos> suministroArticulosListOrphanCheck = variedadesArticulos.getSuministroArticulosList();
            for (SuministroArticulos suministroArticulosListOrphanCheckSuministroArticulos : suministroArticulosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This VariedadesArticulos (" + variedadesArticulos + ") cannot be destroyed since the SuministroArticulos " + suministroArticulosListOrphanCheckSuministroArticulos + " in its suministroArticulosList field has a non-nullable idarticulo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(variedadesArticulos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VariedadesArticulos> findVariedadesArticulosEntities() {
        return findVariedadesArticulosEntities(true, -1, -1);
    }

    public List<VariedadesArticulos> findVariedadesArticulosEntities(int maxResults, int firstResult) {
        return findVariedadesArticulosEntities(false, maxResults, firstResult);
    }

    private List<VariedadesArticulos> findVariedadesArticulosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VariedadesArticulos.class));
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

    public VariedadesArticulos findVariedadesArticulos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VariedadesArticulos.class, id);
        } finally {
            em.close();
        }
    }

    public int getVariedadesArticulosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VariedadesArticulos> rt = cq.from(VariedadesArticulos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
