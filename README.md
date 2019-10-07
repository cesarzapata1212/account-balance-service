
# Account Balance Service

Account Balance Service is a Web Service built in Java 8 used for transferring money between two accounts.

## Requirements
Java Runtime Environment 8 for running.
Maven 3 to build from source.

## Installation

Building a jar with dependencies
```bash
mvn clean compile assembly:single
```

## Usage
If you do not have the build tools, a pre-built jar with dependencies can be found in the **dist** folder.

You can start the server running the following command. 
> NOTE: you will need the Java Runtime Environment to execute the application)

```bash
java -jar dist/account-balance-service-1.0-SNAPSHOT-jar-with-dependencies.jar
```

When the application starts, it will print the port it is listening to. (default 7000)
You should be able to make requests to [http://localhost:7000/](http://localhost:7000/) once the application is running.

The application will automatically start an embedded in memory PostgreSQL database.

Seed data is automatically loaded during startup that can be used for testing. See the script: [V2__initial_data.sql](https://github.com/cesarzapata1212/account-balance-service/blob/master/src/main/resources/database/V2__initial_data.sql). 


**POST Request to /balance-transfer**

```bash
curl -X POST \
  http://localhost:7000/balance-transfer \
  -H 'accept: application/json' \
  -H 'content-type: application/json' \
  -d '{
	"sourceAccount" : {
		"accountNumber":"11111111",
		"sortCode": "111111"
	},
	"destinationAccount" : {
		"accountNumber":"22222222",
		"sortCode": "222222"
	},
	"amount": "1000"
}'
```

**Successful response**

```json
{
    "destinationAccount": {
        "sortCode": "222222",
        "accountNumber": "22222222",
        "balance": {
            "value": 2000
        }
    },
    "sourceAccount": {
        "sortCode": "111111",
        "accountNumber": "11111111",
        "balance": {
            "value": 0
        }
    }
}
```

**Error Response**
```json
{
    "status": 422,
    "message": "INSUFFICIENT_BALANCE"
}
```