package fiveguys.Tom.Cafeteria.Server.auth.controller;


import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.auth.jwt.JwtToken;
import fiveguys.Tom.Cafeteria.Server.auth.jwt.service.JwtUtil;
import fiveguys.Tom.Cafeteria.Server.common.RedisService;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    @PostMapping("/token/access-token")
    @Operation(summary = "토큰 재발급 API", description = "액세스 토큰의 리프레시를 가져와서" +
            " 기간이 유효하면 재발급, 유효하지 않으면 실패 처리를 한다")
    public ApiResponse<String> reissueTokens(HttpServletRequest request){
        String accessToken = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }
        String refreshToken = redisService.getValue(accessToken);
        if(refreshToken == null){ //refreshToken 만료
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_EXPIRED);
        }
        String userId = redisService.getValue(refreshToken);
        redisService.deleteValues(accessToken);
        JwtToken newJwtToken = jwtUtil.generateToken(userId); // 토큰 재발급
        redisService.deleteValues(refreshToken);
        accessToken = newJwtToken.getAccessToken();
        return ApiResponse.onSuccess(accessToken);
    }

}
