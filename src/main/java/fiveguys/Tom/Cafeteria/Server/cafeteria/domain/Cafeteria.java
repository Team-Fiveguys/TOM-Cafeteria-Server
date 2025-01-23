package fiveguys.Tom.Cafeteria.Server.cafeteria.domain;


import fiveguys.Tom.Cafeteria.Server.apiPayload.code.status.ErrorStatus;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Congestion;
import fiveguys.Tom.Cafeteria.Server.common.BaseEntity;
import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.user.entity.UserCafeteria;
import fiveguys.Tom.Cafeteria.Server.exception.GeneralException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cafeteria extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location; //식당 위치?

    @Enumerated(EnumType.STRING)
    private Congestion congestion;

    private boolean runLunch;

    private boolean runBreakfast;

    private LocalTime breakfastStartTime; //조식 운영 시작 시간

    private LocalTime breakfastEndTime; //조식 운영 시작 시간

    private LocalTime lunchStartTime; //중식 운영 시작 시간

    private LocalTime lunchEndTime; //중식 운영 시작 시간

    @OneToMany(mappedBy = "cafeteria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserCafeteria> userCafeteriaList;

    @OneToMany(mappedBy = "cafeteria", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menuList;

    @OneToMany(mappedBy = "cafeteria", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diet> dietList;

    public void setCongestion(Congestion congestion) {
        this.congestion = congestion;
    }

    public Menu addMenu(String menuName){
       checkDuplicateName(menuName);
       Menu newMenu = Menu.builder()
               .name(menuName)
               .cafeteria(this)
               .build();
       this.menuList.add(newMenu);
       return newMenu;
    }

    public Diet registerDiet(LocalDate date, Meals meals, boolean isDayOff, List<Menu> menuList){
        // 이미 등록된 식단이 있다면 제거
        Optional<Diet> optionalDiet = this.dietList.stream()
                .filter(d -> d.getLocalDate().equals(date)
                        && d.getMeals().equals(meals))
                .findFirst();
        if(optionalDiet.isPresent()){
            removeDiet(optionalDiet.get());
        }
        // 식단 객체 생성
        Diet diet = Diet.builder()
                .cafeteria(this)
                .localDate(date)
                .meals(meals)
                .dayOff(isDayOff)
                .menuDietList(new ArrayList<>())
                .build();
        this.dietList.add(diet);
        // 매핑 객체 생성
        menuList.stream()
                .forEach(menu -> MenuDiet.createMenuDiet(menu, diet));

        return diet;
    }
    public Diet registerDietOnDayOff(LocalDate date, Meals meals){
        Diet diet = Diet.builder()
                .cafeteria(this)
                .localDate(date)
                .meals(meals)
                .dayOff(true)
                .menuDietList(new ArrayList<>())
                .build();
        this.dietList.add(diet);
        return diet;
    }
    public Diet getDietByDateAndMeals(LocalDate date, Meals meals){
        return this.dietList.stream()
                .filter(d -> d.getLocalDate().equals(date) && d.getMeals().equals(meals))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.DIET_NOT_FOUND));
    }
    public boolean isRegistered(LocalDate date, Meals meals){
        return this.dietList.stream()
                .anyMatch(diet -> diet.getLocalDate().equals(date) &&
                        diet.getMeals().equals(meals));
    }

    private void checkDuplicateName(String newName){
        boolean exist = this.menuList.stream()
                .anyMatch(menu -> menu.getName().equals(newName));
        if(exist){
            throw new GeneralException(ErrorStatus.MENU_DUPLICATE);
        }
    }
    public void removeMenu(Long menuId){
        Menu menu = menuList.stream()
                .filter(m -> m.getId().equals(menuId))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.MENU_NOT_FOUND));
        if(!menu.getMenuDietList().isEmpty()) {
            menu.clearMenuDiet();
        }
        menu.clearCafeteria();
        menuList.remove(menu);
    }

    public void removeDiet(Diet diet){
        if(!diet.getMenuDietList().isEmpty()) {
            diet.clearMenuDiet();
        }
        diet.clearCafeteria();
        if(!dietList.remove(diet)){
            throw new GeneralException(ErrorStatus.DIET_NOT_FOUND);
        }
    }

    public List<Menu> getMenuListByName(Set<String> menuNameSet){
        List<Menu> existingMenu = this.menuList.stream()
                .filter(menu -> menuNameSet.contains(menu.getName()))
                .collect(Collectors.toList());
        return existingMenu;
    }
}
