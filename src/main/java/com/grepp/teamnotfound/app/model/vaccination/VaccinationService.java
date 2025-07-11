package com.grepp.teamnotfound.app.model.vaccination;


import com.grepp.teamnotfound.app.controller.api.mypage.payload.VaccinatedItem;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.schedule.ScheduleService;
import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleCreateRequestDto;
import com.grepp.teamnotfound.app.model.vaccination.code.VaccineType;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDto;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccinationRepository;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccineRepository;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.VaccinationErrorCode;
import com.grepp.teamnotfound.util.NotFoundException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final VaccineRepository vaccineRepository;
    private final ScheduleService scheduleService;

    ModelMapper modelMapper = new ModelMapper();

    public List<VaccinationDto> findAll() {
        List<Vaccination> vaccinations = vaccinationRepository.findAll();

        return vaccinations.stream()
            .map(VaccinationDto::fromEntity)
            .toList();
    }

    public VaccinationDto get(Long vaccinationId) {
        return vaccinationRepository.findById(vaccinationId)
            .map(VaccinationDto::fromEntity)
            .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(VaccinationDto vaccinationDTO) {
        Vaccination vaccination = modelMapper.map(vaccinationDTO, Vaccination.class);

        vaccinationRepository.save(vaccination);

        return vaccination.getVaccinationId();
    }

    @Transactional
    public void savePetVaccinations(Pet pet, List<? extends VaccinatedItem> vaccinatedItems) {
        if (vaccinatedItems == null || vaccinatedItems.isEmpty()) {
            return;
        }

        // 백신 리스트 저장
        for (VaccinatedItem item : vaccinatedItems) {
            Vaccination vaccination = modelMapper.map(item, Vaccination.class);
            vaccination.setPet(pet);

            Vaccine vaccine = vaccineRepository.findById(item.getVaccineId())
                .orElseThrow(() -> new BusinessException(VaccinationErrorCode.VACCINE_NOT_FOUND));
            vaccination.setVaccine(vaccine);

            // 각 백신에 맞는 일정 등록 로직
            if( item.getVaccineType().equals(VaccineType.ADDITIONAL)) {
                // 추가 접종(1년 단위)
                ScheduleCreateRequestDto scheduleDto = ScheduleCreateRequestDto.builder().name(vaccine.getName() + " 다음 접종일")
                        .date(item.getVaccineAt().plusMonths(vaccine.getAdditionalCycle()))
                        .cycle(ScheduleCycle.YEAR)
                        .cycleEnd(item.getVaccineAt().plusYears(31))
                        .build();
                scheduleService.createSchedule(scheduleDto);

            }else if(item.getCount() <= vaccine.getBoosterCount() && item.getIsVaccine()) {
                // 보강 접종(2주 단위)
                LocalDate cycleEndDate = item.getVaccineAt().plusWeeks((long) (vaccine.getBoosterCount() - item.getCount()) * vaccine.getBoosterCycle());
                ScheduleCreateRequestDto scheduleDto = ScheduleCreateRequestDto.builder().name(vaccine.getName() + " 다음 접종일")
                        .date(item.getVaccineAt().plusWeeks(vaccine.getBoosterCycle()))
                        .cycle(ScheduleCycle.TWO_WEEK)
                        .cycleEnd(cycleEndDate)
                        .build();
                scheduleService.createSchedule(scheduleDto);

                // 보강 접종 완료 후 추가 접종(1년 단위)
                ScheduleCreateRequestDto scheduleDto2 = ScheduleCreateRequestDto.builder().name(vaccine.getName() + " 다음 접종일")
                        .date(cycleEndDate.plusYears(1))
                        .cycle(ScheduleCycle.YEAR)
                        .cycleEnd(cycleEndDate.plusYears(31))
                        .build();
                scheduleService.createSchedule(scheduleDto);

            }

            vaccinationRepository.save(vaccination);
        }
    }

    @Transactional
    public void delete(Long vaccinationId) {
        vaccinationRepository.deleteById(vaccinationId);
    }
    @Transactional
    public void softDelete(Long petId) {
        vaccinationRepository.softDelete(petId, OffsetDateTime.now());
    }

}
