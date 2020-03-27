package carbooking.vehicle;

import carbooking.utils.Location;

/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public interface Vehicle {

    public Boolean getIsReserved();

    public void setIsReserved(boolean bool);

    public Boolean getIsRented();

    public void setIsRented(boolean bool);

    public Location locate();

    public double getRentalPrice();

    public String getId();









}
