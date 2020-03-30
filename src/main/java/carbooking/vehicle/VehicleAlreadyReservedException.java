package carbooking.vehicle;

/**
 * Created by Daniel Handloser on 29.03.2020.
 */
public class VehicleAlreadyReservedException extends Exception{
    public VehicleAlreadyReservedException(){}

    public VehicleAlreadyReservedException(String message) {
        super(message);
    }
}
