package com.grepp.teamnotfound.infra.util.file;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractFileManager {

    public List<FileDto> upload(List<MultipartFile> files, String depth) throws IOException {
        List<FileDto> fileDtos = new ArrayList<>();

        if (files.isEmpty() || files.getFirst().isEmpty()) {
            return fileDtos;
        }

        String savePath = createSavePath(depth);

        for (MultipartFile file : files) {
            String originName = file.getOriginalFilename();
            String renamedName = generateRenamedFileName(originName);
            FileDto fileDto = new FileDto(originName, renamedName, depth, savePath);
            fileDtos.add(fileDto);
            uploadFile(file, fileDto);
        }

        return fileDtos;
    }

    protected void uploadFile(MultipartFile file, FileDto fileDto) throws IOException {

    }


    protected String generateRenamedFileName(String originName) {
        String ext = originName.substring(originName.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }

    protected String createSavePath(String depth) {
        // NOTE 혹시 이정도로 depth 를 나누는 건 데이터 관리에 너무 번잡할까?
        LocalDate now = LocalDate.now();
        return depth + "/" +
            now.getYear() + "/" +
            now.getMonth() + "/" +
            now.getDayOfMonth() + "/";
    }

}
