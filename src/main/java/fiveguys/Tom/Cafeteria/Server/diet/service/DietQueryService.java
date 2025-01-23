package fiveguys.Tom.Cafeteria.Server.diet.service;

import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;

import java.time.LocalDate;
import java.util.List;

public interface DietQueryService {

    List<Diet> getThreeWeeksDiet(Long cafeteriaId);
    public Diet getDiet(Long cafeteriaId, LocalDate localDate, Meals meals);
}
