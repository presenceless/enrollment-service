package cd.presenceless.enrollmentservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "fingerprints")
public class FingerPrints {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "citizen", nullable = false)
    private Citizen citizen;

    @Column(nullable = false, length = 1000)
    private String fingerprint;
}
