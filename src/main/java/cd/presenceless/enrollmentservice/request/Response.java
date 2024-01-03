package cd.presenceless.enrollmentservice.request;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response {
    private String message;
    private String status;
    private Date timestamp;
}
