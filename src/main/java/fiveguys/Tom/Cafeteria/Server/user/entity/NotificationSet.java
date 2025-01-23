package fiveguys.Tom.Cafeteria.Server.user.entity;

import fiveguys.Tom.Cafeteria.Server.common.BaseEntity;
import fiveguys.Tom.Cafeteria.Server.user.dto.UserRequestDTO;
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
public class NotificationSet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String registrationToken;
    @Column(columnDefinition = "boolean default true")
    private boolean general;
    @Column(columnDefinition = "boolean default true")
    private boolean hakGwan;
    @Column(columnDefinition = "boolean default true")
    private boolean myeongJin;
    @Column(columnDefinition = "boolean default true")
    private boolean myeongDon;
    @Column(columnDefinition = "boolean default true")
    private boolean todayDiet;
    @Column(columnDefinition = "boolean default true")
    private boolean dietPhotoEnroll;
    @Column(columnDefinition = "boolean default true")
    private boolean weekDietEnroll;
    @Column(columnDefinition = "boolean default true")
    private boolean dietSoldOut;

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public void setNotificationSet(UserRequestDTO.UpdateNotificationSet updateNotificationSet){
        this.general = updateNotificationSet.isGeneral();
        this.hakGwan = updateNotificationSet.isHakGwan();
        this.myeongJin = updateNotificationSet.isMyeongJin();
        this.myeongDon = updateNotificationSet.isMyeongDon();
        this.todayDiet = updateNotificationSet.isTodayDiet();
        this.weekDietEnroll = updateNotificationSet.isWeekDietEnroll();
        this.dietPhotoEnroll = updateNotificationSet.isDietPhotoEnroll();
        this.dietSoldOut = updateNotificationSet.isDietSoldOut();
    }
}
