package cd.presenceless.enrollmentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "enrolment_data")
public class Citizen {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "citizen")
    private Address address;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "citizen")
    private Photograph photograph;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "citizen")
    private List<FingerPrints> tenFingerprints;

    @Column(name = "presenceless_number", unique = true, length = 14)
    private String presenceLessNumber;

    @Column(name = "full_name", nullable = false, unique = true)
    private String name;

    @Column (name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column (length = 1)
    private String gender;

    @Column (name = "phone_number", unique = true, length = 10)
    private String mobileNumber;

    @Column(unique = true, length = 100)
    private String email;

    private String parentPNumber;
}
