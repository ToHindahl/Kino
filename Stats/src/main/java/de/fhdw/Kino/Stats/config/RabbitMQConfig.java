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
    public static final String CRUD_FANOUT_EXCHANGE = "crud.fanout.exchange";

    // Name der Queue für das Stats-Modul
    public static final String CRUD_STATS_QUEUE = "crud.stats.queue";



    // Name der Queue für die DB-Verarbeitung
    public static final String CRUD_DB_QUEUE = "crud.db.queue";

    // Name der Queue für Antworten
    public static final String CRUD_REPLY_QUEUE = "crud.reply.queue";

    // Bean für den Fanout-Exchange
    @Bean
    public FanoutExchange crudFanoutExchange() {
        return new FanoutExchange(CRUD_FANOUT_EXCHANGE);
    }

    // Bean für die DB-Queue
    @Bean
    public Queue crudDbQueue() {
        return new Queue(CRUD_DB_QUEUE, true); // durable Queue
    }

    // Bean für die Antwort-Queue
    @Bean
    public Queue crudReplyQueue() {
        return new Queue(CRUD_REPLY_QUEUE, true); // durable Queue
    }

    // Binding zwischen Fanout-Exchange und DB-Queue
    @Bean
    public Binding dbBinding() {
        return BindingBuilder.bind(crudDbQueue())
                .to(crudFanoutExchange());
    }

    // Binding zwischen Fanout-Exchange und Antwort-Queue
    @Bean
    public Binding replyBinding() {
        return BindingBuilder.bind(crudReplyQueue())
                .to(crudFanoutExchange());
    }
    // Bean für die Stats-Queue
    @Bean
    public Queue crudStatsQueue() {
        return new Queue(CRUD_STATS_QUEUE, true); // durable Queue
    }


    // Binding zwischen Fanout-Exchange und Stats-Queue
    @Bean
    public Binding statsBinding() {
        return BindingBuilder.bind(crudStatsQueue())
                .to(crudFanoutExchange());
    }

}
