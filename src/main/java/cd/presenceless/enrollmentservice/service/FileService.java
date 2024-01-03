package cd.presenceless.enrollmentservice.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService {
    List<String> getFilesByOrg(Long orgId);
    ByteArrayResource downloadFile(String fileName);
    boolean deleteFile(String fileName);
    Map<String, String> uploadFile(MultipartFile file, Long id, String sub) throws IOException;
}
