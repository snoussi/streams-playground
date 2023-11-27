# streams-interaction project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script

docker-compose -f docker-compose-local.yaml up

./mvnw compile quarkus:dev
```

## Running Kafka Producers and Consumers

The Kafka producer and consumer are JBang Java main classes


You can run them in separate shells:
```shell script

jbang DemoProducer.java

jbang DemoConsumer.java
```

Alternatively, just include the producers and consumers in a Maven project and add the dependencies

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

