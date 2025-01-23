package fiveguys.Tom.Cafeteria.Server.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.dto.JwkResponse;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.apple.AppleAuthClient;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.kakao.KakaoAuthClient;
import fiveguys.Tom.Cafeteria.Server.user.entity.SocialType;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class SocialTokenValidatorImpl implements SocialTokenValidator{
    private final KakaoAuthClient kakaoAuthClient;
    private final AppleAuthClient appleAuthClient;
    @Value("${kakao.iss}")
    private String kakaoIss;
    @Value("${kakao.client-id}")
    private String kakaoAud;
    @Value("${apple.iss}")
    private String appleIss;
    @Value("${apple.client-id}")
    private String appleAud;

    /**
     *  1. 공개키(JWK) 목록 조회하여 맞는 공개키 정보 획득
     *  2. 검증에 사용할 수 있는 공개키로 변환
     *  3. 공개키로 ID토큰 검증
     */
    @Override
    public void validate(String token, SocialType socialType) {
        JwkResponse.Jwk matchingJwk = getMatchingJwk(token, socialType);
        PublicKey publicKey = jwkToPublickey(matchingJwk);
        if( socialType.equals(SocialType.KAKAO)){
            verifyToken(token, kakaoIss, kakaoAud, publicKey);
            log.info("카카오 ID 토큰 인증 성공");
        }
        else {
            verifyToken(token, appleIss, appleAud, publicKey);
        }
    }

    // 발급자, 수신자, 만료 기한, 시그니처를 검증
    private Jwt<Header, Claims> verifyToken(String token, String iss, String aud, PublicKey publicKey) {
        try {
            return (Jwt<Header, Claims>) Jwts.parser()
                    .requireAudience(aud) //수신자 검증
                    .requireIssuer(iss) // 발급자 검증
                    .verifyWith(publicKey) // 시그니처 검증
                    .build()
                    .parse(token);
        }
        catch (MalformedJwtException | UnsupportedJwtException parseEx){
            throw new GeneralException(ErrorStatus.INVALID_ARGUMENT_ERROR);
        }
        catch (InvalidClaimException | ExpiredJwtException validateEx){
            throw new GeneralException(ErrorStatus.INVALID_TOKEN_ERROR);
        }
    }
    private JwkResponse.Jwk getMatchingJwk(String token, SocialType socialType){
        // header 부분만 디코드 해서 kid 가져오기
        String keyId = getKeyId(token);
        JwkResponse.JwkSet keys;
        if( socialType.equals(SocialType.KAKAO)) {
            keys = kakaoAuthClient.getKeys();
        }
        else{
            keys = appleAuthClient.getKeys();
        }
        return keys.getJwkList().stream()
                .filter(jwk -> jwk.getKid().equals(keyId))
                .findFirst()
                .orElseThrow();

    }
    private String getKeyId(String token){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] splitToken = token.split("\\.");
        String header = splitToken[0];
        String headerJson = new String(decoder.decode(header));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> headerMap = null;
        try {
            headerMap = mapper.readValue(headerJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return headerMap.get("kid");
    }
    private PublicKey jwkToPublickey(JwkResponse.Jwk jwk){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        byte[] decodeN = decoder.decode(jwk.getN());
        byte[] decodeE = decoder.decode(jwk.getE());
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }
}
