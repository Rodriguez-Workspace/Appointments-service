package pe.edu.upc.center.agecare.appointments.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record DoctorId(Long doctorId) {

    public DoctorId() {
        this(null);
    }

    public DoctorId {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("DoctorId must be positive and not null");
        }
    }
}