package cd.presenceless.enrollmentservice.service;

import cd.presenceless.enrollmentservice.request.DataTransfer;
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
    public void generateID(DataTransfer data) {
        final var id = generatePresenceLessNumber();

        if (isValidPresenceLessNumber(id)) {
            data.getCitizen().setPresenceLessNumber(id);
            template.convertAndSend(exchangeKey, identityServiceRoutingKey, data);
        } else {
            System.out.println("========================================");
            System.out.println("Invalid presenceLess number " + id);
            System.out.println("========================================");
        }
    }

    // https://github.com/krs92/Aadhar_validator
    // create a private method to check for the validity of the presenceLess number
    private boolean isValidPresenceLessNumber(String presenceNumber) {
        // Regex to check valid presenceLess number.
        String regex = "^[2-9][0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        // If the presenceLess number is empty return false
        if (presenceNumber == null) {
            return false;
        }
        // Pattern class contains matcher() method
        // to find matching between given presenceLess number
        // and regular expression.
        Matcher m = p.matcher(presenceNumber);
        // Return if the presenceLess number
        // matched the ReGex
        return m.matches();
    }

    // create a private method to generate the presenceLess number
    // https://uidai.gov.in/en/my-aadhaar/about-your-aadhaar/aadhaar-generation.html
    private String generatePresenceLessNumber() {
        // create a string buffer
        StringBuilder sb = new StringBuilder();

        // the first digit should be between 2 and 9
        // generate a random number between
        // 2 and 9
        int randomDigit = (int) (Math.random() * 8 + 2);
        // append the random digit to the string buffer
        sb.append(randomDigit);

        // the rest of 11 digits
        for (int i = 0; i < 11; i++) {
            // generate a random number between
            // 0 to 9
            randomDigit = (int) (Math.random() * 10);
            // append the random digit to the string buffer
            sb.append(randomDigit);
            if (i == 2 || i == 6) {
                // after 4th and 8th digit add a space
                sb.append(" ");
            }
        }

        // example: 2234 5678 9012
        return sb.toString();
    }
}
