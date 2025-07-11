package com.grepp.teamnotfound.app.model.vaccination;

import com.grepp.teamnotfound.app.controller.api.mypage.payload.PetEditRequest;
import com.grepp.teamnotfound.app.model.pet.PetService;
import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.vaccination.code.VaccineType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VaccinationServiceTest {

    @Autowired
    private VaccinationService vaccinationService;
    @Autowired
    private PetService petService;

    @Test
    void testSavePetVaccination(){
        Pet pet = petService.getPet(1L);
        List<PetEditRequest.Vaccinated> vaccinations = new ArrayList<>();
        vaccinations.add(new PetEditRequest.Vaccinated(1L, LocalDate.now(), VaccineType.FIRST, 1, true));
        vaccinations.add(new PetEditRequest.Vaccinated(2L, LocalDate.now(), VaccineType.FIRST, 1, true));
        vaccinations.add(new PetEditRequest.Vaccinated(3L, LocalDate.now(), VaccineType.FIRST, 1, true));
        vaccinationService.savePetVaccinations(pet, vaccinations);

    }


}