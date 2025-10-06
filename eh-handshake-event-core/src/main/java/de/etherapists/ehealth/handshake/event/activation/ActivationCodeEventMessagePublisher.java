package de.etherapists.ehealth.handshake.event.activation;

import static java.lang.String.format;
import static java.util.Objects.isNull;

import de.etherapists.ehealth.handshake.event.messaging.EntityType;
import de.etherapists.ehealth.handshake.event.messaging.EventMessage;
import de.etherapists.ehealth.handshake.event.messaging.EventMessagePublisher;
import de.etherapists.ehealth.handshake.event.messaging.EventType;
import de.etherapists.ehealth.handshake.event.messaging.IdGenerator;
import de.etherapists.ehealth.handshake.event.messaging.SendEventMessageResult;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ActivationCodeEventMessagePublisher {

  private final EventMessagePublisher eventMessagePublisher;
  private final Clock utcClock;
  private final IdGenerator idGenerator;

  public SendEventMessageResult publishActivationCodeCreated(ActivationCodeCreatedPayload payload) {

    if (isNull(payload.getAwsRegion())) {
      String error =
          format(
              "No AWS region resolved for company %d and country %s – skipping event emission",
              payload.getCompanyId(), payload.getAwsRegion());
      log.warn(error);
      return SendEventMessageResult.builder().success(false).error(error).build();
    }

    var event =
        EventMessage.builder()
            .eventType(EventType.ACTIVATION_CODE_CREATED)
            .entityType(EntityType.ACTIVATION_CODE)
            .messageId(idGenerator.generateId().toString())
            .entityId(payload.getActivationCodeId().toString())
            .timestamp(utcClock.millis())
            .payload(payload)
            .messageGroupId("company-" + payload.getCompanyId())
            .deduplicationId(payload.getCompanyId().toString())
            .build();

    return eventMessagePublisher.send(event);
  }

  public SendEventMessageResult publishActivationCodeAssigned(
      ActivationCodeAssignedPayload payload) {

    if (isNull(payload.getAwsRegion())) {
      String error =
          format(
              "No AWS region resolved for company %d and country %s – skipping event emission",
              payload.getCompanyId(), payload.getAwsRegion());
      log.warn(error);
      return SendEventMessageResult.builder().success(false).error(error).build();
    }

    var event =
        EventMessage.builder()
            .eventType(EventType.ACTIVATION_CODE_ASSIGNED)
            .entityType(EntityType.ACTIVATION_CODE)
            .messageId(idGenerator.generateId().toString())
            .entityId(payload.getActivationCodeId().toString())
            .timestamp(utcClock.millis())
            .payload(payload)
            .messageGroupId("company-" + payload.getCompanyId())
            .deduplicationId(payload.getCompanyId().toString())
            .build();

    return eventMessagePublisher.send(event);
  }
}
