package org.islab.ongdv.kafkatest.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

    @KafkaListener(topics = "#{myTopic1.name}", groupId = "group1")
    public void consumeMyTopic1(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition){
        log.info("[Consume message]: {} from partition {}" , message, partition);
    }

    @KafkaListener(topics = "#{myTopic2.name}", groupId = "group1")
    public void consumeMyTopic2(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition){
        log.info("[Consume message]: {} from partition {}" , message, partition);
    }
}
