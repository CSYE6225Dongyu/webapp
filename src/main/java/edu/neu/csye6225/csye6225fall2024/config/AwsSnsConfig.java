package edu.neu.csye6225.csye6225fall2024.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsSnsConfig {

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
