package com.grepp.teamnotfound.app.controller.api;


import com.grepp.teamnotfound.app.model.vaccination.VaccineService;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccineDTO;
import com.grepp.teamnotfound.util.ReferencedException;
import com.grepp.teamnotfound.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/vaccines", produces = MediaType.APPLICATION_JSON_VALUE)
public class VaccineResource {

    private final VaccineService vaccineService;

    public VaccineResource(final VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @GetMapping
    public ResponseEntity<List<VaccineDTO>> getAllVaccines() {
        return ResponseEntity.ok(vaccineService.findAll());
    }

    @GetMapping("/{vaccineId}")
    public ResponseEntity<VaccineDTO> getVaccine(
            @PathVariable(name = "vaccineId") final Long vaccineId) {
        return ResponseEntity.ok(vaccineService.get(vaccineId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVaccine(@RequestBody @Valid final VaccineDTO vaccineDTO) {
        final Long createdVaccineId = vaccineService.create(vaccineDTO);
        return new ResponseEntity<>(createdVaccineId, HttpStatus.CREATED);
    }

    @PutMapping("/{vaccineId}")
    public ResponseEntity<Long> updateVaccine(
            @PathVariable(name = "vaccineId") final Long vaccineId,
            @RequestBody @Valid final VaccineDTO vaccineDTO) {
        vaccineService.update(vaccineId, vaccineDTO);
        return ResponseEntity.ok(vaccineId);
    }

    @DeleteMapping("/{vaccineId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVaccine(
            @PathVariable(name = "vaccineId") final Long vaccineId) {
        final ReferencedWarning referencedWarning = vaccineService.getReferencedWarning(vaccineId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        vaccineService.delete(vaccineId);
        return ResponseEntity.noContent().build();
    }

}
