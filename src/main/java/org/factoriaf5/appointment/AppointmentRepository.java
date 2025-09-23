package org.factoriaf5.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatientPatientId(Integer patientId);
    boolean existsByDateTime(LocalDateTime dateTime);
    List<Appointment> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
