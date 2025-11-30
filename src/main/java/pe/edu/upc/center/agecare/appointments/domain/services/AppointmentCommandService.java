package pe.edu.upc.center.agecare.appointments.domain.services;

import pe.edu.upc.center.agecare.appointments.domain.model.aggregates.Appointment;
import pe.edu.upc.center.agecare.appointments.domain.model.commands.CreateAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.model.commands.DeleteAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.model.commands.UpdateAppointmentCommand;

import java.util.Optional;

public interface AppointmentCommandService {
  Long handle(CreateAppointmentCommand command);
  Optional<Appointment> handle(UpdateAppointmentCommand command);
  void handle(DeleteAppointmentCommand command);
}