package com.grepp.teamnotfound.infra.util.file;

import com.grepp.teamnotfound.app.model.board.entity.ArticleImg;
import com.grepp.teamnotfound.app.model.board.repository.ArticleImgRepository;
import com.grepp.teamnotfound.app.model.pet.entity.PetImg;
import com.grepp.teamnotfound.app.model.pet.repository.PetImgRepository;
import com.grepp.teamnotfound.app.model.user.entity.UserImg;
import com.grepp.teamnotfound.app.model.user.repository.UserImgRepository;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageCleanScheduler {

    private final ArticleImgRepository articleImgRepository;
    private final PetImgRepository petImgRepository;
    private final UserImgRepository userImgRepository;
    private final GoogleStorageManager fileManager;

    // 소프트 딜리트 후 이미지를 양구 삭제할 기간
    @Value("${image.clean.period-days}")
    private int softDeletePeriodDays;

    // 매일 오전 1시에 실행
//    @Scheduled(cron = "0 0 1 * * *")
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cleanSoftDeletedImages() {
        log.info("Start image clean up scheduler...");

        OffsetDateTime thresholdTime = OffsetDateTime.now().minusDays(softDeletePeriodDays);

        // 1. ArticleImg 정리
        List<ArticleImg> articleImgsToClean = articleImgRepository.findByDeletedAtBefore(thresholdTime);
        if (!articleImgsToClean.isEmpty()) {
            List<FileDto> articleFileDtos = articleImgsToClean.stream()
                .map(FileDto::fromArticleImg)
                .toList();

            fileManager.delete(articleFileDtos);
            log.info("Images clean up processing finished for {} articles. ",
                articleImgsToClean.size());
        } else {
            log.info("No article images to clean up.");
        }

        // 2. PetImg 정리
        List<PetImg> petImgsToClean = petImgRepository.findByDeletedAtBefore(thresholdTime);
        if (!petImgsToClean.isEmpty()) {
            List<FileDto> petFileDtos = petImgsToClean.stream()
                .map(FileDto::fromPetImg)
                .toList();

            fileManager.delete(petFileDtos);
            log.info("Image clean up processing finished for {} pets.", petImgsToClean.size());
        } else {
            log.info("No pet images to clean up.");
        }

        // 3. UserImg 정리
        List<UserImg> userImgsToClean = userImgRepository.findByDeletedAtBefore(thresholdTime);
        if (!userImgsToClean.isEmpty()) {
            List<FileDto> userFileDtos = userImgsToClean.stream()
                .map(FileDto::fromUserImg)
                .toList();

            fileManager.delete(userFileDtos);
            log.info("Image clean up processing finished for {} users.", userImgsToClean.size());
        } else {
            log.info("No user images to clean up.");
        }

        log.info("Finish image clean up scheduler.");
    }
}
