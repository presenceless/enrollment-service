package cd.presenceless.enrollmentservice.service;

import cd.presenceless.enrollmentservice.data.EnrolmentData;
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
    public void validate(EnrolmentData data) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("========================================");
        System.out.println("Validating " + data);
        // TODO: validate data
        System.out.println("========================================");

        if (isDuplicate(data)) {
            System.out.println("========================================");
            System.out.println("Duplicate " + data.getName());
            System.out.println("========================================");
            return;
        }

        if (isDuplicateBiometric(data)) {
            System.out.println("========================================");
            System.out.println("Duplicate " + data.getTenFingerprints());
            System.out.println("========================================");
            return;
        }

        template.convertAndSend(exchangeKey, idGenerationRoutingKey, data);
    }

    // create a private method that checks for de-duplication of demographic data
    private boolean isDuplicate(EnrolmentData data) {
        System.out.println("========================================");
        System.out.println("No duplication " + data.getName());
        System.out.println("========================================");
        return false;
    }


    // create a private method that checks for de-duplication of biometric data
    private boolean isDuplicateBiometric(EnrolmentData data) {
        System.out.println("========================================");
        System.out.println("No duplication " + data.getTenFingerprints());
        System.out.println("========================================");
        return false;
    }
}
