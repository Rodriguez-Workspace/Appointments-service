package pe.edu.upc.center.agecare.appointments.interfaces.rest.transform;

import pe.edu.upc.center.agecare.appointments.domain.model.aggregates.Appointment;
import pe.edu.upc.center.agecare.appointments.interfaces.rest.resources.AppointmentResource;

public class AppointmentResourceFromEntityAssembler {

    public static AppointmentResource toResourceFromEntity(Appointment entity) {
        return new AppointmentResource(
                entity.getId(),
                entity.getResidentId().residentId(),
                entity.getDoctorId().doctorId(),
                entity.getDateTime().date(),
                entity.getDateTime().time(),
                entity.getStatus().status()
        );
    }
}
