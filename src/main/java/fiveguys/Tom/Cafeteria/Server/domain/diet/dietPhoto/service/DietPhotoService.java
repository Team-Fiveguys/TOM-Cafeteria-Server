package fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.service;

import fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.entity.DietPhoto;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Meals;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

public interface DietPhotoService {
    public Optional<DietPhoto> getDietPhoto(Long cafeteriaId, LocalDate date, Meals meals);

    public DietPhoto uploadDietPhoto(Long cafeteriaId, LocalDate date, Meals meals, MultipartFile multipartFile);

    public DietPhoto reuploadDietPhoto(Long cafeteriaId, LocalDate date, Meals meals, MultipartFile multipartFile);

    public void deleteFile(String fileKey);
}
