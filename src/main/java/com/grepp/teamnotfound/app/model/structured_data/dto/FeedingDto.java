package com.grepp.teamnotfound.app.model.structured_data.dto;

import com.grepp.teamnotfound.app.model.structured_data.FeedUnit;
import com.grepp.teamnotfound.app.model.structured_data.entity.Feeding;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedingDto {

    private Long feedingId;

    private Double amount;
    private OffsetDateTime mealTime;
    private FeedUnit unit;

    public Feeding toEntity(){
        Feeding feeding = new Feeding();
        feeding.setAmount(this.amount);
        feeding.setMealTime(this.mealTime);
        feeding.setUnit(this.unit);

        return feeding;
    }

}
