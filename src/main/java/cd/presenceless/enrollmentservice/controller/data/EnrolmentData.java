package cd.presenceless.enrollmentservice.controller.data;

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
    private String name;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String mobileNumber;
    private String email;
    private String tenFingerprints;
    private String twoIrisScans;
    private String photograph;
    private String pNumber;
}
