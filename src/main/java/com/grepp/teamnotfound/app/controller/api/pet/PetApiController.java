package com.grepp.teamnotfound.app.controller.api.pet;


import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.pet.dto.PetDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/pets")
public class PetApiController {

    private final PetService petService;

    @GetMapping("/v1/user/{userId}")
    public ResponseEntity<List<PetDTO>> getUserPets(
        @PathVariable(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(petService.findByUserId(userId));
    }

    @GetMapping("/v1/{petId}")
    public ResponseEntity<PetDTO> getPet(
        @PathVariable(name = "petId") Long petId
    ) {
        return ResponseEntity.ok(petService.findOne(petId));
    }

    @PostMapping("/v1")
    public ResponseEntity<Long> createPet(
        @RequestBody @Valid PetDTO petDTO
    ) {
        Long createdPetId = petService.create(petDTO);
        return new ResponseEntity<>(createdPetId, HttpStatus.CREATED);
    }

//    @PutMapping("/{petId}")
//    public ResponseEntity<Long> updatePet(@PathVariable(name = "petId") final Long petId,
//            @RequestBody @Valid final PetDTO petDTO) {
//        petService.update(petId, petDTO);
//        return ResponseEntity.ok(petId);
//    }
//
//    @DeleteMapping("/petId}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deletePet(@PathVariable(name = "petId") final Long petId) {
//        final ReferencedWarning referencedWarning = petService.getReferencedWarning(petId);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        petService.delete(petId);
//        return ResponseEntity.noContent().build();
//    }

}
