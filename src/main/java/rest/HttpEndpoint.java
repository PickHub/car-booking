package rest;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
import java.io.*;
import java.net.InetSocketAddress;

import carbooking.database.CustomerDatabase;
import carbooking.database.SimpleCustomerData;
import carbooking.database.SimpleVehicleData;
import carbooking.database.VehicleDatabase;
import com.sun.net.httpserver.*;
import rest.handler.CustomerAuthenticator;
import rest.handler.CustomerHandler;
import rest.handler.RentalHandler;

public class HttpEndpoint {
    private CustomerDatabase customerData;
    private VehicleDatabase vehicleData;
    private int port;

    public HttpEndpoint(CustomerDatabase customerData, VehicleDatabase vehicleData, int port) {
        this.customerData = customerData;
        this.vehicleData = vehicleData;
        this.port = port;
    }
    public static void main(String[] args) throws Exception {
        //TODO port in cmd
        int port = 8000;
        CustomerDatabase customerData = new SimpleCustomerData();
        VehicleDatabase vehicleData = new SimpleVehicleData();
        HttpEndpoint httpServer = new HttpEndpoint(customerData, vehicleData, port);
        httpServer.startHttpServer();

    }

    public void startHttpServer() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/customer", new CustomerHandler(customerData));
        HttpContext rentContext = server.createContext("/rent", new RentalHandler(customerData));
        rentContext.setAuthenticator(new CustomerAuthenticator("rent", customerData));
        server.setExecutor(null); // creates a default executor
        server.start();
    }



}
