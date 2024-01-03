package cd.presenceless.enrollmentservice.service;

import cd.presenceless.enrollmentservice.request.DataTransfer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
    @Value("${rabbitmq.enrollment.exchange}")
    private String exchangeKey;
    @Value("${rabbitmq.enrollment.routing-key.id-generation}")
    private String idGenerationRoutingKey;

    private final RabbitTemplate template;

    public ValidationService(RabbitTemplate template) {
        this.template = template;
    }

    @RabbitListener(queues = "${rabbitmq.enrollment.queue.validation}")
    public void validate(DataTransfer data) {
        System.out.println("Validating " + data);
        // TODO: validate entity

        if (isDuplicate(data)) {
            System.out.println("========================================");
            System.out.println("Duplicate " + data.getCitizen().getName());
            System.out.println("========================================");
            return;
        }

        if (isDuplicateBiometric(data)) {
            System.out.println("========================================");
            System.out.println("Duplicate " + data.getFingerPrints());
            System.out.println("========================================");
            return;
        }

        System.out.println("========================================");
        System.out.println("Data sent");
        template.convertAndSend(exchangeKey, idGenerationRoutingKey, data);
    }

    // create a private method that checks for de-duplication of demographic entity
    private boolean isDuplicate(DataTransfer data) {
        System.out.println("========================================");
        System.out.println("No demographic data duplication " + data.getCitizen().getName());
        System.out.println("========================================");
        return false;
    }


    // create a private method that checks for de-duplication of biometric entity
    private boolean isDuplicateBiometric(DataTransfer data) {
        System.out.println("========================================");
        System.out.println("No fingerprint duplication " + data.getFingerPrints());
        System.out.println("========================================");
        return false;
    }
}
