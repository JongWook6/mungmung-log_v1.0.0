package com.grepp.teamnotfound.app.model.pet;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.app.model.pet.dto.PetDto;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.pet.repository.PetRepository;
import com.grepp.teamnotfound.app.model.vaccination.VaccineService;
import com.grepp.teamnotfound.app.model.vaccination.code.VaccineType;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccinationRepository;
import com.grepp.teamnotfound.app.model.vaccination.repository.VaccineRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PetServiceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetService petService;
    @Autowired
    private VaccineService vaccineService;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private VaccineRepository vaccineRepository;
    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Test
    void testFindAll() {
        List<PetDto> result = petService.findAll();

        result.forEach(p -> System.out.println("PetDTO" + p));
    }

    @Test
    void testFindByUserId() {
        List<PetDto> result = petService.findByUserId(1L);

        result.forEach(p -> System.out.println("PetDTO" + p));
    }

    @Test
    void testFindOne() {
        PetDto result = petService.findOne(1L);

        System.out.println("PetDTO" + result);
    }

    @Test
    void testCreatePet() throws Exception {
        PetDto petDTO = new PetDto();
        petDTO.setName("뽀삐");
        petDTO.setAge(3);
        petDTO.setBirthday(LocalDate.of(2020, 1, 1));
        petDTO.setMetday(LocalDate.of(2021, 3, 1));
        petDTO.setWeight(4.2);
        petDTO.setSize(PetSize.SMALL);
        petDTO.setBreed(PetType.BOXER);
        petDTO.setSex(true);
        petDTO.setIsNeutered(true);
        petDTO.setUser(1L);
        petDTO.setImages(new ArrayList<>());

        Long createdPetId = petService.create(petDTO);
        Pet savedPet = petRepository.findById(createdPetId).orElse(null);
        assertThat(savedPet.getName()).isEqualTo("뽀삐");

        for (int i=0; i<2; i++) {
            Vaccine vaccine = vaccineRepository.findById((long) i+1).orElse(null);

            Vaccination vaccination = new Vaccination();
            vaccination.setPet(savedPet);
            vaccination.setVaccine(vaccine);
            vaccination.setVaccineAt(LocalDate.now().minusDays(i));
            vaccination.setVaccineType(VaccineType.ADDITIONAL);
            vaccination.setCount(i);
            vaccination.setIsVaccine(true);
            vaccination.setVaccine(vaccine);

            vaccinationRepository.save(vaccination);
        }

        System.out.println("Pet Id: " + createdPetId);
    }

}