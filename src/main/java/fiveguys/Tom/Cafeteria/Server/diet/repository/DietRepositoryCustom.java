package fiveguys.Tom.Cafeteria.Server.diet.repository;

import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import java.util.List;

public interface DietRepositoryCustom {
    List<Diet> findDietsByThreeWeeks(Long cafeteriaId);
}
