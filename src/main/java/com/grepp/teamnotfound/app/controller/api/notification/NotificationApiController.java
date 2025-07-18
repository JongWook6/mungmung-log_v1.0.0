package com.grepp.teamnotfound.app.controller.api.notification;

import com.grepp.teamnotfound.app.model.auth.domain.Principal;
import com.grepp.teamnotfound.app.model.notification.NotificationService;
import com.grepp.teamnotfound.app.model.notification.code.NotiType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/notification")
public class NotificationApiController {

    private final NotificationService notificationService;

    /**
     * 알림 조회 관련
     **/
//    @GetMapping("/v1/setting")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> getNotification(
//        @AuthenticationPrincipal Principal principal
//    ) {
//        Long userId = principal.getUserId();
//
//        return ResponseEntity.ok(notificationService.getUserNoti(userId));
//    }

    /**
    * 알림 설정 관련
    **/
    @GetMapping("/v1/setting")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserNotificationSetting(
        @AuthenticationPrincipal Principal principal
    ) {
        Long userId = principal.getUserId();

        return ResponseEntity.ok(notificationService.getUserSetting(userId));
    }

    @PatchMapping("/v1/setting")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeNotificationSetting(
        @RequestParam("target") NotiTar target,
        @AuthenticationPrincipal Principal principal
    ) {
        Long userId = principal.getUserId();

        return ResponseEntity.ok(notificationService.changeNotiSetting(userId, target));
    }

    /**
     * 알림 읽기 관련
     **/
    @PatchMapping("/v1/notifications/{notiId}/{type}")
    public ResponseEntity<?> readNotification(
        @PathVariable("notiId") Long notiId,
        @PathVariable("type") NotiType type
    ) {
        return ResponseEntity.ok(notificationService.readNoti(notiId, type));
    }

    /**
     * 알림 삭제 관련
     **/
    @DeleteMapping("/v1/notifications/{notiId}/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteNotification(
        @PathVariable("notiId") Long notiId,
        @PathVariable("type") NotiType type
    ) {
        notificationService.deleteNoti(notiId, type);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/v1")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteAllNotification(
        @AuthenticationPrincipal Principal principal
    ) {
        Long userId = principal.getUserId();

        notificationService.deleteAllNoti(userId);

        return ResponseEntity.ok().build();
    }
}
