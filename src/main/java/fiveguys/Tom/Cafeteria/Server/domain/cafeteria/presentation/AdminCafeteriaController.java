package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation;

import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.converter.CafeteriaConverter;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.request.CafeteriaRequestDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response.CafeteriaResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Congestion;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.service.CafeteriaCommandService;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.service.CafeteriaQueryService;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.converter.DietConverter;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.request.DietRequestDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response.DietResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.converter.MenuConverter;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.presentation.dto.response.MenuResponseDTO;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Menu;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cafeterias")
public class AdminCafeteriaController {
    private final CafeteriaCommandService cafeteriaCommandService;
    private final CafeteriaQueryService cafeteriaQueryService;

    /*
     * ==========================
     * 식당 API
     * ==========================
     */
    @Operation(summary = "식당을 등록하는 API")
    @PostMapping("")
    public ApiResponse<CafeteriaResponseDTO.CreateResponseDTO> registerCafeteria(@RequestBody CafeteriaRequestDTO.CafeteriaCreateDTO cafeteriaCreateDTO){
        Cafeteria enrolledCafeteria = cafeteriaCommandService.register(cafeteriaCreateDTO);
        return ApiResponse.onSuccess(CafeteriaConverter.toCafeteriaResponse(enrolledCafeteria));
    }

    @Operation(summary = "식당의 현재 혼잡도를 등록하는 API", description = "식당 ID를 받아서 혼잡도를 등록하고 그 결과를 응답한다. ")
    @PostMapping("/{cafeteriaId}/congestion")
    public ApiResponse<CafeteriaResponseDTO.SetCongestionResponseDTO> setCongestion(@PathVariable(name = "cafeteriaId")Long cafeteriaId,
                                                                                    @RequestBody CafeteriaRequestDTO.SetCongestionDTO setCongestionDTO){
        Congestion congestion = cafeteriaCommandService.setCongestion(cafeteriaId, setCongestionDTO.getCongestion());
        return ApiResponse.onSuccess(CafeteriaConverter.toSetCongestionResponseDTO(congestion));
    }

    /*
     * ==========================
     * 메뉴 API
     * ==========================
     */
    @Operation(summary = "식당의 전 메뉴를 조회하는 API", description = "식당의 ID를 받아서 전체 메뉴명을 응답한다.")
    @GetMapping("/{cafeteriaId}/menus")
    public ApiResponse<MenuResponseDTO.MenuResponseListDTO> getAllMenu(@PathVariable(name = "cafeteriaId") Long cafeteriaId){
        Cafeteria cafeteria = cafeteriaQueryService.getByIdWithMenuList(cafeteriaId);
        List<MenuResponseDTO.MenuQueryDTO> menuQueryDTOList = cafeteria.getMenuList().stream()
                .map(menu -> MenuConverter.toMenuQueryDTO(menu))
                .collect(Collectors.toList());
        return ApiResponse.onSuccess(MenuConverter.toMenuResponseListDTO(menuQueryDTOList));
    }

    @Operation(summary = "메뉴를 등록하는 API", description = "특정 식당에 대해 메뉴를 등록한다.")
    @PostMapping("/{cafeteriaId}/menus")
    public ApiResponse<MenuResponseDTO.MenuRegisterDTO> registerMenu(@PathVariable Long cafeteriaId, @RequestBody Map<String, String> request){
        Menu menu = cafeteriaCommandService.addMenu(cafeteriaId, request.get("menuName"));
        return ApiResponse.onSuccess(MenuConverter.toRegisterResponseDTO(menu));
    }

    @Operation(summary = "메뉴를 삭제하는 API", description = "식당 ID와 이름을 받아서 해당 메뉴를 삭제한다. 메뉴가 식단에 포함되어 있었다면 제외시킨다.")
    @DeleteMapping("/{cafeteriaId}/menus/{menuId}")
    public ApiResponse removeMenu(@PathVariable Long cafeteriaId, @PathVariable Long menuId){
        cafeteriaCommandService.removeMenu(cafeteriaId, menuId);
        return ApiResponse.onSuccess(null);
    }

    /*
     * ==========================
     * 식단 API
     * ==========================
     */

    @Operation(summary = "식단을 등록하는 API", description = "식당id를 경로 변수, 날짜와 식때를 쿼리 파라미터로 받아 식단을 생성한다." +
            "만약 해당 조건에 식단이 이미 존재한다면 기존 식단을 삭제한다.")
    @PostMapping("/{cafeteriaId}/diets")
    public ApiResponse<DietResponseDTO.DietCreateDTO> registerDiet(
            @PathVariable(name = "cafeteriaId") Long cafeteriaId,
            @RequestBody DietRequestDTO.DietCreateDTO dto){
        Diet diet = cafeteriaCommandService.registerDiet(cafeteriaId, dto.getDate(), dto.getMeals(), dto.isDayOff(), dto.getMenuNameSet());
        return ApiResponse.onSuccess(DietConverter.toDietCreateResponseDTO(diet));
    }

    @Operation(summary = "식단에 등록된 메뉴를 제거하는 API", description = "식단 id와 메뉴 이름을 받아 식단에 등록된 메뉴를 제거")
    @DeleteMapping("/{cafeteriaId}/diets/menus/{menuName}")
    public ApiResponse deleteMenu(@PathVariable Long cafeteriaId,
                                  @PathVariable String menuName,
                                  @RequestBody DietRequestDTO.ChangeMenuDTO dto){
        cafeteriaCommandService.excludeMenuFromDiet(cafeteriaId, dto.getLocalDate(), dto.getMeals(), menuName);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "식단의 품절 유무를 스위칭하는 API", description = "토글 형식으로 품절 유무를 표시한다 응답으로 soldOut이 true이면 품절")
    @PatchMapping("/{cafeteriaId}/diets/sold-out")
    public ApiResponse<DietResponseDTO.SwitchSoldOutResponseDTO> checkSoldOut(@PathVariable Long cafeteriaId,
                                                                              @RequestBody DietRequestDTO.DietQueryDTO dto){
        boolean isSoldOut = cafeteriaCommandService.toggleSoldOut(cafeteriaId, dto.getLocalDate(), dto.getMeals());
        return ApiResponse.onSuccess(DietConverter.toSwitchSoldOutResponseDTO(isSoldOut));
    }

    @Operation(summary = "해당 식단 날짜의 휴무를 체크하는 API", description = "식당 ID와 날짜를 받아서 토글 형식으로 휴무를 표시한다. 응답으로 dayOff가 true이면 휴무" +
            "만약 해당 날짜/시간에 식단이 없다면 식단을 만들고 휴무로 등록한다. 휴무 해제 시에는 식단 자체를 삭제한다.")
    @PatchMapping("{cafeteriaId}/diets/dayOff")
    public ApiResponse<DietResponseDTO.SwitchDayOffResponseDTO> checkDayOff(
            @PathVariable Long cafeteriaId,
            @RequestParam LocalDate date,
            @RequestParam Meals meals){
        boolean isDayOff = cafeteriaCommandService.switchDayOff(cafeteriaId, date, meals);
        return ApiResponse.onSuccess(DietConverter.toSwitchDayOffResponseDTO(isDayOff));
    }
}
