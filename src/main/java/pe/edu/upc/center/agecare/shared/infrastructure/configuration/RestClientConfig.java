package pe.edu.upc.center.agecare.shared.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * RestClient Configuration
 * <p>
 *     Provides RestClient bean for making HTTP requests to external microservices.
 *     RestClient is the modern replacement for RestTemplate in Spring Boot 3+
 * </p>
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .build();
    }
}
