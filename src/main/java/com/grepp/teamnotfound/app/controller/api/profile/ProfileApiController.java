package com.grepp.teamnotfound.app.controller.api.profile;

import com.grepp.teamnotfound.app.controller.api.profile.payload.ProfilePetResponse;
import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/profile")
public class ProfileApiController {

    private final UserService userService;
    private final PetService petService;

    @GetMapping("/v1/users/{userId}")
    public ResponseEntity<?> getUser(
        @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.findByUserId(userId));
    }

    @GetMapping("/v1/users/{userId}/pet")
    public ResponseEntity<List<?>> getUserPets(
        @PathVariable Long userId
    ) {
        List<ProfilePetResponse> response = petService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/v1/board")
//    public ResponseEntity<PetDto> getUserBoard(
//        @AuthenticationPrincipal Principal principal
//    ) {
//        return ResponseEntity.ok(petService.findOne(userId));
//    }


}

