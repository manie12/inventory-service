package io.inventory_service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    @Bean
    ProducerFactory<String, Object> producerFactory(
            @Value("${kafka.bootstrap-servers}") String bootstrap,
            @Value("${kafka.producer.acks:all}") String acks,
            @Value("${kafka.producer.retries:3}") Integer retries,
            @Value("${kafka.producer.batch-size:16384}") Integer batchSize,
            @Value("${kafka.producer.linger-ms:1}") Integer lingerMs,
            @Value("${kafka.producer.buffer-memory:33554432}") Long bufferMemory,
            @Value("${kafka.producer.compression-type:snappy}") String compressionType,
            @Value("${kafka.producer.enable-idempotence:true}") Boolean enableIdempotence
    ) {
        Map<String, Object> configProps = new HashMap<>();

        // Connection properties
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);

        // Serialization properties
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);

        // Reliability properties
        configProps.put(ProducerConfig.ACKS_CONFIG, acks); // "all" ensures the leader and replicas receive the message
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence); // Prevents duplicate messages

        // Performance tuning
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize); // Size of batches to be sent
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs); // Small delay to batch more records
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory); // Total memory for the producer to buffer records
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType); // Compress messages for better throughput

        // Error handling
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // Ensures ordering with retries
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000); // 30 seconds request timeout
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000); // 2 minutes delivery timeout

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(pf);
        kafkaTemplate.setDefaultTopic("${kafka.default-topic:default-topic}");
        return kafkaTemplate;
    }

    @Bean
    ProducerListener<String, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }
}