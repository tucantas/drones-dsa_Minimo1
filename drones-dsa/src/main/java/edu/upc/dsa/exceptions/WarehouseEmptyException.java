package edu.upc.dsa.exceptions;

public class WarehouseEmptyException extends RuntimeException {
    public WarehouseEmptyException(String message) {
        super(message);
    }
}
