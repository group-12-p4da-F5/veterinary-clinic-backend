package org.factoriaf5.appointment;

import org.factoriaf5.appointment.dto.AppointmentDTO;
import org.factoriaf5.appointment.dto.CreateAppointmentDTO;
import org.factoriaf5.appointment.dto.UpdateAppointmentStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppointmentDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/patient/{id}")
    public List<AppointmentDTO> getByPatient(@PathVariable Integer id) {
        return service.getByPatient(id);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> create(@RequestBody CreateAppointmentDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateStatus(
            @PathVariable("id") Integer id,
            @RequestBody UpdateAppointmentStatusDTO dto) {

        return ResponseEntity.ok(service.updateStatus(id, dto));
    }

}
