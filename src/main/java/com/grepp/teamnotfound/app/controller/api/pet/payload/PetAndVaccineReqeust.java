package com.grepp.teamnotfound.app.controller.api.pet.payload;

import com.grepp.teamnotfound.app.model.pet.code.PetSize;
import com.grepp.teamnotfound.app.model.pet.code.PetType;
import com.grepp.teamnotfound.app.model.pet.dto.PetImgDTO;
import com.grepp.teamnotfound.app.model.vaccination.dto.VaccinationDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetAndVaccineReqeust {
    private Long petId;
    private String registNumber;
    private LocalDate birthday;
    private LocalDate metday;
    private String name;
    private Integer age;
    private PetType breed;
    private PetSize size;
    private Double weight;
    private Boolean sex;
    private Boolean isNeutered;
    private Long user;
    private List<PetImgDTO> images;
    private List<VaccinationDTO> vaccinations;

}
