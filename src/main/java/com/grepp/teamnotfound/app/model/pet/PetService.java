package com.grepp.teamnotfound.app.model.pet;

import com.grepp.teamnotfound.app.controller.api.mypage.payload.PetCreateRequest;
import com.grepp.teamnotfound.app.controller.api.mypage.payload.PetCreateRequest.Vaccinated;
import com.grepp.teamnotfound.app.controller.api.mypage.payload.PetEditRequest;
import com.grepp.teamnotfound.app.controller.api.mypage.payload.VaccinatedItem;
import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.app.model.pet.dto.PetDto;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetImgRepository;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDto;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccinationRepository;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccineRepository;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.PetErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.VaccinationErrorCode;
import com.grepp.teamnotfound.util.NotFoundException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final VaccinationRepository vaccinationRepository;
    private final VaccineRepository vaccineRepository;

    public List<PetDto> findAll() {
        List<Pet> pets = petRepository.findAll();

        return pets.stream()
            .map(pet -> mapToDTO(pet))
            .toList();

    }

    public List<PetDto> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        List<Pet> pets = petRepository.findAllByUser(user);

        return pets.stream()
            .map(pet -> mapToDTO(pet))
            .toList();
    }

    public PetDto findOne(Long petId) {
        return petRepository.findById(petId)
                .map(pet -> mapToDTO(pet))
                .orElseThrow(NotFoundException::new);
    }
    
    @Transactional
    public Long create(PetCreateRequest request) {
        User user = userRepository.findById(request.getUser())
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        Pet pet = new Pet();
        updatePetDetails(pet, request.getRegistNumber(), request.getBirthday(), request.getMetday(),
            request.getName(), request.getBreed(), request.getSize(), request.getSex(), request.getIsNeutered());
        pet.setUser(user);

        petRepository.save(pet);

        saveVaccinations(pet, request.getVaccinations());

        return pet.getPetId();
    }

    @Transactional
    public Long update(Long petId, PetEditRequest request) {
        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new BusinessException(PetErrorCode.PET_NOT_FOUND));

        updatePetDetails(pet, request.getRegistNumber(), request.getBirthday(), request.getMetday(),
            request.getName(), request.getBreed(), request.getSize(), request.getSex(), request.getIsNeutered());
        pet.setUpdatedAt(OffsetDateTime.now());

        petRepository.save(pet);

        vaccinationRepository.softDelete(petId, OffsetDateTime.now());
        saveVaccinations(pet, request.getVaccinations());

        return pet.getPetId();
    }

    @Transactional
    public void delete(Long petId) {
        OffsetDateTime now = OffsetDateTime.now();
        petRepository.softDelete(petId, now);
        vaccinationRepository.softDelete(petId, now);
    }

    private Integer calculateAge(LocalDate birthday) {
        if (birthday == null) {
            return null;
        }
        Period period = Period.between(birthday, LocalDate.now());
        return period.getYears() * 12 + period.getMonths();
    }

    private void updatePetDetails(Pet pet, String registNumber, LocalDate birthday, LocalDate metday,
        String name, PetType breed, PetSize size, Boolean sex, Boolean isNeutered) {
        pet.setRegistNumber(registNumber);
        pet.setBirthday(birthday);
        pet.setMetday(metday);
        pet.setName(name);
        pet.setAge(calculateAge(birthday));
        pet.setBreed(breed);
        pet.setSize(size);
        pet.setSex(sex);
        pet.setIsNeutered(isNeutered);
    }

    private void saveVaccinations(Pet pet, List<? extends VaccinatedItem> vaccinatedItems) {
        if (vaccinatedItems != null && !vaccinatedItems.isEmpty()) {
            for (VaccinatedItem item : vaccinatedItems) {
                Vaccine vaccine = vaccineRepository.findById(item.getVaccineId())
                    .orElseThrow(() -> new BusinessException(VaccinationErrorCode.VACCINE_NOT_FOUND));

                Vaccination vaccination = new Vaccination();
                vaccination.setPet(pet);
                vaccination.setVaccineAt(item.getVaccineAt());
                vaccination.setVaccineType(item.getVaccineType());
                vaccination.setCount(item.getCount());
                vaccination.setIsVaccine(item.getIsVaccine());
                vaccination.setVaccine(vaccine);

                vaccinationRepository.save(vaccination);
            }
        }
    }

    private PetDto mapToDTO(Pet pet) {
        PetDto petDTO = new PetDto();
        petDTO.setPetId(pet.getPetId());
        petDTO.setRegistNumber(pet.getRegistNumber());
        petDTO.setBirthday(pet.getBirthday());
        petDTO.setMetday(pet.getMetday());
        petDTO.setName(pet.getName());
        petDTO.setAge(pet.getAge());
        petDTO.setBreed(pet.getBreed());
        petDTO.setSize(pet.getSize());
        petDTO.setWeight(pet.getWeight());
        petDTO.setSex(pet.getSex());
        petDTO.setIsNeutered(pet.getIsNeutered());
        petDTO.setUser(Optional.ofNullable(pet.getUser()).map(User::getUserId).orElse(null));

        List<Vaccination> vaccinations = vaccinationRepository.findAllByPetEquals(pet);
        petDTO.setVaccinations(vaccinations.stream()
            .map(this::mapToVaccinationDTO)
            .collect(Collectors.toList()));

        return petDTO;
    }

    private VaccinationDto mapToVaccinationDTO(Vaccination vaccination) {
        VaccinationDto vaccinationDTO = new VaccinationDto();
        vaccinationDTO.setVaccinationId(vaccination.getVaccinationId());
        vaccinationDTO.setVaccineAt(vaccination.getVaccineAt());
        vaccinationDTO.setVaccineType(vaccination.getVaccineType());
        vaccinationDTO.setCount(vaccination.getCount());
        vaccinationDTO.setIsVaccine(vaccination.getIsVaccine());
        vaccinationDTO.setVaccine(vaccination.getVaccine());

        return vaccinationDTO;
    }
}
