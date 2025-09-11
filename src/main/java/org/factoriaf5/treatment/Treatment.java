package org.factoriaf5.treatment;

import org.factoriaf5.patient.Patient;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "treatments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer treatmentId;

    private String description;
    private LocalDateTime treatmentDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
