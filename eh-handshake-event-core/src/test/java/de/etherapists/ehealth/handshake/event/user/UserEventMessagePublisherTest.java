package de.etherapists.ehealth.handshake.event.user;

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
class UserEventMessagePublisherTest {

    @Mock private EventMessagePublisher eventMessagePublisher;

    @Mock private Clock utcClock;

    @Mock private IdGenerator idGenerator;

    @InjectMocks private UserEventMessagePublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new UserEventMessagePublisher(eventMessagePublisher, utcClock, idGenerator);
    }

    @Test
    void shouldPublishUserCreatedSuccess() {
        UserCreatedPayload payload = mock(UserCreatedPayload.class);
        when(payload.getAwsRegion()).thenReturn("eu-central-1");
        when(payload.getCompanyId()).thenReturn(1);
        when(payload.getUserId()).thenReturn(2);
        when(utcClock.millis()).thenReturn(123456789L);
        when(idGenerator.generateId()).thenReturn(UUID.randomUUID());

        SendEventMessageResult expectedResult = SendEventMessageResult.builder().success(true).build();
        when(eventMessagePublisher.send(any())).thenReturn(expectedResult);

        SendEventMessageResult result = publisher.publishUserCreated(payload);

        assertTrue(result.isSuccess());
        verify(eventMessagePublisher).send(any());
    }

    @Test
    void shouldPublishUserCreatedNullAwsRegion() {
        UserCreatedPayload payload = mock(UserCreatedPayload.class);
        when(payload.getAwsRegion()).thenReturn("");

        SendEventMessageResult result = publisher.publishUserCreated(payload);

        assertFalse(result.isSuccess());
        assertNotNull(result.getError());
        verify(eventMessagePublisher, never()).send(any());
    }

    @Test
    void shouldPublishUserUpdatedSuccess() {
        UserUpdatedPayload payload = mock(UserUpdatedPayload.class);
        when(payload.getAwsRegion()).thenReturn("eu-central-1");
        when(payload.getCompanyId()).thenReturn(2);
        when(payload.getUserId()).thenReturn(2);
        when(utcClock.millis()).thenReturn(987654321L);
        when(idGenerator.generateId()).thenReturn(UUID.randomUUID());

        SendEventMessageResult expectedResult = SendEventMessageResult.builder().success(true).build();
        when(eventMessagePublisher.send(any())).thenReturn(expectedResult);

        SendEventMessageResult result = publisher.publishUserUpdated(payload);

        assertTrue(result.isSuccess());
        verify(eventMessagePublisher).send(any());
    }

    @Test
    void shouldPublishUserUpdatedNullAwsRegion() {
        UserUpdatedPayload payload = mock(UserUpdatedPayload.class);
        when(payload.getAwsRegion()).thenReturn(null);

        SendEventMessageResult result = publisher.publishUserUpdated(payload);

        assertFalse(result.isSuccess());
        assertNotNull(result.getError());
        verify(eventMessagePublisher, never()).send(any());
    }
}