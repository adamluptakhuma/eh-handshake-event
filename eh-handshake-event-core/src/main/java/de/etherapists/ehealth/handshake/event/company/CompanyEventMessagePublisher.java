package de.etherapists.ehealth.handshake.event.company;

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
public class CompanyEventMessagePublisher {

  private final EventMessagePublisher eventMessagePublisher;
  private final Clock utcClock;
  private final IdGenerator idGenerator;

  public SendEventMessageResult publishCompanyCreated(CompanyCreatedPayload payload) {

    if (StringUtils.isEmpty(payload.getAwsRegion())) {
      log.warn(
          "No AWS region resolved for company {} – skipping event emission",
          payload.getCompanyId());
      return SendEventMessageResult.builder()
          .success(false)
          .error(
              String.format(
                  "No AWS region resolved for company %d – skipping event emission",
                  payload.getCompanyId()))
          .build();
    }

    var event =
        EventMessage.builder()
            .eventType(EventType.COMPANY_CREATED)
            .entityType(EntityType.COMPANY)
            .messageId(idGenerator.generateId().toString())
            .entityId(payload.getCompanyId().toString())
            .timestamp(utcClock.millis())
            .payload(payload)
            .messageGroupId("company-" + payload.getCompanyId())
            .deduplicationId(payload.getCompanyId().toString())
            .build();

    return eventMessagePublisher.send(event);
  }

  public SendEventMessageResult publishCompanyUpdated(CompanyUpdatedPayload payload) {
    if (StringUtils.isEmpty(payload.getAwsRegion())) {
      log.warn(
          "No AWS region resolved for company {} – skipping event emission",
          payload.getCompanyId());
      return SendEventMessageResult.builder()
          .success(false)
          .error(
              String.format(
                  "No AWS region resolved for company %d – skipping event emission",
                  payload.getCompanyId()))
          .build();
    }

    var event =
        EventMessage.builder()
            .eventType(EventType.COMPANY_UPDATED)
            .entityType(EntityType.COMPANY)
            .messageId(idGenerator.generateId().toString())
            .entityId(payload.getCompanyId().toString())
            .timestamp(utcClock.millis())
            .payload(payload)
            .messageGroupId("company-" + payload.getCompanyId().toString())
            .deduplicationId(payload.getCompanyId().toString())
            .build();

    return eventMessagePublisher.send(event);
  }
}
