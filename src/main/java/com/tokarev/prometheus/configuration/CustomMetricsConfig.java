package com.tokarev.prometheus.configuration;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomMetricsConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsToExpose() {
        return registry -> registry
                .config()
                .meterFilter(MeterFilter.denyUnless(this::isMetricToInclude));
    }

    private boolean isMetricToInclude(Meter.Id id) {
        return id.getName().startsWith("app") || id.getName().startsWith("http");
    }
}