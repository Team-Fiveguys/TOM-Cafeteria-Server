package fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.request;

import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

public class DietRequestDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DietQueryDTO{
        private Meals meals;
        private LocalDate localDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class WeekDietQueryDTO{
        private Long cafeteriaId;
        private int year;
        private int month;
        private int weekNum;
        private Meals meals;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CheckDayOffDTO{
       private Long cafeteriaId;
       private LocalDate localDate;
       private Meals meals;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ChangeMenuDTO{
        private LocalDate localDate;
        private Meals meals;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DietCreateDTO {
        private Set<String> menuNameSet;
        private LocalDate date;
        private Meals meals; //key와 enum 클래스명이 같으면 매핑 가능
        private boolean dayOff;
    }
}
