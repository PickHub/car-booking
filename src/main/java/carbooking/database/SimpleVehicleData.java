package carbooking.database;

import carbooking.vehicle.Vehicle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public class SimpleVehicleData implements VehicleDatabase {
    private List<Vehicle> vehicleData;

    public SimpleVehicleData() {
        this.vehicleData = new LinkedList<>();
    }
    public void addVehicle(Vehicle vehicle) {
        vehicleData.add(vehicle);
    }

    public void removeVehicle(String id) {
        Vehicle curr = null;

        for(int i = 0; i < vehicleData.size(); i++) {
            if(vehicleData.get(i).getId() == id) {
                vehicleData.remove(i);
            }
        }
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleData;
    }
}
