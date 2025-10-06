package de.etherapists.ehealth.handshake.event.config;

import de.etherapists.ehealth.handshake.event.messaging.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfiguration {

  @Bean
  public IdGenerator uuidGenerator() {
    return new IdGenerator();
  }
}
