package fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.repository;

import fiveguys.Tom.Cafeteria.Server.domain.diet.dietPhoto.entity.DietPhoto;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Diet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietPhotoRepository extends JpaRepository<DietPhoto, Long> {
    public DietPhoto findByDiet(Diet diet);
}
