package edu.upc.dsa.services;

import edu.upc.dsa.DroneManager;
import edu.upc.dsa.DroneManagerImpl;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Drone;
import edu.upc.dsa.models.FlightPlan;
import edu.upc.dsa.models.Pilot;
import edu.upc.dsa.models.Position;
import edu.upc.dsa.transferObjects.*;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DroneService {

    private static final Logger logger = Logger.getLogger(DroneService.class);

    // usamos la fachada de la parte II (Singleton)
    private DroneManager dm = DroneManagerImpl.getInstance();

    private Response error(int codigo, String mensaje) {
        logger.error("Respuesta " + codigo + ": " + mensaje);
        return Response.status(codigo).entity(new ErrorTO(mensaje)).build();
    }

    // ---------- DRONES ----------

    @POST
    @Path("drones")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDrone(DroneTO d) {
        logger.info("POST /drones");
        if (d == null || d.id == null || d.nombre == null) {
            return error(400, "Faltan datos del dron (id y nombre como minimo)");
        }
        try {
            dm.addDrone(d.id, d.nombre, d.fabricante, d.modelo);
        } catch (IllegalArgumentException e) {
            return error(409, e.getMessage());
        }
        return Response.status(201).entity(DroneTO.from(dm.getDrone(d.id))).build();
    }

    @GET
    @Path("drones")
    public Response getDrones() {
        logger.info("GET /drones");
        List<DroneTO> lista = new ArrayList<>();
        for (Drone d : dm.getDronesByFlightHours()) {
            lista.add(DroneTO.from(d));
        }
        return Response.status(200).entity(lista).build();
    }

    @GET
    @Path("drones/{idDrone}/flightplans")
    public Response getPlansByDrone(@PathParam("idDrone") String idDrone) {
        logger.info("GET /drones/" + idDrone + "/flightplans");
        try {
            List<FlightPlanTO> lista = new ArrayList<>();
            for (FlightPlan fp : dm.getFlightPlansByDrone(idDrone)) {
                lista.add(FlightPlanTO.from(fp));
            }
            return Response.status(200).entity(lista).build();
        } catch (DroneNotFoundException e) {
            return error(404, e.getMessage());
        }
    }

    // ---------- PILOTOS ----------

    @POST
    @Path("pilots")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPilot(PilotTO p) {
        logger.info("POST /pilots");
        if (p == null || p.id == null || p.nombre == null) {
            return error(400, "Faltan datos del piloto (id y nombre como minimo)");
        }
        try {
            dm.addPilot(p.id, p.nombre, p.apellidos);
        } catch (IllegalArgumentException e) {
            return error(409, e.getMessage());
        }
        return Response.status(201).entity(PilotTO.from(dm.getPilot(p.id))).build();
    }

    @GET
    @Path("pilots")
    public Response getPilots() {
        logger.info("GET /pilots");
        List<PilotTO> lista = new ArrayList<>();
        for (Pilot p : dm.getPilotsByFlightHours()) {
            lista.add(PilotTO.from(p));
        }
        return Response.status(200).entity(lista).build();
    }

    @GET
    @Path("pilots/{idPilot}/flightplans")
    public Response getPlansByPilot(@PathParam("idPilot") String idPilot) {
        logger.info("GET /pilots/" + idPilot + "/flightplans");
        try {
            List<FlightPlanTO> lista = new ArrayList<>();
            for (FlightPlan fp : dm.getFlightPlansByPilot(idPilot)) {
                lista.add(FlightPlanTO.from(fp));
            }
            return Response.status(200).entity(lista).build();
        } catch (PilotNotFoundException e) {
            return error(404, e.getMessage());
        }
    }

    // ---------- ALMACEN ----------

    @POST
    @Path("warehouse/{idDrone}")
    public Response storeDrone(@PathParam("idDrone") String idDrone) {
        logger.info("POST /warehouse/" + idDrone);
        try {
            dm.storeDrone(idDrone);
        } catch (DroneNotFoundException e) {
            return error(404, e.getMessage());
        } catch (DroneNotOperativeException e) {
            return error(409, e.getMessage());
        }
        return Response.status(200).entity(DroneTO.from(dm.getDrone(idDrone))).build();
    }

    @PUT
    @Path("warehouse/repair")
    public Response repairDrone() {
        logger.info("PUT /warehouse/repair");
        try {
            Drone d = dm.repairDrone();
            return Response.status(200).entity(DroneTO.from(d)).build();
        } catch (WarehouseEmptyException e) {
            return error(404, e.getMessage());
        }
    }

    // ---------- PLANES DE VUELO ----------

    @POST
    @Path("flightplans")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFlightPlan(FlightPlanTO f) {
        logger.info("POST /flightplans");
        if (f == null || f.idDrone == null || f.idPilot == null || f.fecha == null || f.horas <= 0) {
            return error(400, "Faltan datos del plan (idDrone, idPilot, fecha y horas > 0)");
        }

        LocalDateTime fecha;
        try {
            fecha = LocalDateTime.parse(f.fecha);
        } catch (DateTimeParseException e) {
            return error(400, "La fecha debe tener el formato 2026-11-10T10:00");
        }

        List<Position> posiciones = new ArrayList<>();
        if (f.posiciones != null) {
            for (PositionTO p : f.posiciones) {
                posiciones.add(new Position(p.latitude, p.longitude));
            }
        }

        try {
            FlightPlan fp = dm.createFlightPlan(f.idDrone, f.idPilot, fecha, f.horas, posiciones);
            return Response.status(201).entity(FlightPlanTO.from(fp)).build();
        } catch (DroneNotFoundException | PilotNotFoundException e) {
            return error(404, e.getMessage());
        } catch (PlanConflictException | DroneNotOperativeException e) {
            return error(409, e.getMessage());
        }
    }
}
