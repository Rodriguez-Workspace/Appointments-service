package pe.edu.upc.center.agecare.appointments.interfaces.rest.transform;

import pe.edu.upc.center.agecare.appointments.domain.model.commands.CreateAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.DateTime;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.DoctorId;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.ResidentId;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.Status;
import pe.edu.upc.center.agecare.appointments.interfaces.rest.resources.CreateAppointmentResource;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateAppointmentCommandFromResourceAssembler {

    public static CreateAppointmentCommand toCommandFromResource(CreateAppointmentResource resource) {
        ResidentId residentId = new ResidentId(resource.residentId());
        DoctorId doctorId = new DoctorId(resource.doctorId());
        DateTime dateTime = new DateTime(LocalDate.of(resource.date().getYear(), resource.date().getMonthValue(), resource.date().getDayOfMonth()),
                LocalTime.of(resource.time().getHour(), resource.time().getMinute()));
        Status status = new Status(resource.status());

        return new CreateAppointmentCommand(
                residentId,
                doctorId,
                dateTime,
                status
        );
    }
}