package pe.edu.upc.center.agecare.appointments.infrastructure.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UsersServiceClient {

    private final RestClient restClient;

    @Value("${services.users.url}")
    private String usersServiceUrl;

    public UsersServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Checks whether a doctor with the given id exists in Users service.
     * Returns true if users-service responds 200 for GET /api/v1/doctors/{id}.
     */
    public boolean doctorExists(Long doctorId) {
        try {
            String url = usersServiceUrl + "/api/v1/doctors/" + doctorId;
            // Best-effort: if request completes without throwing, we assume doctor exists (200).
            restClient.get().uri(url).retrieve().toBodilessEntity();
            return true;
        } catch (Exception e) {
            // If the call fails (404 or network), treat as non-existing
            System.err.println("UsersServiceClient.doctorExists error: " + e.getMessage());
            return false;
        }
    }
}
