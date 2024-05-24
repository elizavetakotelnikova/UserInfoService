package com.example.outermicroservice.rabbitMQ;

import com.example.jpa.RabbitMQConfig;
import com.example.outermicroservice.cat.dto.CatInfoDto;
import com.example.outermicroservice.owner.dto.OwnerInfoDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendOwner(OwnerInfoDto ownerInfoDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_CREATING_OWNER, ownerInfoDto);
    }
    /*public void sendCat(CatInfoDto catInfoDto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.WEB_EXCHANGE, RabbitMQConfig.ROUTING_KEY_UPDATING_OWNER, catInfoDto);
    }*/
}
