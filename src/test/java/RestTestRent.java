import carbooking.database.*;
import carbooking.vehicle.MockCar;
import carbooking.vehicle.Vehicle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import rest.HttpEndpoint;

import javax.mail.internet.AddressException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

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
    private static VehicleDatabase vehicleData;
    private static String mockCarId;
    private static HttpEndpoint httpServer;

    @BeforeAll
    public static void setUp() throws AddressException {
        CustomerDatabase customerData = new SimpleCustomerData();
        vehicleData = new SimpleVehicleData();
        addMockVehicle(vehicleData);
        addMockCustomer(customerData);
        httpServer = new HttpEndpoint(customerData, vehicleData, 8000);
        httpServer.startHttpServer();
        httpClient = HttpClient.newHttpClient();
    }

    @AfterAll
    public static void tearDown() {
        httpServer.stopHttpServer();
    }

    @Test
    public void testStartRent() throws IOException, InterruptedException, IdNotFoundException {
        assertEquals(CODE_200, sendGET(RENT_START_URL));
        assertTrue(vehicleData.getVehicleById(mockCarId).getIsRented());
    }

    @Test
    public void testStopRent() throws IOException, InterruptedException, IdNotFoundException {
        sendGET(RENT_START_URL);
        assertEquals(CODE_200, sendGET(RENT_STOP_URL));
        assertFalse(vehicleData.getVehicleById(mockCarId).getIsRented());
    }

    @Test
    public void testStartBlockingVehicle() throws IOException, InterruptedException, IdNotFoundException {
        assertEquals(CODE_200, sendGET(RENT_BLOCK_URL));
        assertTrue(vehicleData.getVehicleById(mockCarId).getIsReserved());
    }

    @Test
    public void testAvailableVehicles() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RENT_AVAILABLE_URL))
                .headers(LATITUDE,"0", LONGITUDE,"0", AUTHORIZATION, basicAuth())
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
                .header(AUTHORIZATION, basicAuth())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(CODE_400, response.statusCode());
    }

    private static void addMockVehicle(VehicleDatabase vehicleData) {
        Vehicle car = new MockCar(1.40);
        mockCarId = car.getId();
        vehicleData.addVehicle(car);
        vehicleData.addVehicle(new MockCar(1.30));
    }

    private static String basicAuth() {
        return "Basic " + Base64.getEncoder().encodeToString((RestTestRent.TEST + ":" + RestTestRent.TEST).getBytes());
    }

    private static void addMockCustomer(CustomerDatabase customerData) throws AddressException {
        customerData.addCustomer("test@test.com", TEST, TEST);
    }

    private static int sendGET(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("car_id", mockCarId, AUTHORIZATION, basicAuth())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode();
    }
}
