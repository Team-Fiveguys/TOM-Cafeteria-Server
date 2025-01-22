package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.converter;

import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.request.CafeteriaRequestDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response.CafeteriaResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Congestion;

import java.util.List;
import java.util.stream.Collectors;

public class CafeteriaConverter {
    public static Cafeteria toCafeteria(CafeteriaRequestDTO.CafeteriaCreateDTO cafeteriaCreateDTO){
        Cafeteria newCafeteria = Cafeteria.builder()
                .location(cafeteriaCreateDTO.getLocation())
                .runBreakfast(cafeteriaCreateDTO.isRunBreakfast())
                .runLunch(cafeteriaCreateDTO.isRunLunch())
                .breakfastStartTime(cafeteriaCreateDTO.getBreakfastStartTime())
                .breakfastEndTime(cafeteriaCreateDTO.getBreakfastEndTime())
                .lunchStartTime(cafeteriaCreateDTO.getLunchStartTime())
                .lunchEndTime(cafeteriaCreateDTO.getLunchEndTime())
                .name(cafeteriaCreateDTO.getName())
                .build();
        return newCafeteria;
    }
    public static CafeteriaResponseDTO.CreateResponseDTO toCafeteriaResponse(Cafeteria cafeteria){
        return CafeteriaResponseDTO.CreateResponseDTO.builder()
                .cafeteriaId(cafeteria.getId())
                .build();
    }

    public static CafeteriaResponseDTO.SetCongestionResponseDTO toSetCongestionResponseDTO(Congestion congestion){
        return CafeteriaResponseDTO.SetCongestionResponseDTO.builder()
                .congestion(congestion)
                .build();
    }
    public static CafeteriaResponseDTO.QueryCongestionResponseDTO toQueryCongestionResponseDTO(Congestion congestion){
        return CafeteriaResponseDTO.QueryCongestionResponseDTO.builder()
                .congestion(congestion)
                .build();
    }

    public static CafeteriaResponseDTO.QueryRunResponseDTO toQueryRunResponseDTO(Cafeteria cafeteria){
        return CafeteriaResponseDTO.QueryRunResponseDTO.builder()
                .runBreakfast(cafeteria.isRunBreakfast())
                .runLunch(cafeteria.isRunLunch())
                .build();
    }

    public static CafeteriaResponseDTO.QueryCafeteriaList toQueryCafeteriaList(List<Cafeteria> cafeteriaList){
        List<CafeteriaResponseDTO.QueryCafeteria> queryCafeteriaList = cafeteriaList.stream()
                .map(CafeteriaConverter::toQueryCafeteria)
                .collect(Collectors.toList());

        return CafeteriaResponseDTO.QueryCafeteriaList.builder()
                .queryCafeteriaList(queryCafeteriaList)
                .build();

    }

    public static CafeteriaResponseDTO.QueryCafeteria toQueryCafeteria(Cafeteria cafeteria){
        return CafeteriaResponseDTO.QueryCafeteria.builder()
                .runLunch(cafeteria.isRunLunch())
                .runBreakfast(cafeteria.isRunBreakfast())
                .breakfastStartTime(cafeteria.getBreakfastStartTime())
                .breakfastEndTime(cafeteria.getBreakfastEndTime())
                .lunchStartTime(cafeteria.getLunchStartTime())
                .lunchEndTime(cafeteria.getLunchEndTime())
                .name(cafeteria.getName())
                .location(cafeteria.getLocation())
                .build();
    }
}
