package com.grepp.teamnotfound.app.controller.api.vaccine;

import com.grepp.teamnotfound.app.model.vaccination.VaccinationService;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/vaccinations", produces = MediaType.APPLICATION_JSON_VALUE)
public class VaccinationApiController {

    private final VaccinationService vaccinationService;

    @GetMapping("/v1")
    public ResponseEntity<List<VaccinationDTO>> getAllVaccinations() {
        return ResponseEntity.ok(vaccinationService.findAll());
    }

    @GetMapping("/v1/{petId}")
    public ResponseEntity<List<VaccinationDTO>> getPetVaccinations() {
        return ResponseEntity.ok(vaccinationService.findAll());
    }

    @GetMapping("/v1/{vaccinationId}")
    public ResponseEntity<VaccinationDTO> getVaccination(
            @PathVariable(name = "vaccinationId") final Long vaccinationId) {
        return ResponseEntity.ok(vaccinationService.get(vaccinationId));
    }

    @PostMapping("/v1")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVaccination(
            @RequestBody @Valid final VaccinationDTO vaccinationDTO) {
        final Long createdVaccinationId = vaccinationService.create(vaccinationDTO);
        return new ResponseEntity<>(createdVaccinationId, HttpStatus.CREATED);
    }

//    @PutMapping("/{vaccinationId}")
//    public ResponseEntity<Long> updateVaccination(
//            @PathVariable(name = "vaccinationId") final Long vaccinationId,
//            @RequestBody @Valid final VaccinationDTO vaccinationDTO) {
//        vaccinationService.update(vaccinationId, vaccinationDTO);
//        return ResponseEntity.ok(vaccinationId);
//    }
//
//    @DeleteMapping("/{vaccinationId}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteVaccination(
//            @PathVariable(name = "vaccinationId") final Long vaccinationId) {
//        vaccinationService.delete(vaccinationId);
//        return ResponseEntity.noContent().build();
//    }

}
