package carbooking.vehicle;

/**
 * Created by Daniel Handloser on 29.03.2020.
 */
public class VehicleAlreadyRentedException extends Exception{
    public VehicleAlreadyRentedException() {}

    public VehicleAlreadyRentedException(String message) {
        super(message);
    }
}
