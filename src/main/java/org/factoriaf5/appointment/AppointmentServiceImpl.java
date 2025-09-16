package org.factoriaf5.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;

    @Override
    public List<Appointment> getAllAppointments() {
        return repository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
    }

    @Override
    public Appointment createAppointment(Appointment appointment, boolean isAdmin) {
        // Si es urgencia, solo admin puede crear y no cuenta para límite diario
        if (appointment.getType() == AppointmentType.EMERGENCY) {
            if (!isAdmin) {
                throw new IllegalArgumentException("Solo el administrador puede crear citas de urgencia");
            }
            appointment.setStatus(AppointmentStatus.PENDING);
            return repository.save(appointment);
        }

        // Validar máximo de 10 citas normales por día
        LocalDate date = appointment.getDateTime().toLocalDate();
        long count = repository.countNormalAppointmentsByDate(date);
        if (count >= 10) {
            throw new IllegalArgumentException("Máximo de 10 citas normales por día alcanzado");
        }

        appointment.setStatus(AppointmentStatus.PENDING);
        return repository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Integer id, Appointment appointment, boolean isAdmin) {
        Appointment existing = getAppointmentById(id);

        // Solo admin puede marcar como urgencia
        if (appointment.getType() == AppointmentType.EMERGENCY && !isAdmin) {
            throw new IllegalArgumentException("Solo el administrador puede marcar una cita como urgencia");
        }

        existing.setDateTime(appointment.getDateTime());
        existing.setType(appointment.getType());
        existing.setReason(appointment.getReason());
        existing.setStatus(appointment.getStatus());
        existing.setPatient(appointment.getPatient());

        return repository.save(existing);
    }

    @Override
    public void deleteAppointment(Integer id) {
        repository.deleteById(id);
    }
}
