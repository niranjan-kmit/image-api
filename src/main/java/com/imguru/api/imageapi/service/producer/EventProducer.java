package com.imguru.api.imageapi.service.producer;

import com.imguru.api.imageapi.service.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventProducer {
    private static final String TOPIC = "profile_topic";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publishProfileEventToTopic(String message) {
        log.info(String.format("user profile event :{} published to topic :{}", message,TOPIC));
        this.kafkaTemplate.send(TOPIC, message);
    }
}
