//usr/bin/env jbang "$0" "$@" ; exit $$
//DEPS org.apache.kafka:kafka-clients:3.6.0

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.*;
 

import java.util.Properties;

public class DemoProducer {
 

     public static void main(String[] args) {
        System.out.println("I am a Kafka Producer");

        String bootstrapServers = "127.0.0.1:9092";

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());

        // create the producer
        KafkaProducer<String, Integer> producer = new KafkaProducer<>(properties);

        // create a producer record
        ProducerRecord<String, Integer> producerRecord = new ProducerRecord<>("payments", "payment-key", 10000);

        // send data - asynchronous
        producer.send(producerRecord);

        // flush data - synchronous
        producer.flush();
        // flush and close producer
        producer.close();
    }
}
