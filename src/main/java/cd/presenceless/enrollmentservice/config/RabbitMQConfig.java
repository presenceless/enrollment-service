package cd.presenceless.enrollmentservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${rabbitmq.enrollment.exchange}")
    private String exchangeKey;

    @Value("${rabbitmq.enrollment.queue.validation}")
    private String validationQueue;
    @Value("${rabbitmq.enrollment.queue.id-generation}")
    private String idGenerationQueue;
    @Value("${rabbitmq.enrollment.queue.identity-service}")
    private String identityServiceQueue;

    @Value("${rabbitmq.enrollment.routing-key.identity-service}")
    private String identityServiceRoutingKey;
    @Value("${rabbitmq.enrollment.routing-key.id-generation}")
    private String idGenerationRoutingKey;
    @Value("${rabbitmq.enrollment.routing-key.validation}")
    private String validationRoutingKey;

    @Bean
    public Queue validationQueue() {
        return new Queue(validationQueue);
    }

    @Bean
    public Queue idGenerationQueue() {
        return new Queue(idGenerationQueue);
    }

    @Bean
    public Queue identityQueue() {
        return new Queue(identityServiceQueue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeKey);
    }

    @Bean
    public Binding validationBinding(@Qualifier("validationQueue") Queue queue, TopicExchange exchange) {
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE,
                exchange.getName(), validationRoutingKey, null);
    }

    @Bean
    public Binding idGenerationBinding(@Qualifier("idGenerationQueue") Queue queue, TopicExchange exchange) {
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE,
                exchange.getName(), idGenerationRoutingKey, null);
    }

    @Bean
    public Binding identityBinding(@Qualifier("identityQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(identityServiceRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
