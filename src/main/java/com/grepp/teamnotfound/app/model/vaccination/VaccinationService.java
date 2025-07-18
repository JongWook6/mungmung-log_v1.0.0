package com.grepp.teamnotfound.app.model.vaccination;


import com.grepp.teamnotfound.app.controller.api.mypage.payload.VaccineWriteRequest;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.schedule.ScheduleService;
import com.grepp.teamnotfound.app.model.schedule.code.ScheduleCycle;
import com.grepp.teamnotfound.app.model.schedule.dto.ScheduleCreateRequestDto;
import com.grepp.teamnotfound.app.model.vaccination.code.VaccineName;
import com.grepp.teamnotfound.app.model.vaccination.code.VaccineType;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDto;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccinationRepository;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccineRepository;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.PetException;
import com.grepp.teamnotfound.infra.error.exception.UserException;
import com.grepp.teamnotfound.infra.error.exception.code.PetErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.VaccinationErrorCode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final PetRepository petRepository;
    private final ScheduleService scheduleService;

    ModelMapper modelMapper = new ModelMapper();

    private static final EnumMap<VaccineName, Integer> boosterCountMap =
        new EnumMap<>(Map.of(
            VaccineName.DHPPL, 5,
            VaccineName.CORONAVIRUS, 2,
            VaccineName.KENNEL_COUGH, 2,
            VaccineName.RABIES, 2,
            VaccineName.INFLUENZA, 1
        ));

    /**
     * 조회
     */
    public List<VaccinationDto> findAll() {
        List<Vaccination> vaccinations = vaccinationRepository.findAll();

        return vaccinations.stream()
            .map(VaccinationDto::fromEntity)
            .toList();
    }

    public List<VaccinationDto> findPetVaccination(Long petId) {
        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));

        List<Vaccination> vaccinations = vaccinationRepository.findByPet(pet.getPetId());
        return vaccinations.stream().map(VaccinationDto::fromEntity).toList();
    }


    /**
     * 생성 및 수정
     */
    @Transactional
    public Long create(VaccinationDto vaccinationDTO) {
        Vaccination vaccination = modelMapper.map(vaccinationDTO, Vaccination.class);

        vaccinationRepository.save(vaccination);

        return vaccination.getVaccinationId();
    }

    @Transactional
    public void savePetVaccinations(Long petId, List<VaccineWriteRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return;
        }

        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));

        // 1. 기존 반려건의 예방접종 기록 가져오기
        List<Vaccination> existing = vaccinationRepository.findByPet(pet.getPetId());
        Map<VaccineName, Vaccination> existingMap = existing.stream().collect(Collectors.toMap(v -> v.getVaccine().getName(), v -> v));

        // 2. 기록된 예방접종의 백신 이름을 리스트로 만들기
        Set<VaccineName> requestedNames = requests.stream()
            .map(VaccineWriteRequest::getName)
            .collect(Collectors.toSet());

        // 3. 새 요청에 없는 백신은 기존 예방접종 기록에서 삭제 (soft-delete)
        for (Vaccination v : existing) {
            if (!requestedNames.contains(v.getVaccine().getName())) {
                vaccinationRepository.softDeleteOne(v.getVaccinationId(), OffsetDateTime.now());
            }
        }

        // 4. 요청 백신을 모두 다시 저장 또는 업데이트
        for (VaccineWriteRequest dto : requests) {
            Vaccine vaccine = vaccineRepository.findByName(dto.getName())
                .orElseThrow(() -> new BusinessException(VaccinationErrorCode.VACCINE_NOT_FOUND));

            VaccineName name = dto.getName();
            VaccineType type = dto.getVaccineType();
            Integer count = dto.getCount();
            Integer expectedBooster = boosterCountMap.getOrDefault(name, -1);

            switch (type) {
                case FIRST:
                    if (count != 1) throw new BusinessException(VaccinationErrorCode.VACCINATION_COUNT_MISMATCH);
                    break;

                case BOOSTER:
                    if (name.equals(VaccineName.INFLUENZA)) {
                        if (count != 1) {
                            throw new BusinessException(VaccinationErrorCode.VACCINATION_COUNT_MISMATCH);
                        }
                    } else {
                        if (count < 1 || count > expectedBooster) {
                            throw new BusinessException(VaccinationErrorCode.VACCINATION_COUNT_MISMATCH);
                        }
                    }
                    break;

                case ADDITIONAL:
                    if (count <= expectedBooster) throw new BusinessException(VaccinationErrorCode.VACCINATION_COUNT_MISMATCH);
                    break;

                default:
                    throw new BusinessException(VaccinationErrorCode.VACCINATION_TYPE_NOT_FOUND);
            }

            if (dto.getVaccineAt().isAfter(LocalDate.now())) {
                throw new BusinessException(VaccinationErrorCode.VACCINATION_COUNT_MISMATCH);
            }

            Vaccination vaccination = existingMap.get(dto.getName());
            if (vaccination != null) {
                // 존재하면 수정
                modelMapper.map(dto, vaccination);
                vaccination.setUpdatedAt(OffsetDateTime.now());
            } else {
                // 존재하지 않으면 새로 생성
                Vaccination newVaccination = modelMapper.map(dto, Vaccination.class);
                newVaccination.setVaccine(vaccine);
                newVaccination.setPet(pet);
                vaccinationRepository.save(newVaccination);
            }
        }
    }

    /**
     * 삭제
     */
    @Transactional
    public void softDelete(Long petId) {
        vaccinationRepository.softDeleteAll(petId, OffsetDateTime.now());
    }

    @Transactional
    public void createVaccinationSchedule(Long petId, Long userId){
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetException(PetErrorCode.PET_NOT_FOUND));
        if(!pet.getUser().getUserId().equals(userId)) throw new UserException(UserErrorCode.USER_ACCESS_DENIED);

        List<Vaccination> vaccinationDtos = vaccinationRepository.findByPet(petId);

        for(Vaccination dto: vaccinationDtos) {

            Vaccine vaccine = vaccineRepository.findById(dto.getVaccine().getVaccineId())
                    .orElseThrow(() -> new BusinessException(VaccinationErrorCode.VACCINE_NOT_FOUND));

            // 각 백신에 맞는 일정 등록 로직
            if (dto.getVaccineType().equals(VaccineType.BOOSTER)) {
                // 보강 접종(1년 단위)
                ScheduleCreateRequestDto scheduleDto = ScheduleCreateRequestDto.builder()
                        .userId(pet.getUser().getUserId())
                        .petId(pet.getPetId())
                        .name(vaccine.getName() + " 다음 접종일")
                        .date(dto.getVaccineAt().plusMonths(vaccine.getAdditionalCycle()))
                        .cycle(ScheduleCycle.YEAR)
                        .cycleEnd(dto.getVaccineAt().plusYears(31))
                        .build();
                scheduleService.createSchedule(scheduleDto);

            } else if (dto.getCount() <= vaccine.getBoosterCount()) {
                // 추가 접종(2주 단위)
                LocalDate cycleEndDate = dto.getVaccineAt().plusWeeks((long) (vaccine.getBoosterCount() - dto.getCount()) * vaccine.getBoosterCycle());
                ScheduleCreateRequestDto scheduleDto = ScheduleCreateRequestDto.builder()
                        .userId(pet.getUser().getUserId())
                        .petId(pet.getPetId()).name(vaccine.getName() + " 다음 접종일")
                        .date(dto.getVaccineAt().plusWeeks(vaccine.getBoosterCycle()))
                        .cycle(ScheduleCycle.TWO_WEEK)
                        .cycleEnd(cycleEndDate)
                        .build();
                scheduleService.createSchedule(scheduleDto);

                // 보강 접종 완료 후 추가 접종(1년 단위)
                ScheduleCreateRequestDto scheduleDto2 = ScheduleCreateRequestDto.builder()
                        .userId(pet.getUser().getUserId())
                        .petId(pet.getPetId()).name(vaccine.getName() + " 다음 접종일")
                        .date(cycleEndDate.plusYears(1))
                        .cycle(ScheduleCycle.YEAR)
                        .cycleEnd(cycleEndDate.plusYears(31))
                        .build();
                scheduleService.createSchedule(scheduleDto2);
            }
        }
    }
}
