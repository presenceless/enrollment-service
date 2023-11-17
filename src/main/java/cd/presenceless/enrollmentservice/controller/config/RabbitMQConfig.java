package cd.presenceless.enrollmentservice.controller.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue enrollmentQueue() {
        return new Queue("enrollments_for_verification");
    }

    @Bean
    public TopicExchange enrollmentsExchange() {
        return new TopicExchange("enrollments");
    }

    @Bean
    public Binding binding(Queue enrollmentQueue, TopicExchange enrollmentsExchange) {
        return new Binding(enrollmentQueue.getName(), Binding.DestinationType.QUEUE,
                enrollmentsExchange.getName(), "enrollments.#", null);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
