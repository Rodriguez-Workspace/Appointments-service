package pe.edu.upc.center.agecare.appointments.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
public record DateTime(LocalDate date, LocalTime time) {

    public DateTime() {
        this(null, null);
    }

    public DateTime {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null or blank");
        }
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null or blank");
        }
    }
}