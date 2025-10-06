package de.etherapists.ehealth.handshake.event.region;

import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegionResolver {
  private final String awsRegion;

  public Optional<String> resolve(String country) {
    try {
      return CountryRegionMapper.getRegionForCountry(CountryEnum.valueOf(country));
    } catch (IllegalArgumentException | NullPointerException e) {
      return this.getAwsRegion();
    }
  }

  public Optional<String> getAwsRegion() {
    if (awsRegion != null && !awsRegion.isEmpty()) {
      return Optional.of(awsRegion);
    }

    String sysRegion = System.getProperty("AWS_REGION");
    if (sysRegion != null && !sysRegion.isEmpty()) {
      return Optional.of(sysRegion);
    }
    String envRegion = System.getenv("AWS_REGION");
    if (envRegion != null && !envRegion.isEmpty()) {
      return Optional.of(envRegion);
    }
    return Optional.empty();
  }
}
