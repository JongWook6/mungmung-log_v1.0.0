package com.grepp.teamnotfound.app.model.vaccination.repository;


import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccination;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    Vaccination findFirstByVaccine(Vaccine vaccine);

    Vaccination findFirstByPet(Pet pet);

    List<Vaccination> findAllByPetEquals(Pet pet);

}
