package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response;


import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Congestion;
import lombok.*;

import java.time.LocalTime;
import java.util.List;


public class CafeteriaResponseDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateResponseDTO {
        private Long cafeteriaId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class QueryCafeteria {
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
    public static class QueryCafeteriaList {
        List<QueryCafeteria> queryCafeteriaList;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SetCongestionResponseDTO {
        private Congestion congestion;
    }
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class QueryCongestionResponseDTO {
        private Congestion congestion;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class QueryRunResponseDTO {
        private boolean runBreakfast;
        private boolean runLunch;
    }
}
