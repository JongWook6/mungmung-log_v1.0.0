package com.grepp.teamnotfound.app.model.user;

import com.grepp.teamnotfound.app.controller.api.mypage.payload.PasswordRequestDto;
import com.grepp.teamnotfound.app.controller.api.mypage.payload.UserWriteRequest;
import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.user.dto.RegisterCommand;
import com.grepp.teamnotfound.app.model.user.dto.UserDto;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.entity.UserImg;
import com.grepp.teamnotfound.app.model.user.repository.UserImgRepository;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.code.ImgType;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.CommonException;
import com.grepp.teamnotfound.infra.error.exception.code.CommonErrorCode;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import com.grepp.teamnotfound.infra.util.file.FileDto;
import com.grepp.teamnotfound.infra.util.file.GoogleStorageManager;
import com.grepp.teamnotfound.infra.util.mail.MailService;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UserImgRepository userImgRepository;
    private final GoogleStorageManager fileManager;

    ModelMapper modelMapper = new ModelMapper();

    public UserDto findByUserId(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setImgUrl(getProfileImgPath(user.getUserId()));

        return userDto;
    }

    @Transactional
    public Long registerAdmin(RegisterCommand request) {

        validateEmailDuplication(request.getEmail());
        validateNicknameDuplication(request.getNickname());

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_ADMIN)
                .provider("local")
                .build();

        userRepository.save(user);
        return user.getUserId();
    }


    @Transactional
    public Long registerUser(RegisterCommand request) {

        validateEmailDuplication(request.getEmail());
        validateNicknameDuplication(request.getNickname());

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .provider("local")
                .build();

        userRepository.save(user);
        return user.getUserId();
    }

    public void sendEmail(String email) {
        mailService.sendVerificationEmail(email);
    }

    @Transactional(readOnly = true)
    public void validateEmailDuplication(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new BusinessException(UserErrorCode.USER_EMAIL_ALREADY_EXISTS);
        });
    }

    @Transactional(readOnly = true)
    public void validateNicknameDuplication(String nickname) {
        userRepository.findByNickname(nickname).ifPresent(user -> {
            throw new BusinessException(UserErrorCode.USER_NICKNAME_ALREADY_EXISTS);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserDto updateUser(Long userId, UserWriteRequest request, List<MultipartFile> images) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        validateNicknameExceptMe(request.getNickname(), user.getUserId()); // 자신 제외 닉네임 중복 검사

        // 로컬 유저만 비밀번호 변경 가능 & 비밀번호 타입 검증
        String password = request.getPassword();
        if (user.getProvider().equals("local") && password != null && !password.isBlank()) {

            if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$")) {
                throw new BusinessException(UserErrorCode.INVALID_PASSWORD_FORMAT);
            }

            user.setPassword(passwordEncoder.encode(password));
        }
        user.setNickname(request.getNickname());
        user.setUpdatedAt(OffsetDateTime.now());

        userRepository.save(user);
        uploadAndSaveImgs(images, user);

        UserDto dto = UserDto.fromEntity(user);
        dto.setImgUrl(getProfileImgPath(user.getUserId()));

        return dto;
    }

    @Transactional(readOnly = true)
    public Boolean matchPassword(Long userId, PasswordRequestDto request) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        user.setDeletedAt(OffsetDateTime.now());
        userRepository.save(user);

        userImgRepository.softDeleteUserImg(user.getUserId());
    }


    public String getProfileImgPath(Long userId) {
        String profileImgPath = null;
        Optional<UserImg> optionalUserImg = userImgRepository.findByUser_UserIdAndDeletedAtIsNull(userId);
        if (optionalUserImg.isPresent()) {
            UserImg userImg = optionalUserImg.get();
            profileImgPath = userImg.getSavePath() + userImg.getRenamedName();
        }
        return profileImgPath;
    }

    private void uploadAndSaveImgs(List<MultipartFile> images, User user) {

        if (images == null || images.isEmpty()) {
            return;
        }

        // 기존 이미지 삭제
        userImgRepository.softDeleteUserImg(user.getUserId());

        try {
            // 버킷에 업로드
            FileDto fileDto = fileManager.upload(images, "user")
                .stream()
                .findFirst()
                .orElseThrow(() -> new CommonException(CommonErrorCode.FILE_UPLOAD_FAILED));

            // 이미지 객체 생성 또는 수정
            UserImg userImg = new UserImg();
            userImg.setSavePath(fileDto.savePath());
            userImg.setOriginName(fileDto.originName());
            userImg.setRenamedName(fileDto.renamedName());
            userImg.setType(ImgType.THUMBNAIL);
            userImg.setUser(user);

            userImgRepository.save(userImg);

        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private void validateNicknameExceptMe(String newNickname, Long currentUserId) {
        userRepository.findByNickname(newNickname).ifPresent(existingUser -> {
            if (!existingUser.getUserId().equals(currentUserId)) {
                throw new BusinessException(UserErrorCode.USER_NICKNAME_ALREADY_EXISTS);
            }
        });
    }
}
