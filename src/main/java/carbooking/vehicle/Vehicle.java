package carbooking.vehicle;

import carbooking.utils.Location;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public interface Vehicle {

    public Boolean getIsReserved();

    public void setIsReservedAtomic(boolean bool) throws VehicleAlreadyReservedException;

    public Boolean getIsRented();

    public void startRental(String customerUsername) throws VehicleAlreadyRentedException;

    public double stopRental();

    public Location locate();

    public double getRentalPrice();

    public String getId();

    public JSONObject toJson();

    public String getCustomerUsername();









}
