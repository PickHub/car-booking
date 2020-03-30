# car-booking

REST API for a car booking system.

## Requirements
* Java 11
* Maven

## Getting Started 
Start by importing the maven dependencies and run `src/main/java/Main.java`. 

The HTTP server is running on http://localhost:8000/.

You will need to create an account with `POST /customer` before you can access the rest of the rental functionality.

### Endpoints
Users can query two different endpoints `customer` and `rent`. 

##### Create customer account
* Request: `POST /customer`
    * Body
        ```
        {
          u : {
            email : [string],
            name : [string],
            password : [alphanumeric]
          }
        }
        ```

##### Rent a car
This endpoint enables you to find cars in your surrounding, block cars, start the rental of a car, and stop renting. Authenticate with the credentials you sent to the `/customer` request. For demonstration purposes the project is initialized with five cars around longitude and latitude 0.
* Request: `GET /rent/available:lattitude:longitude`
    * returns all available vehicles in a 1km radius around your location (defined by latitude and longitude)
* Request: `GET /rent/block:car_id`
    * blocks the car with car_id
* Request: `GET /rent/start:car_id`
    * starts the rental of the car with car_id. Rental fees will be subtracted from you balance.
* Request: `GET /rent/stop:car_id`
    * Stops the rental

 

