package fiveguys.Tom.Cafeteria.Server.cafeteria.service;

import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;

import java.time.LocalDate;
import java.util.List;

public interface CafeteriaQueryService {
    public Cafeteria getById(Long id);
    public Cafeteria getByIdWithMenuList(Long id);

    public Cafeteria getByIdWithDietList(Long id);

    public Cafeteria findByName(String name);

    public List findAll();

    public Diet getDiet(Long cafeteriaId, LocalDate date, Meals meals);

}
