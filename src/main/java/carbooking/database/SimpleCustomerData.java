package carbooking.database;

import carbooking.customer.Customer;
import carbooking.customer.MockCustomer;

import javax.mail.internet.AddressException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Daniel Handloser on 27.03.2020.
 */
public class SimpleCustomerData implements CustomerDatabase {

    private List<Customer> customerData;

    public SimpleCustomerData() {
        customerData = new LinkedList<Customer>();
    }

    public List<Customer> getAllCustomers() {
        return customerData;
    }

    public Customer getCustomerById(String id) {
        for(int i = 0; i < customerData.size(); i++) {
            Customer currCustomer = customerData.get(i);
            if (currCustomer.getId() == id) {
                return currCustomer;
            }
        }

        throw new NoSuchElementException();
    }

    public void chargeCustomer(String requestUsername, double rentalCharge) {
        Customer customer = getCustomerByUsername(requestUsername);
        customer.setBalance((float) (customer.getBalance() - rentalCharge));
    }

    private Customer getCustomerByUsername(String username) throws NoSuchElementException {
        for(Customer customer : customerData) {
            if (customer.getName().equals(username)) {
                return customer;
            }
        }

        throw new NoSuchElementException();
    }


    public void addCustomer(String email, String password, String name) throws AddressException {
        for(Customer existingCustomer : customerData) {
            if (existingCustomer.getEmail().equals(email)) {
                throw new AddressException();
            }
        }

        Customer newCustomer = new MockCustomer(email, password, name);
        customerData.add(newCustomer);
    }

    public Boolean checkPassword(String email, String password) {
        for (Customer existingCustomer : customerData) {
            if (existingCustomer.getEmail().equals(email)) {
                if (existingCustomer.getPassword().equals(password)) {
                    return true;
                } else {
                    return false;
                }

            }
        }
        return false;
    }

    public void removeCustomer(String id) {

    }
}
