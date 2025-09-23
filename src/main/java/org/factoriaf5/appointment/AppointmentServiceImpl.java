package org.factoriaf5.appointment;

import org.factoriaf5.appointment.dto.AppointmentDTO;
import org.factoriaf5.appointment.dto.CreateAppointmentDTO;
import org.factoriaf5.appointment.dto.UpdateAppointmentStatusDTO;
import org.factoriaf5.patient.PatientRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;

    public AppointmentServiceImpl(AppointmentRepository repository,
                                  PatientRepository patientRepository,
                                  JavaMailSender mailSender) {
        this.repository = repository;
        this.patientRepository = patientRepository;
        this.mailSender = mailSender;
    }

    @Override
    public AppointmentDTO create(CreateAppointmentDTO dto) {
        var patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        if ("STANDARD".equals(dto.getType())) {
            LocalDateTime dateTime = dto.getDateTime();
            if (dateTime == null) throw new IllegalArgumentException("La cita estándar requiere fecha y hora");

            // Validar horario
            LocalTime time = dateTime.toLocalTime();
            if (time.isBefore(LocalTime.of(9, 0)) || time.isAfter(LocalTime.of(13, 30))) {
                throw new IllegalArgumentException("Fuera del horario permitido");
            }

            // Verificar si ya hay cita en esa hora
            if (repository.existsByDateTime(dateTime)) {
                throw new IllegalArgumentException("Ya existe una cita en esa hora");
            }
        }

        var entity = AppointmentMapper.toEntity(dto, patient);
        var saved = repository.save(entity);

        // Enviar email al dueño
        if (patient.getManager() != null && patient.getManager().getProfile() != null) {
            sendAppointmentEmail(patient.getManager().getProfile().getEmail(), saved);
        }

        return AppointmentMapper.toDTO(saved);
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

        LocalDateTime start = date.atTime(9, 0);
        LocalDateTime end = date.atTime(14, 0);
        List<Appointment> appointments = repository.findByDateTimeBetween(start, end);

        return allTimes.stream()
                .filter(time -> appointments.stream()
                        .noneMatch(a -> a.getDateTime().toLocalTime().equals(time)))
                .map(time -> time.toString().substring(0, 5))
                .toList();
    }

    private void sendAppointmentEmail(String to, Appointment appointment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("margaritavetclinic@gmail.com");
        message.setTo(to);
        message.setSubject("Nueva cita en VetClinic");

        String text = String.format(
                "Hola,\n\nSe ha programado una nueva cita para tu mascota:\n\n" +
                "Paciente: %s\n" +
                "Fecha y hora: %s\n" +
                "Tipo: %s\n" +
                "Motivo: %s\n\n" +
                "Saludos,\nVetClinic",
                appointment.getPatient().getName(),
                appointment.getDateTime(),
                appointment.getType().getLabel(),
                appointment.getReason()
        );

        message.setText(text);
        mailSender.send(message);
    }
}
