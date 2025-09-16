package org.factoriaf5.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAll() {
        return ResponseEntity.ok(service.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getAppointmentById(id));
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment appointment) {
        boolean isAdmin = true; // temporal, cambiar con Spring Security
        return ResponseEntity.ok(service.createAppointment(appointment, isAdmin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(@PathVariable Integer id, @RequestBody Appointment appointment) {
        boolean isAdmin = true; // temporal, cambiar con Spring Security
        return ResponseEntity.ok(service.updateAppointment(id, appointment, isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
