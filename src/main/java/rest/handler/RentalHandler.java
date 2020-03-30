package rest.handler;

import carbooking.database.CustomerDatabase;
import carbooking.database.IdNotFoundException;
import carbooking.database.VehicleDatabase;
import carbooking.vehicle.Vehicle;
import carbooking.vehicle.VehicleAlreadyRentedException;
import carbooking.vehicle.VehicleAlreadyReservedException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by Daniel Handloser on 28.03.2020.
 */
public class RentalHandler extends ParentHandler {

    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String AVAILABLE = "available";
    private static final String RESPONSE_SUCCESS = "Successfully added customer account.";
    private static final String RESPONSE_NOT_ALLOWED = "Method not allowed";
    private static final String RESPONSE_MISSING_LONG_LAT = "Missing required arguments latitude/longitude.";
    private static final String SUCCESS_MESSAGE = "OK";
    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_CONFLICT_MESSAGE = "This vehicle is associated with a different user.";
    private static final String RENTAL_CONFLICT_MESSAGE = "We do not have a corresponding rental on record.";
    private static final String CAR_ID_MISSING_MESSAGE = "Please specify car_id.";
    private static final String BLOCK = "block";
    private static final String CAR_ID = "car_id";
    private static final String INVALID_ID_MESSAGE = "Invalid id";
    private static final String VEHICLE_BOOKED_MESSAGE = "Vehicle has been reserved by someone else in the meantime";
    private static final String START = "start";
    private static final String STOP = "stop";
    private static final int SEARCH_RADIUS = 1000;
    private static final int HTTP_STATUS_CONFLICT = 409;
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_NOT_ALLOWED = 405;

    private CustomerDatabase customerData;
    private VehicleDatabase vehicleData;

    public RentalHandler(CustomerDatabase customerData, VehicleDatabase vehicleData) {
        this.customerData = customerData;
        this.vehicleData = vehicleData;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        String[] requestUri = String.valueOf(exchange.getRequestURI()).split("/");
        if ("GET".equals(requestMethod)) {
            if (requestUri.length == 3) {
                String rentRequestType = requestUri[2];
                Headers requestHeader = exchange.getRequestHeaders();
                if(rentRequestType.equals(AVAILABLE)) {
                    if (requestHeader.containsKey(LATITUDE) && requestHeader.containsKey(LONGITUDE)) {
                        double latitude = Double.parseDouble(requestHeader.get(LATITUDE).get(0));
                        double longitude = Double.parseDouble(requestHeader.get(LONGITUDE).get(0));
                        JSONArray response = vehicleData.availableVehicles(latitude, longitude, SEARCH_RADIUS);
                        sendResponse(exchange, response.toString(), HTTP_STATUS_OK);
                        return;
                    } else {
                        sendResponse(exchange, RESPONSE_MISSING_LONG_LAT, HTTP_STATUS_BAD_REQUEST);
                        return;
                    }

                }
                else if(rentRequestType.equals(BLOCK)) {
                    if(requestHeader.containsKey(CAR_ID)) {
                        try {
                            vehicleData.blockVehicle(requestHeader.get(CAR_ID).get(0));
                            sendResponse(exchange, SUCCESS_MESSAGE, HTTP_STATUS_OK);
                            return;
                        } catch (IdNotFoundException e) {
                            sendResponse(exchange, INVALID_ID_MESSAGE, HTTP_STATUS_CONFLICT);
                            return;
                        } catch (VehicleAlreadyReservedException e) {
                            sendResponse(exchange, VEHICLE_BOOKED_MESSAGE, HTTP_STATUS_CONFLICT);
                            return;
                        }
                    }
                }
                else if(rentRequestType.equals(START)) {
                    if(requestHeader.containsKey(CAR_ID)) {
                        try {
                            String authorizationHeader = exchange.getRequestHeaders().get(AUTHORIZATION).get(0);
                            String username = getUsernameFromBasicAuth(authorizationHeader);
                            vehicleData.rentVehicle(requestHeader.get(CAR_ID).get(0), username);
                            sendResponse(exchange, SUCCESS_MESSAGE, HTTP_STATUS_OK);
                            return;
                        } catch (IdNotFoundException e) {
                            sendResponse(exchange, INVALID_ID_MESSAGE, HTTP_STATUS_CONFLICT);
                            return;
                        } catch (VehicleAlreadyRentedException e) {
                            sendResponse(exchange, VEHICLE_BOOKED_MESSAGE, HTTP_STATUS_CONFLICT);
                            return;
                        }
                    }
                }
                else if(rentRequestType.equals(STOP)) {
                    if(requestHeader.containsKey(CAR_ID)) {
                        String authorizationHeader = exchange.getRequestHeaders().get(AUTHORIZATION).get(0);
                        String requestingUser = getUsernameFromBasicAuth(authorizationHeader);

                        try {
                            Vehicle vehicle = vehicleData.getVehicleById(String.valueOf(requestHeader.get(CAR_ID).get(0)));
                            String userAssociatedToVehicle = vehicle.getCustomerUsername();

                            if (userAssociatedToVehicle.equals(requestingUser)) {
                                double rentalCharge = vehicleData.stopVehicleRent(vehicle.getId());
                                customerData.chargeCustomer(requestingUser, rentalCharge);
                                sendResponse(exchange, SUCCESS_MESSAGE, HTTP_STATUS_OK);
                                return;

                            } else {
                                sendResponse(exchange, USER_CONFLICT_MESSAGE, HTTP_STATUS_CONFLICT);
                                return;
                            }

                        } catch (IdNotFoundException e) {
                            sendResponse(exchange, RENTAL_CONFLICT_MESSAGE, HTTP_STATUS_CONFLICT);
                            return;
                        }
                    } else {
                        sendResponse(exchange, CAR_ID_MISSING_MESSAGE, HTTP_STATUS_BAD_REQUEST);
                        return;
                    }
                }
                sendResponse(exchange, RESPONSE_SUCCESS, HTTP_STATUS_OK);
                return;
            }
        }
        sendResponse(exchange, RESPONSE_NOT_ALLOWED, HTTP_STATUS_NOT_ALLOWED);
    }

    private String getUsernameFromBasicAuth(String auth) {
        String base64Credentials = auth.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        return values[0];
    }
}
