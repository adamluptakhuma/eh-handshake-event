package de.etherapists.ehealth.handshake.event.region;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegionResolverTest {

  @Test
  void shouldReturnMappedRegionWhenCountryIsMapped() {
    RegionResolver resolver = new RegionResolver("defaultRegion");
    Optional<String> result = resolver.resolve("DE");
    assertTrue(result.isPresent());
    assertEquals("defaultRegion", result.get());
  }

  @Test
  void shouldReturnAwsRegionWhenCountryNotMapped() {
    RegionResolver resolver = new RegionResolver("fallbackRegion");
    Optional<String> result = resolver.resolve("FR");
    assertTrue(result.isPresent());
    assertEquals("fallbackRegion", result.get());
  }

  @Test
  void shouldReturnEnvRegionWhenAwsRegionFieldIsNull() {
    // given
    System.setProperty("AWS_REGION", "envRegion");

    // when
    RegionResolver resolver = new RegionResolver(null);
    Optional<String> result = resolver.getAwsRegion();

    // then
    assertTrue(result.isPresent());
    assertEquals("envRegion", result.get());

    // cleanup
    System.clearProperty("AWS_REGION");
  }

  @Test
  void shouldReturnEmptyWhenNoRegionConfigured() {
    // given
    System.clearProperty("AWS_REGION");

    // when
    RegionResolver resolver = new RegionResolver(null);
    Optional<String> result = resolver.getAwsRegion();

    // then
    assertTrue(result.isEmpty());
  }
}
