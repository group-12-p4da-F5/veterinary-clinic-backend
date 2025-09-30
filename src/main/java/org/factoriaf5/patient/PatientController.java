package org.factoriaf5.patient;

import org.factoriaf5.patient.dto.CreatePatientDTO;
import org.factoriaf5.patient.dto.PatientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping("/owner/{dni}")
    public List<PatientDTO> getByOwner(@PathVariable String dni) {
        return service.getPatientsByOwner(dni);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PatientDTO> create(@RequestBody CreatePatientDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("")
    public List<PatientDTO> index() {
        return service.getAllPatients();
    }

}
