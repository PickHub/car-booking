package carbooking.database;

import carbooking.customer.Customer;

import java.util.List;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public interface CustomerDatabase {
    public List<Customer> getAllCustomers();

    public Customer getCustomerById(String id);

    public void removeCustomer(String id);

    public Boolean addCustomer(String email, String password, String name);

    public Boolean checkPassword(String email, String password);

    public void chargeCustomer(String requestingUser, double rentalCharge);
}
