package org.factoriaf5.patient;

import org.factoriaf5.appointment.Appointment;
import org.factoriaf5.treatment.Treatment;
import org.factoriaf5.user.User;
import org.factoriaf5.user.repository.*;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;
    private String name;
    private int age;
    private String breed;
    private String gender;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User manager;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Treatment> treatments;
}
