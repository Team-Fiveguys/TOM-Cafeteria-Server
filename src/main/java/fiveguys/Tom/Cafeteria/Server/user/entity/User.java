package fiveguys.Tom.Cafeteria.Server.user.entity;


import fiveguys.Tom.Cafeteria.Server.board.entity.Post;
import fiveguys.Tom.Cafeteria.Server.common.BaseEntity;
import fiveguys.Tom.Cafeteria.Server.notification.entity.UserAppNotification;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String name;

    @Email
    private String email;
    private String password;
    private String appleRefreshToken;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private NotificationSet notificationSet;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<UserAppNotification> userAppNotificationList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<UserCafeteria> userCafeteriaList = new ArrayList<>();
    public static void setRoleAdmin(User user) {
        user.role = Role.ADMIN;
    }
    public static void setRoleMember(User user) {
        user.role = Role.MEMBER;
    }

    public static void setAppleRefreshToken(User user, String appleRefreshToken){
        user.setAppleRefreshToken(appleRefreshToken);
    }

    public void setNotificationSet(NotificationSet notificationSet) {
        this.notificationSet = notificationSet;
    }

    private void setAppleRefreshToken(String appleRefreshToken) {
        this.appleRefreshToken = appleRefreshToken;
    }

    public void deleteAllUserAppNotification(){
        this.getUserAppNotificationList().clear();
    }
}
