package org.factoriaf5.appointment.cromjob;

import org.factoriaf5.appointment.AppointmentService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class AppointmentScheduler {

  private final AppointmentService appointmentService;

  public AppointmentScheduler(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @Scheduled(cron = "0 5 0 1 * ?")
  public void updateExpiredAppointments() {
    appointmentService.updateStatus();
  }

  @Scheduled(cron = "0 5 0 7 * ?")
  public void cleanupOldMissedAppointments() {
    appointmentService.deleteOldMissedAppointments();
  }

  // TODO: DESCOMENTAR PARA MOSTRAR EN PRESENTACIÓN
  // @Scheduled(cron = "*/30 * * * * *")
  // public void withCronJobUpdate() {
  // System.out.println("<--------- status updated --------->");
  // appointmentService.updateStatus();
  // }

  // @Scheduled(cron = "*/60 * * * * *")
  // public void withCronJobdelete() {
  // System.out.println("<--------- cleared appointments --------->");
  // appointmentService.deleteOldMissedAppointments();
  // }
}
