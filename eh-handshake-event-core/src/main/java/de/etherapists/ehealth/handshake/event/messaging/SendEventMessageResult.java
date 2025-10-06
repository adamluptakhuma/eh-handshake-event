package de.etherapists.ehealth.handshake.event.messaging;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class SendEventMessageResult {
  boolean success;
  String messageId;
  String error;
}
