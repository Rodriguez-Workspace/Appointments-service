package pe.edu.upc.center.agecare.appointments.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.agecare.appointments.domain.model.aggregates.Appointment;
import pe.edu.upc.center.agecare.appointments.domain.model.queries.GetAllAppointmentsQuery;
import pe.edu.upc.center.agecare.appointments.domain.model.queries.GetAppointmentByIdQuery;
import pe.edu.upc.center.agecare.appointments.domain.model.queries.GetAppointmentByResidentIdQuery;
import pe.edu.upc.center.agecare.appointments.domain.model.queries.GetAppointmentByDoctorIdQuery;
import pe.edu.upc.center.agecare.appointments.domain.services.AppointmentQueryService;
import pe.edu.upc.center.agecare.appointments.infrastructure.persistence.jpa.repositories.AppointmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {

  private final AppointmentRepository appointmentRepository;

  public AppointmentQueryServiceImpl(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = appointmentRepository;
  }

  @Override
  public List<Appointment> handle(GetAllAppointmentsQuery query) {
    return appointmentRepository.findAll();
  }

  @Override
  public Optional<Appointment> handle(GetAppointmentByIdQuery query) {
    return appointmentRepository.findById(query.appointmentId());
  }

  @Override
  public List<Appointment> handle(GetAppointmentByResidentIdQuery query) {
    return appointmentRepository.findByResidentId_ResidentId(query.residentId());
  }

  @Override
  public List<Appointment> handle(GetAppointmentByDoctorIdQuery query) {
    return appointmentRepository.findByDoctorId_DoctorId(query.doctorId());
  }

}
