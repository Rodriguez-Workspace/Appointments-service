package pe.edu.upc.center.agecare.appointments.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateAppointmentResource(
    Long residentId,
    Long doctorId,
    LocalDate date,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    LocalTime time,
    String status
){}