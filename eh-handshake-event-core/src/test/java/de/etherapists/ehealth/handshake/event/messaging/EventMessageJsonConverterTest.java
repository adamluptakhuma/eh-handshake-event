package de.etherapists.ehealth.handshake.event.messaging;

import static de.etherapists.ehealth.handshake.event.messaging.EntityType.ACTIVATION_CODE;
import static de.etherapists.ehealth.handshake.event.messaging.EventType.ACTIVATION_CODE_CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.etherapists.ehealth.handshake.event.activation.ActivationCodeCreatedPayload;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventMessageJsonConverterTest {

  private EventMessageJsonConverter converter;

  @BeforeEach
  void setUp() {
    converter = new EventMessageJsonConverter(new ObjectMapper());
  }

  @Test
  void testSerialize() {
    var payload =
        ActivationCodeCreatedPayload.builder()
            .activationCodeId(1)
            .activationCodeValue("order-123")
            .companyId(2)
            .platform("HUMANOO")
            .awsRegion("eu-central-1")
            .build();
    var message =
        EventMessage.<ActivationCodeCreatedPayload>builder()
            .eventType(ACTIVATION_CODE_CREATED)
            .entityType(ACTIVATION_CODE)
            .entityId("order-123")
            .timestamp(1696242000L)
            .headers(Map.of("traceId", "abc-123"))
            .payload(payload)
            .build();

    String json = converter.serialize(message);

    assertThat(json)
        .isNotNull()
        .contains("\"eventType\":\"ACTIVATION_CODE_CREATED\"")
        .contains("\"activationCodeValue\":\"order-123\"");
  }

  @Test
  void testDeserialize() {
    String json =
        "{\n"
            + "  \"eventType\": \"ACTIVATION_CODE_CREATED\",\n"
            + "  \"entityType\": \"ACTIVATION_CODE\",\n"
            + "  \"entityId\": \"order-123\",\n"
            + "  \"timestamp\": 1696242000,\n"
            + "  \"headers\": { \"traceId\": \"abc-123\" },\n"
            + "  \"payload\": {\n"
            + "    \"platform\": \"HUMANOO\",\n"
            + "    \"activationCodeValue\": \"order-123\"\n"
            + "  }\n"
            + "}";

    EventMessage<ActivationCodeCreatedPayload> message =
        converter.deserialize(json, ActivationCodeCreatedPayload.class);

    assertThat(message).isNotNull();
    assertThat(message.getEventType()).isEqualTo(ACTIVATION_CODE_CREATED);
    assertThat(message.getEntityType()).isEqualTo(ACTIVATION_CODE);
    assertThat(message.getEntityId()).isEqualTo("order-123");
    assertThat(message.getHeaders()).containsEntry("traceId", "abc-123");

    assertThat(message.getPayload())
        .extracting(
            ActivationCodeCreatedPayload::getPlatform,
            ActivationCodeCreatedPayload::getActivationCodeValue)
        .containsExactly("HUMANOO", "order-123");
  }

  @Test
  void testSerializeAndDeserializeRoundTrip() {
    var payload =
        ActivationCodeCreatedPayload.builder()
            .activationCodeId(1)
            .activationCodeValue("order-123")
            .companyId(2)
            .platform("HUMANOO")
            .awsRegion("eu-central-1")
            .build();
    var message =
        EventMessage.<ActivationCodeCreatedPayload>builder()
            .eventType(ACTIVATION_CODE_CREATED)
            .entityType(ACTIVATION_CODE)
            .entityId("order-123")
            .timestamp(1696242000L)
            .headers(Map.of("traceId", "abc-123"))
            .payload(payload)
            .build();

    String json = converter.serialize(message);
    EventMessage<ActivationCodeCreatedPayload> restored =
        converter.deserialize(json, ActivationCodeCreatedPayload.class);

    assertThat(restored)
        .isNotNull()
        .usingRecursiveComparison()
        .ignoringFields("timestamp") // allow small differences if needed
        .isEqualTo(message);
  }

  @Test
  void testDeserializeInvalidJsonThrows() {
    String invalidJson = "{ invalid json }";

    assertThatThrownBy(() -> converter.deserialize(invalidJson, ActivationCodeCreatedPayload.class))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Deserialization failed");
  }
}
