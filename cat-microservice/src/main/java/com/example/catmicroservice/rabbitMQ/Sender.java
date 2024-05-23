package com.example.catmicroservice.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Scheduled
    public void send(String message) {
        String CustomMessage = "This is a message from sender" + message;

        rabbitTemplate.convertAndSend(RabbitMQConfig.CAT_EXCHANGE, RabbitMQConfig.ROUTING_KEY_SEARCHING, CustomMessage);
        System.out.println("Send msg to consumer= " + CustomMessage + " ");
    }
}
