package pe.edu.upc.center.agecare.appointments.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.center.agecare.appointments.domain.model.commands.DeleteAppointmentCommand;
import pe.edu.upc.center.agecare.appointments.domain.model.queries.GetAllAppointmentsQuery;
import pe.edu.upc.center.agecare.appointments.domain.model.queries.GetAppointmentByIdQuery;
import pe.edu.upc.center.agecare.appointments.domain.model.queries.GetAppointmentByResidentIdQuery;
import pe.edu.upc.center.agecare.appointments.domain.model.queries.GetAppointmentByDoctorIdQuery;
import pe.edu.upc.center.agecare.appointments.domain.services.AppointmentCommandService;
import pe.edu.upc.center.agecare.appointments.domain.services.AppointmentQueryService;
import pe.edu.upc.center.agecare.appointments.interfaces.rest.resources.AppointmentResource;
import pe.edu.upc.center.agecare.appointments.interfaces.rest.resources.CreateAppointmentResource;
import pe.edu.upc.center.agecare.appointments.interfaces.rest.transform.AppointmentResourceFromEntityAssembler;
import pe.edu.upc.center.agecare.appointments.interfaces.rest.transform.CreateAppointmentCommandFromResourceAssembler;
import pe.edu.upc.center.agecare.appointments.interfaces.rest.transform.UpdateAppointmentCommandFromResourceAssembler;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Appointments", description = "Appointment Management Endpoints")
public class AppointmentsController {

    private final AppointmentQueryService appointmentQueryService;
    private final AppointmentCommandService appointmentCommandService;

    public AppointmentsController(AppointmentQueryService appointmentQueryService, AppointmentCommandService appointmentCommandService) {
        this.appointmentQueryService = appointmentQueryService;
        this.appointmentCommandService = appointmentCommandService;
    }

    @Operation(
            summary = "Create a new appointment",
            description = "Create a new medical appointment using the provided request body."
    )
    @PostMapping
    public ResponseEntity<AppointmentResource> createAppointment(@RequestBody CreateAppointmentResource resource) {
        var createCommand = CreateAppointmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var appointmentId = appointmentCommandService.handle(createCommand);

        if (appointmentId == null || appointmentId.equals(0L)) {
            return ResponseEntity.badRequest().build();
        }

        var getAppointmentByIdQuery = new GetAppointmentByIdQuery(appointmentId);
        var optionalAppointment = appointmentQueryService.handle(getAppointmentByIdQuery);

        if (optionalAppointment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var appointmentResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(optionalAppointment.get());
        return new ResponseEntity<>(appointmentResource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all appointments",
            description = "Retrieve a list of all medical appointments registered in the system."
    )
    @GetMapping
    public ResponseEntity<List<AppointmentResource>> getAllAppointments() {
        var query = new GetAllAppointmentsQuery();
        var appointments = appointmentQueryService.handle(query);
        var resources = appointments.stream()
                .map(AppointmentResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    // Get by appointmentId removed as requested

    @Operation(
            summary = "Update an appointment",
            description = "Update the details of a specific appointment identified by its ID."
    )
    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResource> updateAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentResource resource) {
        var updateCommand = UpdateAppointmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var optionalAppointment = appointmentCommandService.handle(updateCommand);

        if (optionalAppointment.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var updatedResource = AppointmentResourceFromEntityAssembler.toResourceFromEntity(optionalAppointment.get());
        return ResponseEntity.ok(updatedResource);
    }

    @Operation(
            summary = "Delete an appointment",
            description = "Delete a medical appointment from the system using its ID."
    )
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long appointmentId) {
        var deleteCommand = new DeleteAppointmentCommand(appointmentId);
        appointmentCommandService.handle(deleteCommand);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get appointments by resident ID",
            description = "Retrieve all medical appointments associated with a specific resident."
    )
    @GetMapping("/searchByResidentId")
    public ResponseEntity<List<AppointmentResource>> getAppointmentByResidentId(@RequestParam Long residentId) {
        var query = new GetAppointmentByResidentIdQuery(residentId);
        var appointments = appointmentQueryService.handle(query);

        if (appointments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = appointments.stream()
                .map(AppointmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @Operation(
            summary = "Get appointments by doctor ID",
            description = "Retrieve all medical appointments associated with a specific doctor."
    )
    @GetMapping("/searchByDoctorId")
    public ResponseEntity<List<AppointmentResource>> getAppointmentsByDoctorId(@RequestParam Long doctorId) {
        var query = new GetAppointmentByDoctorIdQuery(doctorId);
        var appointments = appointmentQueryService.handle(query);

        if (appointments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = appointments.stream()
                .map(AppointmentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
