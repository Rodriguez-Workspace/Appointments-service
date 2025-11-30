package pe.edu.upc.center.agecare.appointments.domain.model.commands;

import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.DateTime;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.DoctorId;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.ResidentId;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.Status;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.NotificationId;

public record UpdateAppointmentCommand(
    Long appointmentId,
    ResidentId residentId,
    DoctorId doctorId,
    DateTime dateTime,
    Status status
) {}
