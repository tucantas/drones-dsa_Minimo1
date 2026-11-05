package edu.upc.dsa.models;

import java.time.LocalDateTime;
import java.util.List;

public class FlightPlan {
    private String id;
    private String idDrone;
    private String idPilot;
    private LocalDateTime fecha;   // dia + hora de inicio
    private int duracion;          // horas estimadas
    private List<Position> posiciones;

    public FlightPlan(String id, String idDrone, String idPilot,
                      LocalDateTime fecha, int duracion, List<Position> posiciones) {
        this.id = id;
        this.idDrone = idDrone;
        this.idPilot = idPilot;
        this.fecha = fecha;
        this.duracion = duracion;
        this.posiciones = posiciones;
    }

    public String getId() { return id; }
    public String getIdDrone() { return idDrone; }
    public String getIdPilot() { return idPilot; }
    public LocalDateTime getFecha() { return fecha; }
    public int getDuracion() { return duracion; }
    public List<Position> getPosiciones() { return posiciones; }

    public LocalDateTime getFin() {
        return fecha.plusHours(duracion);
    }

    // Dos reservas se solapan si: inicio1 < fin2 y inicio2 < fin1
    public boolean seSolapaCon(LocalDateTime otroInicio, int otraDuracion) {
        LocalDateTime otroFin = otroInicio.plusHours(otraDuracion);
        return this.fecha.isBefore(otroFin) && otroInicio.isBefore(this.getFin());
    }

    @Override
    public String toString() {
        return "FlightPlan [id=" + id + ", dron=" + idDrone + ", piloto=" + idPilot
                + ", fecha=" + fecha + ", duracion=" + duracion + "h, posiciones=" + posiciones + "]";
    }
}
