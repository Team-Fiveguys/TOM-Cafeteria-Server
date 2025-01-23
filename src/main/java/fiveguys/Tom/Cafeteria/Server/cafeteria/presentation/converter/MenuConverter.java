package fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.converter;

import fiveguys.Tom.Cafeteria.Server.cafeteria.presentation.dto.response.MenuResponseDTO;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Menu;

import java.util.List;

public class MenuConverter {
    public static MenuResponseDTO.MenuResponseListDTO toMenuResponseListDTO(List<MenuResponseDTO.MenuQueryDTO> menuQueryDTOList){
        return MenuResponseDTO.MenuResponseListDTO.builder()
                .menuQueryDTOList(menuQueryDTOList)
                .build();
    }
    public static MenuResponseDTO.MenuRegisterDTO toRegisterResponseDTO(Menu menu){
        return MenuResponseDTO.MenuRegisterDTO.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .build();
    }
    public static MenuResponseDTO.MenuQueryDTO toMenuQueryDTO(Menu menu){
        return MenuResponseDTO.MenuQueryDTO.builder()
                .menuId(menu.getId())
                .name(menu.getName())
                .build();
    }
}
