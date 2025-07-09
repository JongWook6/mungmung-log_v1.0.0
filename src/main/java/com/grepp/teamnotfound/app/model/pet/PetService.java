package com.grepp.teamnotfound.app.model.pet;

import com.grepp.teamnotfound.app.model.pet.dto.PetDTO;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.entity.PetImg;
import com.grepp.teamnotfound.app.model.pet.repository.PetImgRepository;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDTO;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccinationRepository;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccineRepository;
import com.grepp.teamnotfound.util.NotFoundException;
import com.grepp.teamnotfound.util.ReferencedWarning;
import java.util.List;
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
    private final PetImgRepository petImgRepository;
    private final VaccinationRepository vaccinationRepository;
    private final VaccineRepository vaccineRepository;

    public List<PetDTO> findAll() {
        List<Pet> pets = petRepository.findAll();

        return pets.stream()
            .map(pet -> mapToDTO(pet))
            .toList();

    }

    public List<PetDTO> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        List<Pet> pets = petRepository.findAllByUser(user);

        return pets.stream()
            .map(pet -> mapToDTO(pet))
            .toList();
    }

    public PetDTO findOne(Long petId) {
        return petRepository.findById(petId)
                .map(pet -> mapToDTO(pet))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(PetDTO petDTO) {
        Pet pet = new Pet();
        mapToEntity(petDTO, pet);
        petRepository.save(pet);

        if (petDTO.getVaccinations() != null) {
            for (VaccinationDTO dto : petDTO.getVaccinations()) {
                Vaccine vaccine = vaccineRepository.findById(dto.getVaccine().getVaccineId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 백신이 존재하지 않습니다."));

                Vaccination vaccination = new Vaccination();
                vaccination.setPet(pet);
                vaccination.setVaccineAt(dto.getVaccineAt());
                vaccination.setVaccineType(dto.getVaccineType());
                vaccination.setCount(dto.getCount());
                vaccination.setIsVaccine(dto.getIsVaccine());
                vaccination.setVaccine(vaccine);

                vaccinationRepository.save(vaccination);
            }
        }

        return pet.getPetId();
    }
//
//    public void update(final Long petId, final PetDTO petDTO) {
//        final Pet pet = petRepository.findById(petId)
//                .orElseThrow(NotFoundException::new);
//        mapToEntity(petDTO, pet);
//        petRepository.save(pet);
//    }
//
//    public void delete(final Long petId) {
//        petRepository.deleteById(petId);
//    }

    private PetDTO mapToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
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
        petDTO.setUser(pet.getUser() == null ? null : pet.getUser().getUserId());

        List<Vaccination> vaccinations = vaccinationRepository.findAllByPetEquals(pet);

        petDTO.setVaccinations(vaccinations.stream()
            .map(this::mapToVaccinationDTO)
            .collect(Collectors.toList()));

        return petDTO;
    }

    private VaccinationDTO mapToVaccinationDTO(Vaccination vaccination) {
        VaccinationDTO vaccinationDTO = new VaccinationDTO();
        vaccinationDTO.setVaccinationId(vaccination.getVaccinationId());
        vaccinationDTO.setVaccineAt(vaccination.getVaccineAt());
        vaccinationDTO.setVaccineType(vaccination.getVaccineType());
        vaccinationDTO.setCount(vaccination.getCount());
        vaccinationDTO.setIsVaccine(vaccination.getIsVaccine());
        vaccinationDTO.setVaccine(vaccination.getVaccine());

        return vaccinationDTO;
    }

    private Pet mapToEntity(final PetDTO petDTO, final Pet pet) {
        pet.setRegistNumber(petDTO.getRegistNumber());
        pet.setBirthday(petDTO.getBirthday());
        pet.setMetday(petDTO.getMetday());
        pet.setName(petDTO.getName());
        pet.setAge(petDTO.getAge());
        pet.setBreed(petDTO.getBreed());
        pet.setSize(petDTO.getSize());
        pet.setWeight(petDTO.getWeight());
        pet.setSex(petDTO.getSex());
        pet.setIsNeutered(petDTO.getIsNeutered());
        final User user = petDTO.getUser() == null ? null : userRepository.findById(petDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        pet.setUser(user);
        return pet;
    }

    public ReferencedWarning getReferencedWarning(final Long petId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Pet pet = petRepository.findById(petId)
                .orElseThrow(NotFoundException::new);

        final PetImg petPetImg = petImgRepository.findFirstByPet(pet);
        if (petPetImg != null) {
            referencedWarning.setKey("pet.petImg.pet.referenced");
            referencedWarning.addParam(petPetImg.getPetImgId());
            return referencedWarning;
        }

        final Vaccination petVaccination = vaccinationRepository.findFirstByPet(pet);
        if (petVaccination != null) {
            referencedWarning.setKey("pet.vaccination.pet.referenced");
            referencedWarning.addParam(petVaccination.getVaccinationId());
            return referencedWarning;
        }

        return null;
    }

}
