package fiveguys.Tom.Cafeteria.Server.domain.board.repository;

import fiveguys.Tom.Cafeteria.Server.domain.board.entity.Post;
import fiveguys.Tom.Cafeteria.Server.domain.board.entity.PostLike;
import fiveguys.Tom.Cafeteria.Server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    void deleteAllByPost(Post post);
    void deleteByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
}