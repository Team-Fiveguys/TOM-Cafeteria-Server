package fiveguys.Tom.Cafeteria.Server.notification.entity;

import fiveguys.Tom.Cafeteria.Server.common.BaseEntity;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAppNotification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    private User user;

    @ManyToOne(fetch= FetchType.LAZY)
    private AppNotification appNotification;

    @Column(columnDefinition = "boolean DEFAULT false", name = "is_read") //mysql 예약어 피하기
    private boolean read;

    public static UserAppNotification createUserAppNotification(User user, AppNotification notification){
        UserAppNotification userAppNotification = new UserAppNotification();
        userAppNotification.setUser(user);
        userAppNotification.setAppNotification(notification);
        return userAppNotification;
    }
    public void setUser(User user){
        if( user != null){
            user.getUserAppNotificationList().remove(user);
        }
        user.getUserAppNotificationList().add(this);
        this.user = user;
    }
    public void setAppNotification(AppNotification appNotification){
        if( appNotification != null){
            appNotification.getUserAppNotificationList().remove(appNotification);
        }
        appNotification.getUserAppNotificationList().add(this);
        this.appNotification = appNotification;
    }

    public void setRead(){
        this.read = true;
    }

    public void deleteUserAppNotification(User user, AppNotification appNotification){
        user.getUserAppNotificationList().remove(this);
        appNotification.getUserAppNotificationList().remove(this);
    }
}

