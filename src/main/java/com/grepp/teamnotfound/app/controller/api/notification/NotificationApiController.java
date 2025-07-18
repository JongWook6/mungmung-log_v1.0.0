package com.grepp.teamnotfound.app.controller.api.notification;

import com.grepp.teamnotfound.app.model.notification.NotificationService;
import com.grepp.teamnotfound.app.model.notification.code.NotiTarget;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/v1/users/{userId}")
    public ResponseEntity<?> getUserNotificationSetting(
        @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(notificationService.getUserSetting(userId));
    }

    @PatchMapping("/v1/users/{userId}/setting")
    public ResponseEntity<?> changeNotificationSetting(
        @PathVariable("userId") Long userId,
        @RequestParam("target") NotiTarget target
    ) {
        return ResponseEntity.ok(notificationService.changeNotiSetting(userId, target));
    }
//
//    @PatchMapping("/v1/{notiId}/read")
//    public ResponseEntity<?> readNotification(
//        @PathVariable("notiId") Long notiId
//    ) {
//
//    }

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
