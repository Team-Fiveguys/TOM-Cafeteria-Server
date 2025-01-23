package fiveguys.Tom.Cafeteria.Server.auth.service;

import fiveguys.Tom.Cafeteria.Server.auth.dto.LoginRequestDTO;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;

public interface EmailLoginService {
    public void sendAuthCode(LoginRequestDTO.SendAuthCodeDTO requestDTO);

    public void verifyAuthCode(LoginRequestDTO.VerifyAuthCodeDTO requestDTO);

    public User verifyPassword(LoginRequestDTO.PasswordValidateDTO requestDTO);

}
