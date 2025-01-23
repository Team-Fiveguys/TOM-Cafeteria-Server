package fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.converter;

import fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.response.DietPhotoResponseDTO;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.DietPhoto;

public class DietPhotoConverter {
    public static DietPhotoResponseDTO.DietPhotoUploadDTO toDietPhotoUploadDTO (DietPhoto dietPhoto){
        return DietPhotoResponseDTO.DietPhotoUploadDTO.builder()
                .dietPhotoId(dietPhoto.getId())
                .photoURI(dietPhoto.getImageKey())
                .build();
    }
    public static DietPhotoResponseDTO.DietPhotoDTO toDietPhotoDTO (DietPhoto dietPhoto){
        return DietPhotoResponseDTO.DietPhotoDTO.builder()
                .dietPhotoId(dietPhoto.getId())
                .photoURI(dietPhoto.getImageKey())
                .build();
    }
}
