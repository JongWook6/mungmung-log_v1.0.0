package com.grepp.teamnotfound.app.model.pet;


import com.grepp.teamnotfound.app.model.pet.dto.PetDTO;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.entity.PetImg;
import com.grepp.teamnotfound.app.model.pet.repository.PetImgRepository;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccinationRepository;
import com.grepp.teamnotfound.util.NotFoundException;
import com.grepp.teamnotfound.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
//    private final ScheduleRepository scheduleRepository;
    private final PetImgRepository petImgRepository;
//    private final NoteRepository noteRepository;
//    private final AiAnalysisRepository aiAnalysisRepository;
    private final VaccinationRepository vaccinationRepository;
//    private final DailyRecommendRepository dailyRecommendRepository;
//    private final WeightRepository weightRepository;
//    private final WalkingRepository walkingRepository;
//    private final FeedingRepository feedingRepository;
//    private final SleepingRepository sleepingRepository;

    public PetService(
            final PetRepository petRepository,
            final UserRepository userRepository,
//            final ScheduleRepository scheduleRepository,
            final PetImgRepository petImgRepository,
//            final NoteRepository noteRepository,
//            final AiAnalysisRepository aiAnalysisRepository,
            final VaccinationRepository vaccinationRepository
//            final DailyRecommendRepository dailyRecommendRepository,
//            final WeightRepository weightRepository,
//            final WalkingRepository walkingRepository,
//            final FeedingRepository feedingRepository,
//            final SleepingRepository sleepingRepository
    ) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
//        this.scheduleRepository = scheduleRepository;
        this.petImgRepository = petImgRepository;
//        this.noteRepository = noteRepository;
//        this.aiAnalysisRepository = aiAnalysisRepository;
        this.vaccinationRepository = vaccinationRepository;
//        this.dailyRecommendRepository = dailyRecommendRepository;
//        this.weightRepository = weightRepository;
//        this.walkingRepository = walkingRepository;
//        this.feedingRepository = feedingRepository;
//        this.sleepingRepository = sleepingRepository;
    }

    public List<PetDTO> findAll() {
        final List<Pet> pets = petRepository.findAll(Sort.by("petId"));
        return pets.stream()
                .map(pet -> mapToDTO(pet, new PetDTO()))
                .toList();
    }

    public PetDTO get(final Long petId) {
        return petRepository.findById(petId)
                .map(pet -> mapToDTO(pet, new PetDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PetDTO petDTO) {
        final Pet pet = new Pet();
        mapToEntity(petDTO, pet);
        return petRepository.save(pet).getPetId();
    }

    public void update(final Long petId, final PetDTO petDTO) {
        final Pet pet = petRepository.findById(petId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(petDTO, pet);
        petRepository.save(pet);
    }

    public void delete(final Long petId) {
        petRepository.deleteById(petId);
    }

    private PetDTO mapToDTO(final Pet pet, final PetDTO petDTO) {
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
        return petDTO;
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
//        final Schedule petSchedule = scheduleRepository.findFirstByPet(pet);
//        if (petSchedule != null) {
//            referencedWarning.setKey("pet.schedule.pet.referenced");
//            referencedWarning.addParam(petSchedule.getScheduleId());
//            return referencedWarning;
//        }
        final PetImg petPetImg = petImgRepository.findFirstByPet(pet);
        if (petPetImg != null) {
            referencedWarning.setKey("pet.petImg.pet.referenced");
            referencedWarning.addParam(petPetImg.getPetImgId());
            return referencedWarning;
        }
//        final Note petNote = noteRepository.findFirstByPet(pet);
//        if (petNote != null) {
//            referencedWarning.setKey("pet.note.pet.referenced");
//            referencedWarning.addParam(petNote.getNoteId());
//            return referencedWarning;
//        }
//        final AiAnalysis petAiAnalysis = aiAnalysisRepository.findFirstByPet(pet);
//        if (petAiAnalysis != null) {
//            referencedWarning.setKey("pet.aiAnalysis.pet.referenced");
//            referencedWarning.addParam(petAiAnalysis.getAnalysisId());
//            return referencedWarning;
//        }
        final Vaccination petVaccination = vaccinationRepository.findFirstByPet(pet);
        if (petVaccination != null) {
            referencedWarning.setKey("pet.vaccination.pet.referenced");
            referencedWarning.addParam(petVaccination.getVaccinationId());
            return referencedWarning;
        }
//        final DailyRecommend petDailyRecommend = dailyRecommendRepository.findFirstByPet(pet);
//        if (petDailyRecommend != null) {
//            referencedWarning.setKey("pet.dailyRecommend.pet.referenced");
//            referencedWarning.addParam(petDailyRecommend.getDailyId());
//            return referencedWarning;
//        }
//        final Weight petWeight = weightRepository.findFirstByPet(pet);
//        if (petWeight != null) {
//            referencedWarning.setKey("pet.weight.pet.referenced");
//            referencedWarning.addParam(petWeight.getWeightId());
//            return referencedWarning;
//        }
//        final Walking petWalking = walkingRepository.findFirstByPet(pet);
//        if (petWalking != null) {
//            referencedWarning.setKey("pet.walking.pet.referenced");
//            referencedWarning.addParam(petWalking.getWalkingId());
//            return referencedWarning;
//        }
//        final Feeding petFeeding = feedingRepository.findFirstByPet(pet);
//        if (petFeeding != null) {
//            referencedWarning.setKey("pet.feeding.pet.referenced");
//            referencedWarning.addParam(petFeeding.getFeedingId());
//            return referencedWarning;
//        }
//        final Sleeping petSleeping = sleepingRepository.findFirstByPet(pet);
//        if (petSleeping != null) {
//            referencedWarning.setKey("pet.sleeping.pet.referenced");
//            referencedWarning.addParam(petSleeping.getSleepingId());
//            return referencedWarning;
//        }
        return null;
    }

}
