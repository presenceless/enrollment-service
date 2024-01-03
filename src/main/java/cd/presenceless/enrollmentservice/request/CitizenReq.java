package cd.presenceless.enrollmentservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CitizenReq {
    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Date of birth is mandatory")
    @NotBlank(message = "Date of birth is mandatory")
    private Date dateOfBirth;

    @NotNull
    @NotBlank(message = "Gender is mandatory")
    @Size(min = 1, max = 1)
    private String gender;

    private String mobileNumber;

    private String email;

    @NotNull
    @NotBlank(message = "Address is mandatory")
    private AddrRequest address;

    private String parentPNumber;
}
