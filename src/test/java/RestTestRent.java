import carbooking.database.*;
import carbooking.vehicle.MockCar;
import carbooking.vehicle.Vehicle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.HttpEndpoint;

import javax.mail.internet.AddressException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Daniel Handloser on 30.03.2020.
 */
public class RestTestRent {
    private static final String RENT_AVAILABLE_URL = "http://localhost:8000/rent/available";
    private static final String RENT_BLOCK_URL = "http://localhost:8000/rent/block";
    private static final String RENT_START_URL = "http://localhost:8000/rent/start";
    private static final String RENT_STOP_URL = "http://localhost:8000/rent/stop";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String TEST = "test";
    public static final int CODE_400 = 400;
    public static final int CODE_200 = 200;
    public static final String AUTHORIZATION = "Authorization";
    public static final String CAR_ID = "car_id";
    private static HttpClient httpClient;
    private static CustomerDatabase customerData;
    private static VehicleDatabase vehicleData;
    private static String mockCarId;

    @BeforeAll
    public static void setUp() throws AddressException {
        customerData = new SimpleCustomerData();
        vehicleData = new SimpleVehicleData();
        addMockVehicle(vehicleData);
        addMockCustomer(customerData);
        HttpEndpoint httpServer = new HttpEndpoint(customerData, vehicleData, 8000);
        httpServer.startHttpServer();
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    public void testAvailableVehicles() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RENT_AVAILABLE_URL))
                .headers(LATITUDE,"0", LONGITUDE,"0", AUTHORIZATION, basicAuth(TEST, TEST))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        try {
            JSONArray jsonArray = new JSONArray(response.body());
            boolean hasLatitude = ((JSONObject) jsonArray.get(0)).has(LATITUDE);
            boolean hasLongitude = ((JSONObject) jsonArray.get(0)).has(LONGITUDE);
            assertTrue(hasLatitude);
            assertTrue(hasLongitude);
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
        assertEquals(CODE_200, response.statusCode());
    }

    @Test
    public void testAvailableVehiclesMissingParams() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RENT_AVAILABLE_URL))
                .header(AUTHORIZATION, basicAuth(TEST, TEST))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(CODE_400, response.statusCode());
    }

    @Test
    public void testStartRent() throws IOException, InterruptedException, IdNotFoundException {
        assertEquals(CODE_200, sendGET(RENT_START_URL));
        System.out.println("mock id: " + mockCarId);
        assertTrue(vehicleData.getVehicleById(mockCarId).getIsRented());
    }

    private static void addMockVehicle(VehicleDatabase vehicleData) {
        Vehicle car = new MockCar(1.40);
        mockCarId = car.getId();
        vehicleData.addVehicle(car);
    }

    private static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    private static void addMockCustomer(CustomerDatabase customerData) throws AddressException {
        customerData.addCustomer("test@test.com", TEST, TEST);
    }

    private static int sendGET(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("car_id", mockCarId, AUTHORIZATION, basicAuth(TEST, TEST))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode();
    }
}
