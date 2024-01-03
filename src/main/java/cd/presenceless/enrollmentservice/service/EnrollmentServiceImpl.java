package cd.presenceless.enrollmentservice.service;

import cd.presenceless.enrollmentservice.entity.Address;
import cd.presenceless.enrollmentservice.entity.Citizen;
import cd.presenceless.enrollmentservice.entity.FingerPrints;
import cd.presenceless.enrollmentservice.entity.Photograph;
import cd.presenceless.enrollmentservice.repository.AddressRepository;
import cd.presenceless.enrollmentservice.repository.CitizenRepository;
import cd.presenceless.enrollmentservice.repository.FingerPrintsRepository;
import cd.presenceless.enrollmentservice.repository.PhotographRepository;
import cd.presenceless.enrollmentservice.request.DataTransfer;
import cd.presenceless.enrollmentservice.request.CitizenReq;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final CitizenRepository citizenRepository;
    private final PhotographRepository photographRepository;
    private final FingerPrintsRepository fingerPrintsRepository;
    private final AddressRepository addressRepository;
    private final AmqpTemplate template;
    private final FileService fileService;

    @Value("${rabbitmq.enrollment.exchange}")
    private String exchange;
    @Value("${rabbitmq.enrollment.routing-key.validation}")
    private String routingKey;

    public EnrollmentServiceImpl(CitizenRepository repository, PhotographRepository photographRepository, FingerPrintsRepository fingerPrintsRepository, AddressRepository addressRepository, AmqpTemplate template, FileService fileService) {
        this.citizenRepository = repository;
        this.photographRepository = photographRepository;
        this.fingerPrintsRepository = fingerPrintsRepository;
        this.addressRepository = addressRepository;
        this.template = template;
        this.fileService = fileService;
    }

    /**
     * @param req enrollment request
     * @throws Exception if enrollment fails
     */
    @Override
    public Long enroll(CitizenReq req) throws Exception {
        try {
            final var enrollment = Citizen.builder()
                    .name(req.getName())
                    .dateOfBirth(req.getDateOfBirth())
                    .gender(req.getGender())
                    .mobileNumber(req.getMobileNumber())
                    .email(req.getEmail())
                    .parentPNumber(req.getParentPNumber())
                    .build();

            final var enrolled = citizenRepository.save(enrollment);
            final var addr = Address.builder()
                    .citizen(enrolled)
                    .province(req.getAddress().getProvince())
                    .ville(req.getAddress().getVille())
                    .commune(req.getAddress().getCommune())
                    .quartier(req.getAddress().getQuartier())
                    .avenue(req.getAddress().getAvenue())
                    .no(req.getAddress().getNo())
                    .build();
            addressRepository.save(addr);

            return enrolled.getId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @param eId           enrollment id
     * @param photograph    passport photo of the person
     * @throws Exception    if upload fails
     */
    @Override
    public void uploadPhoto(Long eId, MultipartFile photograph) throws Exception {
        final var enrolled = citizenRepository.findById(eId)
                .orElseThrow(() -> new Exception("Enrollment not found"));
        savePhotograph(enrolled, photograph);
    }

    /**
     * @param eId           enrollment id
     * @param fingerprints  fingerprints of the person
     * @throws Exception    if upload fails
     */
    @Override
    public void uploadFingerprints(Long eId, MultipartFile[] fingerprints) throws Exception {
        final var enrolled = citizenRepository.findById(eId)
                .orElseThrow(() -> new Exception("Enrollment not found"));

        Arrays.stream(fingerprints).forEach(f -> {
            try {
                saveFingerPrints(enrolled, f);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        // Transferring all the data to the validation service then to the id generation service
        // before sending it to the identity service
        DataTransfer dataTransfer = DataTransfer.builder()
                .citizen(
                        Citizen.builder()
                                .name(enrolled.getName())
                                .gender(enrolled.getGender())
                                .dateOfBirth(enrolled.getDateOfBirth())
                                .mobileNumber(enrolled.getMobileNumber())
                                .email(enrolled.getEmail())
                                .parentPNumber(enrolled.getParentPNumber())
                                .build()
                )
                .address(
                        Address.builder()
                                .province(enrolled.getAddress().getProvince())
                                .ville(enrolled.getAddress().getVille())
                                .commune(enrolled.getAddress().getCommune())
                                .quartier(enrolled.getAddress().getQuartier())
                                .avenue(enrolled.getAddress().getAvenue())
                                .no(enrolled.getAddress().getNo())
                                .build()
                )
                .photograph(
                        Photograph.builder()
                                .photograph(enrolled.getPhotograph().getPhotograph())
                                .build()
                )
                .fingerPrints(
                        enrolled
                                .getTenFingerprints()
                                .stream().map(fingerPrint -> FingerPrints.builder()
                                        .fingerprint(fingerPrint.getFingerprint())
                                        .build()
                                ).toList()
                )
                .build();

        template.convertAndSend(exchange, routingKey, dataTransfer);
    }

    private void savePhotograph(Citizen enrolled, MultipartFile file) throws Exception {
        // https://www.baeldung.com/jpa-joincolumn-vs-mappedby
        String fileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));

        try {
            isValidFile(file, fileName, 5);

            final var fileInfo = fileService.uploadFile(file, enrolled.getId(), "photos/");

            save(enrolled, fileInfo, photographRepository);
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(file.getSize());
        } catch (Exception e) {
            throw new Exception("Could not save File: " + e.getMessage());
        }
    }

    private void saveFingerPrints(Citizen enrolled, MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));

        try {
            isValidFile(file, fileName, 10);

            final var fileInfo = fileService.uploadFile(file, enrolled.getId(), "fingerprints/");

            save(enrolled, fileInfo, fingerPrintsRepository);
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(file.getSize());
        } catch (Exception e) {
            throw new Exception("Could not save File: " + e.getMessage());
        }
    }

    private static void isValidFile(MultipartFile file, String fileName, int sizeLimit) throws Exception {
        if(fileName.contains("..")) {
            throw  new Exception("Filename contains invalid path sequence " + fileName);
        }

        if (file.getBytes().length > (1024 * 1024 * sizeLimit)) { // 5MB
            throw new Exception("File size exceeds maximum limit");
        }
    }

    private static void save(Citizen enrolled, Map<String, String> fileInfo, PhotographRepository photographRepository) {
        Photograph document = Photograph.builder()
                .citizen(enrolled)
                .photograph(fileInfo.get("mediaLink"))
                .build();

        photographRepository.save(document);
    }

    private static void save(Citizen enrolled, Map<String, String> fileInfo, FingerPrintsRepository photographRepository) {
        FingerPrints document = FingerPrints.builder()
                .citizen(enrolled)
                .fingerprint(fileInfo.get("mediaLink"))
                .build();

        photographRepository.save(document);
    }
}
