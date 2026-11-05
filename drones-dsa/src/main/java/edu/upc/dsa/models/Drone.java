package edu.upc.dsa.models;

public class Drone {
    private String id;
    private String nombre;
    private String fabricante;
    private String modelo;
    private int horasVuelo;
    private boolean operativo;

    public Drone(String id, String nombre, String fabricante, String modelo) {
        this.id = id;
        this.nombre = nombre;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.horasVuelo = 0;
        this.operativo = true;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getFabricante() { return fabricante; }
    public String getModelo() { return modelo; }
    public int getHorasVuelo() { return horasVuelo; }
    public boolean isOperativo() { return operativo; }

    public void setOperativo(boolean operativo) { this.operativo = operativo; }
    public void sumarHoras(int horas) { this.horasVuelo += horas; }

    @Override
    public String toString() {
        return "Drone [id=" + id + ", nombre=" + nombre + ", fabricante=" + fabricante
                + ", modelo=" + modelo + ", horasVuelo=" + horasVuelo + ", operativo=" + operativo + "]";
    }
}
