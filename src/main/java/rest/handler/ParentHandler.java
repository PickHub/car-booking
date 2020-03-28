package rest.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by Daniel Handloser on 28.03.2020.
 */
public abstract class ParentHandler implements HttpHandler {

    public abstract void handle(HttpExchange exchange) throws IOException;

    JSONObject createJsonResponse(HttpExchange exchange) {
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

    void sendResponse(HttpExchange exchange, String response, int code) {
        try {
            exchange.sendResponseHeaders(code, response.getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream os = exchange.getResponseBody();
        try {
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
