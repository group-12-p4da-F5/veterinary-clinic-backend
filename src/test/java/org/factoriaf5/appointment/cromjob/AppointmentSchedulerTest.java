package org.factoriaf5.appointment.cromjob;

// import static org.awaitility.Awaitility.await;
// import static org.mockito.Mockito.atLeastOnce;
// import static org.mockito.Mockito.verify;

// import java.time.Duration;

// import org.factoriaf5.appointment.AppointmentService;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(AppointmentScheduler.class)
class AppointmentSchedulerTest {

  // @MockitoSpyBean
  // AppointmentService appointmentService;

  // @Test
  // void testUpdateExpiredAppointmentsShouldBeScheduled() {
  // await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
  // verify(appointmentService, atLeastOnce()).updateStatus());
  // }

  // @Test
  // void testCleanupOldMissedAppointmentsShouldBeScheduled() {
  // await().atMost(Duration.ofSeconds(2))
  // .untilAsserted(() -> verify(appointmentService,
  // atLeastOnce()).deleteOldMissedAppointments());
  // }
}
