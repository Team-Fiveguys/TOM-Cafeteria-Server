package fiveguys.Tom.Cafeteria.Server.domain.diet.controller;


import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.domain.diet.converter.DietConverter;
import fiveguys.Tom.Cafeteria.Server.domain.diet.dto.DietCreateDTO;
import fiveguys.Tom.Cafeteria.Server.domain.diet.dto.DietResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.diet.entity.Diet;
import fiveguys.Tom.Cafeteria.Server.domain.diet.service.DietCommandService;
import fiveguys.Tom.Cafeteria.Server.domain.menu.entity.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/diet")
public class AdminDietController {

    private final DietCommandService dietCommandService;
    @PostMapping("/")
    public ApiResponse<DietResponseDTO> getDiet(@RequestBody DietCreateDTO dietCreateDTO){
        List<Long> menuList = dietCreateDTO.getMenuIdList();
        Diet diet = dietCommandService.createDiet(DietConverter.toDiet(dietCreateDTO), menuList);
        return ApiResponse.onSuccess(DietConverter.toDietResponseDTO(diet));
    }
}
