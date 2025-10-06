package de.etherapists.ehealth.handshake.event.messaging;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventMessage<T> {
  private EventType eventType;
  private EntityType entityType;
  private String entityId;
  private String messageId;
  private long timestamp;
  private Map<String, String> headers;
  private T payload;

  // --- FIFO SQS specific ---
  private String messageGroupId;
  private String deduplicationId;
}
