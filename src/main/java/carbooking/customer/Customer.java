package carbooking.customer;

import carbooking.utils.Location;

/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public interface Customer {

    public double getBalance();

    public void setBalance(float balance);

    public String getId();

    public Location locate();

}
