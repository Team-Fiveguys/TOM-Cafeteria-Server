package fiveguys.Tom.Cafeteria.Server.cafeteria.event;

import fiveguys.Tom.Cafeteria.Server.cafeteria.entity.Meals;
import fiveguys.Tom.Cafeteria.Server.notification.entity.AppNotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class DietPhotoUploadEvent extends ApplicationEvent {
    private final String CafeteriaName;
    private final LocalDate date;
    private final Meals meals;
    private final AppNotificationType type;


    public DietPhotoUploadEvent(Object source, String cafeteriaName, LocalDate date, Meals meals, AppNotificationType type) {
        super(source);
        CafeteriaName = cafeteriaName;
        this.date = date;
        this.meals = meals;
        this.type = type;
    }
}
