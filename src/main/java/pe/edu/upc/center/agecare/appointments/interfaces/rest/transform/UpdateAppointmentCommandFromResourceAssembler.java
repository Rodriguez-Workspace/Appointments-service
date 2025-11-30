package pe.edu.upc.center.agecare.appointments.interfaces.rest.transform;

import pe.edu.upc.center.agecare.appointments.domain.model.commands.UpdateAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.DateTime;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.DoctorId;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.ResidentId;
import pe.edu.upc.center.agecare.appointments.domain.model.valueobjects.Status;
import pe.edu.upc.center.agecare.appointments.interfaces.rest.resources.AppointmentResource;

public class UpdateAppointmentCommandFromResourceAssembler {

    public static UpdateAppointmentCommand toCommandFromResource(AppointmentResource resource) {
        ResidentId residentId = new ResidentId(resource.residentId());
        DoctorId doctorId = new DoctorId(resource.doctorId());
        DateTime dateTime = new DateTime(resource.date(), resource.time());
        Status status = new Status(resource.status());

        return new UpdateAppointmentCommand(
                resource.id(),
                residentId,
                doctorId,
                dateTime,
                status
        );
    }
}
