package edu.upc.dsa.transferObjects;

import edu.upc.dsa.models.FlightPlan;
import edu.upc.dsa.models.Position;

import java.util.ArrayList;
import java.util.List;

// Se usa tanto para recibir un plan (sin id) como para devolverlo (con id)
public class FlightPlanTO {
    public String id;
    public String idDrone;
    public String idPilot;
    public String fecha;      // formato 2026-11-10T10:00
    public int horas;
    public List<PositionTO> posiciones;

    public FlightPlanTO() {}

    public static FlightPlanTO from(FlightPlan fp) {
        FlightPlanTO to = new FlightPlanTO();
        to.id = fp.getId();
        to.idDrone = fp.getIdDrone();
        to.idPilot = fp.getIdPilot();
        to.fecha = fp.getFecha().toString();
        to.horas = fp.getDuracion();
        to.posiciones = new ArrayList<>();
        for (Position p : fp.getPosiciones()) {
            to.posiciones.add(PositionTO.from(p));
        }
        return to;
    }
}
