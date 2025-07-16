package com.grepp.teamnotfound.app.model.notification.handler;

import com.grepp.teamnotfound.app.model.notification.code.NotiType;
import com.grepp.teamnotfound.app.model.notification.dto.NotiServiceCreateDto;
import com.grepp.teamnotfound.app.model.notification.entity.ServiceNoti;
import com.grepp.teamnotfound.app.model.notification.repository.ServiceNotiRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceNotiHandlerImpl implements ServiceNotiHandler {

    @Autowired
    private ServiceNotiRepository serviceNotiRepository;

    @Override
    public void handle(User user, NotiServiceCreateDto dto) {

        ServiceNoti noti = new ServiceNoti();
        NotiType type = dto.getType();

        noti.setTargetId(dto.getTargetId());
        noti.setNotificationType(type);
        noti.setUser(user);

        switch (type) {
            case LIKE :
                noti.setContent("좋아요를 받았습니다.");
                break;
            case COMMENT:
                noti.setContent("댓글을 받았습니다.");
                break;
            case REPORT_SUCCESS:
                noti.setContent("신고가 정상 접수되었습니다.");
                break;
            case REPORT_FAIL:
                noti.setContent("신고가 반려됐습니다.");
                break;
            case REPORTED:
                noti.setContent("내가 작성한 게시글/댓글로 인해 신고를 받았습니다.");
                break;
            case RECOMMEND:
                noti.setContent("오늘치 제안이 떴습니다.");
                break;
            default:
                break;
        }

        serviceNotiRepository.save(noti);


    }
}
