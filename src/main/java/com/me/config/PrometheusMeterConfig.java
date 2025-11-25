package com.me.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class PrometheusMeterConfig {

  @Value("${info.application.name}")
  private String appName;

  @Value("${info.application.version}")
  private String appVersion;

  @Value("${info.application.description}")
  private String appDescription;

  @Bean("appInfoGauge")
  public Gauge addAppInfo(MeterRegistry meterRegistry) {
    return Gauge.builder("app_info", () -> 1.0f)
        .tag("name", appName)
        .tag("description", appDescription)
        .tag("version", appVersion)
        .description("Application version")
        .register(meterRegistry);
  }
}
