package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.request;

import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Congestion;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

public class CafeteriaRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CafeteriaCreateDTO {
        private String name;
        private String location;
        private boolean runBreakfast;
        private boolean runLunch;
        private LocalTime breakfastStartTime; //조식 운영 시작 시간

        private LocalTime breakfastEndTime; //조식 운영 시작 시간

        private LocalTime lunchStartTime; //중식 운영 시작 시간

        private LocalTime lunchEndTime; //중식 운영 시작 시간

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SetCongestionDTO{
        private Congestion congestion;
    }
}
