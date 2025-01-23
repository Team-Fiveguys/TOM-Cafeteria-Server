package fiveguys.Tom.Cafeteria.Server.notification.controller;

import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.notification.dto.NotificationRequestDTO;
import fiveguys.Tom.Cafeteria.Server.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notifications")
public class FCMAdminController {
    private final NotificationService notificationService;

    @Operation(summary = "특정 사용자에게 알림을 보내는 API", description = "식당id, 내용, 받는 유저 id를")
    @PostMapping("/user")
    public ApiResponse<String> sendOne(@RequestBody NotificationRequestDTO.SendOneDTO dto){
        notificationService.sendOne(dto.getCafeteriaId(), dto.getContent(), dto.getReceiverId());
        return ApiResponse.onSuccess("알림을 성공적으로 보냈습니다.");}


    @Operation(summary = "알림을 허용한 모두에게 공지를 보내는 API", description = "유저별 등록되어있는 기기토큰을 모두 가져와 알림을 보낸다.")
    @PostMapping("/users")
    public ApiResponse<String> sendAll(@RequestBody NotificationRequestDTO.SendAllDTO dto){
        notificationService.sendAll(dto);
        return ApiResponse.onSuccess("알림을 성공적으로 보냈습니다.");
    }

    @Operation(summary = "식당을 구독한 이용자에게 공지를 보내는 API", description = "식당명과 알림 타입을 받아서 구독을 특정하여 메시지를 전달한다.")
    @PostMapping("/topic/subscriber")
    public ApiResponse<String> sendSubscriber(@RequestBody NotificationRequestDTO.SendSubscriberDTO dto){
        notificationService.sendSubScriber(dto);
        return ApiResponse.onSuccess("알림을 성공적으로 보냈습니다.");
    }

    @Operation(summary = "식당을 구독한 이용자에게 공지를 보내는 테스트 API", description = "식당명과 알림 타입을 받아서 구독을 특정하여 메시지를 전달한다.")
    @PostMapping("/topic/subscriber/test")
    public ApiResponse<String> sendSubscriberTest(@RequestBody NotificationRequestDTO.SendSubscriberDTO dto){
        notificationService.sendSubScriberTest(dto);
        return ApiResponse.onSuccess("알림을 성공적으로 보냈습니다.");
    }

    @Operation(summary = "오늘의 식단 알림 테스트 API", description = "테스트 API")
    @PostMapping("/topic/today-diet/test")
    public ApiResponse<String> sendTodayDietTest(@RequestBody NotificationRequestDTO.SendSubscriberDTO dto){
        notificationService.sendTodayDietNotification();
        return ApiResponse.onSuccess("알림을 성공적으로 보냈습니다.");
    }
}

