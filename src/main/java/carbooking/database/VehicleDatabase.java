package carbooking.database;

import carbooking.vehicle.Vehicle;

import java.util.List;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public interface VehicleDatabase {

    public void addVehicle(Vehicle vehicle);

    public void removeVehicle(String id);

    public List<Vehicle> getAllVehicles();

}
