package fiveguys.Tom.Cafeteria.Server.cafeteria.service;

import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.DietPhoto;
import org.springframework.web.multipart.MultipartFile;

public interface DietPhotoService {
    public DietPhoto uploadDietPhoto(Diet diet, MultipartFile multipartFile);

    public DietPhoto reuploadDietPhoto(Diet diet, MultipartFile multipartFile);

    public void deleteFile(String fileKey);
}
