package carbooking.database;

/**
 * Created by Daniel Handloser on 29.03.2020.
 */
public class IdNotFoundException extends Exception {
    public IdNotFoundException() {}

    public IdNotFoundException(String message) {
        super(message);
    }
}
