package fiveguys.Tom.Cafeteria.Server.user.repository;


import fiveguys.Tom.Cafeteria.Server.user.entity.Role;
import fiveguys.Tom.Cafeteria.Server.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findBySocialId(String socialId);
    public Optional<User> findByEmail(String email);

    public boolean existsBySocialId(String socialId);

    public boolean existsByEmail(String email);

    public Page<User> findAll(Pageable pageable);

    public List<User> findAllByRole(Role role);

    List<User> findByNotificationSetIsNotNull();

}
