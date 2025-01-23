package fiveguys.Tom.Cafeteria.Server.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostPreviewListDTO {
    private List<PostPreviewDTO> postPreviewDTOList;
    private int currentPage;
    private int totalPages;
}
