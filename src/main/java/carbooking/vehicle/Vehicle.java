package carbooking.vehicle;

import carbooking.utils.Location;
import org.json.JSONObject;

/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public interface Vehicle {

    Boolean getIsReserved();

    void setIsReservedAtomic(boolean bool) throws VehicleAlreadyReservedException;

    Boolean getIsRented();

    void startRental(String customerUsername) throws VehicleAlreadyRentedException;

    double stopRental();

    Location locate();

    double getRentalPrice();

    String getId();

    JSONObject toJson();

    String getCustomerUsername();









}
