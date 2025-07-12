package com.grepp.teamnotfound.infra.util.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.grepp.teamnotfound.infra.error.exception.CommonException;
import com.grepp.teamnotfound.infra.error.exception.code.CommonErrorCode;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class GoogleStorageManager extends AbstractFileManager {

    @Value("${google.cloud.storage.bucket}")
    private String bucket;
    private final String storageBaseUrl = "https://storage.googleapis.coom/";

    @Override
    protected void uploadFile(MultipartFile file, FileDto fileDto) throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();

        if (file.getOriginalFilename() == null) {
            throw new CommonException(CommonErrorCode.FILE_INVALID_NAME);
        }

        String renamedName = fileDto.renamedName();
        BlobId blobId = BlobId.of(bucket, fileDto.depth() + "/" + renamedName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
    }

    @Override
    protected String createSavePath(String depth) {
        return storageBaseUrl + bucket + "/" + depth + "/";
    }
}
