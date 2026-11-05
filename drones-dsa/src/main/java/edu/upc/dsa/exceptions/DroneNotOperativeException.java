package edu.upc.dsa.exceptions;

public class DroneNotOperativeException extends RuntimeException {
    public DroneNotOperativeException(String message) {
        super(message);
    }
}
