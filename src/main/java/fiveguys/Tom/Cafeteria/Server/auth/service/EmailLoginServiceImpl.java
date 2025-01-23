package fiveguys.Tom.Cafeteria.Server.auth.service;

import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.auth.dto.LoginRequestDTO;
import fiveguys.Tom.Cafeteria.Server.common.RedisService;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import fiveguys.Tom.Cafeteria.Server.user.service.UserQueryService;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailLoginServiceImpl implements EmailLoginService {
    private final UserQueryService userQueryService;
    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender emailSender;
    private final ResourceLoader resourceLoader;
    private final RedisService redisService;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Duration duration = Duration.ofMinutes(3);


    @Override
    public void sendAuthCode(LoginRequestDTO.SendAuthCodeDTO loginFormDTO) {
        if(userQueryService.isExistByEmail(loginFormDTO.getEmail())){
            throw new GeneralException(ErrorStatus.EMAIL_DUPLICATED_ERROR);
        }
        String authCode = generateAuthCode(8);
        redisService.setValue("USER:" + loginFormDTO.getEmail() + "authCode:", authCode, duration );
        MimeMessage mimeMessage = createEmailForm(loginFormDTO.getEmail(), authCode);
        emailSender.send(mimeMessage);
    }

    private MimeMessage createEmailForm(String email, String authCode){
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");


        String htmlMsg = getHtmlFromResource("classpath:/static/emailAuth.html");
        htmlMsg = htmlMsg.replace("AUTH_CODE", authCode);

        try{
            helper.setTo(email);
            helper.setSubject("탐식당 회원가입 인증 코드");
            helper.setFrom(sender);
            helper.setText(htmlMsg, true); // 'true' indicates that this is an HTML email
        }
        catch (MessagingException e){
            e.printStackTrace();
        }
        return mimeMessage;
    }

    private String getHtmlFromResource(String resourcePath) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceLoader.getResource(resourcePath).getInputStream(), StandardCharsets.UTF_8));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String generateAuthCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }


    @Override
    public void verifyAuthCode(LoginRequestDTO.VerifyAuthCodeDTO verifyAuthCodeDTO) {
        String value = redisService.getValue("USER:" + verifyAuthCodeDTO.getEmail() + "authCode:");
        if(value == null){
            throw new GeneralException(ErrorStatus.EMAIL_IS_NOT_SAME);
        }
        if(! (value.equals(verifyAuthCodeDTO.getAuthCode()))){
            throw new GeneralException(ErrorStatus.AUTHCODE_NOT_MATCH);
        }
    }

    @Override
    public User verifyPassword(LoginRequestDTO.PasswordValidateDTO requestDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userQueryService.getUserByEmail(requestDTO.getEmail());
        String userPassword = user.getPassword();

        if(!encoder.matches(requestDTO.getPassword(), userPassword)){
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_MATCH);
        }
        return user;
    }

}
