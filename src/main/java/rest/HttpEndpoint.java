package rest;

import java.io.*;
import java.net.InetSocketAddress;

import carbooking.database.CustomerDatabase;
import carbooking.database.VehicleDatabase;
import com.sun.net.httpserver.*;
import rest.handler.CustomerAuthenticator;
import rest.handler.CustomerHandler;
import rest.handler.RentalHandler;

public class HttpEndpoint {
    private CustomerDatabase customerData;
    private VehicleDatabase vehicleData;
    private HttpServer server;
    private int port;

    public HttpEndpoint(CustomerDatabase customerData, VehicleDatabase vehicleData, int port) {
        this.customerData = customerData;
        this.vehicleData = vehicleData;
        this.port = port;
        this.server = null;
    }

    public void startHttpServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/customer", new CustomerHandler(customerData));
        HttpContext rentContext = server.createContext("/rent", new RentalHandler(customerData, vehicleData));
        rentContext.setAuthenticator(new CustomerAuthenticator("rent", customerData));
        server.setExecutor(null);
        server.start();
    }

    public void stopHttpServer() {
        server.removeContext("/customer");
        server.removeContext("/rent");
        server.stop(0);
    }



}
