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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    // 한달치 일정 조회
    public List<Schedule> getCalendar(Long petId, LocalDate date) {
        // petId 존재 검증
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));

        YearMonth ym = YearMonth.from(date);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        return scheduleRepository.findByPetAndScheduleDateBetweenAndDeletedAtNull(pet, start, end);
    }

    // 일정 등록(생성)
    public void createSchedule(ScheduleCreateRequest request){
        // petId, userId 존재 검증
        Pet pet = petRepository.findById(request.getPetId()).orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if (request.getCycle() == null) {
            Schedule schedule = Schedule.builder()
                    .name(request.getName())
                    .scheduleDate(request.getDate())
                    .cycle(request.getCycle())
                    .cycleEnd(request.getCycleEnd())
                    .isDone(false)
                    .pet(pet)
                    .user(user).build();
            scheduleRepository.save(schedule);
        }else{
            for(LocalDate date = request.getDate(); date.isBefore(request.getCycleEnd()); date = date.plusDays(request.getCycle().getDays(LocalDate.now()))){
                Schedule schedule = Schedule.builder()
                        .name(request.getName())
                        .scheduleDate(date)
                        .cycle(request.getCycle())
                        .cycleEnd(request.getCycleEnd())
                        .isDone(false)
                        .pet(pet)
                        .user(user).build();
                scheduleRepository.save(schedule);
            }
        }

    }

    // 일정 수정
    public Schedule editSchedule(ScheduleEditRequest request){
        // petId, userId 존재 여부 검증
        Pet pet = petRepository.findById(request.getPetId()).orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 일정 존재 여부 검증
        Schedule schedule = scheduleRepository.findById(request.getScheduleId()).orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        schedule.setName(request.getName());
        schedule.setScheduleDate(request.getDate());
        schedule.setCycle(request.getCycle());
        schedule.setCycleEnd(request.getCycleEnd());
        schedule.setPet(pet);
        schedule.setUser(user);
        scheduleRepository.save(schedule);
        return schedule;
    }

    public void deleteSchedule(ScheduleDeleteRequest request){
        // userId 존재 여부 검증
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(request.getScheduleId()).orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        schedule.setDeletedAt(OffsetDateTime.now());
    }
}
