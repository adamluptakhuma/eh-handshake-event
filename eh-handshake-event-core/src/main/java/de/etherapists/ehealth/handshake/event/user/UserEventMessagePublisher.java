package de.etherapists.ehealth.handshake.event.user;

import de.etherapists.ehealth.handshake.event.messaging.EntityType;
import de.etherapists.ehealth.handshake.event.messaging.EventMessage;
import de.etherapists.ehealth.handshake.event.messaging.EventMessagePublisher;
import de.etherapists.ehealth.handshake.event.messaging.EventType;
import de.etherapists.ehealth.handshake.event.messaging.IdGenerator;
import de.etherapists.ehealth.handshake.event.messaging.SendEventMessageResult;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class UserEventMessagePublisher {

  private final EventMessagePublisher eventMessagePublisher;
  private final Clock utcClock;
  private final IdGenerator idGenerator;

  public SendEventMessageResult publishUserCreated(UserCreatedPayload payload) {
    var companyId = payload.getCompanyId();
    if (payload.getAwsRegion().isEmpty()) {
      log.warn("No AWS region resolved for company {} – skipping event emission", companyId);
      return SendEventMessageResult.builder()
          .success(false)
          .error(
              String.format(
                  "No AWS region resolved for company %d – skipping event emission", companyId))
          .build();
    }

    var event =
        EventMessage.builder()
            .eventType(EventType.USER_CREATED)
            .entityType(EntityType.USER)
            .messageId(idGenerator.generateId().toString())
            .entityId(payload.getUserId().toString())
            .timestamp(utcClock.millis())
            .payload(payload)
            .messageGroupId("company-" + companyId)
            .deduplicationId(payload.getUserId().toString())
            .build();

    return eventMessagePublisher.send(event);
  }

  public SendEventMessageResult publishUserUpdated(UserUpdatedPayload payload) {
    var companyId = payload.getCompanyId();
    if (StringUtils.isEmpty(payload.getAwsRegion())) {
      log.warn("No AWS region resolved for company {} – skipping event emission", companyId);
      return SendEventMessageResult.builder()
          .success(false)
          .error(
              String.format(
                  "No AWS region resolved for company %d – skipping event emission", companyId))
          .build();
    }

    var event =
        EventMessage.builder()
            .eventType(EventType.USER_UPDATED)
            .entityType(EntityType.USER)
            .messageId(idGenerator.generateId().toString())
            .entityId(payload.getUserId().toString())
            .timestamp(utcClock.millis())
            .payload(payload)
            .messageGroupId("company-" + companyId)
            .deduplicationId(payload.getUserId().toString())
            .build();

    return eventMessagePublisher.send(event);
  }
}
