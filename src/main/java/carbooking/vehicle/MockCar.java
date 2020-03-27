package carbooking.vehicle;

import carbooking.utils.Location;

import java.util.UUID;

/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public class MockCar implements Vehicle {
    private Boolean isReserved;
    private Boolean isRented;
    private Location location;
    private final double rentalPrice;
    private String id;

    public MockCar(double rentalPrice) {
        this.isReserved = false;
        this.isRented = false;
        this.rentalPrice = rentalPrice;
        this.id = UUID.randomUUID().toString();
    }

    public Boolean getIsReserved() {
        return this.isReserved;
    }

    public void setIsReserved(boolean bool) {
        this.isReserved = bool;
    }

    public Boolean getIsRented() {
        return this.isRented;
    }

    public void setIsRented(boolean bool) {
        this.isRented = bool;
    }

    public Location locate() {
        return new Location(0,0);
    }

    public double getRentalPrice() {
        return this.rentalPrice;
    }

    public String getId() {
        return this.id;
    }
}
