package de.fhdw.Kino.App;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserSenderService {

    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public UserSenderService(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUser(UserImpl user) {
        rabbitTemplate.convertAndSend(RabbitMQAppConfig.EXCHANGE_NAME, RabbitMQAppConfig.ROUTING_KEY, user);
        System.out.println("Gesendet: " + user);
    }
}