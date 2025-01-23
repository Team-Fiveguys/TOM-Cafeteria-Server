package fiveguys.Tom.Cafeteria.Server.cafeteria.domain;

import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.DietPhoto;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.common.BaseEntity;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"cafeteria_id", "local_date", "meals"}))
public class Diet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Meals meals;

    private boolean soldOut;

    private boolean dayOff;

    private LocalDate localDate;

    @JoinColumn(name = "cafeteria_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafeteria cafeteria;

    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MenuDiet> menuDietList = new ArrayList<>();

    @OneToOne(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private DietPhoto dietPhoto;

    public void setDietPhoto(DietPhoto dietPhoto){
        this.dietPhoto = dietPhoto;
    }

    public void addMenuDiet(MenuDiet menuDiet){
        this.menuDietList.add(menuDiet);
    }
    public void clearMenuDiet(){
        for (MenuDiet menuDiet: this.menuDietList) {
            menuDiet.clearMenu();
        }
        this.menuDietList.clear();
    }
    public void clearCafeteria(){
        this.cafeteria = null;
    }

    public void excludeMenu(String menuName){
        MenuDiet menuDiet = this.menuDietList.stream()
                .filter(md -> md.getMenu().getName().equals(menuName))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.MENU_IS_NOT_FOUND_FROM_THIS_DIET));
        menuDiet.clearDiet();
        this.menuDietList.remove(menuDiet);
        // 삭제 후 식단의 메뉴가 존재하지 않으면 식단도 삭제한다.
        if(menuDietList.isEmpty()){
            this.cafeteria.removeDiet(this);
        }
    }

    public void switchSoldOut(){
        this.soldOut = !this.soldOut;
    }

    public void switchDayOff(){
        this.dayOff = !this.dayOff;
    }

    public String getMenuListString(){
        StringBuffer menuString = new StringBuffer();
        getMenuDietList().stream()
                .map(MenuDiet::getMenu)
                .forEach(menu -> menuString.append(menu.getName() + "\n" ) );
        return menuString.toString();
    }

    public static boolean isDayOffOrNull(Diet diet){
        if(diet == null || diet.isDayOff()){
            return true;
        }

        return false;
    }
}
