package carbooking.vehicle;

import carbooking.utils.Location;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public class MockCar implements Vehicle {
    private final double rentalPricePerMinute;
    private AtomicBoolean isReserved;
    private AtomicBoolean isRented;
    private long startOfRent;
    private String id;
    private String customerUsername;

    public MockCar(double rentalPricePerMinute) {
        this.isReserved = new AtomicBoolean(false);
        this.isRented = new AtomicBoolean(false);
        this.rentalPricePerMinute = rentalPricePerMinute;
        this.startOfRent = 0;
        this.customerUsername = "";
        this.id = UUID.randomUUID().toString();
    }

    public Boolean getIsReserved() {
        return this.isReserved.get();
    }

    public void setIsReservedAtomic(boolean bool) throws VehicleAlreadyReservedException {
        if (bool) {
            Boolean expectedValue = this.isReserved.compareAndSet(false, true);
            if (!expectedValue) {
                throw new VehicleAlreadyReservedException();
            }
        } else {
            this.isReserved.set(false);
        }


    }

    public Boolean getIsRented() {
        return this.isRented.get();
    }

    public void startRental(String customerUsername) throws VehicleAlreadyRentedException {
        Boolean expectedValue = this.isRented.compareAndSet(false, true);
        if (!expectedValue) {
            throw new VehicleAlreadyRentedException();
        }
        this.customerUsername = customerUsername;
        startOfRent = System.currentTimeMillis();
    }

    public double stopRental() {
        long rentedMinutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - startOfRent);
        this.isReserved.set(false);

        return rentedMinutes * rentalPricePerMinute;
    }

    public Location locate() {
        return new Location(0, 0);
    }

    public double getRentalPrice() {
        return this.rentalPricePerMinute;
    }

    public String getId() {
        return this.id;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("car_id", id);
        json.put("price/min", rentalPricePerMinute);
        Location location = locate();
        json.put("latitude", location.getLatitude());
        json.put("longitude", location.getLongitude());
        return json;
    }

    public String getCustomerUsername() {
        return this.customerUsername;
    }

}
