# Dumb Dumber Dumberer Dumbest


## Group Members
John O'Donnell: 18368983

Kealan McCormack: 18312236

Lukasz Filanowski: 18414616

Gerard Colman: 18327576

## Project Description
The Project is a representation of a trading spaceport. Ships can come in to the port 
to buy or sell goods to the central inventory via a market. Each transaction is logged into a database and 
receipts are sent to the ship captains. 

## Technologies Used
* REST API's with Springboot
* Actors
* NoSQL Databases: Redis and MongoDB
* Open API/Swagger
* Docker


## Run Instructions

1. Run "mvn clean" to clean project
2. Run "mvn package" to create project jar file for client and containers
3. Edit .env File to have desire configuration for container services
4. Run "docker-compose build" to build docker images
5. Run "docker-compose up" to run docker containers
6. Run Client by "java -Duser.language=en -Duser.country=IE -jar client/target/client-0.0.1.jar -m {money} --id {clientID}
7. Run "docker-compose down" to shut down docker containers

##Notes
    Configuration for Services is done through the .env


## Open Api Code

Open API yml code is provided for the Price, Inventory and market APIs 


