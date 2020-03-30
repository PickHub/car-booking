import carbooking.database.CustomerDatabase;
import carbooking.database.SimpleCustomerData;
import carbooking.database.SimpleVehicleData;
import carbooking.database.VehicleDatabase;
import carbooking.vehicle.MockCar;
import carbooking.vehicle.Vehicle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.HttpEndpoint;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestTestCustomer {
    private static final String CUSTOMER_URL = "http://localhost:8000/customer";
    private static final String RENT_AVAILABLE_URL = "http://localhost:8000/rent/available";
    private static final String RENT_BLOCK_URL = "http://localhost:8000/rent/block";
    private static final String RENT_START_URL = "http://localhost:8000/rent/start";
    private static final String RENT_STOP_URL = "http://localhost:8000/rent/stop";
    private static HttpEndpoint httpServer;
    private static HttpClient httpClient;

    @BeforeAll
    public static void setUp() {
        CustomerDatabase customerData = new SimpleCustomerData();
        VehicleDatabase vehicleData = new SimpleVehicleData();
        addMockVehicle(vehicleData);
        httpServer = new HttpEndpoint(customerData, vehicleData, 8000);
        httpServer.startHttpServer();
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

    }

    @AfterAll
    public static void tearDown() {
        httpServer.stopHttpServer();
    }

    @Test
    public void testAddCustomer() throws IOException, InterruptedException {
        // json formatted data
        String json = new StringBuilder()
                .append("{")
                .append("\"email\":\"test@abc.com\",")
                .append("\"password\":\"test\",")
                .append("\"name\":\"test\"")
                .append("}").toString();

        HttpResponse<String> response = requestPOST(json);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testAddDuplicateEmail() throws IOException, InterruptedException {
        // json formatted data
        String json_init = new StringBuilder()
                .append("{")
                .append("\"email\":\"test@abc.com\",")
                .append("\"password\":\"test\",")
                .append("\"name\":\"test\"")
                .append("}").toString();
        String json_duplicate = new StringBuilder()
                .append("{")
                .append("\"email\":\"test@abc.com\",")
                .append("\"password\":\"abc\",")
                .append("\"name\":\"abc\"")
                .append("}").toString();

        requestPOST(json_init);
        HttpResponse<String> response = requestPOST(json_duplicate);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void testAddCustomerMissingArgs() throws IOException, InterruptedException {
        // json formatted data
        String json = new StringBuilder()
                .append("{")
                .append("\"email\":\"test@abc.com\",")
                .append("}").toString();

        HttpResponse<String> response = requestPOST(json);
        assertEquals(400, response.statusCode());
    }

    private HttpResponse<String> requestPOST(String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(RestTestCustomer.CUSTOMER_URL))
                .setHeader("User-Agent", "Java HttpClient")
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> requestGET(String json, String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java HttpClient")
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void addMockVehicle(VehicleDatabase vehicleData) {
        Vehicle car = new MockCar(1.40);
        vehicleData.addVehicle(car);
    }
}