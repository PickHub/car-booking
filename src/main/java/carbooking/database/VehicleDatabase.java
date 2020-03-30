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

    void addVehicle(Vehicle vehicle);

    void removeVehicle(String id);

    JSONArray availableVehicles(double latitude, double longitude, int radius);

    void blockVehicle(String id) throws IdNotFoundException, VehicleAlreadyReservedException;

    void rentVehicle(String id, String username) throws IdNotFoundException, VehicleAlreadyRentedException;

    double stopVehicleRent(String vehicleId) throws IdNotFoundException;

    Vehicle getVehicleById(String id) throws IdNotFoundException ;
    }
