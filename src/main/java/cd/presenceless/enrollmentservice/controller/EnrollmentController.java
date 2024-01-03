package cd.presenceless.enrollmentservice.controller;

import cd.presenceless.enrollmentservice.request.Response;
import cd.presenceless.enrollmentservice.request.CitizenReq;
import cd.presenceless.enrollmentservice.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/enroll")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<Response> enroll(
            @Valid @RequestBody CitizenReq enrollmentData) {
        try {
            final var enrollmentId = enrollmentService.enroll(enrollmentData);
            return ResponseEntity.ok(
                    new Response(enrollmentId.toString(), "in-progress", new Date()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response(e.getMessage(), "Failed", new Date()));
        }
    }

    @PostMapping("/{pNumber}/photograph")
    public ResponseEntity<Response> uploadPhoto(
            @PathVariable Long pNumber,
            @Valid @RequestParam("image") MultipartFile enrollmentData) {
        try {
            enrollmentService.uploadPhoto(pNumber, enrollmentData);
            return ResponseEntity.ok(
                    new Response(pNumber.toString(), "in-progress", new Date()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response(e.getMessage(), "Failed", new Date()));
        }
    }

    @PostMapping("/{pNumber}/fingerprints")
    public ResponseEntity<Response> uploadFingerprints(
            @Valid @RequestParam("images") MultipartFile[] fingerprintData,
            @PathVariable Long pNumber) {
        try {

            if (fingerprintData.length != 10) {
                return ResponseEntity
                        .badRequest()
                        .body(Response.builder()
                                .message("Invalid number of fingerprints")
                                .status("Failed")
                                .timestamp(new Date())
                                .build()
                        );
            }

            enrollmentService.uploadFingerprints(pNumber, fingerprintData);
            return ResponseEntity.ok(
                    new Response(pNumber.toString(), "in-progress", new Date()));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response(Arrays.toString(e.getStackTrace()), "Failed", new Date()));
        }
    }
}
