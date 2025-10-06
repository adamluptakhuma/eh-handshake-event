package de.etherapists.ehealth.handshake.event.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventMessageJsonConverter {

  private final ObjectMapper mapper;

  public <T> String serialize(EventMessage<T> message) {
    try {
      return mapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Serialization failed", e);
    }
  }

  public <T> EventMessage<T> deserialize(String json, Class<T> payloadClass) {
    try {
      return mapper.readValue(
          json, mapper.getTypeFactory().constructParametricType(EventMessage.class, payloadClass));
    } catch (Exception e) {
      throw new RuntimeException("Deserialization failed", e);
    }
  }
}
