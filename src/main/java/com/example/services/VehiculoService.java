/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Vehiculo;
import com.example.models.VehiculoDTO;
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


@Path("/vehiculos")
@Produces(MediaType.APPLICATION_JSON)
public class VehiculoService {

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
    public Response getVehiculos() {
        try {
            Query q = entityManager.createQuery("select v from Vehiculo v order by v.placa ASC");
            List<Vehiculo> vehiculos = q.getResultList();
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(vehiculos).build();
        } catch (Throwable t) {
            t.printStackTrace();
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al obtener los vehículos").build();
        }
    }


    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVehiculo(VehiculoDTO vehiculo) {
        try {
            entityManager.getTransaction().begin();
            Vehiculo vehiculoTmp = new Vehiculo();
            vehiculoTmp.setPlaca(vehiculo.getPlaca());
            vehiculoTmp.setMarca(vehiculo.getMarca());
            vehiculoTmp.setModelo(vehiculo.getModelo());
            vehiculoTmp.setCapacidadCarga(vehiculo.getCapacidadCarga());
            vehiculoTmp.setTipoCarroceria(vehiculo.getTipoCarroceria());
            entityManager.persist(vehiculoTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(vehiculoTmp);

            JSONObject rta = new JSONObject();
            rta.put("Vehiculo", vehiculoTmp.getId()+" añadido");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al añadir el vehiculo").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateVehiculo(@PathParam("id") Long id, VehiculoDTO vehiculo) {
        try {
            entityManager.getTransaction().begin();

            // Buscar el usuario por su ID
            Vehiculo vehiculoTmp = entityManager.find(Vehiculo.class, id);

            if (vehiculoTmp == null) {
                return Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Vehiculo no encontrado").build();
            }

            // Actualizar los datos del Vehiculo
            vehiculoTmp.setPlaca(vehiculo.getPlaca());
            vehiculoTmp.setMarca(vehiculo.getMarca());
            vehiculoTmp.setModelo(vehiculo.getModelo());
            vehiculoTmp.setCapacidadCarga(vehiculo.getCapacidadCarga());
            vehiculoTmp.setTipoCarroceria(vehiculo.getTipoCarroceria());

            entityManager.getTransaction().commit();

            // Crear una respuesta JSON con un mensaje de éxito
            JSONObject rta = new JSONObject();
            rta.put("message", "Vehiculo "+id+" actualizado exitosamente");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al actualizar el Vehiculo").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }


    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVehiculo(@PathParam("id") Long id) {
        try {
            entityManager.getTransaction().begin();

            // Buscar el Vehiculo por su ID
            Vehiculo vehiculoTmp = entityManager.find(Vehiculo.class, id);

            if (vehiculoTmp == null) {
                return Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Vehiculo no encontrado").build();
            }

            // Eliminar el Vehiculo
            entityManager.remove(vehiculoTmp);
            entityManager.getTransaction().commit();

            // Crear una respuesta JSON con un mensaje de éxito
            JSONObject rta = new JSONObject();
            rta.put("message", "Vehiculo "+id+" eliminado exitosamente");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al eliminar el Vehiculo").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

}
