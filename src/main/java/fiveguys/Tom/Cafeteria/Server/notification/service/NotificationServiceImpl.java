package fiveguys.Tom.Cafeteria.Server.notification.service;

import com.google.firebase.messaging.*;
import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.cafeteria.event.DietPhotoUploadEvent;
import fiveguys.Tom.Cafeteria.Server.cafeteria.service.CafeteriaQueryService;
import fiveguys.Tom.Cafeteria.Server.common.RedisService;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.diet.service.DietQueryService;
import fiveguys.Tom.Cafeteria.Server.notification.dto.NotificationRequestDTO;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotification;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotificationType;
import fiveguys.Tom.Cafeteria.Server.notification.entity.UserAppNotification;
import fiveguys.Tom.Cafeteria.Server.notification.repository.UserAppNotificationRepository;
import fiveguys.Tom.Cafeteria.Server.user.entity.NotificationSet;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import fiveguys.Tom.Cafeteria.Server.user.service.UserQueryService;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{
    private final FCMService fcmService;
    private final UserQueryService userQueryService;
    private final CafeteriaQueryService cafeteriaQueryService;
    private final UserAppNotificationRepository userAppNotificationRepository;
    private final DietQueryService dietQueryService;
    private final RedisService redisService;
    @Override
    public void sendAll(NotificationRequestDTO.SendAllDTO dto) {
        AppNotification notification = fcmService.storeNotification(dto.getTitle(), dto.getContent());

        List<User> userList = userQueryService.getUsersAgreedNotification();
        List<String> tokenList = userList.stream()
                .map(User::getNotificationSet)
                .map(NotificationSet::getRegistrationToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        MulticastMessage multicastMessage = fcmService.createMultiCastMessage(dto.getTitle(), dto.getContent(), tokenList);
        fcmService.sendMessage(multicastMessage, tokenList);

        userList.stream()
                .forEach(user -> {
                    UserAppNotification userAppNotification = UserAppNotification.createUserAppNotification(user, notification);
                    userAppNotificationRepository.save(userAppNotification);
                });
    }

    @Override
    @Transactional
    public void sendSubScriber(NotificationRequestDTO.SendSubscriberDTO dto) {
        AppNotification notification = fcmService.storeNotification(dto.getTitle(), dto.getContent());

        List<User> userList = userQueryService.getUserByNotificationSet(dto.getCafeteriaName(), dto.getNotificationType());
        List<String> tokenList = userList.stream()
                .map(User::getNotificationSet)
                .map(NotificationSet::getRegistrationToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        MulticastMessage multiCastMessage = fcmService.createMultiCastMessage(dto.getTitle(), dto.getContent(), tokenList);
        fcmService.sendMessage(multiCastMessage, tokenList);

        // 각 유저의 알림 저장
        userList.stream()
                .forEach(user -> {
                    UserAppNotification userAppNotification = UserAppNotification.createUserAppNotification(user, notification);
                    userAppNotificationRepository.save(userAppNotification);
                });
    }


    @Transactional
    public void sendSubScriberTest(NotificationRequestDTO.SendSubscriberDTO dto) {
        AppNotification notification = fcmService.storeNotification(dto.getTitle(), dto.getContent());
        Message message = fcmService.createPMessage(dto.getTitle(), dto.getContent(),"eL0KqaoVokzJjORzjrG4vE:APA91bH5NUaQzgiSxwV1DCfQm2NOESxjfNcyUSjkzWEzfHqPv0Ofkn4TKBKbnopyIcJKTKJMvfFXJ0hfpVNqItolEok1epUXXtRtKWPFz1u8gGHpZy10kOIeMTrE59UBS8ltyY4Sute_");
        fcmService.sendMessage(message);

        // 각 유저의 알림 저장
        List<User> userList = userQueryService.getUserByNotificationSet(dto.getCafeteriaName(), dto.getNotificationType());

        userList.stream()
                .forEach(user -> {
                    UserAppNotification userAppNotification = UserAppNotification.createUserAppNotification(user, notification);
                    userAppNotificationRepository.save(userAppNotification);
                });

    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Seoul")
    public void sendTodayDietNotification() {
        String lockKey = "notificationLock";

        long lockTimeout = 10; // 10초 동안 lock 역할을 하는 key 유지, 10:00에 동시에 실행되는 것을 방지하기 위한 시간 설정
        boolean isLocked = redisService.tryLock(lockKey, "tam", lockTimeout, TimeUnit.SECONDS);

        if (isLocked) {
            // 임계 구역 (critical section)
            log.info("메시지 전송 시작");
            List<User> onlyMyeongJinAndTodayDietUserList = userQueryService.getUserByNotificationSet("myeongJin", "hakGwan",AppNotificationType.todayDiet);
            List<User> onlyHakGwanAndTodayDietUserList = userQueryService.getUserByNotificationSet("hakGwan", "myeongJin", AppNotificationType.todayDiet);
            List<User> myeongJinAndHakGwanAndTodayDietUserList = userQueryService.getUserByNotificationSet("myeongJin", AppNotificationType.todayDiet, "hakGwan");
            sendTodayDietOfMyeongJin(onlyMyeongJinAndTodayDietUserList);
            sendTodayDietOfHakGwan(onlyHakGwanAndTodayDietUserList);
            sendTodayDietsOfAll(myeongJinAndHakGwanAndTodayDietUserList);
            log.info("메시지 전송 완료");
        } else {
            System.out.println("Could not acquire lock");
        }
    }

    @Override
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void sendDietUploadNotification(DietPhotoUploadEvent event) {
        List<User> userList = userQueryService.getUserByNotificationSet(event.getCafeteriaName(), event.getType());
        List<String> tokenList = userList.stream()
                .map(User::getNotificationSet)
                .map(NotificationSet::getRegistrationToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 메시지 생성
        String title = "[" + event.getCafeteriaName() + "] " + "식단 이미지 업로드 알림";
        String content = createMessage(event.getCafeteriaName(), event.getMeals().toString(), event.getType());

        // 알림 메시지 저장
        AppNotification notification = fcmService.storeNotification(title, content);
        MulticastMessage multiCastMessage = fcmService.createMultiCastMessage(title, content, tokenList);
        fcmService.sendMessage(multiCastMessage, tokenList);
        log.info("식단 이미지 업로드 알림 메시지 저장");
        // 각 유저의 알림 저장
        userList.stream()
                .forEach(user -> {
                    UserAppNotification userAppNotification = UserAppNotification.createUserAppNotification(user, notification);
                    userAppNotificationRepository.save(userAppNotification);
                });
    }
    private String createMessage(String cafeteriaName, String meals, AppNotificationType type){
        switch (type){
            case dietPhotoEnroll:
                return new StringBuilder()
                        .append(cafeteriaName)
                        .append("의")
                        .append(meals)
                        .append(" 이미지가 등록되었습니다.")
                        .toString();
        }
        return null;
    }

    private void sendTodayDietOfMyeongJin(List<User> userList){
        Diet diet;
        // 식단 정보 가져와서 알림 메시지 만들기
        try{
            diet = dietQueryService.getDiet(1L, LocalDate.now(), Meals.LUNCH);
            if(diet.isDayOff()){
                return;
            }
        }
        catch ( GeneralException e){ // 식단 없으면 return (추후에 예외 클래스 리팩토링)
            return ;
        }
        AppNotification notification = fcmService.storeNotification("오늘의 식단 알림", "[명진당]\n" +diet.getMenuListString());

        // 메시지 받을 사용자 토큰 받아서 전송
        List<String> tokenList = userList.stream()
                .map(user -> user.getNotificationSet())
                .map(NotificationSet::getRegistrationToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        try{
            MulticastMessage multiCastMessage = fcmService.createMultiCastMessage(notification.getTitle(), notification.getContent(), tokenList);
            fcmService.sendMessage(multiCastMessage, tokenList);
        }
        catch (GeneralException e){
            return;
        }

        // 알림 받은 사용자의 알림 센터에 알림 저장
        userList.stream()
                .forEach(user -> {
                    UserAppNotification userAppNotification = UserAppNotification.createUserAppNotification(user, notification);
                    userAppNotificationRepository.save(userAppNotification);
                });
    }
    private void sendTodayDietOfHakGwan(List<User> userList){
        Diet diet;
        try{
            diet = dietQueryService.getDiet(2L, LocalDate.now(), Meals.LUNCH);
            if(diet.isDayOff()){
                return;
            }
        }
        catch (GeneralException e){
            return;
        }
         // 식단 정보 가져와서 알림 메시지 만들기
        AppNotification notification = fcmService.storeNotification("오늘의 식단 알림", "[학관]\n" + diet.getMenuListString());

        // 메시지 받을 사용자 토큰 받아서 전송
        List<String> tokenList = userList.stream()
                .map(user -> user.getNotificationSet())
                .map(NotificationSet::getRegistrationToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        try{
            MulticastMessage multiCastMessage = fcmService.createMultiCastMessage(notification.getTitle(), notification.getContent(), tokenList);
            fcmService.sendMessage(multiCastMessage, tokenList);
        }
        catch (GeneralException e){
            return;
        }
        // 알림 받은 사용자의 알림 센터에 알림 저장
        userList.stream()
                .forEach(user -> {
                    UserAppNotification userAppNotification = UserAppNotification.createUserAppNotification(user, notification);
                    userAppNotificationRepository.save(userAppNotification);
                });
    }
    private void sendTodayDietsOfAll(List<User> userList){
        // 식단 정보 가져와서 알림 메시지 만들기

        // 여기선 미등록이어도 에러 처리 하지 않음
        Diet myeongJinDiet = null;
        Diet hakGwanDiet = null;
        try{
            myeongJinDiet = dietQueryService.getDiet(1L, LocalDate.now(), Meals.LUNCH);
        }
        catch (GeneralException e){

        }
        try{
            hakGwanDiet = dietQueryService.getDiet(2L, LocalDate.now(), Meals.LUNCH);
        }
        catch (GeneralException e){

        }
        // 명진당만 미등록 이거나 휴무 이면 학관 식단 정보만 알림
        if( Diet.isDayOffOrNull(myeongJinDiet) && !Diet.isDayOffOrNull(hakGwanDiet) ){
            sendTodayDietOfHakGwan(userList);
        }
        // 학관만 미등록 이거나 휴무 이면 명진당 식단 정보만 알림
        else if (Diet.isDayOffOrNull(hakGwanDiet) && !Diet.isDayOffOrNull(myeongJinDiet)) {
            sendTodayDietOfMyeongJin(userList);
        }
        // 둘 다 미등록 이거나 둘 다 휴무 이면 알림을 보내지 않음
        else if( Diet.isDayOffOrNull(myeongJinDiet) && Diet.isDayOffOrNull(hakGwanDiet) ){
            return;
        } // 아니면 둘 다 포함해서 하나의 알림을 보냄
        else{
            AppNotification notification = fcmService.storeNotification("오늘의 식단 알림", "[명진당]\n" + myeongJinDiet.getMenuListString() +
                    "[학관]\n" + hakGwanDiet.getMenuListString());
            // 메시지 받을 사용자 토큰 받아서 전송
            List<String> tokenList = userList.stream()
                    .map(user -> user.getNotificationSet())
                    .map(NotificationSet::getRegistrationToken)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            try{
                MulticastMessage multiCastMessage = fcmService.createMultiCastMessage(notification.getTitle(), notification.getContent(), tokenList);
                fcmService.sendMessage(multiCastMessage, tokenList);
            }
            catch (GeneralException e){
                return;
            }

            // 알림 받은 사용자의 알림 센터에 알림 저장
            userList.stream()
                    .forEach(user -> {
                        UserAppNotification userAppNotification = UserAppNotification.createUserAppNotification(user, notification);
                        userAppNotificationRepository.save(userAppNotification);
                    });
        }
    }

    @Override
    public void sendOne(Long cafeteriaId, String content, Long receiverId) {
        User receiver = userQueryService.getUserById(receiverId);
        Cafeteria cafeteria = cafeteriaQueryService.getById(cafeteriaId);
        NotificationSet notificationSet = receiver.getNotificationSet();
        String token = notificationSet.getRegistrationToken();
        String registrationToken = token;

        // See documentation on defining a message payload.
        Notification notification = Notification.builder()
                .setTitle("하이")
                .setBody(content)
                .build();

        Message message = Message.builder()
                .putData("식당", cafeteria.getName())
                .putData("내용", content)
                .setToken(registrationToken)
                .setNotification(notification)
                .build();



        // Send a message to the device corresponding to the provided
        // registration token.
        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
    }

    @Override
    public void sendAdmins(NotificationRequestDTO.SendAdminsDTO dto) {
        AppNotification notification = fcmService.storeNotification(dto.getTitle(), dto.getContent());
        List<User> admins = userQueryService.getAdmins();
        List<String> tokenList = admins.stream()
                .map(admin -> admin.getNotificationSet().getRegistrationToken())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        MulticastMessage multiCastMessage = fcmService.createMultiCastMessage(dto.getTitle(), dto.getContent(), tokenList);
        fcmService.sendMessage(multiCastMessage, tokenList);

        admins.stream()
                .forEach(admin -> {
                    UserAppNotification userAppNotification = UserAppNotification.createUserAppNotification(admin, notification);
                    userAppNotificationRepository.save(userAppNotification);
                });
    }

}
