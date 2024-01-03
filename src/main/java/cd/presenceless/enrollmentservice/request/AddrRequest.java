package cd.presenceless.enrollmentservice.request;


import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddrRequest {
    private String province, ville,
            commune, quartier, avenue, no;
}
