package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.service;

import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.request.CafeteriaRequestDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Congestion;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Menu;

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
}
