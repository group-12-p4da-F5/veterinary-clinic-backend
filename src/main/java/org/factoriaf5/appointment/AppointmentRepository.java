package org.factoriaf5.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("SELECT COUNT(a) FROM Appointment a WHERE DATE(a.dateTime) = :date AND a.type = 'STANDARD'")
    long countNormalAppointmentsByDate(@Param("date") LocalDate date);
}
