package fiveguys.Tom.Cafeteria.Server.notification.converter;

import fiveguys.Tom.Cafeteria.Server.auth.dto.LoginRequestDTO;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.kakao.dto.KakaoResponseDTO;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotification;
import fiveguys.Tom.Cafeteria.Server.notification.entity.UserAppNotification;
import fiveguys.Tom.Cafeteria.Server.user.dto.UserResponseDTO;
import fiveguys.Tom.Cafeteria.Server.user.entity.Role;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Slf4j
public class NotificationConverter {
    public static User toUser(KakaoResponseDTO.UserInfoResponseDTO userInfoResponseDTO){
        return User.builder()
                .socialId(userInfoResponseDTO.getSocialId())
                .email(userInfoResponseDTO.getEmail())
                .role(Role.MEMBER)
                .build();
    }
    public static User toUser(LoginRequestDTO.AppleTokenValidateDTO appleTokenValidateDTO){
        return User.builder()
                .socialId(appleTokenValidateDTO.getSocialId())
                .role(Role.MEMBER)
                .build();
    }
    public static User touser(LoginRequestDTO.SignUpDTO signUpDTO){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("rawPassword = {}", signUpDTO.getPassword());
        String encodedPassword = encoder.encode(signUpDTO.getPassword());
        log.info("encodedPassword = {}", encodedPassword);
        return User.builder()
                .role(Role.MEMBER)
                .name(signUpDTO.getName())
                .email(signUpDTO.getEmail())
                .password(encodedPassword)
                .build();
    }
    public static UserResponseDTO.QueryNotification toQueryNotification(UserAppNotification userAppNotification){
        AppNotification appNotification = userAppNotification.getAppNotification();
        return UserResponseDTO.QueryNotification.builder()
                .id(appNotification.getId())
                .title(appNotification.getTitle())
                .content(appNotification.getContent())
                .transmitTime(appNotification.getCreatedAt())
                .isRead(userAppNotification.isRead())
                .build();
    }

    public static UserResponseDTO.QueryNotificationList toQueryNotificationList(List<UserResponseDTO.QueryNotification> notificationList){
        return UserResponseDTO.QueryNotificationList.builder()
                .notificationList(notificationList)
                .build();
    }
}
