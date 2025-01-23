package fiveguys.Tom.Cafeteria.Server.diet.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.Diet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.QDiet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.QMenu;
import fiveguys.Tom.Cafeteria.Server.cafeteria.domain.QMenuDiet;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.QDietPhoto;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

@Repository
@Slf4j
public class DietRepositoryImpl implements DietRepositoryCustom{
    private JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Diet> findDietsByThreeWeeks(Long cafeteriaId) {
        QDiet qDiet = QDiet.diet;
        QMenu qMenu = QMenu.menu;
        QDietPhoto qDietPhoto = QDietPhoto.dietPhoto;
        QMenuDiet qMenuDiet = QMenuDiet.menuDiet;

        // 현재 주차 계산 (주차 옵션 5)
        NumberTemplate<Integer> currentWeek = numberTemplate(Integer.class, "WEEK(CURDATE())");

        // 전주, 이번주, 다음주 주차 계산
        NumberTemplate<Integer> entityWeek = numberTemplate(Integer.class, "WEEK({0})", qDiet.localDate);

        // 조건 설정: 전주, 이번주, 다음주
        BooleanExpression isWithinThreeWeeks = entityWeek.between(currentWeek.subtract(1), currentWeek.add(1));

        List<Diet> dietList = queryFactory.selectFrom(qDiet)
                .leftJoin(qDiet.dietPhoto, qDietPhoto)
                .fetchJoin()
                .leftJoin(qDiet.menuDietList, qMenuDiet)
                .fetchJoin()
                .leftJoin(qMenuDiet.menu, qMenu)
                .fetchJoin()
                .where(isWithinThreeWeeks
                        .and(qDiet.cafeteria.id.eq(cafeteriaId))
                )
                .fetch();
        Collections.sort(dietList, Comparator.comparing(Diet::getLocalDate));
        return dietList;
    }
}
