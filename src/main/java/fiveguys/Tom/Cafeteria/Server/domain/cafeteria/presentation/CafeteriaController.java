package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation;


import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.converter.CafeteriaConverter;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response.CafeteriaResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Congestion;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.service.CafeteriaQueryService;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.converter.DietConverter;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response.DietResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Meals;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cafeterias")
public class CafeteriaController {
    private final CafeteriaQueryService cafeteriaQueryService;
    @Value("${cloud.aws.s3.path.prefix}")
    private String prefixURI;
    @Operation(summary = "식당의 혼잡도를 조회하는 API", description = "식당 ID를 받아서 혼잡도를 응답한다.")
    @GetMapping("/{cafeteriaId}/congestion")
    public ApiResponse<CafeteriaResponseDTO.QueryCongestionResponseDTO> getCongestion(@PathVariable(name = "cafeteriaId")Long cafeteriaId){
        Cafeteria cafeteria = cafeteriaQueryService.getById(cafeteriaId);
        Congestion congestion = cafeteria.getCongestion();
        return ApiResponse.onSuccess(CafeteriaConverter.toQueryCongestionResponseDTO(congestion));
    }

    @Operation(summary = "식당의 조,중식 운영여부를 조회하는 API", description = "식당명을 받아서 조/중식 운영여부를 응답한다.")
    @GetMapping("/{cafeteriaName}/run")
    public ApiResponse<CafeteriaResponseDTO.QueryRunResponseDTO> getCongestion(@PathVariable(name = "cafeteriaName")String cafeteriaName){
        Cafeteria cafeteria = cafeteriaQueryService.findByName(cafeteriaName);
        return ApiResponse.onSuccess(CafeteriaConverter.toQueryRunResponseDTO(cafeteria));
    }

    @Operation(summary = "식단 조회 API", description = "식당id를 경로 변수, 날짜와 식때를 쿼리 파라미터로 받아 해당 식단의 id, 이미지, 메뉴명 리스트를 반환한다.")
    @GetMapping("/{cafeteriaId}/diets")
    public ApiResponse<DietResponseDTO.DietQueryDTO> getDietByCafeteriaAndDateAndMeals(@PathVariable(name = "cafeteriaId") Long cafeteriaId,
                                                             @RequestParam(name = "localDate") LocalDate date,
                                                             @RequestParam(name = "meals") Meals meals){
        Diet diet = cafeteriaQueryService.getDiet(cafeteriaId, date, meals);
        DietResponseDTO.DietQueryDTO dietQueryResponseDTO = DietConverter.toDietResponseDTO(diet, prefixURI);
        return ApiResponse.onSuccess(dietQueryResponseDTO);
    }
}
