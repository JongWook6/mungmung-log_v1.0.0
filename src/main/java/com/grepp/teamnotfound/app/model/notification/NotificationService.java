package com.grepp.teamnotfound.app.model.notification;

import com.grepp.teamnotfound.app.model.notification.code.NotiTarget;
import com.grepp.teamnotfound.app.model.notification.code.NotiType;
import com.grepp.teamnotfound.app.model.notification.entity.NotiManagement;
import com.grepp.teamnotfound.app.model.notification.entity.ScheduleNoti;
import com.grepp.teamnotfound.app.model.notification.repository.NotiManagementRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final UserRepository userRepository;
    private final NotiManagementRepository notiManagementRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public void createManagement(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        NotiManagement noti = new NotiManagement();
        noti.setUser(user);

        notiManagementRepository.save(noti);
    }

    @Transactional
    public void changeNotiSetting(Long userId, NotiTarget target) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        NotiManagement noti = notiManagementRepository.findByUser(user)
            .orElseGet(() -> {
                log.warn("회원가입 당시 NotiManagement 미생성 오류 지금 생성 작업 진행 userId: {}", userId);

                NotiManagement created = new NotiManagement();
                created.setUser(user);

                return notiManagementRepository.save(created);
            });

        if (target.equals(NotiTarget.SERVICE)) {
            noti.setIsNotiService(!noti.getIsNotiService());
        } else if (target.equals(NotiTarget.SCHEDULE)) {
            noti.setIsNotiSchedule(!noti.getIsNotiSchedule());
        } else {
            Boolean allNotiState = noti.getIsNotiAll();
            noti.setIsNotiService(!allNotiState);
            noti.setIsNotiSchedule(!allNotiState);
            noti.setIsNotiAll(!allNotiState);
        }
        noti.setUpdatedAt(OffsetDateTime.now());

        notiManagementRepository.save(noti);
    }
}
