package cd.presenceless.enrollmentservice.service;

import cd.presenceless.enrollmentservice.request.CitizenReq;
import org.springframework.web.multipart.MultipartFile;

public interface EnrollmentService {
    Long enroll(CitizenReq req) throws Exception;
    void uploadPhoto(Long eId, MultipartFile photograph) throws Exception;
    void uploadFingerprints(Long eId, MultipartFile[] fingerprints) throws Exception;
}
