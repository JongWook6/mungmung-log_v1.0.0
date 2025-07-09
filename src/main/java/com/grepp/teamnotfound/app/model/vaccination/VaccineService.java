package com.grepp.teamnotfound.app.model.vaccination;


import com.grepp.teamnotfound.app.model.vaccination.dto.VaccineDto;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccinationRepository;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccineRepository;
import com.grepp.teamnotfound.util.NotFoundException;
import com.grepp.teamnotfound.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final VaccinationRepository vaccinationRepository;

    public VaccineService(final VaccineRepository vaccineRepository,
            final VaccinationRepository vaccinationRepository) {
        this.vaccineRepository = vaccineRepository;
        this.vaccinationRepository = vaccinationRepository;
    }

    public List<VaccineDto> findAll() {
        final List<Vaccine> vaccines = vaccineRepository.findAll(Sort.by("vaccineId"));
        return vaccines.stream()
                .map(vaccine -> mapToDTO(vaccine, new VaccineDto()))
                .toList();
    }

    public VaccineDto get(Long vaccineId) {
        return vaccineRepository.findById(vaccineId)
                .map(vaccine -> mapToDTO(vaccine, new VaccineDto()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VaccineDto vaccineDTO) {
        final Vaccine vaccine = new Vaccine();
        mapToEntity(vaccineDTO, vaccine);
        return vaccineRepository.save(vaccine).getVaccineId();
    }

    public void update(final Long vaccineId, final VaccineDto vaccineDTO) {
        final Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vaccineDTO, vaccine);
        vaccineRepository.save(vaccine);
    }

    public void delete(final Long vaccineId) {
        vaccineRepository.deleteById(vaccineId);
    }

    private VaccineDto mapToDTO(final Vaccine vaccine, final VaccineDto vaccineDTO) {
        vaccineDTO.setVaccineId(vaccine.getVaccineId());
        vaccineDTO.setName(vaccine.getName());
        vaccineDTO.setPeriod(vaccine.getPeriod());
        vaccineDTO.setBoosterCycle(vaccine.getBoosterCycle());
        vaccineDTO.setBoosterCount(vaccine.getBoosterCount());
        vaccineDTO.setAdditionalCycle(vaccine.getAdditionalCycle());
        return vaccineDTO;
    }

    private Vaccine mapToEntity(final VaccineDto vaccineDTO, final Vaccine vaccine) {
        vaccine.setName(vaccineDTO.getName());
        vaccine.setPeriod(vaccineDTO.getPeriod());
        vaccine.setBoosterCycle(vaccineDTO.getBoosterCycle());
        vaccine.setBoosterCount(vaccineDTO.getBoosterCount());
        vaccine.setAdditionalCycle(vaccineDTO.getAdditionalCycle());
        return vaccine;
    }

    public ReferencedWarning getReferencedWarning(final Long vaccineId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Vaccine vaccine = vaccineRepository.findById(vaccineId).orElseThrow(NotFoundException::new);
        final Vaccination vaccineVaccination = vaccinationRepository.findFirstByVaccine(vaccine);
        if (vaccineVaccination != null) {
            referencedWarning.setKey("vaccine.vaccination.vaccine.referenced");
            referencedWarning.addParam(vaccineVaccination.getVaccinationId());
            return referencedWarning;
        }
        return null;
    }

}
