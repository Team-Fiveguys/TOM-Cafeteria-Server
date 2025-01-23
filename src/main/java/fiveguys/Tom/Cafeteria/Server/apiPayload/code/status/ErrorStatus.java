package fiveguys.Tom.Cafeteria.Server.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import fiveguys.Tom.Cafeteria.Server.apiPayload.code.BaseCode;
import fiveguys.Tom.Cafeteria.Server.apiPayload.code.ReasonDTO;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 유저 관련 응답
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "해당 사용자가 존재하지 않습니다."),
    USER_PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4002", "해당 사용자의 프로필 이미지가 존재하지 않습니다."),
    NOTIFICATION_SET_IS_NOT_SET(HttpStatus.NOT_FOUND, "USER4003", "알림을 허용하지 않은 사용자 입니다."),
    // 게시글 관련 응답
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4001", "게시글이 존재하지 않습니다."),
    INVALID_POST_TYPE(HttpStatus.BAD_REQUEST, "POST4002", "요청한 게시물의 유형이 해당 기능을 지원하지 않습니다."),
    DUPLICATED_REPORT(HttpStatus.BAD_REQUEST, "REPORT4000", "이미 신고를 한 게시글 입니다."),
    // 메뉴 관련 응답
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU4001", "해당 메뉴 ID가 존재하지 않습니다."),
    MENU_DUPLICATE(HttpStatus.BAD_REQUEST, "MENU4002", "해당 메뉴 이름이 이미 존재합니다."),
    // 식단 관련 응답
    DIET_NOT_FOUND(HttpStatus.NOT_FOUND, "DIET4001", "해당 요일에 식단이 존재하지 않습니다."),
    DIET_PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "DIET4006", "해당 식단에 이미지가 존재하지 않습니다."),
    DIET_IS_EMPTY(HttpStatus.BAD_REQUEST, "DIET4002", "식당은 null일 수 없습니다."),
    DIET_IS_BLANK(HttpStatus.BAD_REQUEST, "DIET4003", "식당은 공백일 수 없습니다."),
    DIET_TOO_LONG(HttpStatus.BAD_REQUEST, "DIET4004", "해당 식당의 길이가 100을 넘어갑니다."),
    DIET_IS_ALREADY_ENROLLED(HttpStatus.BAD_REQUEST, "DIET4005", "식당에 이미 해당 날짜와 식때에 대한 식단이 등록되어 있습니다."),
    MENU_IS_NOT_FOUND_FROM_THIS_DIET(HttpStatus.NOT_FOUND, "DIET4007", "삭제하려는 메뉴가 해당 식단에 존재하지 않습니다."),
    // 식당 관련 응답
    CAFETERIA_NOT_FOUND(HttpStatus.NOT_FOUND, "CAFETERIA4001", "해당 식당 id가 존재하지 않습니다."),
    CAFETERIA_IS_EMPTY(HttpStatus.BAD_REQUEST, "CAFETERIA4002", "식당은 null일 수 없습니다."),
    CAFETERIA_IS_BLANK(HttpStatus.BAD_REQUEST, "CAFETERIA4003", "식당은 공백일 수 없습니다."),
    CAFETERIA_TOO_LONG(HttpStatus.BAD_REQUEST, "CAFETERIA4004", "해당 식당의 길이가 100을 넘어갑니다."),
    CAFETERIA_NAME_DUPLICATE(HttpStatus.BAD_REQUEST, "CAFETERIA4005", "등록하려는 식당의 이름이 이미 존재합니다."),

    // 알림 관련 에러
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION4001", "id에 해당하는 알림이 존재하지 않습니다."),
    NOTIFICATION_NOT_RELATIONAL(HttpStatus.BAD_REQUEST, "NOTIFICATION4002", "해당 user와 notification은 서로 관계가 없습니다."),
    NOTIFICATION_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTIFICATION4004", "옳지 않은 알림 타입입니다. 서버측에 확인 요청을 하세요"),

    REGISTRATION_TOKEN_EMPTY(HttpStatus.NOT_FOUND, "NOTIFICATION4003", "알림을 보낼 대상이 없습니다."),

    // 로그인 관련 에러
    INVALID_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "LOGIN4001", "토큰값이 잘못되었거나 유효하지 않습니다."),
    INVALID_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "LOGIN4002", "필수인자가 포함되지 않거나 인자값의 데이터 타입 또는 범위가 적절하지 않습니다."),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "LOGIN4003", "추가로 입력한 업무분야나 연차의 형식이 올바르지 않습니다."),
    EMAIL_DUPLICATED_ERROR(HttpStatus.BAD_REQUEST, "LOGIN4004","입력한 이메일로 가입한 아이디가 있습니다."),
    AUTHCODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "LOGIN4005", "입력한 검증코드가 올바르지 않습니다."),
    TEMPORARY_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR , "LOGIN5000", "카카오 플랫폼 서비스의 일시적 내부 장애입니다. 잠시후에 다시 시도해주세요"),
    EMAIL_IS_NOT_SAME(HttpStatus.BAD_REQUEST, "LOGIN4006", "인증을 요청한 이메일과 검증을 요청한 이메일이 다릅니다."),
    EMAIL_SENDER_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "LOGIN5001", "서버내의 이메일 sender가 정해지지 않았습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "LOGIN4006", "입력한 패스워드가 올바르지 않습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "LOGIN4008", "액세스 토큰이 만료되었습니다. 토큰 재발급 요청을 해주세요."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "LOGIN4007", "리프레시 토큰이 만료되었습니다. 재인증이 필요합니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ReasonDTO getReasonHttpStatus(){
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
