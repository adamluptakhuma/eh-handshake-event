package de.etherapists.ehealth.handshake.event.messaging;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@ExtendWith(MockitoExtension.class)
class SqsEventMessagePublisherTest {

  @Mock private EventMessageJsonConverter eventMessageJsonConverter;
  @Mock private SqsClient sqsClient;

  private final String queueUrl = "test-queue-url";

  @InjectMocks private SqsEventMessagePublisher publisher;

  @BeforeEach
  void setUp() {
    publisher = new SqsEventMessagePublisher(eventMessageJsonConverter, sqsClient, queueUrl);
  }

  @Test
  void sendShouldReturnSuccessResultWhenMessageIsSuccessful() {
    var message = mock(EventMessage.class);
    when(eventMessageJsonConverter.serialize(message)).thenReturn("json-body");
    when(message.getMessageGroupId()).thenReturn(null);
    when(message.getEntityType()).thenReturn(EntityType.USER);
    when(message.getDeduplicationId()).thenReturn(null);
    when(message.getMessageId()).thenReturn("msg-id");
    SendMessageResponse response = SendMessageResponse.builder().messageId("aws-msg-id").build();
    when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenReturn(response);

    SendEventMessageResult actual = publisher.send(message);

    assertThat(actual.isSuccess()).isTrue();
    assertThat(actual.getMessageId()).isEqualTo("aws-msg-id");
    verify(sqsClient).sendMessage(any(SendMessageRequest.class));
  }

  @Test
  void sendShouldReturnErrorResultWhenMessageFails() {
    EventMessage<String> message = mock(EventMessage.class);
    when(eventMessageJsonConverter.serialize(message)).thenReturn("json-body");
    when(message.getMessageGroupId()).thenReturn(null);
    when(message.getEventType()).thenReturn(EventType.USER_UPDATED);
    when(message.getEntityType()).thenReturn(EntityType.USER);
    when(message.getDeduplicationId()).thenReturn(null);
    when(message.getMessageId()).thenReturn("msg-id");
    when(sqsClient.sendMessage(any(SendMessageRequest.class)))
        .thenThrow(new RuntimeException("SQS error"));

    SendEventMessageResult actual = publisher.send(message);

    assertThat(actual.isSuccess()).isFalse();
    assertThat(actual.getError()).isEqualTo("SQS error");
    verify(sqsClient).sendMessage(any(SendMessageRequest.class));
  }

  @Test
  void sendShouldUseProvidedGroupIdAndDeduplicationId() {
    var message = mock(EventMessage.class);
    when(eventMessageJsonConverter.serialize(message)).thenReturn("json-body");
    when(message.getMessageGroupId()).thenReturn("custom-group");
    when(message.getEntityType()).thenReturn(EntityType.USER);
    when(message.getDeduplicationId()).thenReturn("custom-dedup");
    SendMessageResponse response = SendMessageResponse.builder().messageId("aws-msg-id").build();
    when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenReturn(response);

    publisher.send(message);

    verify(sqsClient)
        .sendMessage(
            argThat(
                (SendMessageRequest req) ->
                    "custom-group".equals(req.messageGroupId())
                        && "custom-dedup".equals(req.messageDeduplicationId())));
  }

  @Test
  void sendShouldFallbackToEntityTypeAndMessageIdForGroupIdAndDeduplicationId() {
    var message = mock(EventMessage.class);
    when(eventMessageJsonConverter.serialize(message)).thenReturn("json-body");
    when(message.getMessageGroupId()).thenReturn(null);
    when(message.getEntityType()).thenReturn(EntityType.USER);
    when(message.getDeduplicationId()).thenReturn(null);
    when(message.getMessageId()).thenReturn("msg-id");
    SendMessageResponse response = SendMessageResponse.builder().messageId("aws-msg-id").build();
    when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenReturn(response);

    publisher.send(message);

    verify(sqsClient)
        .sendMessage(
            argThat(
                (SendMessageRequest req) ->
                    "USER".equals(req.messageGroupId())
                        && "msg-id".equals(req.messageDeduplicationId())));
  }
}
