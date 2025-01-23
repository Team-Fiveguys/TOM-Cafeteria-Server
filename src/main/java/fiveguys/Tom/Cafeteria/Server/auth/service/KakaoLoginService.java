package fiveguys.Tom.Cafeteria.Server.auth.service;


import fiveguys.Tom.Cafeteria.Server.auth.feignClient.TokenResponse;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.kakao.KakaoApiClient;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.kakao.KakaoAuthClient;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.kakao.dto.KakaoReqeustDTO;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.kakao.dto.KakaoResponseDTO;
import fiveguys.Tom.Cafeteria.Server.user.entity.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoLoginService implements LoginService {
    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthClient kakaoAuthClient;
    private final SocialTokenValidator validator;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    @Value("${kakao.admin-key}")
    private String adminKey;

    public KakaoResponseDTO.UserInfoResponseDTO getUserInfo(String accessToken){
        String cleanedAccessToken = cleanToken(accessToken);
        accessToken = "bearer " + cleanedAccessToken;
        KakaoResponseDTO.UserInfoResponseDTO userInfo = kakaoApiClient.getUserInfo(accessToken);
        return userInfo;
    }

    @Override
    public TokenResponse getAccessTokenByCode(String code) {
        return kakaoAuthClient.getAccessToken("authorization_code",
                clientId,
                redirectUri,
                code);
    }

    @Override
    public void validate(String idToken) {
        String cleanedAccessToken = cleanToken(idToken);
        validator.validate(cleanedAccessToken, SocialType.KAKAO);
    }

    @Override
    public void revokeTokens(String socialId) {
        MultiValueMap<String, String> urlEncoded = KakaoReqeustDTO.UnlinkRequest.builder()
                .targetIdType("user_id")
                .targetId(socialId)
                .build()
                .toUrlEncoded();
        log.info("adminkey = {}", adminKey);
        ResponseEntity responseEntity = kakaoApiClient.unlink("KakaoAK " + adminKey, urlEncoded);
        log.info("삭제된 회원 정보 = {}", responseEntity.getBody());
    }
}
