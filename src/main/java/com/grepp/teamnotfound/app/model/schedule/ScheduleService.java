package com.grepp.teamnotfound.app.model.schedule;

import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleCreateRequest;
import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleDeleteRequest;
import com.grepp.teamnotfound.app.controller.api.schedule.payload.ScheduleEditRequest;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.schedule.entity.Schedule;
import com.grepp.teamnotfound.app.model.schedule.repository.ScheduleRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.error.exception.PetException;
import com.grepp.teamnotfound.infra.error.exception.ScheduleException;
import com.grepp.teamnotfound.infra.error.exception.UserException;
import com.grepp.teamnotfound.infra.error.exception.code.PetErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.ScheduleErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class ScheduleService {
    private ScheduleRepository scheduleRepository;
    private PetRepository petRepository;
    private UserRepository userRepository;

    // 한달치 일정 조회
    public List<Schedule> getCalendar(Long petId, LocalDate date) {
        // petId 존재 검증
        if (petRepository.findById(petId).isEmpty()){
            throw new PetException(PetErrorCode.PET_NOT_FOUND);
        }

        YearMonth ym = YearMonth.from(date);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        return scheduleRepository.findByPetAndScheduleDateBetweenAndDeletedAtNull(new Pet(petId), start, end);
    }

    // 일정 등록(생성)
    public Schedule createSchedule(ScheduleCreateRequest request){
        // petId, userId 존재 검증
        if (petRepository.findById(request.getPetId()).isEmpty()){
            throw new PetException(PetErrorCode.PET_NOT_FOUND);
        }
        if (userRepository.findById(request.getUserId()).isEmpty()){
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }

        Schedule schedule = Schedule.builder()
                .name(request.getName())
                .scheduleDate(request.getDate())
                .cycle(request.getCycle())
                .cycleEnd(request.getCycleEnd())
                .pet(new Pet(request.getPetId()))
                .user(new User(request.getUserId())).build();
        scheduleRepository.save(schedule);

        return schedule;
    }

    // 일정 수정
    public Schedule editSchedule(ScheduleEditRequest request){
        // petId, userId 존재 여부 검증
        if (petRepository.findById(request.getPetId()).isEmpty()){
            throw new PetException(PetErrorCode.PET_NOT_FOUND);
        }
        if (userRepository.findById(request.getUserId()).isEmpty()){
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }
        Optional<Schedule> schedule = scheduleRepository.findById(request.getScheduleId());
        if(schedule.isEmpty()){ // 일정 존재 여부 검증
            throw new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
        }else {
            schedule.get().setName(request.getName());
            schedule.get().setScheduleDate(request.getDate());
            schedule.get().setCycle(request.getCycle());
            schedule.get().setCycleEnd(request.getCycleEnd());
            schedule.get().setPet(new Pet(request.getPetId()));
            schedule.get().setUser(new User(request.getUserId()));
            scheduleRepository.save(schedule.get());
        }
        return schedule.get();
    }

    public void deleteSchedule(ScheduleDeleteRequest request){
        // userId 존재 여부 검증
        if (userRepository.findById(request.getUserId()).isEmpty()){
            throw new UserException(UserErrorCode.USER_NOT_FOUND);
        }
        Optional<Schedule> schedule = scheduleRepository.findById(request.getScheduleId());
        if(schedule.isEmpty()){ // 일정 존재 여부 검증
            throw new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
        }else {
            schedule.get().setDeletedAt(OffsetDateTime.now());
        }
    }
}
