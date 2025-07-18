package com.grepp.teamnotfound.app.model.notification;

import com.grepp.teamnotfound.app.model.notification.code.NotiTarget;
import com.grepp.teamnotfound.app.model.notification.code.NotiType;
import com.grepp.teamnotfound.app.model.notification.dto.NotiBasicDto;
import com.grepp.teamnotfound.app.model.notification.dto.NotiScheduleCreateDto;
import com.grepp.teamnotfound.app.model.notification.dto.NotiServiceCreateDto;
import com.grepp.teamnotfound.app.model.notification.dto.NotiUserSettingDto;
import com.grepp.teamnotfound.app.model.notification.entity.NotiManagement;
import com.grepp.teamnotfound.app.model.notification.handler.ScheduleNotiHandlerImpl;
import com.grepp.teamnotfound.app.model.notification.handler.ServiceNotiHandlerImpl;
import com.grepp.teamnotfound.app.model.notification.repository.NotiManagementRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.NotificationErrorCode;
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
    private final ScheduleNotiHandlerImpl scheduleNotiHandlerImpl;
    private final ServiceNotiHandlerImpl serviceNotiHandlerImpl;

    ModelMapper modelMapper = new ModelMapper();

    public NotiUserSettingDto getUserSetting(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        NotiManagement noti = notiManagementRepository.findByUser(user)
            .orElseGet(() -> {
                log.warn("회원가입 당시 NotiManagement 미생성 오류 지금 생성 작업 진행 userId: {}", userId);

                NotiManagement created = new NotiManagement();
                created.setUser(user);

                return notiManagementRepository.save(created);
            });

        NotiUserSettingDto dto = modelMapper.map(noti, NotiUserSettingDto.class);
        return dto;
    }

    @Transactional
    public void createManagement(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        NotiManagement noti = new NotiManagement();
        noti.setUser(user);

        notiManagementRepository.save(noti);
    }

    @Transactional
    public void createNoti(Long userId, NotiType notiType, NotiBasicDto dto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        if (notiType == NotiType.SCHEDULE) {
            scheduleNotiHandlerImpl.handle(user, (NotiScheduleCreateDto) dto);
        } else {
            serviceNotiHandlerImpl.handle(user, (NotiServiceCreateDto) dto);
        }
    }

    @Transactional
    public NotiUserSettingDto changeNotiSetting(Long userId, NotiTarget target) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        NotiManagement noti = notiManagementRepository.findByUser(user)
            .orElseThrow(() -> new BusinessException(NotificationErrorCode.NOTIFICATION_MANAGEMENT_NOT_FOUND));

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

        NotiUserSettingDto dto = modelMapper.map(noti, NotiUserSettingDto.class);
        return dto;
    }

    @Transactional
    public void deleteNoti(Long notiId, NotiType type) {
        if (type == NotiType.SCHEDULE) {
            ScheduleNoti scheduleNoti = scheduleNotiRepository.findById(notiId)
                .orElseThrow(() -> new BusinessException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

            scheduleNoti.setDeletedAt(OffsetDateTime.now());

        } else if (SERVICE_TYPES.contains(type)) {
            ServiceNoti serviceNoti = serviceNotiRepository.findById(notiId)
                .orElseThrow(() -> new BusinessException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

            serviceNoti.setDeletedAt(OffsetDateTime.now());

        } else {
            throw new BusinessException(NotificationErrorCode.NOTIFICATION_NOT_FOUND);
        }
    }

    @Transactional
    public void deleteAllNoti(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        scheduleNotiRepository.deleteAllByUser(user.getUserId());
        serviceNotiRepository.deleteAllByUser(user.getUserId());
    }
}
