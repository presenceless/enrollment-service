package cd.presenceless.enrollmentservice.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EnrolmentData {
    private String pNumber;

    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "date of birth is mandatory")
    private Date dateOfBirth;

    @NotBlank(message = "Gender is mandatory")
    @Size(min = 1, max = 1)
    private String gender;

    @NotBlank(message = "Address is mandatory")
    private String address;

    private String mobileNumber;

    private String email;

    @NotBlank(message = "tenFingerprints are mandatory")
    private String tenFingerprints;

    @NotBlank(message = "photograph is mandatory")
    private String photograph;

    private String parentPNumber;
}
