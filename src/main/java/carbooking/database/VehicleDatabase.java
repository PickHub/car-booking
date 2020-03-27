package carbooking.database;

import carbooking.vehicle.Vehicle;

import java.util.List;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public interface VehicleDatabase {

    public void addVehicle();

    public void removeVehicle(int id);

    public List<Vehicle> getAllVehicles();

}
