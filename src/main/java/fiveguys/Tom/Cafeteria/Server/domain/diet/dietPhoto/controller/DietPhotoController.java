package fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.controller;


import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.converter.DietPhotoConverter;
import fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.dto.DietPhotoResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.entity.DietPhoto;
import fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.service.DietPhotoService;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.request.DietRequestDTO;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequiredArgsConstructor
@RequestMapping("/diets")
public class DietPhotoController {
    private final DietPhotoService dietPhotoService;
    @Operation(summary = "식단 사진을 업로드하는 API")
    @PostMapping(value = "/{cafeteriaId}/diets/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DietPhotoResponseDTO.DietPhotoUploadDTO> uploadDietPhoto(
            @PathVariable(name = "cafeteriaId")Long cafeteriaId,
            @RequestPart(name = "photo")MultipartFile multipartFile,
            @RequestPart(name = "dietQuery") DietRequestDTO.DietQueryDTO dto){
        DietPhoto dietPhoto = dietPhotoService.uploadDietPhoto(cafeteriaId, dto.getLocalDate(), dto.getMeals(), multipartFile);
        return ApiResponse.onSuccess(DietPhotoConverter.toDietPhotoUploadDTO(dietPhoto));
    }

    @Operation(summary = "식단 사진을 재업로드하는 API")
    @PutMapping(value = "/{cafeteriaId}/diets/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DietPhotoResponseDTO.DietPhotoUploadDTO> updateDietPhoto(
            @PathVariable(name = "cafeteriaId")Long cafeteriaId,
            @RequestPart(name = "photo")MultipartFile multipartFile,
            @RequestPart(name = "dietQuery") DietRequestDTO.DietQueryDTO dto){
        DietPhoto previousDietPhoto = dietPhotoService.getDietPhoto(cafeteriaId, dto.getLocalDate(), dto.getMeals())
                .orElseThrow(() -> new GeneralException(ErrorStatus.DIET_PHOTO_NOT_FOUND));
        String previousImageKey = previousDietPhoto.getImageKey();
        DietPhoto dietPhoto = dietPhotoService.reuploadDietPhoto(cafeteriaId, dto.getLocalDate(), dto.getMeals(), multipartFile);
        dietPhotoService.deleteFile(previousImageKey);
        return ApiResponse.onSuccess(DietPhotoConverter.toDietPhotoUploadDTO(dietPhoto));
    }
}
