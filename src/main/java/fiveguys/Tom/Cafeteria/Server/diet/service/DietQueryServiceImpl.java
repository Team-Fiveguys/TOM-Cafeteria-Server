package fiveguys.Tom.Cafeteria.Server.diet.service;

import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.diet.repository.DietRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class DietQueryServiceImpl implements DietQueryService{
    private final DietRepositoryCustom dietRepositoryCustom;

    @Override
    public List<Diet> getThreeWeeksDiet(Long cafeteriaId) {
        return dietRepositoryCustom.findDietsByThreeWeeks(cafeteriaId);
    }

    @Override
    public Diet getDiet(Long cafeteriaId, LocalDate localDate, Meals meals) {
        return null;
    }
}
