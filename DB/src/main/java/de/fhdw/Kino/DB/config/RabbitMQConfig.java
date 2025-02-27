package de.fhdw.Kino.DB.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Name des Fanout-Exchanges
    public static final String COMMAND_FANOUT_EXCHANGE = "command.fanout.exchange";

    // Name der Queue für das Stats-Modul
    public static final String COMMAND_STATS_QUEUE = "command.stats.queue";

    // Name der Queue für die DB-Verarbeitung
    public static final String COMMAND_DB_QUEUE = "command.db.queue";

    // Bean für den Fanout-Exchange
    @Bean
    public FanoutExchange commandFanoutExchange() {
        return new FanoutExchange(COMMAND_FANOUT_EXCHANGE);
    }

    // Bean für die DB-Queue
    @Bean
    public Queue commandDbQueue() {
        return new Queue(COMMAND_DB_QUEUE, true); // durable Queue
    }

    // Binding zwischen Fanout-Exchange und DB-Queue
    @Bean
    public Binding dbBinding() {
        return BindingBuilder.bind(commandDbQueue())
                .to(commandFanoutExchange());
    }

    // Bean für die Stats-Queue
    @Bean
    public Queue commandStatsQueue() {
        return new Queue(COMMAND_STATS_QUEUE, true); // durable Queue
    }


    // Binding zwischen Fanout-Exchange und Stats-Queue
    @Bean
    public Binding statsBinding() {
        return BindingBuilder.bind(commandStatsQueue())
                .to(commandFanoutExchange());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        return new Jackson2JsonMessageConverter(objectMapper);
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        return rabbitTemplate;
    }

}
