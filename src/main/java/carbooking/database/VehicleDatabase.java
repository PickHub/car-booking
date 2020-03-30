package carbooking.database;

import carbooking.vehicle.Vehicle;
import carbooking.vehicle.VehicleAlreadyRentedException;
import carbooking.vehicle.VehicleAlreadyReservedException;
import org.json.JSONArray;

import java.util.List;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public interface VehicleDatabase {

    public void addVehicle(Vehicle vehicle);

    public void removeVehicle(String id);

    public List<Vehicle> getAllVehicles();

    public JSONArray availableVehicles(double latitude, double longitude, int radius);

    public void blockVehicle(String id) throws IdNotFoundException, VehicleAlreadyReservedException;

    public void rentVehicle(String id, String username) throws IdNotFoundException, VehicleAlreadyRentedException;

    public double stopVehicleRent(String vehicleId) throws IdNotFoundException;

    public Vehicle getVehicleById(String id) throws IdNotFoundException ;
    }
