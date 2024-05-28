/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.SolicitudDeCarga;
import com.example.models.SolicitudDeCargaDTO;
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



@Path("/solicitudes")
@Produces(MediaType.APPLICATION_JSON)
public class SolicitudCargaService {

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
    public Response getSolicitudes() {
        try {
            Query q = entityManager.createQuery("select v from SolicitudDeCarga v order by v.propietarioCarga ASC");
            List<SolicitudDeCarga> solicitudes = q.getResultList();
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(solicitudes).build();
        } catch (Throwable t) {
            t.printStackTrace();
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al obtener las solicitudes").build();
        }
    }


    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSolicitud(SolicitudDeCargaDTO solicitud) {
        try {
            entityManager.getTransaction().begin();
            SolicitudDeCarga solicitudTmp = new SolicitudDeCarga();
            solicitudTmp.setFecha(solicitud.getFecha());
            solicitudTmp.setPropietarioCarga(solicitud.getPropietarioCarga());
            solicitudTmp.setOrigen(solicitud.getOrigen());
            solicitudTmp.setDestino(solicitud.getDestino());
            solicitudTmp.setDimensiones(solicitud.getDimensiones());
            solicitudTmp.setPeso(solicitud.getPeso());
            solicitudTmp.setValorAsegurado(solicitud.getValorAsegurado());
            solicitudTmp.setEmpaque(solicitud.getEmpaque());
            entityManager.persist(solicitudTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(solicitudTmp);

            JSONObject rta = new JSONObject();
            rta.put("Solicitud", solicitudTmp.getId()+" agregada");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al añadir una solicitud").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSolicitud(@PathParam("id") Long id, SolicitudDeCargaDTO solicitud) {
        try {
            entityManager.getTransaction().begin();

            // Buscar el usuario por su ID
            SolicitudDeCarga solicitudTmp = entityManager.find(SolicitudDeCarga.class, id);

            if (solicitudTmp == null) {
                return Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Vehiculo no encontrado").build();
            }

            // Actualizar los datos del Vehiculo
            solicitudTmp.setFecha(solicitud.getFecha());
            solicitudTmp.setPropietarioCarga(solicitud.getPropietarioCarga());
            solicitudTmp.setOrigen(solicitud.getOrigen());
            solicitudTmp.setDestino(solicitud.getDestino());
            solicitudTmp.setDimensiones(solicitud.getDimensiones());
            solicitudTmp.setPeso(solicitud.getPeso());
            solicitudTmp.setValorAsegurado(solicitud.getValorAsegurado());
            solicitudTmp.setEmpaque(solicitud.getEmpaque());

            entityManager.getTransaction().commit();

            // Crear una respuesta JSON con un mensaje de éxito
            JSONObject rta = new JSONObject();
            rta.put("message", "Solicitud "+id+" actualizada exitosamente");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al actualizar la solicitud").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }


    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSolicitud(@PathParam("id") Long id) {
        try {
            entityManager.getTransaction().begin();

            // Buscar la Solicitud por su ID
            SolicitudDeCarga solicitudTmp = entityManager.find(SolicitudDeCarga.class, id);

            if (solicitudTmp == null) {
                return Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Solicitud no encontrada").build();
            }

            // Eliminar la Solicitud
            entityManager.remove(solicitudTmp);
            entityManager.getTransaction().commit();

            // Crear una respuesta JSON con un mensaje de éxito
            JSONObject rta = new JSONObject();
            rta.put("message", "Solicitud "+id+" eliminada exitosamente");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al eliminar la Solicitud").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

}

