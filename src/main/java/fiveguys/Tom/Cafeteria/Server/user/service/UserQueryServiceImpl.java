package fiveguys.Tom.Cafeteria.Server.user.service;

import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.auth.UserContext;
import fiveguys.Tom.Cafeteria.Server.board.dto.PostPreviewDTO;
import fiveguys.Tom.Cafeteria.Server.board.entity.Post;
import fiveguys.Tom.Cafeteria.Server.board.repository.PostLikeRepository;
import fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.converter.CafeteriaConverter;
import fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.response.CafeteriaResponseDTO;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.notification.converter.NotificationConverter;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotificationType;
import fiveguys.Tom.Cafeteria.Server.notification.entity.UserAppNotification;
import fiveguys.Tom.Cafeteria.Server.user.converter.UserConverter;
import fiveguys.Tom.Cafeteria.Server.user.dto.UserResponseDTO;
import fiveguys.Tom.Cafeteria.Server.user.entity.NotificationSet;
import fiveguys.Tom.Cafeteria.Server.user.entity.Role;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import fiveguys.Tom.Cafeteria.Server.user.entity.UserCafeteria;
import fiveguys.Tom.Cafeteria.Server.user.repository.UserRepository;
import fiveguys.Tom.Cafeteria.Server.user.repository.UserRepositoryCustom;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService{
    private static int userPageSize = 20;
    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;
    private final PostLikeRepository postLikeRepository;

    @Override
    public UserResponseDTO.QueryUser getMyInfo() {
        Long userId = UserContext.getUserId();
        User user = getUserById(userId);
        return UserConverter.toQueryUser(user);
    }

    @Override
    public List<User> getAdmins() {
        return userRepository.findAllByRole(Role.ADMIN);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND)
                );
    }

    @Override
    public User getUserBySocialId(String socialId) {
        return userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND)
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    @Override
    public boolean isExistBySocialId(String socialId) {
        return userRepository.existsBySocialId(socialId);
    }

    @Override
    public boolean isExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserResponseDTO.QueryNotificationList getNotifications() {
        Long userId = UserContext.getUserId();
        User user = getUserById(userId);
        List<UserAppNotification> userAppNotificationList = user.getUserAppNotificationList();
        List<UserResponseDTO.QueryNotification> notificationDTOList = userAppNotificationList.stream()
                .map(userAppNotification -> NotificationConverter.toQueryNotification(userAppNotification))
                .sorted(Comparator.comparing(UserResponseDTO.QueryNotification::getTransmitTime))
                .collect(Collectors.toList());

        return NotificationConverter.toQueryNotificationList(notificationDTOList);

    }

    @Override
    public UserResponseDTO.QueryUserList getUsers(int page) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page - 1, userPageSize, Sort.by(Sort.Order.desc("createdAt"))));
        return UserConverter.toQueryUserList(userPage);
    }

    @Override
    public UserResponseDTO.QueryNotificationSet getNotificationSet() {
        Long userId = UserContext.getUserId();
        User user = getUserById(userId);
        NotificationSet notificationSet = user.getNotificationSet();
        if (notificationSet == null){
            throw new GeneralException(ErrorStatus.NOTIFICATION_SET_IS_NOT_SET);
        }
        return UserConverter.toQueryNotificationSet(user.getNotificationSet());
    }

    @Override
    public UserResponseDTO.QueryRegistrationToken getRegistrationToken() {
        Long userId = UserContext.getUserId();
        User user = getUserById(userId);
        NotificationSet notificationSet = user.getNotificationSet();
        return UserResponseDTO.QueryRegistrationToken.builder()
                .registrationToken(notificationSet.getRegistrationToken())
                .build();
    }

    @Override
    public CafeteriaResponseDTO.QueryCafeteriaList getRunningCafeteriaList() {
        Long userId = UserContext.getUserId();
        User user = getUserById(userId);
        List<UserCafeteria> userCafeteriaList = user.getUserCafeteriaList();
        List<Cafeteria> cafeteriaList = userCafeteriaList.stream()
                .map(userCafeteria -> userCafeteria.getCafeteria())
                .collect(Collectors.toList());
        return CafeteriaConverter.toQueryCafeteriaList(cafeteriaList);
    }

    @Override
    public List<PostPreviewDTO> getCreatedPostList() {
        Long userId = UserContext.getUserId();
        User user = getUserById(userId);
        List<Post> postList = user.getPostList();
        List<PostPreviewDTO> previewDTOList = postList.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .limit(20)
                .map(post -> PostPreviewDTO.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .uploadTime(post.getCreatedAt())
                        .likeCount(post.getLikeCount())
                        .publisherName(post.getUser().getName())
                        .toggleLike(postLikeRepository.existsByUserAndPost(user, post))
                        .build()
                )
                .collect(Collectors.toList());
        return previewDTOList;
    }

    @Override
    public List<User> getUserByNotificationSet(String subscribedCafeteriaName, AppNotificationType type) {
        return userRepositoryCustom.findUsersByNotificationSet(subscribedCafeteriaName, type);
    }
    @Override
    public List<User> getUserByNotificationSet(String subscribedCafeteriaName, String unsubscribedCafeteriaName, AppNotificationType type) {
        return userRepositoryCustom.findUsersByNotificationSet(subscribedCafeteriaName, unsubscribedCafeteriaName, type);
    }
    @Override
    public List<User> getUserByNotificationSet(String subscribedCafeteriaName1, AppNotificationType type, String subscribedCafeteriaName2) {
        return userRepositoryCustom.findUsersByNotificationSet(subscribedCafeteriaName1, type, subscribedCafeteriaName2);
    }


    @Override
    public List<User> getUsersAgreedNotification() {
        return userRepository.findByNotificationSetIsNotNull();
    }
}
