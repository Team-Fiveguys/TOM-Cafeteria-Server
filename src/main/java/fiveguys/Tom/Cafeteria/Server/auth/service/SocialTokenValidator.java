package fiveguys.Tom.Cafeteria.Server.auth.service;

import fiveguys.Tom.Cafeteria.Server.user.entity.SocialType;

/**
 * ID토큰 검증을 처리하는 역할
 */
public interface SocialTokenValidator {
    void validate(String token, SocialType socialType);

}
