package com.grepp.teamnotfound.app.model.pet.repository;


import com.grepp.teamnotfound.app.model.pet.entity.Pet;
import com.grepp.teamnotfound.app.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet findFirstByUser(User user);

}
