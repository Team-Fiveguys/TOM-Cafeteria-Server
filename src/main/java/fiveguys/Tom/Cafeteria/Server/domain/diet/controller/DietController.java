package fiveguys.Tom.Cafeteria.Server.domain.diet.controller;


import fiveguys.Tom.Cafeteria.Server.domain.diet.service.DietQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diets")
public class DietController {
    private final DietQueryService dietQueryService;
    @Value("${cloud.aws.s3.path.prefix}")
    private String prefixURI;

//    @Operation(summary =  "특정 식당의 3주치의 식단표 조회 API", description = "식당id를 입력 받아 해당 식당의 3주치의 식단 정보 리스트를 반환한다. 날짜순 오름차순으로 반환한다.")
//    @GetMapping("/main")
//    public ApiResponse<List<DietResponseDTO.DietQueryDTO>> getWeekDietsTable(@RequestParam(name = "cafeteriaId") Long cafeteriaId) {
//        // 식당 id 조건으로 해서 3주치 식단 정보 가져오기
//        List<Diet> threeWeeksDiet = dietQueryService.getThreeWeeksDiet(cafeteriaId);
//        List<DietResponseDTO.DietQueryDTO> dietQueryDTOList = threeWeeksDiet.stream() // diet와 연관된 dietphoto, dietMenu 가져오기
//                .map(diet -> {
//                    List<MenuResponseDTO.MenuQueryDTO> menuQueryDTOList = diet.getMenuDietList().stream()
//                            .map(MenuDiet::getMenu)
//                            .map(MenuConverter::toMenuQueryDTO)
//                            .collect(Collectors.toList());
//                    DietResponseDTO.DietQueryDTO dietQueryResponseDTO = DietConverter.toDietResponseDTO(prefixURI, diet, MenuConverter.toMenuResponseListDTO(menuQueryDTOList));
//                    return dietQueryResponseDTO;
//                })
//                .collect(Collectors.toList());
//        return ApiResponse.onSuccess(dietQueryDTOList);
//    }
}
