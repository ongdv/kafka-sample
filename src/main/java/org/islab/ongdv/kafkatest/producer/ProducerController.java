package org.islab.ongdv.kafkatest.producer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("kafka")
@AllArgsConstructor
@Slf4j
public class ProducerController {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NewTopic myTopic1;
    private final NewTopic myTopic2;

    @GetMapping("/publish/mytopic1")
    public String publishSpringTopic1() {
        String message = "publish message to my_topic_1" + UUID.randomUUID();

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(myTopic1.name(), message);
        future.whenComplete((result, error) -> {
            if(error == null){
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }else{
                log.info("Unable to send message=[" + message + "] due to : " + error.getMessage());
            }
        });

        return "done";

    }

    @GetMapping("/publish/mytopic2")
    public String publishSpringTopic2() {
        String message = "publish message to my_topic_1" + UUID.randomUUID();

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(myTopic2.name(), message);
        future.whenComplete((result, error) -> {
            if(error == null){
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }else{
                log.info("Unable to send message=[" + message + "] due to : " + error.getMessage());
            }
        });

        return "done";

    }
}