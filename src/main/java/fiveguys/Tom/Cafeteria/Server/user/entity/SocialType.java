package fiveguys.Tom.Cafeteria.Server.user.entity;

public enum SocialType {
    EMAIL, KAKAO, APPLE, GOOGLE;

    public static boolean isApple(SocialType socialType) {
        return socialType == APPLE;
    }
}
