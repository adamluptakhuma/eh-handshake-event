package de.etherapists.ehealth.handshake.event.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {

  @Bean
  public Clock utcClock() {
    return Clock.systemUTC();
  }
}
