package com.grepp.teamnotfound.infra.util.file;

import com.grepp.teamnotfound.app.model.board.entity.ArticleImg;
import com.grepp.teamnotfound.app.model.pet.entity.PetImg;
import com.grepp.teamnotfound.app.model.user.entity.UserImg;

public record FileDto(
    String originName,
    String renamedName,
    String depth,
    String savePath
) {

    public static FileDto fromArticleImg(ArticleImg articleImg) {
        return new FileDto(
            articleImg.getOriginName(),
            articleImg.getRenamedName(),
            "article",
            articleImg.getSavePath()
        );
    }

    public static FileDto fromPetImg(PetImg petImg) {
        return new FileDto(
            petImg.getOriginName(),
            petImg.getRenamedName(),
            "pet",
            petImg.getSavePath()
        );
    }

    public static FileDto fromUserImg(UserImg userImg) {
        return new FileDto(
            userImg.getOriginName(),
            userImg.getRenamedName(),
            "user",
            userImg.getSavePath()
        );
    }
}
