package rest.handler;

import carbooking.database.CustomerDatabase;
import carbooking.database.IdNotFoundException;
import carbooking.database.VehicleDatabase;
import carbooking.vehicle.Vehicle;
import carbooking.vehicle.VehicleAlreadyRentedException;
import carbooking.vehicle.VehicleAlreadyReservedException;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Daniel Handloser on 28.03.2020.
 */
public class RentalHandler extends ParentHandler {

    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String AVAILABLE = "available";
    public static final String RESPONSE_SUCCESS = "Successfully added customer account.";
    private static final int SEARCH_RADIUS = 1000;
    public static final String EMAIL_RESPONSE = "Conflict. This email address is already in use. Cannot create account.";
    public static final int HTTP_STATUS_CONFLICT = 409;
    public static final String RESPONSE_PARAMETER = "Missing required parameter: name, e-mail and/or password.";
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_OK = 200;
    public static final String RESPONSE_NOT_ALLOWED = "Method not allowed";
    public static final int HTTP_STATUS_NOT_ALLOWED = 405;
    public static final String RESPONSE_MISSING_LONG_LAT = "Missing required arguments latitude/longitude.";

    private CustomerDatabase customerData;
    private VehicleDatabase vehicleData;

    public RentalHandler(CustomerDatabase customerData, VehicleDatabase vehicleData) {
        this.customerData = customerData;
        this.vehicleData = vehicleData;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String[] requestUri = String.valueOf(exchange.getRequestURI()).split("/");

        if ("GET".equals(requestMethod)) {
            if (requestUri.length == 2) {
                String rentRequestType = requestUri[1];
                JSONObject jsonBody = createJsonResponse(exchange);

                if(rentRequestType.equals(AVAILABLE)) {
                    if (jsonBody.has(LATITUDE) && jsonBody.has(LONGITUDE)) {
                        double latitude = jsonBody.getDouble(LATITUDE);
                        double longitude = jsonBody.getDouble(LONGITUDE);
                        JSONArray response = vehicleData.availableVehicles(latitude, longitude, SEARCH_RADIUS);
                        sendResponse(exchange, response.toString(), HTTP_STATUS_OK);
                        return;
                    } else {
                        sendResponse(exchange, RESPONSE_MISSING_LONG_LAT, HTTP_STATUS_BAD_REQUEST);
                    }

                }
                else if(rentRequestType.equals("block")) {
                    if(jsonBody.has("car_id")) {
                        try {
                            vehicleData.blockVehicle( String.valueOf(jsonBody.get("car_id")));
                            sendResponse(exchange, "Blocked successful", HTTP_STATUS_OK);
                            return;
                        } catch (IdNotFoundException e) {
                            sendResponse(exchange, "Invalid id", HTTP_STATUS_CONFLICT);
                            return;
                        } catch (VehicleAlreadyReservedException e) {
                            sendResponse(exchange, "Vehicle has been reserved by someone else in the meantime", HTTP_STATUS_CONFLICT);
                            return;
                        }
                    }
                }
                else if(rentRequestType.equals("start")) {
                    if(jsonBody.has("car_id")) {
                        try {
                            String username = String.valueOf(exchange.getRequestHeaders().get("username"));
                            vehicleData.rentVehicle(String.valueOf(jsonBody.get("car_id")), username);
                            sendResponse(exchange, "Blocked successful", HTTP_STATUS_OK);
                            return;
                        } catch (IdNotFoundException e) {
                            sendResponse(exchange, "Invalid id", HTTP_STATUS_CONFLICT);
                            return;
                        } catch (VehicleAlreadyRentedException e) {
                            sendResponse(exchange, "Vehicle has been rented by someone else in the meantime", HTTP_STATUS_CONFLICT);
                            return;
                        }
                    }
                }
                else if(rentRequestType.equals("stop")) {
                    if(jsonBody.has("car_id")) {
                        String requestingUser = String.valueOf(exchange.getRequestHeaders().get("username"));

                        try {
                            Vehicle vehicle = vehicleData.getVehicleById(String.valueOf(jsonBody.get("car_id")));
                            String userAssociatedToVehicle = vehicle.getCustomerUsername();

                            if (userAssociatedToVehicle.equals(requestingUser)) {
                                double rentalCharge = vehicleData.stopVehicleRent(vehicle.getId());
                                customerData.chargeCustomer(requestingUser, rentalCharge);
                                sendResponse(exchange, "Rental terminated successfully", HTTP_STATUS_OK);

                            } else {
                                sendResponse(exchange, "This vehicle is associated with a different user.", HTTP_STATUS_CONFLICT);
                            }

                        } catch (IdNotFoundException e) {
                            sendResponse(exchange, "We do not have a corresponding rental on record.", HTTP_STATUS_CONFLICT);
                        }
                    } else {
                        sendResponse(exchange, "Please specify car_id.", HTTP_STATUS_BAD_REQUEST);
                        return;
                    }
                }

                if (jsonBody.has("email") && jsonBody.has("password") && jsonBody.has("name")) {
                    String email = String.valueOf(jsonBody.get("email"));
                    String password = String.valueOf(jsonBody.get("password"));
                    String name = String.valueOf(jsonBody.get("name"));

                    if (!customerData.addCustomer(email, password, name)) {
                        sendResponse(exchange, EMAIL_RESPONSE, HTTP_STATUS_CONFLICT);
                        return;
                    }


                } else {
                    sendResponse(exchange, RESPONSE_PARAMETER, HTTP_STATUS_BAD_REQUEST);
                    return;
                }
                sendResponse(exchange, RESPONSE_SUCCESS, HTTP_STATUS_OK);
                return;
            }
        }
        sendResponse(exchange, RESPONSE_NOT_ALLOWED, HTTP_STATUS_NOT_ALLOWED);
    }
}
