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



## Open Api Code

openapi: 3.0.0
info:
  title: Inventory Api
  version: 1.0.0
servers:
  - url: http://localhost:8081
paths:
  /inventory/items:
    get:
      tags:
        - General
      summary: Browse all
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}
  /inventory/add:
    post:
      tags:
        - General
      summary: Add item
      requestBody:
        content:
          application/json:
            schema:
              type: object
              example:
                name: Mats
                amount: 10
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}
  /inventory/delete:
    post:
      tags:
        - General
      summary: Delete Item
      requestBody:
        content:
          application/json:
            schema:
              type: object
              example:
                name: Qats
                amount: 10
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}
  /inventory/Qats:
    get:
      tags:
        - General
      summary: Find one
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}
            
            
openapi: 3.0.0
info:
  title: Price Api
  version: 1.0.0
servers:
  - url: http://localhost:8080
paths:
  /price/browse/all:
    get:
      tags:
        - General
      summary: Get all items
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}
  /price/add:
    post:
      tags:
        - General
      summary: Add item
      requestBody:
        content:
          application/json:
            schema:
              type: object
              example:
                name: Mats
                price: 10
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}
  /price/Mats:
    get:
      tags:
        - General
      summary: Get item price
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}
  /price/delete:
    post:
      tags:
        - General
      summary: Delete item
      requestBody:
        content:
          application/json:
            schema:
              type: object
              example:
                name: Qats
                amount: 10
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}
  /price/browse:
    get:
      tags:
        - General
      summary: Browse x page
      parameters:
        - name: page
          in: query
          schema:
            type: integer
          example: '0'
        - name: max
          in: query
          schema:
            type: integer
          example: '5'
      responses:
        '200':
          description: Successful response
          content:
            application/json: {}   


