package fiveguys.Tom.Cafeteria.Server.cafeteria.service;

import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.DietPhoto;
import fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.request.CafeteriaRequestDTO;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Congestion;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Menu;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

public interface CafeteriaCommandService {
    public Cafeteria register(CafeteriaRequestDTO.CafeteriaCreateDTO cafeteriaCreateDTO);

    public Congestion setCongestion(Long cafeteriaId, Congestion congestion);

    public Menu addMenu(Long cafeteriaId, String menuName);

    public void removeMenu(Long cafeteriaId, Long menuId);

    public Diet registerDiet(Long cafeteriaId, LocalDate date, Meals meals, boolean dayOff, Set<String> menuNameSet);

    public void excludeMenuFromDiet(Long cafeteriaId, LocalDate date, Meals meals, String menuName);

    public boolean toggleSoldOut(Long cafeteriaId, LocalDate date, Meals meals);

    public boolean switchDayOff(Long cafeteriaId, LocalDate date, Meals meals);

    public DietPhoto uploadDietPhoto(Long cafeteriaId, LocalDate date, Meals meals, MultipartFile multipartFile);
    public DietPhoto reuploadDietPhoto(Long cafeteriaId, LocalDate date, Meals meals, MultipartFile multipartFile);
}
