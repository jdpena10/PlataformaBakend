package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Usuario;
import com.example.models.UsuarioDTO;
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


@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioService {

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
    public Response getAll() {
        Query q = entityManager.createQuery("select u from Usuario u order by u.nombre ASC");
        List<Usuario> usuarios = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(usuarios).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUsuario(UsuarioDTO usuario) {
        try {
            entityManager.getTransaction().begin();
            Usuario usuarioTmp = new Usuario();
            usuarioTmp.setNombre(usuario.getNombre());
            usuarioTmp.setCorreo(usuario.getCorreo());
            usuarioTmp.setTeléfono(usuario.getTeléfono());
            usuarioTmp.setDirección(usuario.getDirección());
            entityManager.persist(usuarioTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(usuarioTmp);

            JSONObject rta = new JSONObject();
            rta.put("Usuario", usuarioTmp.getId()+" registrado");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al registrar usuario").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUsuario(@PathParam("id") Long id, UsuarioDTO usuario) {
        try {
            entityManager.getTransaction().begin();

            // Buscar el usuario por su ID
            Usuario usuarioTmp = entityManager.find(Usuario.class, id);

            if (usuarioTmp == null) {
                return Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Usuario no encontrado").build();
            }

            // Actualizar los datos del usuario
            usuarioTmp.setNombre(usuario.getNombre());
            usuarioTmp.setCorreo(usuario.getCorreo());
            usuarioTmp.setTeléfono(usuario.getTeléfono());
            usuarioTmp.setDirección(usuario.getDirección());

            entityManager.getTransaction().commit();

            // Crear una respuesta JSON con un mensaje de éxito
            JSONObject rta = new JSONObject();
            rta.put("message", "Usuario "+id+" actualizado exitosamente");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al actualizar el usuario").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }


    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUsuario(@PathParam("id") Long id) {
        try {
            entityManager.getTransaction().begin();

            // Buscar el usuario por su ID
            Usuario usuarioTmp = entityManager.find(Usuario.class, id);

            if (usuarioTmp == null) {
                return Response.status(404).header("Access-Control-Allow-Origin", "*").entity("Usuario no encontrado").build();
            }

            // Eliminar el usuario
            entityManager.remove(usuarioTmp);
            entityManager.getTransaction().commit();

            // Crear una respuesta JSON con un mensaje de éxito
            JSONObject rta = new JSONObject();
            rta.put("message", "Usuario "+id+" eliminado exitosamente");

            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta).build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("Error al eliminar el usuario").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

}
