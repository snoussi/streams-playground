package com.redhat.wallet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Map;


@ApplicationScoped
public class PaymentsStream {

    // Deserializer for message keys.
    private final Serde<String> keySerde = Serdes.String();

    // Serializer for message values
    private final Serde<Integer> valueSerde = Serdes.Integer();

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Integer> stream = builder.stream("payments", Consumed.with(keySerde, valueSerde));

        stream.foreach((key, amount) -> System.out.println("Received payment: " + amount));


        KStream<String, Integer> filteredStream  = stream.filter((key, amount) -> amount > 0);

        Map<String, KStream<String, Integer>> branches = filteredStream.split(Named.as("payments-"))
                .branch(
                        (key, amount)-> (amount >= 1000 && amount <= 10000),
                        Branched.withConsumer(ks -> ks.to("large-payments", Produced.with(keySerde, valueSerde)),"large"))
                .branch(
                        (key, amount)-> amount > 10000,
                        Branched.withConsumer(ks -> ks.to("very-large-payments", Produced.with(keySerde, valueSerde)),"very-large"))
                .defaultBranch(
                        Branched.as("small"));

        branches.get("payments-small").to("small-payments", Produced.with(keySerde, valueSerde));

//        KTable<String, Integer> lastLargePaymentKTable = builder.table(
//                "large-payments",
//                Consumed.with(keySerde,valueSerde)
//        );
//
//        lastLargePaymentKTable.toStream().peek((key, amount) -> System.out.println("last large payment: " + amount + " by "+ key));
//
//        GlobalKTable<String, Integer> lastVeryLargePaymentGlobalKTable = builder.globalTable(
//                "very-large-payments",
//                Consumed.with(keySerde,valueSerde)
//        );

        return builder.build();

    }

}
