package de.fhdw.Kino.DB.config;

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
    public Queue auffuehrungCreationQueue() {
        return new Queue("auffuehrung.create.queue");
    }

    @Bean
    public DirectExchange auffuehrungCreationExchange() {
        return new DirectExchange("auffuehrung.create.exchange");
    }

    @Bean
    public Binding auffuehrungCreationBinding(Queue auffuehrungCreationQueue, DirectExchange auffuehrungCreationExchange) {
        return BindingBuilder.bind(auffuehrungCreationQueue)
                .to(auffuehrungCreationExchange)
                .with("auffuehrung.create.routingkey");
    }

    @Bean
    public Queue auffuehrungRequestAllQueue() {
        return new Queue("auffuehrung.get_all.queue");
    }

    @Bean
    public DirectExchange auffuehrungRequestAllExchange() {
        return new DirectExchange("auffuehrung.get_all.exchange");
    }

    @Bean
    public Binding auffuehrungRequestAllBinding(Queue auffuehrungRequestAllQueue, DirectExchange auffuehrungRequestAllExchange) {
        return BindingBuilder.bind(auffuehrungRequestAllQueue)
                .to(auffuehrungRequestAllExchange)
                .with("auffuehrung.get_all.routingkey");
    }

    @Bean
    public Queue filmCreationQueue() {
        return new Queue("film.create.queue");
    }

    @Bean
    public DirectExchange filmCreationExchange() {
        return new DirectExchange("film.create.exchange");
    }

    @Bean
    public Binding filmCreationBinding(Queue filmCreationQueue, DirectExchange filmCreationExchange) {
        return BindingBuilder.bind(filmCreationQueue)
                .to(filmCreationExchange)
                .with("film.create.routingkey");
    }

    @Bean
    public Queue filmRequestAllQueue() {
        return new Queue("film.get_all.queue");
    }

    @Bean
    public DirectExchange filmRequestAllExchange() {
        return new DirectExchange("film.get_all.exchange");
    }

    @Bean
    public Binding filmRequestAllBinding(Queue filmRequestAllQueue, DirectExchange filmRequestAllExchange) {
        return BindingBuilder.bind(filmRequestAllQueue)
                .to(filmRequestAllExchange)
                .with("film.get_all.routingkey");
    }

    @Bean
    public Queue kinoCreationQueue() {
        return new Queue("kino.create.queue");
    }

    @Bean
    public DirectExchange kinoCreationExchange() {
        return new DirectExchange("kino.create.exchange");
    }

    @Bean
    public Binding kinoCreationBinding(Queue kinoCreationQueue, DirectExchange kinoCreationExchange) {
        return BindingBuilder.bind(kinoCreationQueue)
                .to(kinoCreationExchange)
                .with("kino.create.routingkey");
    }

    @Bean
    public Queue kinoRequestQueue() {
        return new Queue("kino.get.queue");
    }

    @Bean
    public DirectExchange kinoRequestExchange() {
        return new DirectExchange("kino.get.exchange");
    }

    @Bean
    public Binding kinoRequestBinding(Queue kinoRequestQueue, DirectExchange kinoRequestExchange) {
        return BindingBuilder.bind(kinoRequestQueue)
                .to(kinoRequestExchange)
                .with("kino.get.routingkey");
    }

    @Bean
    public Queue kundeCreationQueue() {
        return new Queue("kunde.create.queue");
    }

    @Bean
    public DirectExchange kundeCreationExchange() {
        return new DirectExchange("kunde.create.exchange");
    }

    @Bean
    public Binding kundeCreationBinding(Queue kundeCreationQueue, DirectExchange kundeCreationExchange) {
        return BindingBuilder.bind(kundeCreationQueue)
                .to(kundeCreationExchange)
                .with("kunde.create.routingkey");
    }

    @Bean
    public Queue reservierungCreationQueue() {
        return new Queue("reservierung.create.queue");
    }

    @Bean
    public DirectExchange reservierungCreationExchange() {
        return new DirectExchange("reservierung.create.exchange");
    }

    @Bean
    public Binding reservierungCreationBinding(Queue reservierungCreationQueue, DirectExchange reservierungCreationExchange) {
        return BindingBuilder.bind(reservierungCreationQueue)
                .to(reservierungCreationExchange)
                .with("reservierung.create.routingkey");
    }

    @Bean
    public Queue reservierungCancelationQueue() {
        return new Queue("reservierung.cancel.queue");
    }

    @Bean
    public DirectExchange reservierungCancelationExchange() {
        return new DirectExchange("reservierung.cancel.exchange");
    }

    @Bean
    public Binding reservierungCancelationBinding(Queue reservierungCancelationQueue, DirectExchange reservierungCancelationExchange) {
        return BindingBuilder.bind(reservierungCancelationQueue)
                .to(reservierungCancelationExchange)
                .with("reservierung.cancel.routingkey");
    }

    @Bean
    public Queue reservierungBookingQueue() {
        return new Queue("reservierung.book.queue");
    }

    @Bean
    public DirectExchange reservierungBookingExchange() {
        return new DirectExchange("reservierung.book.exchange");
    }

    @Bean
    public Binding reservierungBookingBinding(Queue reservierungBookingQueue, DirectExchange reservierungBookingExchange) {
        return BindingBuilder.bind(reservierungBookingQueue)
                .to(reservierungBookingExchange)
                .with("reservierung.book.routingkey");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        return rabbitTemplate;
    }

}
