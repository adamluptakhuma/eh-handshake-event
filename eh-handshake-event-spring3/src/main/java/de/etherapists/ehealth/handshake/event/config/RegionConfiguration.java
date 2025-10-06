package de.etherapists.ehealth.handshake.event.config;

import de.etherapists.ehealth.handshake.event.region.RegionResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegionConfiguration {
  @Value("${aws.region:#{null}}")
  private String awsRegion;

  @Bean
  public RegionResolver regionResolver() {
    return new RegionResolver(awsRegion);
  }
}
