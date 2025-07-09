package com.grepp.teamnotfound.app.model.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserImgDto {
    Long userImgId;
    String url;

}
