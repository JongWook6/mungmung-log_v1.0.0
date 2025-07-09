package com.grepp.teamnotfound.app.model.vaccination;


import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDto;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccinationRepository;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccineRepository;
import com.grepp.teamnotfound.util.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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

    public List<VaccinationDto> findAll() {
        final List<Vaccination> vaccinations = vaccinationRepository.findAll(Sort.by("vaccinationId"));
        return vaccinations.stream()
                .map(vaccination -> mapToDTO(vaccination, new VaccinationDto()))
                .toList();
    }

    public VaccinationDto get(Long vaccinationId) {
        return vaccinationRepository.findById(vaccinationId)
                .map(vaccination -> mapToDTO(vaccination, new VaccinationDto()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(VaccinationDto vaccinationDTO) {
        Vaccination vaccination = new Vaccination();
        mapToEntity(vaccinationDTO, vaccination);
        return vaccinationRepository.save(vaccination).getVaccinationId();
    }

    public void update(final Long vaccinationId, final VaccinationDto vaccinationDTO) {
        final Vaccination vaccination = vaccinationRepository.findById(vaccinationId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vaccinationDTO, vaccination);
        vaccinationRepository.save(vaccination);
    }

    public void delete(final Long vaccinationId) {
        vaccinationRepository.deleteById(vaccinationId);
    }

    private VaccinationDto mapToDTO(final Vaccination vaccination,
            final VaccinationDto vaccinationDTO) {
        vaccinationDTO.setVaccinationId(vaccination.getVaccinationId());
        vaccinationDTO.setVaccineAt(vaccination.getVaccineAt());
        vaccinationDTO.setVaccineType(vaccination.getVaccineType());
        vaccinationDTO.setCount(vaccination.getCount());
        vaccinationDTO.setIsVaccine(vaccination.getIsVaccine());
        vaccinationDTO.setVaccinationId(vaccination.getVaccinationId());
        return vaccinationDTO;
    }

    private Vaccination mapToEntity(
        VaccinationDto vaccinationDTO,
        Vaccination vaccination
    ) {
        vaccination.setVaccineAt(vaccinationDTO.getVaccineAt());
        vaccination.setVaccineType(vaccinationDTO.getVaccineType());
        vaccination.setCount(vaccinationDTO.getCount());
        vaccination.setIsVaccine(vaccinationDTO.getIsVaccine());
        Vaccine vaccine = vaccinationDTO.getVaccine() == null ? null : vaccineRepository.findById(vaccinationDTO.getVaccine().getVaccineId())
                .orElseThrow(() -> new NotFoundException("vaccine not found"));
        vaccination.setVaccine(vaccine);
        Pet pet = vaccinationDTO.getPet() == null ? null : petRepository.findById(vaccinationDTO.getPet())
                .orElseThrow(() -> new NotFoundException("pet not found"));
        vaccination.setPet(pet);
        return vaccination;
    }

}
