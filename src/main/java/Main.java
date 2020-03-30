import carbooking.database.CustomerDatabase;
import carbooking.database.SimpleCustomerData;
import carbooking.database.SimpleVehicleData;
import carbooking.database.VehicleDatabase;
import carbooking.vehicle.MockCar;
import rest.HttpEndpoint;

/**
 * Created by Daniel Handloser on 30.03.2020.
 */
public class Main {

    private final double RENTAL_PRICE = 1.10;
    private static final int port = 8000;

    public static void main(String[] args) {
        CustomerDatabase customerData = new SimpleCustomerData();
        VehicleDatabase vehicleData = new SimpleVehicleData();
        new Main().mockVehicles(vehicleData);
        HttpEndpoint httpServer = new HttpEndpoint(customerData, vehicleData, port);
        httpServer.startHttpServer();

    }

    private void mockVehicles(VehicleDatabase vehicleData) {
        for(int i = 0; i < 10; i++) {
            vehicleData.addVehicle(new MockCar(RENTAL_PRICE));
        }
    }
}
