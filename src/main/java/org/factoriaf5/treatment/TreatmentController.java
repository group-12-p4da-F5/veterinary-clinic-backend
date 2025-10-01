package org.factoriaf5.treatment;

import org.factoriaf5.treatment.dto.TreatmentDTO;
import org.factoriaf5.treatment.dto.CreateTreatmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/treatments")
public class TreatmentController {

    private final TreatmentService service;

    public TreatmentController(TreatmentService service) {
        this.service = service;
    }

    @GetMapping("/patient/{id}")
    public List<TreatmentDTO> getByPatient(@PathVariable Integer id) {
        return service.getByPatient(id);
    }

    @PostMapping
    public ResponseEntity<TreatmentDTO> create(@RequestBody CreateTreatmentDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }
}
