package com.tokarev.prometheus.configuration;

import io.micrometer.core.instrument.ImmutableTag;
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
                .meterFilter(MeterFilter.denyUnless(this::isMetricToInclude)).commonTags("a_tag", "some_common_tags"); // Add tags for all or specific metrics
    }

    private boolean isMetricToInclude(Meter.Id id) {
        return (id.getName().startsWith("app") || id.getName().startsWith("http")) && id.getTags().contains(new ImmutableTag("outcome", "SUCCESS"));  // Filter by tags
    }
}