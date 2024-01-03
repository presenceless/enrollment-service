package cd.presenceless.enrollmentservice.request;

import cd.presenceless.enrollmentservice.entity.Address;
import cd.presenceless.enrollmentservice.entity.Citizen;
import cd.presenceless.enrollmentservice.entity.FingerPrints;
import cd.presenceless.enrollmentservice.entity.Photograph;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataTransfer {
    Citizen citizen;
    Address address;
    Photograph photograph;
    List<FingerPrints> fingerPrints;
}
