package pe.edu.upc.center.agecare.appointments.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.agecare.appointments.domain.model.aggregates.Appointment;
import pe.edu.upc.center.agecare.appointments.domain.model.commands.CreateAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.model.commands.DeleteAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.model.commands.UpdateAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.services.AppointmentCommandService;
import pe.edu.upc.center.agecare.appointments.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import pe.edu.upc.center.agecare.appointments.infrastructure.integration.NotificationServiceClient;
import pe.edu.upc.center.agecare.appointments.infrastructure.integration.UsersServiceClient;
import pe.edu.upc.center.agecare.appointments.infrastructure.integration.ResidentsServiceClient;

import java.util.Optional;
import java.time.LocalTime;


@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

  private final AppointmentRepository appointmentRepository;
  private final NotificationServiceClient notificationServiceClient;
  private final UsersServiceClient usersServiceClient;
  private final ResidentsServiceClient residentsServiceClient;

  public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository,
                                       NotificationServiceClient notificationServiceClient,
                                       UsersServiceClient usersServiceClient,
                                       ResidentsServiceClient residentsServiceClient) {
    this.appointmentRepository = appointmentRepository;
    this.notificationServiceClient = notificationServiceClient;
    this.usersServiceClient = usersServiceClient;
    this.residentsServiceClient = residentsServiceClient;
    }

  @Override
  public Long handle(CreateAppointmentCommand command) {
    if (command.dateTime().time().isBefore(LocalTime.of(8, 0)) ||
            command.dateTime().time().isAfter(LocalTime.of(19, 0))) {
      throw new IllegalArgumentException("Appointments can only be registered between 08:00 and 20:00.");
    }

    // Verify referenced resident exists in Residents service
    if (!residentsServiceClient.residentExists(command.residentId().residentId())) {
      throw new IllegalArgumentException("Referenced resident with id " + command.residentId().residentId() + " does not exist");
    }

    // Verify referenced doctor exists in Users service
    if (!usersServiceClient.doctorExists(command.doctorId().doctorId())) {
      throw new IllegalArgumentException("Referenced doctor with id " + command.doctorId().doctorId() + " does not exist");
    }
    if (this.appointmentRepository.existsByDateTime_DateAndDateTime_TimeAndDoctorId_DoctorId(command.dateTime().date(), command.dateTime().time(), command.doctorId().doctorId())) {
      throw new IllegalArgumentException("Appointment with the doctor "+ command.doctorId().doctorId() +" with date " + command.dateTime().date() + " and time " + command.dateTime().time() + " already exists");
    }

    Appointment appointment = new Appointment(command);
    try {
      this.appointmentRepository.save(appointment);
      
      // 🔗 INTEGRACIÓN: Enviar notificación al residente sobre la cita creada
      String message = String.format(
          "Su cita médica ha sido programada para el %s a las %s. Estado: %s",
          command.dateTime().date(),
          command.dateTime().time(),
          command.status()
      );
      notificationServiceClient.sendNotification(command.residentId().residentId(), message);
      
    } catch (Exception e) {
      throw new IllegalArgumentException("Error while saving appointment: " + e.getMessage());
    }
    return appointment.getId();
  }


  @Override
  public Optional<Appointment> handle(UpdateAppointmentCommand command) {
    var appointmentId = command.appointmentId();

    if (!this.appointmentRepository.existsById(appointmentId)) {
      throw new IllegalArgumentException("Appointment with id " + appointmentId + " does not exist");
    }

    if (this.appointmentRepository.existsByDateTime_DateAndDateTime_TimeAndDoctorId_DoctorId(command.dateTime().date(), command.dateTime().time(), command.doctorId().doctorId())) {
      throw new IllegalArgumentException("Appointment with the doctor "+ command.doctorId().doctorId() +" with date " + command.dateTime().date() + " and time " + command.dateTime().time() + " already exists");
    }

    var appointmentToUpdate = this.appointmentRepository.findById(appointmentId).get();
    appointmentToUpdate.UpdateAppointmentCommand(
        command.residentId(), 
        command.doctorId(), 
        command.dateTime(), 
        command.status()
        );

    try {
      var updatedAppointment = this.appointmentRepository.save(appointmentToUpdate);
      
      // 🔗 INTEGRACIÓN: Enviar notificación al residente sobre la cita actualizada
      String message = String.format(
          "Su cita médica ha sido actualizada. Nueva fecha: %s a las %s. Estado: %s",
          command.dateTime().date(),
          command.dateTime().time(),
          command.status()
      );
      notificationServiceClient.sendNotification(command.residentId().residentId(), message);
      
      return Optional.of(updatedAppointment);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error while updating appointment: " + e.getMessage());
    }
  }

  @Override
  public void handle(DeleteAppointmentCommand command) {
    if (!this.appointmentRepository.existsById(command.appointmentId())) {
      throw new IllegalArgumentException("Appointment with id " + command.appointmentId() + " does not exist");
    }

    try {
      // Obtener la cita antes de eliminarla para enviar notificación
      var appointment = this.appointmentRepository.findById(command.appointmentId()).get();
      Long residentId = appointment.getResidentId().residentId();
      
      this.appointmentRepository.deleteById(command.appointmentId());
      
      // 🔗 INTEGRACIÓN: Enviar notificación al residente sobre la cita cancelada
      String message = String.format(
          "Su cita médica del %s a las %s ha sido cancelada.",
          appointment.getDateTime().date(),
          appointment.getDateTime().time()
      );
      notificationServiceClient.sendNotification(residentId, message);
      
    } catch (Exception e) {
      throw new IllegalArgumentException("Error while deleting appointment: " + e.getMessage());
    }
  }
}