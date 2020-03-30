package rest.handler;

import carbooking.database.CustomerDatabase;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import javax.mail.internet.AddressException;
import java.io.*;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public class CustomerHandler extends ParentHandler {
    private static final String INVALID_EMAIL_MESSAGE = "This email address is already in use or not valid. Cannot create account.";
    private static final String MISSING_PARAM_MESSAGE = "Missing required parameter: name, e-mail and/or password.";
    private static final String SUCCESS_MESSAGE = "Successfully added customer account.";
    private static final String NOT_ALLOWED_MESSAGE = "Method not allowed";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final int BAD_REQUEST_CODE = 400;
    private static final int OK_CODE = 200;
    private static final int NOT_ALLOWED_CODE = 405;
    private static final String POST = "POST";
    private CustomerDatabase customerData;

    public CustomerHandler(CustomerDatabase customerData) {
        this.customerData = customerData;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();

        if (POST.equals(requestMethod)) {
            JSONObject jsonBody = createJsonResponse(exchange);

            if (jsonBody.has(EMAIL) && jsonBody.has(PASSWORD) && jsonBody.has(NAME)) {
                String email = getStringFromJson(jsonBody, EMAIL);
                String password = getStringFromJson(jsonBody, PASSWORD);
                String name = getStringFromJson(jsonBody, NAME);

                try {
                    customerData.addCustomer(email, password, name);
                }
                catch(AddressException e) {
                    sendResponse(exchange, INVALID_EMAIL_MESSAGE, BAD_REQUEST_CODE);
                    return;
                }


            } else {
                sendResponse(exchange, MISSING_PARAM_MESSAGE, BAD_REQUEST_CODE);
                return;
            }
            sendResponse(exchange, SUCCESS_MESSAGE, OK_CODE);
            return;
        }
        sendResponse(exchange, NOT_ALLOWED_MESSAGE, NOT_ALLOWED_CODE);
    }
}

