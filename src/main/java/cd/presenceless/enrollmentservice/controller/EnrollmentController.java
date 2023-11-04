package cd.presenceless.enrollmentservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/enroll")
public class EnrollmentController {

    @PostMapping
    public Object enroll(@RequestBody Object enrollmentData) {
        return enrollmentData;
    }

    @PostMapping("/biometrics")
    public Object biometrics(@RequestBody Object biometricsData) {
        return biometricsData;
    }
}
