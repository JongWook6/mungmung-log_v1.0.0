package com.grepp.teamnotfound.app.controller.api;

import com.grepp.teamnotfound.app.model.pet.PetImgService;
import com.grepp.teamnotfound.app.model.pet.dto.PetImgDTO;
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
@RequestMapping(value = "/api/v1/petImgs", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetImgResource {

    private final PetImgService petImgService;

    public PetImgResource(final PetImgService petImgService) {
        this.petImgService = petImgService;
    }

    @GetMapping
    public ResponseEntity<List<PetImgDTO>> getAllPetImgs() {
        return ResponseEntity.ok(petImgService.findAll());
    }

    @GetMapping("/{petImgId}")
    public ResponseEntity<PetImgDTO> getPetImg(
            @PathVariable(name = "petImgId") final Long petImgId) {
        return ResponseEntity.ok(petImgService.get(petImgId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPetImg(@RequestBody @Valid final PetImgDTO petImgDTO) {
        final Long createdPetImgId = petImgService.create(petImgDTO);
        return new ResponseEntity<>(createdPetImgId, HttpStatus.CREATED);
    }

    @PutMapping("/{petImgId}")
    public ResponseEntity<Long> updatePetImg(@PathVariable(name = "petImgId") final Long petImgId,
            @RequestBody @Valid final PetImgDTO petImgDTO) {
        petImgService.update(petImgId, petImgDTO);
        return ResponseEntity.ok(petImgId);
    }

    @DeleteMapping("/{petImgId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePetImg(@PathVariable(name = "petImgId") final Long petImgId) {
        petImgService.delete(petImgId);
        return ResponseEntity.noContent().build();
    }

}
