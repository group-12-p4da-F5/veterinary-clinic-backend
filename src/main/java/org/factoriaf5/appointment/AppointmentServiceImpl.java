package org.factoriaf5.appointment;

import org.factoriaf5.appointment.dto.AppointmentDTO;
import org.factoriaf5.appointment.dto.CreateAppointmentDTO;
import org.factoriaf5.appointment.dto.UpdateAppointmentDTO;
import org.factoriaf5.appointment.dto.UpdateAppointmentStatusDTO;
import org.factoriaf5.patient.PatientRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
            if (dateTime == null)
                throw new IllegalArgumentException("La cita estándar requiere fecha y hora");

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
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        var patient = appointment.getPatient();

        // Enviar email antes de eliminar
        if (patient.getManager() != null && patient.getManager().getProfile() != null) {
            sendCancellationEmail(patient.getManager().getProfile().getEmail(), appointment);
        }

        repository.deleteById(id);
    }

    @Override
    public void deleteOldMissedAppointments() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(3);

        List<Appointment> oldMissedAppointments = repository
                .findByStatusAndDateTimeBefore(AppointmentStatus.MISSED, cutoffDate);

        if (!oldMissedAppointments.isEmpty()) {
            repository.deleteAll(oldMissedAppointments);
        }
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
    public void updateStatus() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);

        List<Appointment> expiredAppointments = repository.findByStatusAndDateTimeBefore(AppointmentStatus.PENDING,
                cutoff);

        for (Appointment appointment : expiredAppointments) {
            appointment.setStatus(AppointmentStatus.MISSED);
        }

        if (!expiredAppointments.isEmpty()) {
            repository.saveAll(expiredAppointments);
        }
    }

    @Override
    public AppointmentDTO updateAppointment(Integer appointmentId, UpdateAppointmentDTO dto) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        if (dto.getDateTime() != null) {
            appointment.setDateTime(dto.getDateTime());
        }
        if (dto.getReason() != null) {
            appointment.setReason(dto.getReason());
        }

        Appointment updated = repository.save(appointment);

        var patient = appointment.getPatient();
        if (patient.getManager() != null && patient.getManager().getProfile() != null) {
            sendUpdateEmail(patient.getManager().getProfile().getEmail(), updated);
        }

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

    // ===== Métodos privados de envío de emails =====

    private void sendAppointmentEmail(String to, Appointment appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("margaritavetclinic@gmail.com");
            helper.setTo(to);
            helper.setSubject("🐾 Nueva cita en VetClinic - Confirmación");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Nueva Cita VetClinic</title>
                        <style>
                            * {
                                margin: 0;
                                padding: 0;
                                box-sizing: border-box;
                            }

                            body {
                                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                line-height: 1.6;
                                color: #2c3e50;
                                background-color: #f8fffe;
                            }

                            .email-container {
                                max-width: 600px;
                                margin: 0 auto;
                                background: linear-gradient(135deg, #e8f5e8 0%%, #f0fff0 100%%);
                                border-radius: 15px;
                                box-shadow: 0 10px 30px rgba(76, 175, 80, 0.1);
                                overflow: hidden;
                            }

                            .header {
                                background: linear-gradient(135deg, #81c784 0%%, #66bb6a 100%%);
                                padding: 20px 20px 25px;
                                text-align: center;
                                position: relative;
                            }

                            .header::before {
                                content: '';
                                position: absolute;
                                top: -50%%;
                                left: -50%%;
                                width: 200%%;
                                height: 200%%;
                                background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="50" cy="50" r="2" fill="white" opacity="0.1"/></svg>') repeat;
                                animation: float 20s infinite linear;
                            }

                            @keyframes float {
                                0%% { transform: translateX(0) translateY(0); }
                                100%% { transform: translateX(-50px) translateY(-50px); }
                            }

                            .logo {
                                text-align: center;
                                margin: 0 auto 10px;
                                display: block;
                                position: relative;
                                z-index: 1;
                                width: 250px;
                                height: 250px;
                            }

                            .logo img {
                                width: 250px !important;
                                height: 250px !important;
                                max-width: 250px !important;
                                max-height: 250px !important;
                                min-width: 250px !important;
                                min-height: 250px !important;
                                object-fit: contain;
                                filter: drop-shadow(0 5px 15px rgba(0,0,0,0.2));
                                display: block !important;
                                margin: 0 auto !important;
                                border: none !important;
                                outline: none !important;
                            }

                            .header h1 {
                                color: white;
                                font-size: 28px;
                                font-weight: 600;
                                margin-bottom: 5px;
                                text-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                position: relative;
                                z-index: 1;
                            }

                            .header p {
                                color: #e8f5e8;
                                font-size: 16px;
                                position: relative;
                                z-index: 1;
                            }

                            .content {
                                padding: 40px 30px;
                                background: white;
                            }

                            .greeting {
                                font-size: 18px;
                                color: #2e7d32;
                                margin-bottom: 20px;
                                font-weight: 500;
                            }

                            .message {
                                font-size: 16px;
                                color: #424242;
                                margin-bottom: 30px;
                                line-height: 1.7;
                            }

                            .appointment-card {
                                background: linear-gradient(135deg, #f1f8e9 0%%, #e8f5e8 100%%);
                                border-left: 5px solid #66bb6a;
                                border-radius: 10px;
                                padding: 25px;
                                margin: 25px 0;
                                box-shadow: 0 3px 10px rgba(76, 175, 80, 0.1);
                            }

                            .appointment-title {
                                color: #2e7d32;
                                font-size: 20px;
                                font-weight: 600;
                                margin-bottom: 20px;
                                display: flex;
                                align-items: center;
                                gap: 10px;
                            }

                            .detail-item {
                                display: flex;
                                align-items: flex-start;
                                margin-bottom: 15px;
                                padding: 12px 0;
                                border-bottom: 1px solid #e0e0e0;
                            }

                            .detail-item:last-child {
                                border-bottom: none;
                                margin-bottom: 0;
                            }

                            .detail-icon {
                                width: 20px;
                                height: 20px;
                                margin-right: 12px;
                                margin-top: 2px;
                                flex-shrink: 0;
                            }

                            .detail-label {
                                font-weight: 600;
                                color: #2e7d32;
                                min-width: 100px;
                                margin-right: 15px;
                            }

                            .detail-value {
                                color: #424242;
                                flex: 1;
                            }

                            .footer {
                                background: #f5f5f5;
                                padding: 25px 30px;
                                text-align: center;
                                border-top: 1px solid #e0e0e0;
                            }

                            .footer-message {
                                color: #666;
                                font-size: 14px;
                                margin-bottom: 15px;
                                line-height: 1.5;
                            }

                            .contact-info {
                                color: #4caf50;
                                font-weight: 500;
                                font-size: 14px;
                            }

                            .divider {
                                height: 2px;
                                background: linear-gradient(to right, transparent, #81c784, transparent);
                                margin: 20px 0;
                            }

                            .paw-print {
                                color: #81c784;
                                font-size: 20px;
                                margin: 0 5px;
                            }

                            /* Soporte para clientes de email que no soportan algunas propiedades */
                            @media only screen and (max-width: 600px) {
                                .email-container {
                                    margin: 10px;
                                    border-radius: 10px;
                                }

                                .content {
                                    padding: 20px 15px;
                                }

                                .header {
                                    padding: 20px 15px;
                                }

                                .logo {
                                    width: 180px;
                                    height: 180px;
                                }

                                .logo img {
                                    width: 180px !important;
                                    height: 180px !important;
                                    max-width: 180px !important;
                                    max-height: 180px !important;
                                    min-width: 180px !important;
                                    min-height: 180px !important;
                                }
                            }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="header">
                                <div class="logo">
                                    <img src="cid:logomargarita" alt="VetClinic Logo" />
                                </div>
                                <h1>Margarita Veterinaria</h1>
                                <p>Cuidando a tu mejor amigo</p>
                            </div>

                            <div class="content">
                                <div class="greeting">¡Hola!</div>

                                <div class="message">
                                    Nos complace confirmar que se ha programado exitosamente una nueva cita para tu querida mascota.
                                    A continuación encontrarás todos los detalles importantes:
                                </div>

                                <div class="appointment-card">
                                    <div class="appointment-title">
                                        <span class="paw-print">🐾</span>
                                        Detalles de la Cita
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#4caf50" viewBox="0 0 24 24">
                                            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                                        </svg>
                                        <span class="detail-label">Paciente:</span>
                                        <span class="detail-value">%s</span>
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#4caf50" viewBox="0 0 24 24">
                                            <path d="M9 11H7v2h2v-2zm4 0h-2v2h2v-2zm4 0h-2v2h2v-2zm2-7h-1V2h-2v2H8V2H6v2H5c-1.1 0-1.99.9-1.99 2L3 20c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V9h14v11z"/>
                                        </svg>
                                        <span class="detail-label">Fecha y Hora:</span>
                                        <span class="detail-value">%s</span>
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#4caf50" viewBox="0 0 24 24">
                                            <path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"/>
                                        </svg>
                                        <span class="detail-label">Motivo:</span>
                                        <span class="detail-value">%s</span>
                                    </div>
                                </div>

                                <div class="divider"></div>

                                <div class="message">
                                    <strong>Recordatorios importantes:</strong><br>
                                    • Por favor, llega 10 minutos antes de tu cita<br>
                                    • Trae la cartilla de vacunación de tu mascota<br>
                                    • Si necesitas cancelar o reprogramar, contáctanos con al menos 24 horas de anticipación
                                </div>
                            </div>

                            <div class="footer">
                                <div class="footer-message">
                                    Gracias por confiar en nosotros para el cuidado de tu mascota.<br>
                                    ¡Esperamos verte pronto!
                                </div>
                                <div class="contact-info">
                                    <strong>Margarita Veterinaria</strong><br>
                                    📧 margaritavetclinic@gmail.com | 📞 (123) 456-7890
                                </div>
                            </div>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(
                            appointment.getPatient().getName(),
                            appointment.getDateTime(),
                            appointment.getReason());

            helper.setText(htmlContent, true);

            // Adjuntar el logo como recurso embebido DESPUÉS de establecer el contenido
            ClassPathResource logoResource = new ClassPathResource("static/Logomargarita.png");
            helper.addInline("logomargarita", logoResource);

            mailSender.send(message);

        } catch (MessagingException e) {
            // Loguea el error sin romper la aplicación
            System.err.println("Error enviando email de confirmación de cita: " + e.getMessage());
        }
    }

    private void sendCancellationEmail(String to, Appointment appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("margaritavetclinic@gmail.com");
            helper.setTo(to);
            helper.setSubject("❌ Cita cancelada en VetClinic - Notificación");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Cita Cancelada VetClinic</title>
                        <style>
                            * {
                                margin: 0;
                                padding: 0;
                                box-sizing: border-box;
                            }

                            body {
                                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                line-height: 1.6;
                                color: #2c3e50;
                                background-color: #fdf8f8;
                            }

                            .email-container {
                                max-width: 600px;
                                margin: 0 auto;
                                background: linear-gradient(135deg, #ffeaea 0%%, #fff5f5 100%%);
                                border-radius: 15px;
                                box-shadow: 0 10px 30px rgba(244, 67, 54, 0.1);
                                overflow: hidden;
                            }

                            .header {
                                background: linear-gradient(135deg, #e57373 0%%, #f44336 100%%);
                                padding: 20px 20px 25px;
                                text-align: center;
                                position: relative;
                            }

                            .header::before {
                                content: '';
                                position: absolute;
                                top: -50%%;
                                left: -50%%;
                                width: 200%%;
                                height: 200%%;
                                background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="50" cy="50" r="2" fill="white" opacity="0.1"/></svg>') repeat;
                                animation: float 20s infinite linear;
                            }

                            @keyframes float {
                                0%% { transform: translateX(0) translateY(0); }
                                100%% { transform: translateX(-50px) translateY(-50px); }
                            }

                            .logo {
                                text-align: center;
                                margin: 0 auto 10px;
                                display: block;
                                position: relative;
                                z-index: 1;
                                width: 250px;
                                height: 250px;
                            }

                            .logo img {
                                width: 250px !important;
                                height: 250px !important;
                                max-width: 250px !important;
                                max-height: 250px !important;
                                min-width: 250px !important;
                                min-height: 250px !important;
                                object-fit: contain;
                                filter: drop-shadow(0 5px 15px rgba(0,0,0,0.2));
                                display: block !important;
                                margin: 0 auto !important;
                                border: none !important;
                                outline: none !important;
                            }

                            .header h1 {
                                color: white;
                                font-size: 28px;
                                font-weight: 600;
                                margin-bottom: 5px;
                                text-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                position: relative;
                                z-index: 1;
                            }

                            .header p {
                                color: #ffeaea;
                                font-size: 16px;
                                position: relative;
                                z-index: 1;
                            }

                            .content {
                                padding: 40px 30px;
                                background: white;
                            }

                            .greeting {
                                font-size: 18px;
                                color: #c62828;
                                margin-bottom: 20px;
                                font-weight: 500;
                            }

                            .message {
                                font-size: 16px;
                                color: #424242;
                                margin-bottom: 30px;
                                line-height: 1.7;
                            }

                            .appointment-card {
                                background: linear-gradient(135deg, #ffebee 0%%, #ffeaea 100%%);
                                border-left: 5px solid #f44336;
                                border-radius: 10px;
                                padding: 25px;
                                margin: 25px 0;
                                box-shadow: 0 3px 10px rgba(244, 67, 54, 0.1);
                            }

                            .appointment-title {
                                color: #c62828;
                                font-size: 20px;
                                font-weight: 600;
                                margin-bottom: 20px;
                                display: flex;
                                align-items: center;
                                gap: 10px;
                            }

                            .detail-item {
                                display: flex;
                                align-items: flex-start;
                                margin-bottom: 15px;
                                padding: 12px 0;
                                border-bottom: 1px solid #e0e0e0;
                            }

                            .detail-item:last-child {
                                border-bottom: none;
                                margin-bottom: 0;
                            }

                            .detail-icon {
                                width: 20px;
                                height: 20px;
                                margin-right: 12px;
                                margin-top: 2px;
                                flex-shrink: 0;
                            }

                            .detail-label {
                                font-weight: 600;
                                color: #c62828;
                                min-width: 100px;
                                margin-right: 15px;
                            }

                            .detail-value {
                                color: #424242;
                                flex: 1;
                            }

                            .footer {
                                background: #f5f5f5;
                                padding: 25px 30px;
                                text-align: center;
                                border-top: 1px solid #e0e0e0;
                            }

                            .footer-message {
                                color: #666;
                                font-size: 14px;
                                margin-bottom: 15px;
                                line-height: 1.5;
                            }

                            .contact-info {
                                color: #f44336;
                                font-weight: 500;
                                font-size: 14px;
                            }

                            .divider {
                                height: 2px;
                                background: linear-gradient(to right, transparent, #e57373, transparent);
                                margin: 20px 0;
                            }

                            .cancel-icon {
                                color: #e57373;
                                font-size: 20px;
                                margin: 0 5px;
                            }

                            /* Soporte para clientes de email que no soportan algunas propiedades */
                            @media only screen and (max-width: 600px) {
                                .email-container {
                                    margin: 10px;
                                    border-radius: 10px;
                                }

                                .content {
                                    padding: 20px 15px;
                                }

                                .header {
                                    padding: 20px 15px;
                                }

                                .logo {
                                    width: 180px;
                                    height: 180px;
                                }

                                .logo img {
                                    width: 180px !important;
                                    height: 180px !important;
                                    max-width: 180px !important;
                                    max-height: 180px !important;
                                    min-width: 180px !important;
                                    min-height: 180px !important;
                                }
                            }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="header">
                                <div class="logo">
                                    <img src="cid:logomargarita" alt="VetClinic Logo" />
                                </div>
                                <h1>Margarita Veterinaria</h1>
                                <p>Cuidando a tu mejor amigo</p>
                            </div>

                            <div class="content">
                                <div class="greeting">¡Hola!</div>

                                <div class="message">
                                    Lamentamos informarte que tu cita programada ha sido cancelada.
                                    A continuación encontrarás todos los detalles de la cita que fue cancelada:
                                </div>

                                <div class="appointment-card">
                                    <div class="appointment-title">
                                        <span class="cancel-icon">❌</span>
                                        Cita Cancelada
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#f44336" viewBox="0 0 24 24">
                                            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                                        </svg>
                                        <span class="detail-label">Paciente:</span>
                                        <span class="detail-value">%s</span>
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#f44336" viewBox="0 0 24 24">
                                            <path d="M9 11H7v2h2v-2zm4 0h-2v2h2v-2zm4 0h-2v2h2v-2zm2-7h-1V2h-2v2H8V2H6v2H5c-1.1 0-1.99.9-1.99 2L3 20c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V9h14v11z"/>
                                        </svg>
                                        <span class="detail-label">Fecha y Hora:</span>
                                        <span class="detail-value">%s</span>
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#f44336" viewBox="0 0 24 24">
                                            <path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"/>
                                        </svg>
                                        <span class="detail-label">Motivo:</span>
                                        <span class="detail-value">%s</span>
                                    </div>
                                </div>

                                <div class="divider"></div>

                                <div class="message">
                                    <strong>¿Necesitas agendar una nueva cita?</strong><br>
                                    • Puedes contactarnos por teléfono o email<br>
                                    • Estamos disponibles para reprogramar tu cita cuando lo necesites<br>
                                    • Nuestro equipo estará encantado de ayudarte a encontrar el mejor horario
                                </div>
                            </div>

                            <div class="footer">
                                <div class="footer-message">
                                    Lamentamos cualquier inconveniente causado.<br>
                                    ¡Esperamos verte pronto con tu mascota!
                                </div>
                                <div class="contact-info">
                                    <strong>Margarita Veterinaria</strong><br>
                                    📧 margaritavetclinic@gmail.com | 📞 (123) 456-7890
                                </div>
                            </div>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(
                            appointment.getPatient().getName(),
                            appointment.getDateTime(),
                            appointment.getReason());

            helper.setText(htmlContent, true);

            // Adjuntar el logo como recurso embebido DESPUÉS de establecer el contenido
            ClassPathResource logoResource = new ClassPathResource("static/Logomargarita.png");
            helper.addInline("logomargarita", logoResource);

            mailSender.send(message);

        } catch (MessagingException e) {
            // Loguea el error sin romper la aplicación
            System.err.println("Error enviando email de cancelación: " + e.getMessage());
        }
    }

    private void sendUpdateEmail(String to, Appointment appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("margaritavetclinic@gmail.com");
            helper.setTo(to);
            helper.setSubject("✏️ Cita modificada en VetClinic - Actualización");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Cita Modificada VetClinic</title>
                        <style>
                            * {
                                margin: 0;
                                padding: 0;
                                box-sizing: border-box;
                            }

                            body {
                                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                line-height: 1.6;
                                color: #2c3e50;
                                background-color: #f8fffe;
                            }

                            .email-container {
                                max-width: 600px;
                                margin: 0 auto;
                                background: linear-gradient(135deg, #fff3e0 0%%, #ffeaa7 100%%);
                                border-radius: 15px;
                                box-shadow: 0 10px 30px rgba(255, 152, 0, 0.1);
                                overflow: hidden;
                            }

                            .header {
                                background: linear-gradient(135deg, #ffb74d 0%%, #ff9800 100%%);
                                padding: 20px 20px 25px;
                                text-align: center;
                                position: relative;
                            }

                            .header::before {
                                content: '';
                                position: absolute;
                                top: -50%%;
                                left: -50%%;
                                width: 200%%;
                                height: 200%%;
                                background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="50" cy="50" r="2" fill="white" opacity="0.1"/></svg>') repeat;
                                animation: float 20s infinite linear;
                            }

                            @keyframes float {
                                0%% { transform: translateX(0) translateY(0); }
                                100%% { transform: translateX(-50px) translateY(-50px); }
                            }

                            .logo {
                                text-align: center;
                                margin: 0 auto 10px;
                                display: block;
                                position: relative;
                                z-index: 1;
                                width: 250px;
                                height: 250px;
                            }

                            .logo img {
                                width: 250px !important;
                                height: 250px !important;
                                max-width: 250px !important;
                                max-height: 250px !important;
                                min-width: 250px !important;
                                min-height: 250px !important;
                                object-fit: contain;
                                filter: drop-shadow(0 5px 15px rgba(0,0,0,0.2));
                                display: block !important;
                                margin: 0 auto !important;
                                border: none !important;
                                outline: none !important;
                            }

                            .header h1 {
                                color: white;
                                font-size: 28px;
                                font-weight: 600;
                                margin-bottom: 5px;
                                text-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                position: relative;
                                z-index: 1;
                            }

                            .header p {
                                color: #fff8e1;
                                font-size: 16px;
                                position: relative;
                                z-index: 1;
                            }

                            .content {
                                padding: 40px 30px;
                                background: white;
                            }

                            .greeting {
                                font-size: 18px;
                                color: #e65100;
                                margin-bottom: 20px;
                                font-weight: 500;
                            }

                            .message {
                                font-size: 16px;
                                color: #424242;
                                margin-bottom: 30px;
                                line-height: 1.7;
                            }

                            .update-notice {
                                background: linear-gradient(135deg, #fff3e0 0%%, #ffe0b2 100%%);
                                border: 2px solid #ffb74d;
                                border-radius: 10px;
                                padding: 20px;
                                margin: 20px 0;
                                text-align: center;
                                box-shadow: 0 3px 10px rgba(255, 152, 0, 0.1);
                            }

                            .update-notice h3 {
                                color: #e65100;
                                font-size: 18px;
                                margin-bottom: 10px;
                                display: flex;
                                align-items: center;
                                justify-content: center;
                                gap: 10px;
                            }

                            .update-notice p {
                                color: #bf360c;
                                font-weight: 500;
                                font-size: 14px;
                            }

                            .appointment-card {
                                background: linear-gradient(135deg, #fff8e1 0%%, #fff3e0 100%%);
                                border-left: 5px solid #ff9800;
                                border-radius: 10px;
                                padding: 25px;
                                margin: 25px 0;
                                box-shadow: 0 3px 10px rgba(255, 152, 0, 0.1);
                            }

                            .appointment-title {
                                color: #e65100;
                                font-size: 20px;
                                font-weight: 600;
                                margin-bottom: 20px;
                                display: flex;
                                align-items: center;
                                gap: 10px;
                            }

                            .detail-item {
                                display: flex;
                                align-items: flex-start;
                                margin-bottom: 15px;
                                padding: 12px 0;
                                border-bottom: 1px solid #e0e0e0;
                            }

                            .detail-item:last-child {
                                border-bottom: none;
                                margin-bottom: 0;
                            }

                            .detail-icon {
                                width: 20px;
                                height: 20px;
                                margin-right: 12px;
                                margin-top: 2px;
                                flex-shrink: 0;
                            }

                            .detail-label {
                                font-weight: 600;
                                color: #e65100;
                                min-width: 100px;
                                margin-right: 15px;
                            }

                            .detail-value {
                                color: #424242;
                                flex: 1;
                            }

                            .footer {
                                background: #f5f5f5;
                                padding: 25px 30px;
                                text-align: center;
                                border-top: 1px solid #e0e0e0;
                            }

                            .footer-message {
                                color: #666;
                                font-size: 14px;
                                margin-bottom: 15px;
                                line-height: 1.5;
                            }

                            .contact-info {
                                color: #ff9800;
                                font-weight: 500;
                                font-size: 14px;
                            }

                            .divider {
                                height: 2px;
                                background: linear-gradient(to right, transparent, #ffb74d, transparent);
                                margin: 20px 0;
                            }

                            .paw-print {
                                color: #ffb74d;
                                font-size: 20px;
                                margin: 0 5px;
                            }

                            .edit-icon {
                                color: #ff9800;
                                font-size: 20px;
                            }

                            /* Soporte para clientes de email que no soportan algunas propiedades */
                            @media only screen and (max-width: 600px) {
                                .email-container {
                                    margin: 10px;
                                    border-radius: 10px;
                                }

                                .content {
                                    padding: 20px 15px;
                                }

                                .header {
                                    padding: 20px 15px;
                                }

                                .logo {
                                    width: 180px;
                                    height: 180px;
                                }

                                .logo img {
                                    width: 180px !important;
                                    height: 180px !important;
                                    max-width: 180px !important;
                                    max-height: 180px !important;
                                    min-width: 180px !important;
                                    min-height: 180px !important;
                                }
                            }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="header">
                                <div class="logo">
                                    <img src="cid:logomargarita" alt="VetClinic Logo" />
                                </div>
                                <h1>Margarita Veterinaria</h1>
                                <p>Cuidando a tu mejor amigo</p>
                            </div>

                            <div class="content">
                                <div class="greeting">¡Hola!</div>

                                <div class="update-notice">
                                    <h3>
                                        <span class="edit-icon">✏️</span>
                                        Cita Modificada
                                    </h3>
                                    <p>Tu cita ha sido actualizada exitosamente</p>
                                </div>

                                <div class="message">
                                    Queremos informarte que se han realizado cambios en la cita de tu querida mascota.
                                    A continuación encontrarás los <strong>nuevos detalles actualizados</strong>:
                                </div>

                                <div class="appointment-card">
                                    <div class="appointment-title">
                                        <span class="paw-print">🐾</span>
                                        Nuevos Detalles de la Cita
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#ff9800" viewBox="0 0 24 24">
                                            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                                        </svg>
                                        <span class="detail-label">Paciente:</span>
                                        <span class="detail-value">%s</span>
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#ff9800" viewBox="0 0 24 24">
                                            <path d="M9 11H7v2h2v-2zm4 0h-2v2h2v-2zm4 0h-2v2h2v-2zm2-7h-1V2h-2v2H8V2H6v2H5c-1.1 0-1.99.9-1.99 2L3 20c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V9h14v11z"/>
                                        </svg>
                                        <span class="detail-label">Nueva Fecha:</span>
                                        <span class="detail-value">%s</span>
                                    </div>

                                    <div class="detail-item">
                                        <svg class="detail-icon" fill="#ff9800" viewBox="0 0 24 24">
                                            <path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"/>
                                        </svg>
                                        <span class="detail-label">Motivo:</span>
                                        <span class="detail-value">%s</span>
                                    </div>
                                </div>

                                <div class="divider"></div>

                                <div class="message">
                                    <strong>Recordatorios importantes:</strong><br>
                                    • Por favor, toma nota de la nueva fecha y hora<br>
                                    • Llega 10 minutos antes de tu cita<br>
                                    • Trae la cartilla de vacunación de tu mascota<br>
                                    • Si necesitas realizar más cambios, contáctanos con al menos 24 horas de anticipación
                                </div>
                            </div>

                            <div class="footer">
                                <div class="footer-message">
                                    Gracias por tu comprensión y por confiar en nosotros para el cuidado de tu mascota.<br>
                                    ¡Esperamos verte en la nueva fecha programada!
                                </div>
                                <div class="contact-info">
                                    <strong>Margarita Veterinaria</strong><br>
                                    📧 margaritavetclinic@gmail.com | 📞 (123) 456-7890
                                </div>
                            </div>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(
                            appointment.getPatient().getName(),
                            appointment.getDateTime(),
                            appointment.getReason());

            helper.setText(htmlContent, true);

            // Adjuntar el logo como recurso embebido DESPUÉS de establecer el contenido
            ClassPathResource logoResource = new ClassPathResource("static/Logomargarita.png");
            helper.addInline("logomargarita", logoResource);

            mailSender.send(message);

        } catch (MessagingException e) {
            // Loguea el error sin romper la aplicación
            System.err.println("Error enviando email de actualización de cita: " + e.getMessage());
        }
    }

}
