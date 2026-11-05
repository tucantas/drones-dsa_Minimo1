package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Drone;
import edu.upc.dsa.models.FlightPlan;
import edu.upc.dsa.models.Pilot;
import edu.upc.dsa.models.Position;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;

public class DroneManagerImpl implements DroneManager {

    private static final Logger logger = Logger.getLogger(DroneManagerImpl.class);

    // Singleton
    private static DroneManagerImpl instance;

    // Estructuras de datos (las de la parte 1)
    private HashMap<String, Drone> drones;
    private HashMap<String, Pilot> pilots;
    private Queue<Drone> almacen;          // FIFO: el primero que entra es el primero que se repara
    private ArrayList<FlightPlan> planes;

    private DroneManagerImpl() {
        drones = new HashMap<>();
        pilots = new HashMap<>();
        almacen = new LinkedList<>();
        planes = new ArrayList<>();
    }

    public static DroneManagerImpl getInstance() {
        if (instance == null) {
            instance = new DroneManagerImpl();
        }
        return instance;
    }

    @Override
    public void addDrone(String id, String nombre, String fabricante, String modelo) {
        logger.info("addDrone INICIO -> id=" + id + ", nombre=" + nombre
                + ", fabricante=" + fabricante + ", modelo=" + modelo);
        if (drones.containsKey(id)) {
            logger.error("addDrone: ya existe un dron con id " + id);
            throw new IllegalArgumentException("Ya existe el dron " + id);
        }
        Drone d = new Drone(id, nombre, fabricante, modelo);
        drones.put(id, d);
        logger.info("addDrone FIN -> " + d);
    }

    @Override
    public void addPilot(String id, String nombre, String apellidos) {
        logger.info("addPilot INICIO -> id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos);
        if (pilots.containsKey(id)) {
            logger.error("addPilot: ya existe un piloto con id " + id);
            throw new IllegalArgumentException("Ya existe el piloto " + id);
        }
        Pilot p = new Pilot(id, nombre, apellidos);
        pilots.put(id, p);
        logger.info("addPilot FIN -> " + p);
    }

    @Override
    public List<Drone> getDronesByFlightHours() {
        logger.info("getDronesByFlightHours INICIO");
        List<Drone> lista = new ArrayList<>(drones.values());
        lista.sort((a, b) -> b.getHorasVuelo() - a.getHorasVuelo());   // de mayor a menor
        logger.info("getDronesByFlightHours FIN -> " + lista);
        return lista;
    }

    @Override
    public List<Pilot> getPilotsByFlightHours() {
        logger.info("getPilotsByFlightHours INICIO");
        List<Pilot> lista = new ArrayList<>(pilots.values());
        lista.sort((a, b) -> b.getHorasVuelo() - a.getHorasVuelo());
        logger.info("getPilotsByFlightHours FIN -> " + lista);
        return lista;
    }

    @Override
    public void storeDrone(String idDrone) {
        logger.info("storeDrone INICIO -> idDrone=" + idDrone);
        Drone d = drones.get(idDrone);
        if (d == null) {
            logger.error("storeDrone: no existe el dron " + idDrone);
            throw new DroneNotFoundException("No existe el dron " + idDrone);
        }
        if (!d.isOperativo()) {
            logger.error("storeDrone: el dron " + idDrone + " ya esta en el almacen");
            throw new DroneNotOperativeException("El dron " + idDrone + " ya esta en el almacen");
        }
        d.setOperativo(false);
        almacen.add(d);   // se mete al fondo de la cola
        logger.info("storeDrone FIN -> dron " + idDrone + " guardado. Drones en almacen: " + almacen.size());
    }

    @Override
    public Drone repairDrone() {
        logger.info("repairDrone INICIO");
        Drone d = almacen.poll();   // el que esta mas cerca de la puerta
        if (d == null) {
            logger.error("repairDrone: el almacen esta vacio");
            throw new WarehouseEmptyException("No hay drones en el almacen");
        }
        d.setOperativo(true);
        logger.info("repairDrone FIN -> reparado " + d);
        return d;
    }

    @Override
    public FlightPlan createFlightPlan(String idDrone, String idPilot, LocalDateTime fecha,
                                       int horas, List<Position> posiciones) {
        logger.info("createFlightPlan INICIO -> idDrone=" + idDrone + ", idPilot=" + idPilot
                + ", fecha=" + fecha + ", horas=" + horas + ", posiciones=" + posiciones);

        Drone d = drones.get(idDrone);
        if (d == null) {
            logger.error("createFlightPlan: no existe el dron " + idDrone);
            throw new DroneNotFoundException("No existe el dron " + idDrone);
        }
        Pilot p = pilots.get(idPilot);
        if (p == null) {
            logger.error("createFlightPlan: no existe el piloto " + idPilot);
            throw new PilotNotFoundException("No existe el piloto " + idPilot);
        }
        if (!d.isOperativo()) {
            logger.error("createFlightPlan: el dron " + idDrone + " esta en el almacen");
            throw new DroneNotOperativeException("El dron " + idDrone + " no esta operativo");
        }

        // Comprobar solapamientos recorriendo los planes
        for (FlightPlan fp : planes) {
            if (fp.getIdDrone().equals(idDrone) && fp.seSolapaCon(fecha, horas)) {
                logger.error("createFlightPlan: el dron " + idDrone + " ya tiene un plan en ese intervalo");
                throw new PlanConflictException("El dron " + idDrone + " ya tiene un plan en ese intervalo");
            }
            if (fp.getIdPilot().equals(idPilot) && fp.seSolapaCon(fecha, horas)) {
                logger.error("createFlightPlan: el piloto " + idPilot + " ya tiene un plan en ese intervalo");
                throw new PlanConflictException("El piloto " + idPilot + " ya tiene un plan en ese intervalo");
            }
        }

        FlightPlan plan = new FlightPlan(UUID.randomUUID().toString(), idDrone, idPilot,
                fecha, horas, posiciones);
        planes.add(plan);

        // Sumamos las horas al dron y al piloto
        d.sumarHoras(horas);
        p.sumarHoras(horas);

        logger.info("createFlightPlan FIN -> " + plan);
        return plan;
    }

    @Override
    public List<FlightPlan> getFlightPlansByPilot(String idPilot) {
        logger.info("getFlightPlansByPilot INICIO -> idPilot=" + idPilot);
        if (!pilots.containsKey(idPilot)) {
            logger.error("getFlightPlansByPilot: no existe el piloto " + idPilot);
            throw new PilotNotFoundException("No existe el piloto " + idPilot);
        }
        List<FlightPlan> resultado = new ArrayList<>();
        for (FlightPlan fp : planes) {
            if (fp.getIdPilot().equals(idPilot)) {
                resultado.add(fp);
            }
        }
        logger.info("getFlightPlansByPilot FIN -> " + resultado.size() + " planes");
        return resultado;
    }

    @Override
    public List<FlightPlan> getFlightPlansByDrone(String idDrone) {
        logger.info("getFlightPlansByDrone INICIO -> idDrone=" + idDrone);
        if (!drones.containsKey(idDrone)) {
            logger.error("getFlightPlansByDrone: no existe el dron " + idDrone);
            throw new DroneNotFoundException("No existe el dron " + idDrone);
        }
        List<FlightPlan> resultado = new ArrayList<>();
        for (FlightPlan fp : planes) {
            if (fp.getIdDrone().equals(idDrone)) {
                resultado.add(fp);
            }
        }
        logger.info("getFlightPlansByDrone FIN -> " + resultado.size() + " planes");
        return resultado;
    }

    @Override
    public Drone getDrone(String idDrone) {
        logger.info("getDrone INICIO -> idDrone=" + idDrone);
        Drone d = drones.get(idDrone);
        logger.info("getDrone FIN -> " + d);
        return d;
    }

    @Override
    public Pilot getPilot(String idPilot) {
        logger.info("getPilot INICIO -> idPilot=" + idPilot);
        Pilot p = pilots.get(idPilot);
        logger.info("getPilot FIN -> " + p);
        return p;
    }

    @Override
    public void clear() {
        logger.info("clear INICIO");
        drones.clear();
        pilots.clear();
        almacen.clear();
        planes.clear();
        logger.info("clear FIN");
    }
}
