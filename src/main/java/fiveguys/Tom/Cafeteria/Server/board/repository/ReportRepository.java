package fiveguys.Tom.Cafeteria.Server.board.repository;

import fiveguys.Tom.Cafeteria.Server.board.entity.Post;
import fiveguys.Tom.Cafeteria.Server.board.entity.Report;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    void deleteAllByPost(Post post);
    void deleteByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
}
