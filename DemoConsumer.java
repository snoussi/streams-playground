//usr/bin/env jbang "$0" "$@" ; exit $$
//DEPS org.apache.kafka:kafka-clients:3.6.0
 

 
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.Consumer;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.time.Duration;
import java.util.Collections;

public class  DemoConsumer {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String groupId = "payment-consumer-group";
        String topic = "large-payments";

        // Consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());

        // Create Kafka Consumer
        Consumer<String, Integer> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic
        consumer.subscribe(Collections.singletonList(topic));

        // Poll for new messages
        while (true) {
            ConsumerRecords<String, Integer> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, Integer> record : records) {
                String key = record.key();
                Integer value = record.value();
                System.out.println("Key: " + key + ", Value: " + value);
            }
        }
    }
}
