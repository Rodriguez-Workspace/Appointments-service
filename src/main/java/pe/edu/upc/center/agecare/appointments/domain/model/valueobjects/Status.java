package pe.edu.upc.center.agecare.appointments.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record Status(String status) {

    public Status() {
        this(null);
    }

    public Status {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or blank");
        }
    }
}