package de.fhdw.Kino.Stats.listener;

import de.fhdw.Kino.Lib.dto.CrudRequest;
import de.fhdw.Kino.Stats.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrudListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.CRUD_STATS_QUEUE)
    public void logCrudRequest(CrudRequest request) {

        log.info("Stats-Modul: CRUD-Request empfangen - " + request.toString());

    }


}