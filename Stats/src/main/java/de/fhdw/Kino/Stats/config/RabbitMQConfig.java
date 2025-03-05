package de.fhdw.Kino.Stats.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fhdw.Kino.Lib.command.CommandRequest;
import de.fhdw.Kino.Lib.command.CommandRequestDeserializer;
import de.fhdw.Kino.Lib.command.CommandResponse;
import de.fhdw.Kino.Lib.command.CommandResponseDeserializer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COMMAND_FANOUT_EXCHANGE = "command.fanout.exchange";
    public static final String COMMAND_STATS_QUEUE = "command.stats.queue";
    public static final String RESPONSE_FANOUT_EXCHANGE = "response.fanout.exchange";
    public static final String RESPONSE_STATS_QUEUE = "response.stats.queue";

    @Bean
    public FanoutExchange commandFanoutExchange() {
        return new FanoutExchange(COMMAND_FANOUT_EXCHANGE);
    }

    @Bean
    public Queue commandStatsQueue() {
        return new Queue(COMMAND_STATS_QUEUE, true);
    }

    @Bean
    public Binding statsBinding() {
        return BindingBuilder.bind(commandStatsQueue()).to(commandFanoutExchange());
    }

    @Bean
    public FanoutExchange responseFanoutExchange() {
        return new FanoutExchange(RESPONSE_FANOUT_EXCHANGE);
    }

    @Bean
    public Queue responseStatsQueue() {
        return new Queue(RESPONSE_STATS_QUEUE, true);
    }

    @Bean
    public Binding responseStatsBinding() {
        return BindingBuilder.bind(responseStatsQueue()).to(responseFanoutExchange());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule module = new SimpleModule();

        module.addDeserializer(CommandRequest.class, new CommandRequestDeserializer());
        objectMapper.registerModule(module);

        module.addDeserializer(CommandResponse.class, new CommandResponseDeserializer());
        objectMapper.registerModule(module);

        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
