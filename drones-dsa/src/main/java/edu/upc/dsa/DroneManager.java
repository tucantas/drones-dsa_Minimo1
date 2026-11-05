package edu.upc.dsa;

import edu.upc.dsa.models.Drone;
import edu.upc.dsa.models.FlightPlan;
import edu.upc.dsa.models.Pilot;
import edu.upc.dsa.models.Position;

import java.time.LocalDateTime;
import java.util.List;

public interface DroneManager {

    // Anade un nuevo dron
    void addDrone(String id, String nombre, String fabricante, String modelo);

    // Anade un nuevo piloto
    void addPilot(String id, String nombre, String apellidos);

    // Drones ordenados de mayor a menor horas de vuelo
    List<Drone> getDronesByFlightHours();

    // Pilotos ordenados de mayor a menor horas de vuelo
    List<Pilot> getPilotsByFlightHours();

    // Guarda un dron en el almacen para mantenimiento
    void storeDrone(String idDrone);

    // Repara el dron mas cercano a la puerta y lo saca del almacen
    Drone repairDrone();

    // Crea un plan de vuelo (error si dron/piloto ocupados o dron en el almacen)
    FlightPlan createFlightPlan(String idDrone, String idPilot, LocalDateTime fecha,
                                int horas, List<Position> posiciones);

    // Planes de vuelo de un piloto
    List<FlightPlan> getFlightPlansByPilot(String idPilot);

    // Planes de vuelo de un dron
    List<FlightPlan> getFlightPlansByDrone(String idDrone);

    // Auxiliares
    Drone getDrone(String idDrone);
    Pilot getPilot(String idPilot);

    // Vacia todo (lo usamos en los tests)
    void clear();
}
