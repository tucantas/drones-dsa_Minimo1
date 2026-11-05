package edu.upc.dsa;

import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.Drone;
import edu.upc.dsa.models.FlightPlan;
import edu.upc.dsa.models.Pilot;
import edu.upc.dsa.models.Position;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DroneManagerTest {

    private DroneManager dm;
    private List<Position> ruta;

    @Before
    public void setUp() {
        dm = DroneManagerImpl.getInstance();
        dm.clear();

        dm.addDrone("D01", "Halcon", "DJI", "Mavic 3");
        dm.addDrone("D02", "Aguila", "Parrot", "Anafi");
        dm.addDrone("D03", "Cuervo", "DJI", "Mini 4");
        dm.addPilot("P01", "Eric", "Lopez");
        dm.addPilot("P02", "Toni", "Oller");

        ruta = new ArrayList<>();
        ruta.add(new Position(41.275, 1.987));
        ruta.add(new Position(41.280, 1.990));
    }

    @Test
    public void testAddDroneAndPilot() {
        assertNotNull(dm.getDrone("D01"));
        assertEquals("Halcon", dm.getDrone("D01").getNombre());
        assertTrue(dm.getDrone("D01").isOperativo());
        assertNotNull(dm.getPilot("P01"));
        assertEquals("Lopez", dm.getPilot("P01").getApellidos());
    }


    @Test
    public void testListadoOrdenadoPorHoras() {
        dm.createFlightPlan("D01", "P01", LocalDateTime.of(2026, 11, 10, 9, 0), 2, ruta);
        dm.createFlightPlan("D02", "P01", LocalDateTime.of(2026, 11, 11, 9, 0), 5, ruta);
        dm.createFlightPlan("D03", "P02", LocalDateTime.of(2026, 11, 12, 9, 0), 1, ruta);

        List<Drone> drones = dm.getDronesByFlightHours();
        assertEquals("D02", drones.get(0).getId());
        assertEquals("D01", drones.get(1).getId());
        assertEquals("D03", drones.get(2).getId());

        List<Pilot> pilotos = dm.getPilotsByFlightHours();
        assertEquals("P01", pilotos.get(0).getId());
        assertEquals("P02", pilotos.get(1).getId());
    }

    @Test
    public void testAlmacenFIFO() {
        dm.storeDrone("D01");
        dm.storeDrone("D02");
        assertFalse(dm.getDrone("D01").isOperativo());
        assertFalse(dm.getDrone("D02").isOperativo());

        Drone reparado = dm.repairDrone();
        assertEquals("D01", reparado.getId());
        assertTrue(dm.getDrone("D01").isOperativo());
        assertFalse(dm.getDrone("D02").isOperativo());

        assertEquals("D02", dm.repairDrone().getId());
    }




    @Test
    public void testCrearPlanDeVuelo() {
        FlightPlan fp = dm.createFlightPlan("D01", "P01", LocalDateTime.of(2026, 11, 10, 10, 0), 2, ruta);
        assertNotNull(fp);
        assertEquals("D01", fp.getIdDrone());
        assertEquals(2, dm.getDrone("D01").getHorasVuelo());
    }

    @Test(expected = PlanConflictException.class)
    public void testConflictoDron() {
        dm.createFlightPlan("D01", "P01", LocalDateTime.of(2026, 11, 10, 10, 0), 3, ruta);
        // mismo dron, otro piloto, se pisa con el plan anterior (10:00-13:00)
        dm.createFlightPlan("D01", "P02", LocalDateTime.of(2026, 11, 10, 12, 0), 2, ruta);
    }

}
