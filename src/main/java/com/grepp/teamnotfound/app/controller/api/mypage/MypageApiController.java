package com.grepp.teamnotfound.app.controller.api.mypage;


import com.grepp.teamnotfound.app.controller.api.mypage.payload.PetWriteRequest;
import com.grepp.teamnotfound.app.controller.api.mypage.payload.UserProfileArticleRequest;
import com.grepp.teamnotfound.app.controller.api.mypage.payload.UserProfileArticleResponse;
import com.grepp.teamnotfound.app.controller.api.mypage.payload.VaccineWriteRequest;
import com.grepp.teamnotfound.app.controller.api.profile.payload.ProfilePetResponse;
import com.grepp.teamnotfound.app.model.auth.domain.Principal;
import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.board.code.ProfileBoardType;
import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.pet.dto.PetDto;
import com.grepp.teamnotfound.app.model.user.UserService;
import com.grepp.teamnotfound.app.model.vaccination.VaccinationService;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/mypage")
public class MypageApiController {

    private final PetService petService;
    private final UserService userService;
    private final VaccinationService vaccinationService;
    private final ArticleService articleService;

    /**
     * 나 & 내 펫 & 게시물 정보 반환
     **/
    @GetMapping("/v1/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUser(
        @AuthenticationPrincipal Principal principal
    ) {
        Long userId = principal.getUserId();

        return ResponseEntity.ok(userService.findByUserId(userId));
    }

    @GetMapping("/v1/pets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<?>> getUserPets(
        @AuthenticationPrincipal Principal principal
    ) {
        Long userId = principal.getUserId();

        List<ProfilePetResponse> response = petService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/board/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserBoard(
        @AuthenticationPrincipal Principal principal,
        @PathVariable(name = "type") ProfileBoardType type,
        @ModelAttribute @Valid UserProfileArticleRequest request
    ) {
        Long userId = principal.getUserId();

        UserProfileArticleResponse response = articleService.getUsersArticles(userId, type, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 펫 관련 API
     **/

    @PostMapping(value = "/v3/pets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createPet(
        @RequestPart("request") PetWriteRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @AuthenticationPrincipal Principal principal
    ) {
        Long userId = principal.getUserId();
        List<MultipartFile> images = (image != null) ? List.of(image) : List.of();

        return ResponseEntity.ok(petService.create(userId, request, images));
    }

    @GetMapping("/v1/pets/{petId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PetDto> getPet(
        @PathVariable(name = "petId") Long petId
    ) {
        PetDto petDto = petService.findOne(petId);
        return ResponseEntity.ok(petDto);
    }

    @PutMapping(value = "/v3/pets/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePet(
        @PathVariable(name = "petId") Long petId,
        @RequestPart("request") PetWriteRequest request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        List<MultipartFile> images = (image != null) ? List.of(image) : List.of();
        return ResponseEntity.ok(petService.update(petId, request, images));
    }

    @DeleteMapping("/v2/pets/{petId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deletePet(
        @PathVariable(name = "petId") Long petId
    ) {
        petService.delete(petId);
        return ResponseEntity.ok().build();
    }



    /**
     * 펫의 백신 관련 API
     **/

    @PostMapping("/v1/pets/{petId}/vaccination")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createVaccination(
        @PathVariable(name = "petId") Long petId,
        @RequestBody @Valid List<VaccineWriteRequest> requests
    ) {
        vaccinationService.savePetVaccinations(petId, requests);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/pets/{petId}/vaccination")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VaccinationDto>> getVaccination(
        @PathVariable(name = "petId") Long petId
    ) {
        List<VaccinationDto> response = vaccinationService.findPetVaccination(petId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/v1/pets/{petId}/vaccination")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateVaccination(
        @PathVariable(name = "petId") Long petId,
        @RequestBody @Valid List<VaccineWriteRequest> requests
    ) {
        vaccinationService.savePetVaccinations(petId, requests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/pets/{petId}/vaccination-schedule")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createVaccineSchedule(
            @PathVariable(name = "petId") Long petId,
            @AuthenticationPrincipal Principal principal
    ){
        vaccinationService.createVaccinationSchedule(petId, principal.getUserId());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}