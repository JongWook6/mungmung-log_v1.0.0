package com.grepp.teamnotfound.app.controller.api.profile;

import com.grepp.teamnotfound.app.controller.api.mypage.payload.UserProfileArticleRequest;
import com.grepp.teamnotfound.app.controller.api.mypage.payload.UserProfileArticleResponse;
import com.grepp.teamnotfound.app.controller.api.profile.payload.ProfilePetResponse;
import com.grepp.teamnotfound.app.model.board.ArticleService;
import com.grepp.teamnotfound.app.model.board.code.ProfileBoardType;
import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.user.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final ArticleService articleService;

    @GetMapping("/v1/users/{userId}")
    @PreAuthorize("isAnonymous() or isAuthenticated()")
    public ResponseEntity<?> getUser(
        @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.findByUserId(userId));
    }

    @GetMapping("/v1/users/{userId}/pet")
    @PreAuthorize("isAnonymous() or isAuthenticated()")
    public ResponseEntity<List<?>> getUserPets(
        @PathVariable Long userId
    ) {
        List<ProfilePetResponse> response = petService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/users/{userId}/board")
    @PreAuthorize("isAnonymous() or isAuthenticated()")
    public ResponseEntity<?> getUserBoard(
        @PathVariable Long userId,
        @ModelAttribute @Valid UserProfileArticleRequest request
    ) {
        ProfileBoardType type = ProfileBoardType.WRITE;

        UserProfileArticleResponse response = articleService.getUsersArticles(userId, type, request.getPage(), request.getSize(), request.getSortType());
        return ResponseEntity.ok(response);
    }


}

