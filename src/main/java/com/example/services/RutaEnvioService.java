/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.RutaEnvio;
import com.example.models.RutaEnvioDTO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONObject;



@Path("/rutas")
@Produces(MediaType.APPLICATION_JSON)
public class RutaEnvioService {

    @PersistenceContext(unitName = "CompetitorsPU")
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        try {
            entityManager = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRutas() {
        try {
            Query q = entityManager.createQuery("select v from RutaEnvio v order by v.conductor ASC");
            List<RutaEnvio> rutas = q.getResultList();
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rutas).build();
        } catch (Throwable t) {
            t.printStackTrace();
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al obtener las rutas").build();
        }
    }


    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRuta(RutaEnvioDTO ruta) {
        try {
            entityManager.getTransaction().begin();
            RutaEnvio rutaTmp = new RutaEnvio();
            rutaTmp.setFechaYhoraRecogida(ruta.getFechaYhoraRecogida());
            rutaTmp.setOrigen(ruta.getOrigen());
            rutaTmp.setDestino(ruta.getDestino());
            rutaTmp.setPlaca(ruta.getPlaca());
            rutaTmp.setConductor(ruta.getConductor());
            rutaTmp.setRutaAseguir(ruta.getRutaAseguir());
            entityManager.persist(rutaTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(rutaTmp);

            JSONObject rta = new JSONObject();
            rta.put("Ruta", rutaTmp.getId()+" agregada");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al añadir una ruta").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRuta(@PathParam("id") Long id, RutaEnvioDTO ruta) {
        try {
            entityManager.getTransaction().begin();

            // Buscar la ruta por su ID
            RutaEnvio rutaTmp = entityManager.find(RutaEnvio.class, id);

            if (rutaTmp == null) {
                return Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Ruta no encontrada").build();
            }

            // Actualizar los datos la ruta
            rutaTmp.setFechaYhoraRecogida(ruta.getFechaYhoraRecogida());
            rutaTmp.setOrigen(ruta.getOrigen());
            rutaTmp.setDestino(ruta.getDestino());
            rutaTmp.setPlaca(ruta.getPlaca());
            rutaTmp.setConductor(ruta.getConductor());
            rutaTmp.setRutaAseguir(ruta.getRutaAseguir());

            entityManager.getTransaction().commit();

            // Crear una respuesta JSON con un mensaje de éxito
            JSONObject rta = new JSONObject();
            rta.put("message", "Ruta "+id+" actualizada exitosamente");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al actualizar la ruta").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }


    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRuta(@PathParam("id") Long id) {
        try {
            entityManager.getTransaction().begin();

            // Buscar la ruta por su ID
            RutaEnvio rutaTmp = entityManager.find(RutaEnvio.class, id);

            if (rutaTmp == null) {
                return Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Ruta no encontrada").build();
            }

            // Eliminar la ruta
            entityManager.remove(rutaTmp);
            entityManager.getTransaction().commit();

            // Crear una respuesta JSON con un mensaje de éxito
            JSONObject rta = new JSONObject();
            rta.put("message", "Ruta "+id+" eliminada exitosamente");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al eliminar la ruta").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

}

