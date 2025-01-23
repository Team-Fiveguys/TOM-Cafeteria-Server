package fiveguys.Tom.Cafeteria.Server.user.service;

import fiveguys.Tom.Cafeteria.Server.board.dto.PostPreviewDTO;
import fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.response.CafeteriaResponseDTO;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotificationType;
import fiveguys.Tom.Cafeteria.Server.user.dto.UserResponseDTO;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;

import java.util.List;

public interface UserQueryService {

    public UserResponseDTO.QueryUser getMyInfo();

    public List<User> getAdmins();
    public User getUserById(Long userId);
    public User getUserBySocialId(String socialId);
    public User getUserByEmail(String email);
    public boolean isExistBySocialId(String socialId);

    public boolean isExistByEmail(String email);

    public UserResponseDTO.QueryNotificationList getNotifications();

    public UserResponseDTO.QueryUserList getUsers(int page);

    public UserResponseDTO.QueryNotificationSet getNotificationSet();

    public UserResponseDTO.QueryRegistrationToken getRegistrationToken();

    public CafeteriaResponseDTO.QueryCafeteriaList getRunningCafeteriaList();

    public List<PostPreviewDTO> getCreatedPostList();

    public List<User> getUserByNotificationSet(String cafeteriaName, AppNotificationType type);

    public List<User> getUserByNotificationSet(String subscribedCafeteriaName, String unsubscribedCafeteriaName, AppNotificationType type);

    public List<User> getUserByNotificationSet(String subscribedCafeteriaName1, AppNotificationType type, String subscribedCafeteriaName2);
    public List<User> getUsersAgreedNotification();

}
