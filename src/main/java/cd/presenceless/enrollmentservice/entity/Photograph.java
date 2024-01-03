package cd.presenceless.enrollmentservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "photograph")
public class Photograph {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen", nullable = false)
    private Citizen citizen;

    @Column(name = "passport_size", nullable = false, length = 1000)
    private String photograph;
}
