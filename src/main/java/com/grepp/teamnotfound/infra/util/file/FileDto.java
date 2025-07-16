package com.grepp.teamnotfound.infra.util.file;

public record FileDto(
    String originName,
    String renamedName,
    String depth,
    String savePath
) {

}
