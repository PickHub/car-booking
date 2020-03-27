package carbooking.database;

import carbooking.customer.Customer;

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


    public void removeCustomer(String id) {

    }
}
