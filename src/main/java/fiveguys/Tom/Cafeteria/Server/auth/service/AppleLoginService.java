package fiveguys.Tom.Cafeteria.Server.auth.service;

import fiveguys.Tom.Cafeteria.Server.auth.feignClient.TokenResponse;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.apple.AppleAuthClient;
import fiveguys.Tom.Cafeteria.Server.auth.feignClient.dto.AppleRequestDTO;
import fiveguys.Tom.Cafeteria.Server.user.entity.SocialType;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleLoginService implements LoginService{
    private final AppleAuthClient appleAuthClient;
    private final SocialTokenValidator validator;
    @Value("${apple.key.id}")
    private String kid;
    @Value("${apple.key.path}")
    private String keyPath;
    @Value("${apple.client-id}")
    private String clientId;
    @Value("${apple.iss}")
    private String iss;
    @Value("${apple.team-id}")
    private String teamId;
    @Override
    public TokenResponse getAccessTokenByCode(String code) {
        // client secret 만들기
        String clientSecret = createClientSecret();
        log.info("clientSecret = {}", clientSecret);
        // 요청
        MultiValueMap<String, String> urlEncoded = AppleRequestDTO.TokenRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .grantType("authorization_code")
                .build().toUrlEncoded();
        return appleAuthClient.getToken(urlEncoded);
    }
    private String createClientSecret() {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

        try {
            log.info("teamId = {}", teamId);
            log.info("kid = {}", kid);
            return Jwts.builder()
                    .header()
                    .keyId(kid)
                    .add("alg", "ES256")
                    .and()
                    .subject(clientId) // 토큰의 주체 = 우리 앱
                    .issuer(teamId)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(expirationDate) // 만료 시간
                    .audience()
                    .add(iss)
                    .and()
                    .signWith(getPrivateKey(), Jwts.SIG.ES256)
                    .compact();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private PrivateKey getPrivateKey() throws IOException {
        String privateKey = new String(Files.readAllBytes(Paths.get(keyPath) ) );
        log.info("privateKey = {}", privateKey);
        PemReader pemReader = new PemReader(new StringReader(privateKey));
        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(content);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void validate(String identityToken){
        String cleanedIdentityToken = cleanToken(identityToken);
        validator.validate(cleanedIdentityToken, SocialType.APPLE);
    }

    public void revokeTokens(String refreshToken) {
        String clientSecret = createClientSecret();
        MultiValueMap<String, String> urlEncoded = AppleRequestDTO.RevokeTokenReqeust.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .token(refreshToken)
                .build().toUrlEncoded();
        appleAuthClient.revokeTokens(urlEncoded);
    }

}