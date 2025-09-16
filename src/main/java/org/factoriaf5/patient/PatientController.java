package org.factoriaf5.patient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    public List<Patient> getAll() {
        return service.getAllPatients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getPatientById(id));
    }

    @GetMapping("/identification/{identificationNumber}")
    public ResponseEntity<Patient> getByIdentification(@PathVariable String identificationNumber) {
        return ResponseEntity.ok(service.getPatientByIdentification(identificationNumber));
    }

    @GetMapping("/owner/{dni}")
    public ResponseEntity<Patient> getByOwnerDni(@PathVariable String dni) {
        return ResponseEntity.ok(service.getPatientByOwnerDni(dni));
    }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        return ResponseEntity.ok(service.createPatient(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable Integer id, @RequestBody Patient patient) {
        return ResponseEntity.ok(service.updatePatient(id, patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
