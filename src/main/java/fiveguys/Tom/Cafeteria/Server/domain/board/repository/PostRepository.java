package fiveguys.Tom.Cafeteria.Server.domain.board.repository;

import fiveguys.Tom.Cafeteria.Server.domain.board.entity.Post;
import fiveguys.Tom.Cafeteria.Server.domain.board.entity.BoardType;
import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Cafeteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    public Page<Post> findAllByCafeteriaAndBoardType(Pageable pageable, Cafeteria cafeteria, BoardType boardType);

    public List<Post> findAllByCafeteriaOrderByReportCount(Cafeteria cafeteria);
}
