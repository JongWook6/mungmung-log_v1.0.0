package com.grepp.teamnotfound.app.model.pet;


import com.grepp.teamnotfound.app.model.pet.dto.PetImgDto;
import com.grepp.teamnotfound.app.model.pet.entity.PetImg;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class PetImgService {

//    private final PetImgRepository petImgRepository;
//    private final PetRepository petRepository;
//
//    public PetImgService(final PetImgRepository petImgRepository,
//        final PetRepository petRepository) {
//        this.petImgRepository = petImgRepository;
//        this.petRepository = petRepository;
//    }
//
//    public List<PetImgDto> findAll() {
//        final List<PetImg> petImgs = petImgRepository.findAll(Sort.by("petImgId"));
//        return petImgs.stream()
//            .map(petImg -> mapToDTO(petImg, new PetImgDto()))
//            .toList();
//    }
//
//    public PetImgDto get(final Long petImgId) {
//        return petImgRepository.findById(petImgId)
//            .map(petImg -> mapToDTO(petImg, new PetImgDto()))
//            .orElseThrow(NotFoundException::new);
//    }
//
//    public Long create(final PetImgDto PetImgDto) {
//        final PetImg petImg = new PetImg();
//        mapToEntity(PetImgDto, petImg);
//        return petImgRepository.save(petImg).getPetImgId();
//    }
//
//    public void update(final Long petImgId, final PetImgDto PetImgDto) {
//        final PetImg petImg = petImgRepository.findById(petImgId)
//            .orElseThrow(NotFoundException::new);
//        mapToEntity(PetImgDto, petImg);
//        petImgRepository.save(petImg);
//    }
//
//    public void delete(final Long petImgId) {
//        petImgRepository.deleteById(petImgId);
//    }
//
//    private PetImgDto mapToDTO(final PetImg petImg, final PetImgDto PetImgDto) {
//        PetImgDto.setPetImgId(petImg.getPetImgId());
//        PetImgDto.setSavePath(petImg.getSavePath());
//        PetImgDto.setType(petImg.getType());
//        PetImgDto.setOriginName(petImg.getOriginName());
//        PetImgDto.setRenamedName(petImg.getRenamedName());
//        PetImgDto.setPet(petImg.getPet() == null ? null : petImg.getPet().getPetId());
//        return PetImgDto;
//    }
//
//    private PetImg mapToEntity(final PetImgDto PetImgDto, final PetImg petImg) {
//        petImg.setSavePath(PetImgDto.getSavePath());
//        petImg.setType(PetImgDto.getType());
//        petImg.setOriginName(PetImgDto.getOriginName());
//        petImg.setRenamedName(PetImgDto.getRenamedName());
//        final Pet pet = PetImgDto.getPet() == null ? null : petRepository.findById(PetImgDto.getPet())
//            .orElseThrow(() -> new NotFoundException("pet not found"));
//        petImg.setPet(pet);
//        return petImg;
//    }
//
//    public boolean petExists(final Long petId) {
//        return petImgRepository.existsByPetPetId(petId);
//    }

}

