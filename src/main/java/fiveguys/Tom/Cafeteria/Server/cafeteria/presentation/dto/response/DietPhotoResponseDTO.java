package fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DietPhotoResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DietPhotoUploadDTO {
        private Long dietPhotoId;
        private String photoURI;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DietPhotoDTO{
        private Long dietPhotoId;
        private String photoURI;
    }
}
