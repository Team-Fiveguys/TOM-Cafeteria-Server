package fiveguys.Tom.Cafeteria.Server.domain.user.controller;


import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.domain.user.dto.UserResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.user.service.UserCommandService;
import fiveguys.Tom.Cafeteria.Server.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;



    @Operation(summary = "관리자 권한 부여 API",description = "일반 회원에게 관리자 권한을 부여합니다.")
    @PatchMapping("/users/{user-id}/role/admin")
    public ApiResponse<String> grantAdminRole(@PathVariable(value = "user-id") Long userId){
        userCommandService.grantAdmin(userId);
        return ApiResponse.onSuccess(userId + "번 회원 관리자 권한 부여");
    }
    @Operation(summary = "관리자 권한 회수 API",description = "지정된 회원의 관리자 권한을 회수합니다.")
    @PatchMapping("/users/{user-id}/role/user")
    public ApiResponse<String> depriveAdminRole(@PathVariable(value = "user-id") Long userId){
        userCommandService.depriveAdmin(userId);
        return ApiResponse.onSuccess(userId + "번 회원 관리자 권한 회수");
    }

//    @GetMapping("/me/cafeterias")
//    @Operation(summary = "관리하는 식당 리스트를 조회하는 API", description = "관리하는 식당의 정보들을 응답한다.")
//    public ApiResponse<CafeteriaResponseDTO.QueryCafeteriaList> QueryRunningCafeterias(){
//        CafeteriaResponseDTO.QueryCafeteriaList runningCafeteriaList = userQueryService.getRunningCafeteriaList();
//        return ApiResponse.onSuccess(runningCafeteriaList);
//    }

    @GetMapping("")
    @Operation(summary = "유저 목록 조회 API", description = "유저 id, 이름, 권한, 이메일")
    public ApiResponse<UserResponseDTO.QueryUserList> deleteNotifications(@RequestParam(name = "page") int page){
        UserResponseDTO.QueryUserList users = userQueryService.getUsers(page);
        return ApiResponse.onSuccess(users);
    }
}
