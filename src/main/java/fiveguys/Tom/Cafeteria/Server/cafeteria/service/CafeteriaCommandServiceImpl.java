package fiveguys.Tom.Cafeteria.Server.cafeteria.service;

import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.cafeteria.event.DietPhotoUploadEvent;
import fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.converter.CafeteriaConverter;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.DietPhoto;
import fiveguys.Tom.Cafeteria.Server.cafeteria.repository.CafeteriaRepository;
import fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.request.CafeteriaRequestDTO;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Congestion;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Menu;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CafeteriaCommandServiceImpl implements CafeteriaCommandService {
    private final CafeteriaRepository cafeteriaRepository;
    private final CafeteriaQueryService cafeteriaQueryService;
    private final DietPhotoService dietPhotoService;
    private final ApplicationEventPublisher publisher;

    @Override
    public Cafeteria register(CafeteriaRequestDTO.CafeteriaCreateDTO cafeteriaCreateDTO) {
        boolean exists = cafeteriaRepository.existsByName(cafeteriaCreateDTO.getName());
        if (exists){
            throw new GeneralException(ErrorStatus.CAFETERIA_NAME_DUPLICATE);
        }
        return cafeteriaRepository.save(CafeteriaConverter.toCafeteria(cafeteriaCreateDTO));
    }

    @Override
    public Congestion setCongestion(Long cafeteriaId, Congestion congestion) {
        Cafeteria cafeteria = cafeteriaRepository.
                findById(cafeteriaId).orElseThrow(() -> new GeneralException(ErrorStatus.CAFETERIA_NOT_FOUND));
        cafeteria.setCongestion(congestion);
        return cafeteria.getCongestion();
    }

    @Override
    public Menu addMenu(Long cafeteriaId, String menuName) {
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithMenuList(cafeteriaId);
        return cafeteria.addMenu(menuName);
    }

    @Transactional
    @Override
    public void removeMenu(Long cafeteriaId, Long menuId) {
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithMenuList(cafeteriaId);
        cafeteria.removeMenu(menuId);
    }

    @Transactional
    @Override
    public Diet registerDiet(Long cafeteriaId, LocalDate date, Meals meals, boolean dayOff, Set<String> menuNameSet) {
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithMenuList(cafeteriaId);
        List<Menu> menuListByName = cafeteria.getMenuListByName(menuNameSet);
        return cafeteria.registerDiet(date, meals, dayOff, menuListByName);
    }

    @Transactional
    @Override
    public void excludeMenuFromDiet(Long cafeteriaId, LocalDate date, Meals meals, String menuName) {
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithMenuList(cafeteriaId);
        Diet diet = cafeteria.getDietByDateAndMeals(date, meals);
        diet.excludeMenu(menuName);
    }

    @Transactional
    @Override
    public boolean toggleSoldOut(Long cafeteriaId, LocalDate date, Meals meals) {
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithDietList(cafeteriaId);
        Diet diet = cafeteria.getDietByDateAndMeals(date, meals);
        diet.switchSoldOut();
        return diet.isSoldOut();
    }

    @Transactional
    @Override
    public boolean switchDayOff(Long cafeteriaId, LocalDate date, Meals meals) {
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithDietList(cafeteriaId);
        // 식단 미등록 시엔 휴무 식단 등록
        if(!cafeteria.isRegistered(date, meals)){
            cafeteria.registerDietOnDayOff(date, meals);
            return true;
        }
        else{
            // 휴무 해제시엔 식단 자체를 삭제
            Diet diet = cafeteria.getDietByDateAndMeals(date, meals);
            if(diet.isDayOff()) {
                cafeteria.removeDiet(diet);
            }
            else {
                diet.switchDayOff();
            }
            return diet.isDayOff();
        }
    }

    @Override
    public DietPhoto uploadDietPhoto(Long cafeteriaId, LocalDate date, Meals meals, MultipartFile multipartFile) {
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithDietList(cafeteriaId);
        Diet diet = cafeteria.getDietByDateAndMeals(date, meals);
        DietPhoto dietPhoto = dietPhotoService.uploadDietPhoto(diet, multipartFile);
        publisher.publishEvent(new DietPhotoUploadEvent(this, cafeteria.getName(), diet.getLocalDate(), diet.getMeals(), AppNotificationType.dietPhotoEnroll));
        return dietPhoto;

    }

    @Override
    public DietPhoto reuploadDietPhoto(Long cafeteriaId, LocalDate date, Meals meals, MultipartFile multipartFile) {
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithDietList(cafeteriaId);
        Diet diet = cafeteria.getDietByDateAndMeals(date, meals);
        if(diet.getDietPhoto() == null){
            throw new GeneralException(ErrorStatus.DIET_PHOTO_NOT_FOUND);
        }
        String previousImageKey = diet.getDietPhoto().getImageKey();
        DietPhoto dietPhoto = dietPhotoService.reuploadDietPhoto(diet, multipartFile);
        dietPhotoService.deleteFile(previousImageKey);
        return dietPhoto;
    }
}
