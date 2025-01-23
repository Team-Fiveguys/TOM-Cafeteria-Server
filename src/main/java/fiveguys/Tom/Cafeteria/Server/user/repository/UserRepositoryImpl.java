package fiveguys.Tom.Cafeteria.Server.user.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotificationType;
import fiveguys.Tom.Cafeteria.Server.user.entity.QNotificationSet;
import fiveguys.Tom.Cafeteria.Server.user.entity.QUser;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    private JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
         this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<User> findUsersByNotificationSet(String cafeteriaName, AppNotificationType type) {
        QUser user = QUser.user;
        QNotificationSet notificationSet = QNotificationSet.notificationSet;

        BooleanBuilder builder = new BooleanBuilder();
        // 어떤 식당 구독 여부를 조건으로 할 지에 따라 where문 조건이 달라짐
        switch (cafeteriaName){
            case "명진당":
                builder.and(notificationSet.myeongJin.isTrue());
                break;
            case "학관":
                builder.and(notificationSet.hakGwan.isTrue());
                break;
            case "명돈이네":
                builder.and(notificationSet.myeongDon.isTrue());
                break;
        }
        // 어떤 알림항목 구독 여부를 조건으로 할 지에 따라 where문 조건이 달라짐
        switch (type) {
            case todayDiet:
                builder.and(notificationSet.todayDiet.isTrue());
                break;
            case weekDietEnroll:
                builder.and(notificationSet.weekDietEnroll.isTrue());
                break;
            case dietPhotoEnroll:
                builder.and(notificationSet.dietPhotoEnroll.isTrue());
                break;
            case dietSoldOut:
                builder.and(notificationSet.dietSoldOut.isTrue());
                break;
            case general:
                builder.and(notificationSet.general.isTrue());
                break;
        }

        return queryFactory.selectFrom(user)
                .join(user.notificationSet, notificationSet).fetchJoin()
                .where(builder)
                .fetch();
    }

    @Override
    public List<User> findUsersByNotificationSet(String subscribedCafeteriaName, String unsubscribedCafeteriaName, AppNotificationType type) {
        QUser user = QUser.user;
        QNotificationSet notificationSet = QNotificationSet.notificationSet;

        BooleanBuilder builder = new BooleanBuilder();
        // 어떤 식당 구독 여부를 조건으로 할 지에 따라 where문 조건이 달라짐

        // 구독했는지 확인하는 식당 조건 추가
        switch (subscribedCafeteriaName){
            case "myeongJin":
                builder.and(notificationSet.myeongJin.isTrue());
                break;
            case "hakGwan":
                builder.and(notificationSet.hakGwan.isTrue());
                break;
            case "myeongDon":
                builder.and(notificationSet.myeongDon.isTrue());
                break;
        }
        // 구독 안했는지 확인하는 식당 조건 추가
        switch (unsubscribedCafeteriaName){
            case "myeongJin":
                builder.and(notificationSet.myeongJin.isFalse());
                break;
            case "hakGwan":
                builder.and(notificationSet.hakGwan.isFalse());
                break;
            case "myeongDon":
                builder.and(notificationSet.myeongDon.isFalse());
                break;
        }
        // 어떤 알림항목 구독 여부를 조건으로 할 지에 따라 where문 조건이 달라짐
        switch (type) {
            case todayDiet:
                builder.and(notificationSet.todayDiet.isTrue());
                break;
            case weekDietEnroll:
                builder.and(notificationSet.weekDietEnroll.isTrue());
                break;
            case dietPhotoEnroll:
                builder.and(notificationSet.dietPhotoEnroll.isTrue());
                break;
            case dietSoldOut:
                builder.and(notificationSet.dietSoldOut.isTrue());
                break;
            case general:
                builder.and(notificationSet.general.isTrue());
                break;
        }

        return queryFactory.selectFrom(user)
                .join(user.notificationSet, notificationSet)
                .where(builder)
                .fetch();
    }

    @Override
    public List<User> findUsersByNotificationSet(String subscribedCafeteriaName1, AppNotificationType type, String subscribedCafeteriaName2) {
        QUser user = QUser.user;
        QNotificationSet notificationSet = QNotificationSet.notificationSet;

        BooleanBuilder builder = new BooleanBuilder();
        // 어떤 식당 구독 여부를 조건으로 할 지에 따라 where문 조건이 달라짐
        switch (subscribedCafeteriaName1){
            case "myeongJin":
                builder.and(notificationSet.myeongJin.isTrue());
                break;
            case "hakGwan":
                builder.and(notificationSet.hakGwan.isTrue());
                break;
            case "myeongDon":
                builder.and(notificationSet.myeongDon.isTrue());
                break;
        }
        switch (subscribedCafeteriaName2){
            case "myeongJin":
                builder.and(notificationSet.myeongJin.isTrue());
                break;
            case "hakGwan":
                builder.and(notificationSet.hakGwan.isTrue());
                break;
            case "myeongDon":
                builder.and(notificationSet.myeongDon.isTrue());
                break;
        }
        // 어떤 알림항목 구독 여부를 조건으로 할 지에 따라 where문 조건이 달라짐
        switch (type) {
            case todayDiet:
                builder.and(notificationSet.todayDiet.isTrue());
                break;
            case weekDietEnroll:
                builder.and(notificationSet.weekDietEnroll.isTrue());
                break;
            case dietPhotoEnroll:
                builder.and(notificationSet.dietPhotoEnroll.isTrue());
                break;
            case dietSoldOut:
                builder.and(notificationSet.dietSoldOut.isTrue());
                break;
            case general:
                builder.and(notificationSet.general.isTrue());
                break;
        }

        return queryFactory.selectFrom(user)
                .join(user.notificationSet, notificationSet).fetchJoin()
                .where(builder)
                .fetch();
    }
}
