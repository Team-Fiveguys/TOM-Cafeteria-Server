package fiveguys.Tom.Cafeteria.Server.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostPreviewDTO {
    private Long id;
    private String title;
    private String content;
    private String publisherName;
    private int likeCount;
    private LocalDateTime uploadTime;
    private boolean toggleLike;
    //DTO 수정하고 Controller, Service 수정해야함

    public void setToggleLike(boolean toggleLike) {
        this.toggleLike = toggleLike;
    }
}
