package pe.edu.upc.center.agecare.appointments.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.center.agecare.appointments.domain.model.aggregates.Appointment;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    List<Appointment> findByResidentId_ResidentId(Long residentId);

    List<Appointment> findByDoctorId_DoctorId(Long doctorId);

    boolean existsByDateTime_DateAndDateTime_TimeAndDoctorId_DoctorId(
            java.time.LocalDate date, java.time.LocalTime time, Long doctorId
    );


}

