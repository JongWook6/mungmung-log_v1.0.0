package com.grepp.teamnotfound.app.model.vaccination.dto;

import com.grepp.teamnotfound.app.model.vaccination.code.VaccineName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccineDto {

    private Long vaccineId;
    private VaccineName name;
    private Integer period;
    private Integer boosterCycle;
    private Integer boosterCount;
    private Integer additionalCycle;

}
