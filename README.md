# Micronaut & Java Demo with a Postgres Database

Demo Micronaut application written in Java exposing a REST API to enable performing of CRUD operations on an entity that is persisted to Postgres.

The application stores entities in memory.

<div style="text-align:center"><img src="micronaut-postgres.png" /></div>
<p style="text-align: center;"><I>Figure 1: Micronaut application with Postgres</I></p>

## Running The Demo

The project requires Java 21 to build.

Build and test the Micronaut application:
```
./gradlew test
```

Optionally build a native executable with GraalVM (version 21) - [install instructions](https://www.graalvm.org/latest/docs/getting-started/), then:
```
./gradlew nativeCompile
```

Run the Micronaut application:
```
./gradlew run
```

In a terminal window use curl to submit a POST REST request to the application to create an item:
```
curl -i -X POST localhost:9001/v1/items -H "Content-Type: application/json" -d '{"name": "test-item"}'
```

A response should be returned with the 201 CREATED status code and the new item id in the Location header:
```
HTTP/1.1 201 
Location: 653d06f08faa89580090466e
```

The Micronaut application should log the successful item persistence:
```
Item created with id: 653d06f08faa89580090466e
```

Get the item that has been created using curl:
```
curl -i -X GET localhost:9001/v1/items/653d06f08faa89580090466e
```

A response should be returned with the 200 SUCCESS status code and the item in the response body:
```
HTTP/1.1 200 
Content-Type: application/json

{"id":"653d06f08faa89580090466e","name":"test-item"}
```

In a terminal window use curl to submit a PUT REST request to the application to update the item:
```
curl -i -X PUT localhost:9001/v1/items/653d06f08faa89580090466e -H "Content-Type: application/json" -d '{"name": "test-item-update"}'
```

A response should be returned with the 204 NO CONTENT status code:
```
HTTP/1.1 204 
```

The Micronaut application should log the successful update of the item:
```
Item updated with id: 653d06f08faa89580090466e - name: test-item-update
```

Delete the item using curl:
```
curl -i -X DELETE localhost:9001/v1/items/653d06f08faa89580090466e
```

The Micronaut application should log the successful deletion of the item:
```
Deleted item with id: 653d06f08faa89580090466e
```
