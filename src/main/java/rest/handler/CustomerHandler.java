package rest.handler;

import carbooking.database.CustomerDatabase;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import javax.mail.internet.AddressException;
import java.io.*;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public class CustomerHandler extends ParentHandler {
    public static final String INVALID_EMAIL_MESSAGE = "This email address is already in use or not valid. Cannot create account.";
    public static final String MISSING_PARAM_MESSAGE = "Missing required parameter: name, e-mail and/or password.";
    CustomerDatabase customerData;

    public CustomerHandler(CustomerDatabase customerData) {
        this.customerData = customerData;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if ("POST".equals(requestMethod)) {
            JSONObject jsonBody = createJsonResponse(exchange);

            if (jsonBody.has("email") && jsonBody.has("password") && jsonBody.has("name")) {
                String email = String.valueOf(jsonBody.get("email"));
                String password = String.valueOf(jsonBody.get("password"));
                String name = String.valueOf(jsonBody.get("name"));

                try {
                    customerData.addCustomer(email, password, name);
                }
                catch(AddressException e) {
                    sendResponse(exchange, INVALID_EMAIL_MESSAGE, 400);
                    return;
                }


            } else {
                sendResponse(exchange, MISSING_PARAM_MESSAGE, 400);
                return;
            }
            sendResponse(exchange, "Successfully added customer account.", 200);
            return;
        }
        sendResponse(exchange, "Method not allowed", 405);
    }
  /*  private JSONObject createJsonResponse(HttpExchange exchange) {
        InputStream stream = exchange.getRequestBody();
        BufferedReader streamReader = null;
        StringBuilder responseStrBuilder = null;

        try {
            streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            responseStrBuilder = new StringBuilder();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String inputStr;
        try {
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(responseStrBuilder.toString());
    }

    private void sendResponse(HttpExchange exchange, String response, int code) {
        try {
            exchange.sendResponseHeaders(code, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}

