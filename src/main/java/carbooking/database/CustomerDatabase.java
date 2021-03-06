package carbooking.database;

import carbooking.customer.Customer;

import javax.mail.internet.AddressException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public interface CustomerDatabase {

    void addCustomer(String email, String password, String name) throws AddressException;

    void chargeCustomer(String requestingUser, double rentalCharge);

    boolean checkPassword(String username, String password) throws NoSuchElementException;
}
