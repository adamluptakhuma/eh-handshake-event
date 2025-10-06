package de.etherapists.ehealth.handshake.event.messaging;

public interface EventMessagePublisher {
  <T> SendEventMessageResult send(EventMessage<T> message);
}
