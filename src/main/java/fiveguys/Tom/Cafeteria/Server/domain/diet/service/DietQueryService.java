package fiveguys.Tom.Cafeteria.Server.domain.diet.service;

import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Meals;

import java.time.LocalDate;
import java.util.List;

public interface DietQueryService {

    List<Diet> getThreeWeeksDiet(Long cafeteriaId);
    public Diet getDiet(Long cafeteriaId, LocalDate localDate, Meals meals);

    public boolean existsDiet(Long cafeteriaId, LocalDate localDate, Meals meals);

}
