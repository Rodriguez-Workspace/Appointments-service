package pe.edu.upc.center.agecare.appointments.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record NotificationId(Long notificationId) {

    public NotificationId() {
        this(null);
    }

    public NotificationId {
        if (notificationId == null || notificationId <= 0) {
            throw new IllegalArgumentException("NotificationId must be positive and not null");
        }
    }
}
