package carbooking.database;

import carbooking.utils.Location;
import carbooking.vehicle.Vehicle;
import carbooking.vehicle.VehicleAlreadyRentedException;
import carbooking.vehicle.VehicleAlreadyReservedException;
import org.json.JSONArray;

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
            if(vehicleData.get(i).getId().equals(id)) {
                vehicleData.remove(i);
                break;
            }
        }
    }

    public JSONArray availableVehicles(double latitude, double longitude, int radius) {
        Location customerLocation = new Location(longitude, latitude);
        JSONArray availableCars = new JSONArray();

        for (Vehicle vehicle : vehicleData) {
            if (!vehicle.getIsRented() && !vehicle.getIsReserved()) {
                double distance = vehicle.locate().calculateDistance(customerLocation);
                if(distance <= radius) {
                    availableCars.put(vehicle.toJson());
                }
            }
        }
        return availableCars;
    }

    public void blockVehicle(String id) throws IdNotFoundException, VehicleAlreadyReservedException {
        Vehicle vehicleToBlock = getVehicleById(id);
        vehicleToBlock.setIsReservedAtomic(true);
    }

    public void rentVehicle(String id, String username) throws IdNotFoundException, VehicleAlreadyRentedException {
        Vehicle vehicleToBlock = getVehicleById(id);
        vehicleToBlock.startRental(username);
    }

    public double stopVehicleRent(String vehicleId) throws IdNotFoundException {
        Vehicle vehicleToBlock = getVehicleById(vehicleId);
        return vehicleToBlock.stopRental();
    }

    public Vehicle getVehicleById(String id) throws IdNotFoundException {
        for (Vehicle veh : vehicleData) {
            if(veh.getId().equals(id)) {
                return veh;
            }
        }

        throw new IdNotFoundException();
    }
}
