package com.grepp.teamnotfound.app.controller.api;


import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.pet.dto.PetDTO;
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
@RequestMapping(value = "/api/v1/pets", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetResource {

    private final PetService petService;

    public PetResource(final PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<List<PetDTO>> getAllPets() {
        return ResponseEntity.ok(petService.findAll());
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetDTO> getPet(@PathVariable(name = "petId") final Long petId) {
        return ResponseEntity.ok(petService.get(petId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPet(@RequestBody @Valid final PetDTO petDTO) {
        final Long createdPetId = petService.create(petDTO);
        return new ResponseEntity<>(createdPetId, HttpStatus.CREATED);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<Long> updatePet(@PathVariable(name = "petId") final Long petId,
            @RequestBody @Valid final PetDTO petDTO) {
        petService.update(petId, petDTO);
        return ResponseEntity.ok(petId);
    }

    @DeleteMapping("/{petId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePet(@PathVariable(name = "petId") final Long petId) {
        final ReferencedWarning referencedWarning = petService.getReferencedWarning(petId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        petService.delete(petId);
        return ResponseEntity.noContent().build();
    }

}
