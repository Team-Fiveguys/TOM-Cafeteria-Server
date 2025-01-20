package fiveguys.Tom.Cafeteria.Server.domain.menu.entity;

import fiveguys.Tom.Cafeteria.Server.domain.cafeteria.entity.Cafeteria;
import fiveguys.Tom.Cafeteria.Server.domain.common.BaseEntity;
import fiveguys.Tom.Cafeteria.Server.domain.diet.entity.MenuDiet;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isMain;

    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafeteria_id")
    private Cafeteria cafeteria;

    @JoinColumn(name = "menu_category_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MenuCategory menuCategory;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuDiet> menuDietList;

    public void clearMenuDiet(){
        for (MenuDiet menuDiet: this.menuDietList) {
            menuDiet.clearMenu();
        }
        menuDietList.clear();
    }
    public void clearCafeteria(){
        this.cafeteria = null;
    }
}
