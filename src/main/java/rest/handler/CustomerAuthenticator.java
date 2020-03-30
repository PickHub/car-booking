package rest.handler;

import carbooking.database.CustomerDatabase;
import com.sun.net.httpserver.BasicAuthenticator;

import java.util.NoSuchElementException;

/**
 * Created by Daniel Handloser on 28.03.2020.
 */
public class CustomerAuthenticator extends BasicAuthenticator {
    private CustomerDatabase customerData;

    public CustomerAuthenticator(String realm, CustomerDatabase customerData) {
        super(realm);
        this.customerData = customerData;
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        try{
            return customerData.checkPassword(username, password);
        }
        catch (NoSuchElementException e) {
            return false;
        }
    }
}
