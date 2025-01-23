package fiveguys.Tom.Cafeteria.Server.board.dto;

import fiveguys.Tom.Cafeteria.Server.board.entity.BoardType;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private BoardType boardType;
    private Long userId;
    private String title;
    private String content;
    private int likeCount;
    private boolean toggleLike;
    private boolean isAdminPick;

}
