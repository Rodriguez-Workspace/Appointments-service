package pe.edu.upc.center.agecare.appointments.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import pe.edu.upc.center.agecare.appointments.domain.model.commands.CreateAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.DateTime;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.DoctorId;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.NotificationId;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.Status;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.ResidentId;
import pe.edu.upc.center.agecare.shared.domain.aggregates.AuditableAbstractAggregateRoot;


@Entity
@Table(name = "appointments")
@Getter
@NoArgsConstructor

public class Appointment extends AuditableAbstractAggregateRoot<Appointment> {

    @Embedded
    @AttributeOverride(name = "residentId", column = @Column(name = "resident_id", nullable = false))
    private ResidentId residentId;

    @Embedded
    @AttributeOverride(name = "doctorId", column = @Column(name = "doctor_id", nullable = false))
    private DoctorId doctorId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "scheduled_date")),
        @AttributeOverride(name = "time", column = @Column(name = "scheduled_time"))
    })
    private DateTime dateTime;

    @Embedded
    @AttributeOverride(name = "status", column = @Column(name = "status", nullable = false))
    private Status status;

    //@Embedded
    //@AttributeOverride(name = "notificationId", column = @Column(name = "notification_id", nullable = false))
    //private NotificationId notificationId;

    // Constructor completo
    public Appointment(ResidentId residentId, DoctorId doctorId, DateTime dateTime, Status status) {
        this.residentId = residentId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.status = status;
        //this.notificationId = notificationId;
    }

    // Constructor desde CreateAppointmentCommand
    public Appointment(CreateAppointmentCommand command) {
        this(
                command.residentId(),
                command.doctorId(),
                command.dateTime(),
                command.status()
        );
    }

    // Método para actualizar información básica
    public Appointment UpdateAppointmentCommand(ResidentId residentId, DoctorId doctorId,DateTime dateTime,Status status) {
        this.residentId = residentId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.status = status;
        return this;
    }

}