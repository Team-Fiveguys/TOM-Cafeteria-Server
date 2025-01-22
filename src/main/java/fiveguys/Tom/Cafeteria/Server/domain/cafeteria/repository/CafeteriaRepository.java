package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.repository;

import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain.Cafeteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CafeteriaRepository extends JpaRepository<Cafeteria, Long> {

    @Query("SELECT c FROM Cafeteria c " +
            "LEFT JOIN FETCH c.menuList " +
            "WHERE c.id = :cafeteriaId")
    public Optional<Cafeteria> findByIdWithMenuList(@Param("cafeteriaId") Long cafeteriaId);

    @Query("SELECT c FROM Cafeteria c " +
            "LEFT JOIN FETCH c.dietList d " +
            "LEFT JOIN FETCH d.dietPhoto " +
            "WHERE c.id = :cafeteriaId")
    public Optional<Cafeteria> findByIdWithDietList(@Param("cafeteriaId") Long cafeteriaId);
    public boolean existsByName(String name);
    public Optional<Cafeteria> findByName(String name);
}
