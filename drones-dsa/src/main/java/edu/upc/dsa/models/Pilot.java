package edu.upc.dsa.models;

public class Pilot {
    private String id;
    private String nombre;
    private String apellidos;
    private int horasVuelo;

    public Pilot(String id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.horasVuelo = 0;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public int getHorasVuelo() { return horasVuelo; }

    public void sumarHoras(int horas) { this.horasVuelo += horas; }

    @Override
    public String toString() {
        return "Pilot [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos
                + ", horasVuelo=" + horasVuelo + "]";
    }
}
