package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.converter;


import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response.DietResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response.MenuResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DietConverter {

    public static DietResponseDTO.DietQueryDTO toDietResponseDTO(Diet diet, String prefixURI){

        List<MenuResponseDTO.MenuQueryDTO> menuQueryDTOList = diet.getMenuDietList().stream()
                .map(menuDiet -> menuDiet.getMenu())
                .map(menu -> MenuConverter.toMenuQueryDTO(menu))
                .collect(Collectors.toList());
        MenuResponseDTO.MenuResponseListDTO menuResponseListDTO = MenuConverter.toMenuResponseListDTO(menuQueryDTOList);

        return DietResponseDTO.DietQueryDTO.builder()
                .dietId(diet.getId())
                .menuResponseListDTO(menuResponseListDTO)
                .photoURI(diet.getDietPhoto() != null ? prefixURI + diet.getDietPhoto().getImageKey() : "사진이 등록되어있지 않습니다.")
                .dayOff(diet.isDayOff())
                .soldOut(diet.isSoldOut())
                .date(diet.getLocalDate())
                .meals(diet.getMeals())
                .build();
    }
    public static DietResponseDTO.DietCreateDTO toDietCreateResponseDTO(Diet diet){
        List<String> registeredMenuList = diet.getMenuDietList().stream()
                .map(menuDiet -> menuDiet.getMenu().getName())
                .collect(Collectors.toList());
        return DietResponseDTO.DietCreateDTO.builder()
                .menuNameList(registeredMenuList)
                .cafeteriaId(diet.getCafeteria().getId())
                .date(diet.getLocalDate())
                .meals(diet.getMeals())
                .build();
    }

    public static DietResponseDTO.ThreeWeeksDietsResponseDTO toThreeWeeksDietsResponseDTO(List<DietResponseDTO.DietQueryDTO> dietResponseDTOList){
        return DietResponseDTO.ThreeWeeksDietsResponseDTO.builder()
                .ThreeWeeksResponseDTO(dietResponseDTOList)
                .build();
    }
    public static DietResponseDTO.SwitchSoldOutResponseDTO toSwitchSoldOutResponseDTO(boolean isSoldOut){
        return DietResponseDTO.SwitchSoldOutResponseDTO.builder()
                .isSoldOut(isSoldOut)
                .build();
    }
    public static DietResponseDTO.SwitchDayOffResponseDTO toSwitchDayOffResponseDTO(boolean isDayOff){
        return DietResponseDTO.SwitchDayOffResponseDTO.builder()
                .isDayOff(isDayOff)
                .build();
    }
}
