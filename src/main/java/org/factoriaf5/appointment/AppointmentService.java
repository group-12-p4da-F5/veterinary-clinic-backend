package org.factoriaf5.appointment;

import org.factoriaf5.appointment.dto.AppointmentDTO;
import org.factoriaf5.appointment.dto.CreateAppointmentDTO;
import org.factoriaf5.appointment.dto.UpdateAppointmentDTO;
import org.factoriaf5.appointment.dto.UpdateAppointmentStatusDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO create(CreateAppointmentDTO dto);
    List<AppointmentDTO> getAll();
    List<AppointmentDTO> getByPatient(Integer patientId);
    void delete(Integer id);
    AppointmentDTO updateStatus(Integer appointmentId, UpdateAppointmentStatusDTO dto);
    List<String> getAvailableHours(LocalDate date);
    AppointmentDTO updateAppointment(Integer appointmentId, UpdateAppointmentDTO dto);

}
