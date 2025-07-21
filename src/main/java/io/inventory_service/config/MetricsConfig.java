package io.inventory_service.config;

import io.micrometer.core.instrument.*;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MetricsConfig {

    // --- Counters -----------------------------------------------------------
    @Bean
    @Getter
    public Counter inventoryAdjustCounter(MeterRegistry reg) {
        return Counter.builder("inventory_adjust_total")
                .description("Count of stock adjustments by reason")
                .tag("reason", "UNSET")        // dynamic tag overridden in code
                .register(reg);
    }

    @Bean
    @Getter
    public Counter redisLockContention(MeterRegistry reg) {
        return Counter.builder("inventory_lock_contention_total")
                .description("Failed Redis lock attempts")
                .register(reg);
    }

    // --- Histogram / Timer --------------------------------------------------
    @Bean
    @Getter
    public Timer reserveLatency(MeterRegistry reg) {
        return Timer.builder("inventory_reserve_latency_seconds")
                .description("Latency of StockService.reserve()")
                .publishPercentileHistogram()
                .maximumExpectedValue(Duration.ofSeconds(10))
                .register(reg);
    }
    meterRegistry.gauge("inventory_unit_state_total",
            Tags.of("tenant", tenant, "sku", sku, "location", loc, "state", state.name()),
    count);
}