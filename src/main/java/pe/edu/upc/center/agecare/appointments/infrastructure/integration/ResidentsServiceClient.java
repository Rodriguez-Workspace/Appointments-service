package pe.edu.upc.center.agecare.appointments.infrastructure.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ResidentsServiceClient {

    private final RestClient restClient;

    @Value("${services.residents.url}")
    private String residentsServiceUrl;

    public ResidentsServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Checks whether a resident with the given id exists in Residents service.
     * Returns true if the GET call succeeds (2xx). Treats failures as non-existent.
     */
    public boolean residentExists(Long residentId) {
        try {
            String url = residentsServiceUrl + "/api/v1/residents/" + residentId;
            restClient.get().uri(url).retrieve().toBodilessEntity();
            return true;
        } catch (Exception e) {
            System.err.println("ResidentsServiceClient.residentExists error: " + e.getMessage());
            return false;
        }
    }
}
