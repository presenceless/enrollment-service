package cd.presenceless.enrollmentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "citizen_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen", nullable = false)
    private Citizen citizen;

    private String province, ville,
            commune, quartier, avenue, no;

    public String toString() {
        return
                "Province du " + province +
                ", ville de '" + ville +
                ", commune de '" + commune +
                ", quartier '" + quartier +
                ", avenue '" + avenue +
                ", no '" + no;
    }
}
