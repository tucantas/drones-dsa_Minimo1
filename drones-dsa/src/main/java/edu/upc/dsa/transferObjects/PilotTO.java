package edu.upc.dsa.transferObjects;

import edu.upc.dsa.models.Pilot;

public class PilotTO {
    public String id;
    public String nombre;
    public String apellidos;
    public int horasVuelo;

    public PilotTO() {}

    public static PilotTO from(Pilot p) {
        PilotTO to = new PilotTO();
        to.id = p.getId();
        to.nombre = p.getNombre();
        to.apellidos = p.getApellidos();
        to.horasVuelo = p.getHorasVuelo();
        return to;
    }
}
