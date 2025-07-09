package com.grepp.teamnotfound.app.model.vaccination.dto;

import com.grepp.teamnotfound.app.model.vaccination.code.VaccineType;
import com.grepp.teamnotfound.app.model.vaccination.entity.Vaccine;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDTO {

    private Long pet;
    private Long vaccinationId;
    private LocalDate vaccineAt;
    private VaccineType vaccineType;
    private Integer count;
    private Boolean isVaccine;
    private Vaccine vaccine;

}
