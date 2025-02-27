package de.fhdw.Kino.Stats.listener;

import de.fhdw.Kino.Lib.dto.CommandRequest;
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
public class CommandListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.COMMAND_STATS_QUEUE)
    public void logCommandRequest(CommandRequest request) {

        log.info("Stats-Modul: Command-Request empfangen - " + request.toString());

    }


}