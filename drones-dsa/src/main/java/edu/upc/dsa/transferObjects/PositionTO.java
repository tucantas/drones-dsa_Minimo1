package edu.upc.dsa.transferObjects;

import edu.upc.dsa.models.Position;

public class PositionTO {
    public double latitude;
    public double longitude;

    public PositionTO() {}

    public static PositionTO from(Position p) {
        PositionTO to = new PositionTO();
        to.latitude = p.getLatitude();
        to.longitude = p.getLongitude();
        return to;
    }
}
