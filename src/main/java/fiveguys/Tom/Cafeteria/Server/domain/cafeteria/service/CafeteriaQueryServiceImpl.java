package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.service;

import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.repository.CafeteriaRepository;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CafeteriaQueryServiceImpl implements CafeteriaQueryService{
    private final CafeteriaRepository cafeteriaRepository;
    @Override
    public Cafeteria getById(Long id) {
        Cafeteria cafeteria = cafeteriaRepository
                .findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CAFETERIA_NOT_FOUND));
        return cafeteria;

    }
    @Override
    public Cafeteria getByIdWithMenuList(Long id) {
        Cafeteria cafeteria = cafeteriaRepository
                .findByIdWithMenuList(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CAFETERIA_NOT_FOUND));
        return cafeteria;
    }

    @Override
    public Cafeteria getByIdWithDietList(Long id) {
        Cafeteria cafeteria = cafeteriaRepository
                .findByIdWithDietList(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CAFETERIA_NOT_FOUND));
        return cafeteria;
    }

    @Override
    public Cafeteria findByName(String name) {
        Cafeteria findedCafeteria = cafeteriaRepository.
                findByName(name).orElseThrow(() -> new GeneralException(ErrorStatus.CAFETERIA_NOT_FOUND));
        return findedCafeteria;
    }

    @Override
    public List<Cafeteria> findAll() {
        return cafeteriaRepository.findAll();
    }

    @Override
    public Diet getDiet(Long cafeteriaId, LocalDate date, Meals meals) {
        Cafeteria cafeteria = getById(cafeteriaId);
        return cafeteria.getDietByDateAndMeals(date, meals);
    }
}
