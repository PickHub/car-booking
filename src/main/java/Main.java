import carbooking.database.CustomerDatabase;
import carbooking.database.SimpleCustomerData;
import carbooking.database.SimpleVehicleData;
import carbooking.database.VehicleDatabase;
import rest.HttpEndpoint;

/**
 * Created by Daniel Handloser on 30.03.2020.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //TODO port in cmd
        int port = 8000;
        CustomerDatabase customerData = new SimpleCustomerData();
        VehicleDatabase vehicleData = new SimpleVehicleData();
        HttpEndpoint httpServer = new HttpEndpoint(customerData, vehicleData, port);
        httpServer.startHttpServer();

    }
}
