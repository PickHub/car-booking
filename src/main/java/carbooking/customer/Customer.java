package carbooking.customer;

import carbooking.utils.Location;

/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public interface Customer {

    double getBalance();

    void setBalance(float balance);

    String getName();

    String getEmail();

    String getPassword();

    String getId();

}
