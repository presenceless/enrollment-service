package cd.presenceless.enrollmentservice.service;

import cd.presenceless.enrollmentservice.data.EnrolmentData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IDGeneratorService {

    @Value("${rabbitmq.enrollment.exchange}")
    private String exchangeKey;

    @Value("${rabbitmq.enrollment.routing-key.identity-service}")
    private String identityServiceRoutingKey;

    private final RabbitTemplate template;

    public IDGeneratorService(RabbitTemplate template) {
        this.template = template;
    }

    @RabbitListener(queues = "${rabbitmq.enrollment.queue.id-generation}")
    public void generateID(EnrolmentData data) {
        System.out.println("Generating ID for " + data);
        final var id = generateAadhaarNumber();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("========================================");
        System.out.println("Generated ID " + id);
        System.out.println("========================================");

        System.out.println("========================================");
        System.out.println("is valid ID " + isValidAadhaarNumber(id));
        System.out.println("========================================");

        template.convertAndSend(exchangeKey, identityServiceRoutingKey, data);
    }

    // https://github.com/krs92/Aadhar_validator
    // create a private method to check for the validity of the aadhaar number
    private boolean isValidAadhaarNumber(String aadhaarNumber) {
        // Regex to check valid Aadhaar number.
        String regex = "^[2-9][0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        // If the aadhaar number is empty return false
        if (aadhaarNumber == null) {
            return false;
        }
        // Pattern class contains matcher() method
        // to find matching between given aadhaar number
        // and regular expression.
        Matcher m = p.matcher(aadhaarNumber);
        // Return if the aadhaar number
        // matched the ReGex
        return m.matches();
    }

    // create a private method to generate the aadhaar number
    // https://uidai.gov.in/en/my-aadhaar/about-your-aadhaar/aadhaar-generation.html

    private String generateAadhaarNumber() {
        // create a string buffer
        StringBuilder sb = new StringBuilder();
        // 12 digit aadhaar number
        for (int i = 0; i < 12; i++) {
            // generate a random number between
            // 0 to 9
            int randomDigit = (int) (Math.random() * 10);
            // append the random digit to the string buffer
            sb.append(randomDigit);
            if (i == 3 || i == 7) {
                // after 4th and 8th digit add a space
                sb.append(" ");
            }
        }
        // return the string
        return sb.toString();
    }
}
