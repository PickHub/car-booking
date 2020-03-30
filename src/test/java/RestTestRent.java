import carbooking.database.CustomerDatabase;
import carbooking.database.SimpleCustomerData;
import carbooking.database.SimpleVehicleData;
import carbooking.database.VehicleDatabase;
import carbooking.vehicle.MockCar;
import carbooking.vehicle.Vehicle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.HttpEndpoint;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Daniel Handloser on 30.03.2020.
 */
public class RestTestRent {
    private static final String RENT_AVAILABLE_URL = "http://localhost:8000/rent/available";
    private static final String RENT_BLOCK_URL = "http://localhost:8000/rent/block";
    private static final String RENT_START_URL = "http://localhost:8000/rent/start";
    private static final String RENT_STOP_URL = "http://localhost:8000/rent/stop";
    private static HttpClient httpClient;

    @BeforeAll
    public static void setUp() {
        CustomerDatabase customerData = new SimpleCustomerData();
        VehicleDatabase vehicleData = new SimpleVehicleData();
        addMockVehicle(vehicleData);
        HttpEndpoint httpServer = new HttpEndpoint(customerData, vehicleData, 8000);
        httpServer.startHttpServer();
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    public void testAvailableVehiclesMissingParams() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RENT_AVAILABLE_URL))
                .header("Authorization", basicAuth("test", "test"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        assertEquals(400, response.statusCode());

    }

    private static void addMockVehicle(VehicleDatabase vehicleData) {
        Vehicle car = new MockCar(1.40);
        vehicleData.addVehicle(car);
    }

    private static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
