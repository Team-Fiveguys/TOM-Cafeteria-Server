package fiveguys.Tom.Cafeteria.Server.board.dto;

import fiveguys.Tom.Cafeteria.Server.board.entity.BoardType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardRequestDTO {
    private BoardType boardType;
    private Long userId;
    private String title;
    private String content;

}
