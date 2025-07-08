package com.grepp.teamnotfound.app.model.pet;


import com.grepp.teamnotfound.app.model.pet.dto.PetImgDTO;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.entity.PetImg;
import com.grepp.teamnotfound.app.model.pet.repository.PetImgRepository;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PetImgService {

    private final PetImgRepository petImgRepository;
    private final PetRepository petRepository;

    public PetImgService(final PetImgRepository petImgRepository,
            final PetRepository petRepository) {
        this.petImgRepository = petImgRepository;
        this.petRepository = petRepository;
    }

    public List<PetImgDTO> findAll() {
        final List<PetImg> petImgs = petImgRepository.findAll(Sort.by("petImgId"));
        return petImgs.stream()
                .map(petImg -> mapToDTO(petImg, new PetImgDTO()))
                .toList();
    }

    public PetImgDTO get(final Long petImgId) {
        return petImgRepository.findById(petImgId)
                .map(petImg -> mapToDTO(petImg, new PetImgDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PetImgDTO petImgDTO) {
        final PetImg petImg = new PetImg();
        mapToEntity(petImgDTO, petImg);
        return petImgRepository.save(petImg).getPetImgId();
    }

    public void update(final Long petImgId, final PetImgDTO petImgDTO) {
        final PetImg petImg = petImgRepository.findById(petImgId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(petImgDTO, petImg);
        petImgRepository.save(petImg);
    }

    public void delete(final Long petImgId) {
        petImgRepository.deleteById(petImgId);
    }

    private PetImgDTO mapToDTO(final PetImg petImg, final PetImgDTO petImgDTO) {
        petImgDTO.setPetImgId(petImg.getPetImgId());
        petImgDTO.setSavePath(petImg.getSavePath());
        petImgDTO.setType(petImg.getType());
        petImgDTO.setOriginName(petImg.getOriginName());
        petImgDTO.setRenamedName(petImg.getRenamedName());
        petImgDTO.setPet(petImg.getPet() == null ? null : petImg.getPet().getPetId());
        return petImgDTO;
    }

    private PetImg mapToEntity(final PetImgDTO petImgDTO, final PetImg petImg) {
        petImg.setSavePath(petImgDTO.getSavePath());
        petImg.setType(petImgDTO.getType());
        petImg.setOriginName(petImgDTO.getOriginName());
        petImg.setRenamedName(petImgDTO.getRenamedName());
        final Pet pet = petImgDTO.getPet() == null ? null : petRepository.findById(petImgDTO.getPet())
                .orElseThrow(() -> new NotFoundException("pet not found"));
        petImg.setPet(pet);
        return petImg;
    }

    public boolean petExists(final Long petId) {
        return petImgRepository.existsByPetPetId(petId);
    }

}
