package rest.handler;

import carbooking.database.CustomerDatabase;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Daniel Handloser on 28.03.2020.
 */
public class RentalHandler extends ParentHandler {

    CustomerDatabase customerData;

    public RentalHandler(CustomerDatabase customerData) {
        this.customerData = customerData;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if ("GET".equals(requestMethod)) {
            JSONObject jsonBody = createJsonResponse(exchange);

            if (jsonBody.has("email") && jsonBody.has("password") && jsonBody.has("name")) {
                String email = String.valueOf(jsonBody.get("email"));
                String password = String.valueOf(jsonBody.get("password"));
                String name = String.valueOf(jsonBody.get("name"));

                if(!customerData.addCustomer(email, password, name)) {
                    sendResponse(exchange,"This email address is already in use. Cannot create account.", 400);
                    return;
                }


            } else {
                String response = "Missing required parameter: name, e-mail and/or password.";
                sendResponse(exchange, response, 400);
                return;
            }
            sendResponse(exchange, "Successfully added customer account.", 200);
            return;
        }
        sendResponse(exchange, "Method not allowed", 405);
    }
}
