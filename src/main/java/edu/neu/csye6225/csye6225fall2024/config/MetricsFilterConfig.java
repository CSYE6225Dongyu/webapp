package edu.neu.csye6225.csye6225fall2024.config;

import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsFilterConfig {

    @Bean
    public MeterFilter customMetricsFilter() {
        return MeterFilter.deny(id -> {
            // Keep only metrics with "api." prefix
            String name = id.getName();
            return !(name.startsWith("api.") || name.startsWith("s3.") || name.startsWith("db."));
        });
    }
}
