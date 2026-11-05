package edu.upc.dsa.transferObjects;

import edu.upc.dsa.models.Drone;

public class DroneTO {
    public String id;
    public String nombre;
    public String fabricante;
    public String modelo;
    public int horasVuelo;
    public boolean operativo;

    public DroneTO() {}

    public static DroneTO from(Drone d) {
        DroneTO to = new DroneTO();
        to.id = d.getId();
        to.nombre = d.getNombre();
        to.fabricante = d.getFabricante();
        to.modelo = d.getModelo();
        to.horasVuelo = d.getHorasVuelo();
        to.operativo = d.isOperativo();
        return to;
    }
}
