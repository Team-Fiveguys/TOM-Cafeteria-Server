package fiveguys.Tom.Cafeteria.Server.domain.cafeteria.domain;


import fiveguys.Tom.Cafeteria.Server.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDiet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @JoinColumn(name = "diet_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Diet diet;

    public static MenuDiet createMenuDiet(Menu menu, Diet diet){
        MenuDiet menuDiet = new MenuDiet();
        menuDiet.setDiet(diet);
        menuDiet.setMenu(menu);
        diet.addMenuDiet(menuDiet);
        menu.addMenuDiet(menuDiet);
        return menuDiet;
    }

    public void clearMenu(){
        this.menu = null;
    }
    public void clearDiet(){
        this.diet = null;
    }
}
