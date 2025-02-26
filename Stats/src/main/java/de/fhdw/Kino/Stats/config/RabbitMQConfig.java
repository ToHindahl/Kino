package de.fhdw.Kino.Stats.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
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
}
