/**
 * Created by Daniel Handloser on 30.03.2020.
 */

import carbooking.database.CustomerDatabase;
import carbooking.database.SimpleCustomerData;
import carbooking.database.SimpleVehicleData;
import carbooking.database.VehicleDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.HttpEndpoint;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestTest {
    public static final String CUSTOMER_URL = "http://localhost:8000/customer";
    private HttpEndpoint httpServer;
    private static HttpClient httpClient;

    @BeforeAll
    public static void setUp() {
        CustomerDatabase customerData = new SimpleCustomerData();
        VehicleDatabase vehicleData = new SimpleVehicleData();
        HttpEndpoint httpServer = new HttpEndpoint(customerData, vehicleData, 8000);
        httpServer.startHttpServer();
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
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

        HttpResponse<String> response = postRequest(json, CUSTOMER_URL);
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

        postRequest(json_init, CUSTOMER_URL);
        HttpResponse<String> response = postRequest(json_duplicate, CUSTOMER_URL);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void testAddCustomerMissingArgs() throws IOException, InterruptedException {
        // json formatted data
        String json = new StringBuilder()
                .append("{")
                .append("\"email\":\"test@abc.com\",")
                .append("}").toString();

        HttpResponse<String> response = postRequest(json, CUSTOMER_URL);
        assertEquals(400, response.statusCode());
    }

    private HttpResponse<String> postRequest(String json, String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java HttpClient") // add request header
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}