package fiveguys.Tom.Cafeteria.Server.user.controller;


import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.board.dto.PostPreviewDTO;
import fiveguys.Tom.Cafeteria.Server.user.dto.UserRequestDTO;
import fiveguys.Tom.Cafeteria.Server.user.dto.UserResponseDTO;
import fiveguys.Tom.Cafeteria.Server.user.service.UserCommandService;
import fiveguys.Tom.Cafeteria.Server.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping("/me")
    @Operation(summary = "본인정보를 조회하는 API", description = "토큰을 통해 유저를 식별하고 정보를 응답합니다.")
    public ApiResponse<UserResponseDTO.QueryUser> getMyInfo(){
        return ApiResponse.onSuccess(userQueryService.getMyInfo());
    }

    @PostMapping("/notificationSet")
    @Operation(summary = "알림을 허용시 호출하는 API", description = "기기토큰을 저장시키고" +
            " general 토픽에 대해서 구독을 실시한다, 모든 알림 타입에 대해서도 on 상태")
    public ApiResponse<String> allowNotification(@RequestParam(name = "registrationToken") String token){
        userCommandService.initNotificationSet(token);
        return ApiResponse.onSuccess("알림이 활성화 되었습니다.");
    }

    @PutMapping("/notificationSet/registration-token")
    @Operation(summary = "기기 토큰을 업데이트하는 API", description = "기기토큰이 변경될 시 호출하여 서버에 기기토큰 정보를 최신화한다.")
    public ApiResponse<String> updateRegistrationToken(@RequestParam(name = "registrationToken") String token){
        userCommandService.updateRegistrationToken(token);
        return ApiResponse.onSuccess("알림이 활성화 되었습니다.");
    }

    @GetMapping("/notificationSet/registration-token")
    @Operation(summary = "기기토큰을 조회하는 API", description = "현재 설정된 알림 항목에 대한 정보를 응답한다.")
    public ApiResponse<UserResponseDTO.QueryRegistrationToken> queryRegistrationToken(){
        return ApiResponse.onSuccess(userQueryService.getRegistrationToken());
    }
    @GetMapping("/notificationSet")
    @Operation(summary = "알림 항목을 조회하는 API", description = "현재 설정된 알림 항목에 대한 정보를 응답한다.")
    public ApiResponse<UserResponseDTO.QueryNotificationSet> queryNotification(){
        UserResponseDTO.QueryNotificationSet notificationSet = userQueryService.getNotificationSet();
        return ApiResponse.onSuccess(notificationSet);
    }

    @PutMapping("/notificationSet")
    @Operation(summary = "알림 항목을 업데이트 하는 API", description = "알림 항목에 대한 동의 여부를 받아서 저장시킨다. 구독과 항상 같이 이루어져야 한다.")
    public ApiResponse<String> modifyNotification(@RequestBody UserRequestDTO.UpdateNotificationSet dto){
        userCommandService.updateNotificationSet(dto);
        return ApiResponse.onSuccess("알림 항목이 수정 되었습니다.");
    }

    @GetMapping("/notifications")
    @Operation(summary = "수신받은 알림을 조회하는 API", description = "토큰에 있는 userId를 통해 유저를 식별하여 유저가" +
            "수신받은 알림 리스트를 응답한다. 추후에 한달이 지나면 자동으로 삭제하는 API 만들 것임")
    public ApiResponse<UserResponseDTO.QueryNotificationList> receiveNotification(){
        return ApiResponse.onSuccess(userQueryService.getNotifications());
    }

    @PatchMapping("/notifications/{notification-id}/read")
    @Operation(summary = "알림 하나를 읽음 처리 하는 API", description = "알림 id를 받아 알림을 조회하여 읽음 처리를 한다.")
    public ApiResponse<String> readNotification(@PathVariable(name = "notification-id") Long id){
        userCommandService.readNotification(id);
        return ApiResponse.onSuccess("해당 알림이 읽음 처리 되었습니다.");
    }

    @PatchMapping("/notifications/read")
    @Operation(summary = "유저의 모든 알림을 읽음 처리 하는 API", description = "모든 알림을 읽음 처리를 한다.")
    public ApiResponse<String> readNotifications(){
        userCommandService.readAllNotification();
        return ApiResponse.onSuccess("모든 알림들이 읽음 처리 되었습니다.");
    }

    @DeleteMapping("/notifications/{notification-id}")
    @Operation(summary = "알림 하나를 삭제 처리 하는 API", description = "알림 id를 받아 유저에게 보내진 알림을 삭제 한다.")
    public ApiResponse<String> deleteNotification(@PathVariable(name = "notification-id") Long id){
        userCommandService.deleteNotification(id);
        return ApiResponse.onSuccess("해당 알림이 삭제 처리 되었습니다.");
    }
    @DeleteMapping("/notifications")
    @Operation(summary = "알림 하나를 삭제 처리 하는 API", description = "모든 알림을 삭제 한다.")
    public ApiResponse<String> deleteNotifications(){
        userCommandService.deleteNotifications();
        return ApiResponse.onSuccess("모든 알림들이 삭제 되었습니다.");
    }

    @GetMapping("/me/posts")
    @Operation(summary = "내가 작성한 게시글을 조회하는 API", description = "")
    public ApiResponse<List<PostPreviewDTO>> queryMyPost(){
        return ApiResponse.onSuccess(userQueryService.getCreatedPostList());
    }
}
