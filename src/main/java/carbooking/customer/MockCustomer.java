package carbooking.customer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.UUID;

/**
 * Created by Daniel Handloser on 26.03.2020.
 */
public class MockCustomer implements Customer {
    private String id;
    private String email;
    private String password;
    private String name;
    private double balance;

    public MockCustomer(String email, String password, String name) throws AddressException {
        InternetAddress emailAddr = new InternetAddress(email);
        emailAddr.validate();
        this.email = email;
        this.password = password;
        this.name = name;
        this.balance = 0.0;
        this.id = UUID.randomUUID().toString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getID() {
        return this.id;
    }

}
