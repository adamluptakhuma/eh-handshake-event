package de.etherapists.ehealth.handshake.event.company;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.etherapists.ehealth.handshake.event.messaging.EventMessagePublisher;
import de.etherapists.ehealth.handshake.event.messaging.IdGenerator;
import de.etherapists.ehealth.handshake.event.messaging.SendEventMessageResult;
import java.time.Clock;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyEventMessagePublisherTest {

  @Mock private EventMessagePublisher eventMessagePublisher;

  @Mock private Clock utcClock;

  @Mock private IdGenerator idGenerator;

  @InjectMocks private CompanyEventMessagePublisher publisher;

  @BeforeEach
  void setUp() {
    publisher = new CompanyEventMessagePublisher(eventMessagePublisher, utcClock, idGenerator);
  }

  @Test
  void shouldPublishCompanyCreatedSuccess() {
    CompanyCreatedPayload payload = mock(CompanyCreatedPayload.class);
    when(payload.getAwsRegion()).thenReturn("eu-central-1");
    when(payload.getCompanyId()).thenReturn(1);
    when(utcClock.millis()).thenReturn(123456789L);
    when(idGenerator.generateId()).thenReturn(UUID.randomUUID());

    SendEventMessageResult expectedResult = SendEventMessageResult.builder().success(true).build();
    when(eventMessagePublisher.send(any())).thenReturn(expectedResult);

    SendEventMessageResult result = publisher.publishCompanyCreated(payload);

    assertTrue(result.isSuccess());
    verify(eventMessagePublisher).send(any());
  }

  @Test
  void shouldPublishCompanyCreatedNullAwsRegion() {
    CompanyCreatedPayload payload = mock(CompanyCreatedPayload.class);
    when(payload.getAwsRegion()).thenReturn(null);

    SendEventMessageResult result = publisher.publishCompanyCreated(payload);

    assertFalse(result.isSuccess());
    assertNotNull(result.getError());
    verify(eventMessagePublisher, never()).send(any());
  }

  @Test
  void shouldPublishCompanyUpdatedSuccess() {
    CompanyUpdatedPayload payload = mock(CompanyUpdatedPayload.class);
    when(payload.getAwsRegion()).thenReturn("eu-central-1");
    when(payload.getCompanyId()).thenReturn(2);
    when(utcClock.millis()).thenReturn(987654321L);
    when(idGenerator.generateId()).thenReturn(UUID.randomUUID());

    SendEventMessageResult expectedResult = SendEventMessageResult.builder().success(true).build();
    when(eventMessagePublisher.send(any())).thenReturn(expectedResult);

    SendEventMessageResult result = publisher.publishCompanyUpdated(payload);

    assertTrue(result.isSuccess());
    verify(eventMessagePublisher).send(any());
  }

  @Test
  void shouldPublishCompanyUpdatedNullAwsRegion() {
    CompanyUpdatedPayload payload = mock(CompanyUpdatedPayload.class);
    when(payload.getAwsRegion()).thenReturn(null);

    SendEventMessageResult result = publisher.publishCompanyUpdated(payload);

    assertFalse(result.isSuccess());
    assertNotNull(result.getError());
    verify(eventMessagePublisher, never()).send(any());
  }
}
