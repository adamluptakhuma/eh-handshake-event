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
  void shouldReturnEnvRegionWhenFieldIsNull() {
    RegionResolver resolver = new RegionResolver(null);
    String oldEnv = System.getenv("AWS_REGION");
    try {
      setEnv("AWS_REGION", "envRegion");
      Optional<String> result = resolver.getAwsRegion();
      assertTrue(result.isPresent());
      assertEquals("envRegion", result.get());
    } finally {
      setEnv("AWS_REGION", oldEnv);
    }
  }

  @Test
  void shouldReturnEmptyWhenNoRegionSet() {
    RegionResolver resolver = new RegionResolver(null);
    String oldEnv = System.getenv("AWS_REGION");
    try {
      setEnv("AWS_REGION", "");
      Optional<String> result = resolver.getAwsRegion();
      assertTrue(result.isEmpty());
    } finally {
      setEnv("AWS_REGION", oldEnv);
    }
  }

  // Helper to set environment variable (works only in some JVMs, for test only)
  private static void setEnv(String key, String value) {
    try {
      java.util.Map<String, String> env = System.getenv();
      java.lang.reflect.Field field = env.getClass().getDeclaredField("m");
      field.setAccessible(true);
      @SuppressWarnings("unchecked")
      java.util.Map<String, String> writableEnv = (java.util.Map<String, String>) field.get(env);
      if (value == null) {
        writableEnv.remove(key);
      } else {
        writableEnv.put(key, value);
      }
    } catch (Exception ignored) {
    }
  }
}
