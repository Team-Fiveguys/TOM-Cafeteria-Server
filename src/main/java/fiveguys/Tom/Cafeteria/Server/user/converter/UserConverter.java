package fiveguys.Tom.Cafeteria.Server.user.converter;

import fiveguys.Tom.Cafeteria.Server.auth.dto.LoginRequestDTO;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.kakao.dto.KakaoResponseDTO;
import fiveguys.Tom.Cafeteria.Server.user.dto.UserRequestDTO;
import fiveguys.Tom.Cafeteria.Server.user.dto.UserResponseDTO;
import fiveguys.Tom.Cafeteria.Server.user.entity.NotificationSet;
import fiveguys.Tom.Cafeteria.Server.user.entity.Role;
import fiveguys.Tom.Cafeteria.Server.user.entity.SocialType;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserConverter {
    public static NotificationSet toNotificationSet(UserRequestDTO.UpdateNotificationSet dto){
        return NotificationSet.builder()
                .general(dto.isGeneral())
                .myeongJin(dto.isMyeongJin())
                .hakGwan(dto.isHakGwan())
                .todayDiet(dto.isTodayDiet())
                .weekDietEnroll(dto.isWeekDietEnroll())
                .dietPhotoEnroll(dto.isDietPhotoEnroll())
                .dietSoldOut(dto.isDietSoldOut())
                .build();
    }
    public static User toUser(KakaoResponseDTO.UserInfoResponseDTO userInfoResponseDTO){
        return User.builder()
                .socialId(userInfoResponseDTO.getSocialId())
                .email(userInfoResponseDTO.getEmail())
                .role(Role.MEMBER)
                .socialType(SocialType.KAKAO)
                .build();
    }
    public static User toUser(LoginRequestDTO.AppleTokenValidateDTO appleTokenValidateDTO){
        return User.builder()
                .socialId(appleTokenValidateDTO.getSocialId())
                .role(Role.MEMBER)
                .socialType(SocialType.APPLE)
                .build();
    }
    public static User touser(LoginRequestDTO.SignUpDTO signUpDTO){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("rawPassword = {}", signUpDTO.getPassword());
        String encodedPassword = encoder.encode(signUpDTO.getPassword());
        log.info("encodedPassword = {}", encodedPassword);
        return User.builder()
                .role(Role.MEMBER)
                .socialType(SocialType.EMAIL)
                .name(signUpDTO.getName())
                .email(signUpDTO.getEmail())
                .password(encodedPassword)
                .build();
    }

    public static UserResponseDTO.QueryUser toQueryUser(User user){
       return UserResponseDTO.QueryUser.builder()
               .id(user.getId())
               .name(user.getName())
               .email(user.getEmail())
               .role(user.getRole())
               .build();
    }

    public static UserResponseDTO.QueryUserList toQueryUserList(Page<User> userPage){
        List<UserResponseDTO.QueryUser> userDTOList = userPage.stream()
                .map(user -> UserConverter.toQueryUser(user))
                .collect(Collectors.toList());

        return UserResponseDTO.QueryUserList.builder()
                .userList(userDTOList)
                .totalPage(userPage.getTotalPages())
                .nowPage(userPage.getNumber()+1)
                .build();
    }

    public static UserResponseDTO.QueryNotificationSet toQueryNotificationSet(NotificationSet notificationSet){
        return UserResponseDTO.QueryNotificationSet.builder()
                .general(notificationSet.isGeneral())
                .hakGwan(notificationSet.isHakGwan())
                .myeongJin(notificationSet.isMyeongJin())
                .myeongDon(notificationSet.isMyeongDon())
                .todayDiet(notificationSet.isTodayDiet())
                .weekDietEnroll(notificationSet.isWeekDietEnroll())
                .dietSoldOut(notificationSet.isDietSoldOut())
                .dietPhotoEnroll(notificationSet.isDietPhotoEnroll())
                .build();
    }
}
