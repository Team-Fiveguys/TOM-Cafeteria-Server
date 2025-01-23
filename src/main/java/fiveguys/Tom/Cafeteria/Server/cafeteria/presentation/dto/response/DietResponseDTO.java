package fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.response;

import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


public class DietResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DietCreateDTO {
        private List<String> menuNameList;
        private LocalDate date;
        private Meals meals; //key와 enum 클래스명이 같으면 매핑 가능
        private Long cafeteriaId;
        private boolean dayOff;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DietQueryDTO {
        private Long dietId;
        private LocalDate date;
        private String photoURI;
        private MenuResponseDTO.MenuResponseListDTO menuResponseListDTO;
        private boolean soldOut;
        private boolean dayOff;
        private Meals meals;
    }
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ThreeWeeksDietsResponseDTO {
        private Long cafeteriaId;
        private List<DietResponseDTO.DietQueryDTO> ThreeWeeksResponseDTO;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SwitchSoldOutResponseDTO {
        private boolean isSoldOut;
    }
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SwitchDayOffResponseDTO {
        private boolean isDayOff;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MainViewResponseDTO{
        List<ThreeWeeksDietsResponseDTO> threeWeeksDietsResponseDTOS;
    }

}
