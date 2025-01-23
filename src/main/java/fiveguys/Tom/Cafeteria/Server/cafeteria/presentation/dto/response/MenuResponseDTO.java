package fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class MenuResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MenuRegisterDTO{
        private Long menuId;
        private String menuName;
    }
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MenuQueryDTO{
        private Long menuId;
        private String name;
    }
    @Builder
    @AllArgsConstructor
    @Getter
    public static class MenuResponseListDTO {
        private List<MenuQueryDTO> menuQueryDTOList;
    }
}
