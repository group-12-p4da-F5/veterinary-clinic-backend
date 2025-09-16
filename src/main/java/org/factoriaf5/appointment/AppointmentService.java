package org.factoriaf5.appointment;

import java.util.List;

public interface AppointmentService {
    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Integer id);
    Appointment createAppointment(Appointment appointment, boolean isAdmin);
    Appointment updateAppointment(Integer id, Appointment appointment, boolean isAdmin);
    void deleteAppointment(Integer id);
}
