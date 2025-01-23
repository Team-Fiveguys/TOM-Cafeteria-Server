package fiveguys.Tom.Cafeteria.Server.auth.controller;


import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.auth.UserContext;
import fiveguys.Tom.Cafeteria.Server.auth.converter.LoginConverter;
import fiveguys.Tom.Cafeteria.Server.auth.dto.LoginRequestDTO;
import fiveguys.Tom.Cafeteria.Server.auth.dto.LoginResponseDTO;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.TokenResponse;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.kakao.dto.KakaoResponseDTO;
import fiveguys.Tom.Cafeteria.Server.auth.jwt.JwtToken;
import fiveguys.Tom.Cafeteria.Server.auth.jwt.service.JwtUtil;
import fiveguys.Tom.Cafeteria.Server.auth.service.AppleLoginService;
import fiveguys.Tom.Cafeteria.Server.auth.service.EmailLoginService;
import fiveguys.Tom.Cafeteria.Server.auth.service.KakaoLoginService;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import fiveguys.Tom.Cafeteria.Server.user.service.UserCommandService;
import fiveguys.Tom.Cafeteria.Server.user.service.UserQueryService;
import fiveguys.Tom.Cafeteria.Server.user.converter.UserConverter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final KakaoLoginService kakaoLoginService;
    private final AppleLoginService appleLoginService;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final EmailLoginService emailLoginService;
    private final JwtUtil jwtUtil;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    @Value("${kakao.client-id}")
    private String kakaoClientId;


    /*
  테스트용 API, CORS 때문에 직접 호출하지 않고 redirect
   */
    @GetMapping("/oauth2/authorize/kakao")
    public String login(){
        String toRedirectUri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
        return "redirect:" + toRedirectUri;
    }

    @Operation(summary = "카카오 소셜 토큰 검증 API",description = "추가정보와 ID토큰을 받으면 ID토큰을 검증하고 통과 시" +
            "서버에서 발급한 토큰을 받습니다. 회원가입을 하지 않은 사용자의 경우 회원가입을 시킵니다.")
    @ResponseBody
    @PostMapping("/oauth2/kakao/token/validate")
    public ApiResponse<LoginResponseDTO.LoginDTO> validateKakoToken(@RequestBody @Valid LoginRequestDTO.KakaoTokenValidateDTO requestDTO)  {
        // identity 토큰 검증
         kakaoLoginService.validate(requestDTO.getIdentityToken());
        // ok -> 유저 정보 가져오기
        KakaoResponseDTO.UserInfoResponseDTO userInfo;
        userInfo = kakaoLoginService.getUserInfo(requestDTO.getAccessToken());
        // 유저 정보에 DB 조회하고 정보 있으면 응답만, 없으면 저장까지, 추가정보 입력 여부에 따라서 응답 다르게
        String socialId = userInfo.getSocialId();
        User user;
        if( userQueryService.isExistBySocialId(socialId)){
            user = userQueryService.getUserBySocialId(socialId);
        }
        else{
            user = UserConverter.toUser(userInfo);//, requestDTO.getAdditionalInfo());
            user = userCommandService.create(user);
        }
        // 응답본문에 토큰 추가
        JwtToken token = jwtUtil.generateToken(String.valueOf(user.getId()) );
        // refresh token Redis에 저장
        return ApiResponse.onSuccess(LoginConverter.toLoginDTO(token.getAccessToken()));
    }

    @Operation(summary = "애플 소셜 토큰 검증 API",description = "추가정보와 ID토큰을 받으면 ID토큰을 검증하고 통과 시" +
            "액세스/리프레시 토큰을 얻어서 저장시키고. 응답으로 서버에서 발급한 토큰을 받습니다. 회원가입을 하지 않은 사용자의 경우 회원가입을 시킵니다.")
    @ResponseBody
    @PostMapping("/oauth2/apple/token/validate")
    public ApiResponse<LoginResponseDTO.LoginDTO> validateAppleToken(@RequestBody @Valid LoginRequestDTO.AppleTokenValidateDTO requestDTO)  {
        // 검증하기
        appleLoginService.validate(requestDTO.getIdentityToken());
        log.info("애플 ID 토큰 검증 성공");
        // 검증 성공 시 리프레시 토큰 발급받아 저장(기한 무제한, 회원탈퇴 시 필요)
        TokenResponse tokenResponse = appleLoginService.getAccessTokenByCode(requestDTO.getAuthorizationCode());
        // 유저 정보 조회 및 저장
        String socialId = requestDTO.getSocialId();
        User user;
        if(userQueryService.isExistBySocialId(socialId)){
            user = userQueryService.getUserBySocialId(socialId);
        }
        else{
            user = UserConverter.toUser(requestDTO);
            User.setAppleRefreshToken(user, tokenResponse.getRefreshToken());
            user = userCommandService.create(user);
        }
        // 응답본문에 토큰 추가
        JwtToken token = jwtUtil.generateToken(String.valueOf(user.getId()) );
        return ApiResponse.onSuccess(LoginConverter.toLoginDTO(token.getAccessToken() ) );
    }

    @Operation(summary = "이메일 로그인 API", description = "이메일과 비밀번호를 받아서 이메일과 비밀번호의 해시값 쌍이 같은지를 판단하여" +
            "로그인 성공 여부를 응답합니다. 성공 시 리프레시 토큰을 저장하고 액세스 토큰을 본문에 담아 보냅니다.")
    @PostMapping("/auth/email/sign-in")
    @ResponseBody
    public ApiResponse<LoginResponseDTO.LoginDTO> validatePassword(@RequestBody @Valid LoginRequestDTO.PasswordValidateDTO passwordValidateDTO){
        User user = emailLoginService.verifyPassword(passwordValidateDTO);
        JwtToken token = jwtUtil.generateToken(String.valueOf(user.getId() ) );
        return ApiResponse.onSuccess(LoginConverter.toLoginDTO(token.getAccessToken() ) );
    }

    @Operation(summary = "이메일 인증 코드 전송 API",description = "이메일 주소를 받아 해당 이메일에 인증코드를 발송합니다.")
    @PostMapping("/auth/email")
    @ResponseBody
    public ApiResponse<String> sendAuthCode(@RequestBody @Valid LoginRequestDTO.SendAuthCodeDTO requestDTO)  {
        emailLoginService.sendAuthCode(requestDTO);
        return ApiResponse.onSuccess("이메일 발송에 성공하였습니다.");
    }
    @Operation(summary = "이메일 인증 코드 검증 API",description = "이메일 주소와 코드를 받아 해당 코드가 발송된 코드가 맞는지를 검증합니다.")
    @PostMapping("/auth/email/verification")
    @ResponseBody
    public ApiResponse<String> verifyAuthCode(@RequestBody @Valid LoginRequestDTO.VerifyAuthCodeDTO requestDTO)  {
        emailLoginService.verifyAuthCode(requestDTO);
        return ApiResponse.onSuccess("인증코드 검증에 성공하였습니다.");
    }

    @Operation(summary = "회원가입 API", description = "이름, 이메일, 비밀번호, 성별을 받아 유저 정보를 저장시킨다")
    @PostMapping("/sign-up")
    @ResponseBody
    public ApiResponse<LoginResponseDTO.SignUpDTO> signUp(@RequestBody @Valid LoginRequestDTO.SignUpDTO signUpDTO){
        User user = userCommandService.create(UserConverter.touser(signUpDTO));
        return ApiResponse.onSuccess(LoginConverter.toSignUpDTO(user.getId()));
    }
    /*
    테스트용 API
     */
    @GetMapping("/oauth2/login/kakao")
    @Operation(summary = "인증 코드를 통해서 인증 토큰을 발급해주는 API", description = "카카오 서버에 인증 코드를 보내 인증토큰을 받아 응답본문에 넣어 응답한다.")
    @ResponseBody
    public ResponseEntity<String> getAccessToken(@RequestParam(name = "code") String code){
        TokenResponse tokenResponse = kakaoLoginService.getAccessTokenByCode(code);
        return ResponseEntity.ok("accessToken="+ tokenResponse.getAccessToken() +
                "\n\nidToken=" + tokenResponse.getIdToken());
    }


    @ResponseBody
    @DeleteMapping("/logout")
    @Operation(summary = "로그 아웃 API",description = "redis에 저장된 리프레시 토큰을 삭제하고 fcm 구독목록을 모두 해제한다.")
    public ApiResponse<String> logout(HttpServletRequest request){
        // redis에 저장된 토큰 만료 시키기
        String accessToken = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }
        jwtUtil.revokeTokens(accessToken);
        // 기기토큰 구독정보 만료 시키고, DB에 저장된 기기토큰 정보 삭제
        User user = userCommandService.revokeRegistrationToken(UserContext.getUserId());

        return ApiResponse.onSuccess(user.getId() + "번 유저를 로그아웃 처리 하였습니다.");
    }

    @ResponseBody
    @DeleteMapping("/users/me")
    @Operation(summary = "회원 탈퇴 API",description = "유저정보를 삭제하고 외래키로 사용된 ID를 null로 만듭니다.(HARD DELETE)")
    public ApiResponse<String> withdrawUser(){
        return ApiResponse.onSuccess(userCommandService.withdrawUser().getId() + "번 유저가 삭제되었습니다.");
    }

}
