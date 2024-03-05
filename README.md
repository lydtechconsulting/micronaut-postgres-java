# Micronaut & Java Demo with a Postgres Database

Demo Micronaut application written in Java exposing a REST API to enable performing of CRUD operations on an entity that is persisted to Postgres.

<div style="text-align:center"><img src="micronaut-postgres.png" /></div>
<p style="text-align: center;"><I>Figure 1: Micronaut application with Postgres</I></p>

The companion project, with the application written in Kotlin instead of Java, is available here:
https://github.com/lydtechconsulting/micronaut-postgres-kotlin

## Running The Demo

The project requires Java 21 to build.

Start the Postgres Docker container:
```
docker-compose up -d
```

Build and test the Micronaut application, and then run, with:
```
./gradlew clean test
./gradlew run
```

In a terminal window use curl request to create (which returns a location header), retrieve, update and delete an item:
```
curl -i -X POST localhost:9001/v1/items -H "Content-Type: application/json" -d '{"name": "test-item"}'
curl -i -X GET localhost:9001/v1/items/653d06f08faa89580090466e
curl -i -X PUT localhost:9001/v1/items/653d06f08faa89580090466e -H "Content-Type: application/json" -d '{"name": "test-item-update"}'
curl -i -X DELETE localhost:9001/v1/items/653d06f08faa89580090466e
```

## Component Tests

The component tests bring up the application and Postgres in docker containers and call the application via its REST API to create, retrieve, update, and delete an item.

For more on the component tests see: https://github.com/lydtechconsulting/component-test-framework

First ensure the Project module paths are set to `Inherit project compile output path` to ensure the fat jar is built in `./build/libs`.  Then build the Micronaut application jar, followed by the Docker container:
```
./gradlew clean build
docker build -t ct/micronaut-postgres-java:latest .
```

Run component tests:
```
./gradlew componentTest --rerun-tasks
```

Run tests leaving containers up:
```
./gradlew componentTest --rerun-tasks -Dcontainers.stayup=true
```

Note that `--rerun-tasks` is required for subsequent runs when no change has happened between test runs.

## Docker Clean Up

Manual clean up (if left containers up):
```
docker rm -f $(docker ps -aq)
```
