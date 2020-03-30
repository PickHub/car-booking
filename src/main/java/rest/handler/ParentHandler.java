package rest.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Daniel Handloser on 28.03.2020.
 */
public abstract class ParentHandler implements HttpHandler {
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    public abstract void handle(HttpExchange exchange) throws IOException;

    JSONObject createJsonResponse(HttpExchange exchange) {
        System.out.println("Starts");
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
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("7");
        return new JSONObject(responseStrBuilder.toString());
    }

    String getStringFromJson(JSONObject json, String key) {
        return String.valueOf(json.get(key));
    }

    void sendResponse(HttpExchange exchange, String response, int code) {
        Headers headers = exchange.getResponseHeaders();
        //headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        final byte[] rawResponseBody = response.getBytes(CHARSET);
        try {
            exchange.sendResponseHeaders(code, rawResponseBody.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream os = exchange.getResponseBody();
        try {
            os.write(rawResponseBody);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
