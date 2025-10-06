package de.etherapists.ehealth.handshake.event.activation;

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
class ActivationCodeEventMessagePublisherTest {

  @Mock private EventMessagePublisher eventMessagePublisher;

  @Mock private Clock utcClock;

  @Mock private IdGenerator idGenerator;

  @InjectMocks private ActivationCodeEventMessagePublisher publisher;

  @BeforeEach
  void setUp() {
    publisher = new ActivationCodeEventMessagePublisher(eventMessagePublisher, utcClock, idGenerator);
  }

  @Test
  void shouldPublishActivationCodeCreatedSuccess() {
    ActivationCodeCreatedPayload payload = mock(ActivationCodeCreatedPayload.class);
    when(payload.getAwsRegion()).thenReturn("eu-central-1");
    when(payload.getCompanyId()).thenReturn(1);
    when(payload.getActivationCodeId()).thenReturn(2);
    when(utcClock.millis()).thenReturn(123456789L);
    when(idGenerator.generateId()).thenReturn(UUID.randomUUID());

    SendEventMessageResult expectedResult = SendEventMessageResult.builder().success(true).build();
    when(eventMessagePublisher.send(any())).thenReturn(expectedResult);

    SendEventMessageResult result = publisher.publishActivationCodeCreated(payload);

    assertTrue(result.isSuccess());
    verify(eventMessagePublisher).send(any());
  }

  @Test
  void shouldPublishActivationCodeCreatedNullAwsRegion() {
    ActivationCodeCreatedPayload payload = mock(ActivationCodeCreatedPayload.class);
    when(payload.getAwsRegion()).thenReturn(null);
    when(payload.getCompanyId()).thenReturn(1);

    SendEventMessageResult result = publisher.publishActivationCodeCreated(payload);

    assertFalse(result.isSuccess());
    assertNotNull(result.getError());
    verify(eventMessagePublisher, never()).send(any());
  }

  @Test
  void shouldPublishActivationCodeAssignedSuccess() {
    ActivationCodeAssignedPayload payload = mock(ActivationCodeAssignedPayload.class);
    when(payload.getAwsRegion()).thenReturn("eu-central-1");
    when(payload.getCompanyId()).thenReturn(2);
    when(payload.getActivationCodeId()).thenReturn(3);
    when(utcClock.millis()).thenReturn(987654321L);
    when(idGenerator.generateId()).thenReturn(UUID.randomUUID());

    SendEventMessageResult expectedResult = SendEventMessageResult.builder().success(true).build();
    when(eventMessagePublisher.send(any())).thenReturn(expectedResult);

    SendEventMessageResult result = publisher.publishActivationCodeAssigned(payload);

    assertTrue(result.isSuccess());
    verify(eventMessagePublisher).send(any());
  }

  @Test
  void shouldPublishActivationCodeAssignedNullAwsRegion() {
    ActivationCodeAssignedPayload payload = mock(ActivationCodeAssignedPayload.class);
    when(payload.getAwsRegion()).thenReturn(null);
    when(payload.getCompanyId()).thenReturn(2);

    SendEventMessageResult result = publisher.publishActivationCodeAssigned(payload);

    assertFalse(result.isSuccess());
    assertNotNull(result.getError());
    verify(eventMessagePublisher, never()).send(any());
  }
}
