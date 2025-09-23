package org.factoriaf5.appointment;

import org.factoriaf5.appointment.dto.AppointmentDTO;
import org.factoriaf5.appointment.dto.CreateAppointmentDTO;
import org.factoriaf5.appointment.dto.UpdateAppointmentStatusDTO;
import org.factoriaf5.patient.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final PatientRepository patientRepository;

    public AppointmentServiceImpl(AppointmentRepository repository, PatientRepository patientRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
    }

    @Override
    public AppointmentDTO create(CreateAppointmentDTO dto) {
        var patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        if (dto.getType().equals("STANDARD")) {
            LocalDateTime dateTime = dto.getDateTime();
            if (dateTime == null)
                throw new IllegalArgumentException("La cita estándar requiere fecha y hora");

            // Validar horario
            LocalTime time = dateTime.toLocalTime();
            if (time.isBefore(LocalTime.of(9, 0)) || time.isAfter(LocalTime.of(14, 0))) {
                throw new IllegalArgumentException("Fuera del horario permitido");
            }

            if (repository.existsByDateTime(dateTime)) {
                throw new IllegalArgumentException("Ya existe una cita en esa hora");
            }
        }

        var entity = AppointmentMapper.toEntity(dto, patient);
        return AppointmentMapper.toDTO(repository.save(entity));
    }

    @Override
    public List<AppointmentDTO> getAll() {
        return repository.findAll().stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentDTO> getByPatient(Integer patientId) {
        return repository.findByPatientPatientId(patientId).stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public AppointmentDTO updateStatus(Integer appointmentId, UpdateAppointmentStatusDTO dto) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        appointment.setStatus(dto.getStatus());
        Appointment updated = repository.save(appointment);
        return AppointmentMapper.toDTO(updated);
    }

    @Override
    public List<String> getAvailableHours(LocalDate date) {
        // Horario de 9:00 a 13:30 cada 30 minutos (10 turnos)
        List<LocalTime> allTimes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            allTimes.add(LocalTime.of(9, 0).plusMinutes(i * 30L));
        }

        // Obtener todas las citas en ese día
        LocalDateTime start = date.atTime(9, 0);
        LocalDateTime end = date.atTime(14, 0);
        List<Appointment> appointments = repository.findByDateTimeBetween(start, end);

        // Filtrar las horas ocupadas y formatear como "HH:mm"
        List<String> available = allTimes.stream()
                .filter(time -> appointments.stream()
                        .noneMatch(a -> a.getDateTime().toLocalTime().equals(time)))
                .map(time -> time.toString().substring(0, 5)) // "HH:mm"
                .toList();

        return available;
    }

}
