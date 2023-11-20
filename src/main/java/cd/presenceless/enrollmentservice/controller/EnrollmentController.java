package cd.presenceless.enrollmentservice.controller;

import cd.presenceless.enrollmentservice.data.EnrolmentData;
import cd.presenceless.enrollmentservice.data.Response;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/enroll")
public class EnrollmentController {
    private final RabbitTemplate template;

    @Value("${rabbitmq.enrollment.exchange}")
    private String exchange;
    @Value("${rabbitmq.enrollment.routing-key.validation}")
    private String routingKey;

    public EnrollmentController(RabbitTemplate template) {
        this.template = template;
    }

    @PostMapping
    public ResponseEntity<Response> enroll(@Valid @RequestBody EnrolmentData enrollmentData) {
        assert template != null;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        template.convertAndSend(exchange, routingKey, enrollmentData);
        return ResponseEntity.ok(new Response("Enrollment request sent", "in-progress", new Date()));
    }
}
