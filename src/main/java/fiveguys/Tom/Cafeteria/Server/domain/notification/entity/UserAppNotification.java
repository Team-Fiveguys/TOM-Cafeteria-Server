package fiveguys.Tom.Cafeteria.Server.domain.notification.entity;

import fiveguys.Tom.Cafeteria.Server.domain.common.BaseEntity;
import fiveguys.Tom.Cafeteria.Server.domain.user.entity.NotificationSet;
import fiveguys.Tom.Cafeteria.Server.domain.user.entity.User;
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

}

