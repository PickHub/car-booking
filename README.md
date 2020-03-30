# car-booking

REST API for a car booking system.

## Requirements
* Java 11
* Maven

## Getting Started 
Running...

The HTTP server is running on http://localhost:8000/.

### Endpoints
Users can query two different endpoints `customer` and `rent`. 

##### Create customer account
* Request: `POST /customer:email:password:name`

##### Rent a car
This endpoint enables you to find cars in your surrounding, block cars, start the rental of a car, and stop renting. Authenticate with the credentials you sent to the `/customer` request.
* Request: `GET /rent/available:lattitude:longitude`
    * returns all available vehicles in a 1km radius around your location (defined by lattitude and longitude)
* Request: `GET /rent/block:car_id`
    * blocks the car with car_id
* Request: `GET /rent/start:car_id`
    * starts the rental of the car with car_id. Rental fees will be subtracted from you balance.
* Request: `GET /rent/stop:car_id`
    * Stops the rental

 

