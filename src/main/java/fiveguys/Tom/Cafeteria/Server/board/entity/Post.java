package fiveguys.Tom.Cafeteria.Server.board.entity;


import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.common.BaseEntity;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafeteria_id")
    private Cafeteria cafeteria;

    @Lob
    private String content;

    private int likeCount;

    private int reportCount;

    private boolean isAdminPick;

    public void updatePost(String title, String content){
        this.title = title;
        this.content = content;
    }

}
