package de.etherapists.ehealth.handshake.event.messaging;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Slf4j
@AllArgsConstructor
public class SqsEventMessagePublisher implements EventMessagePublisher {

  private final EventMessageJsonConverter eventMessageJsonConverter;
  private final SqsClient sqsClient;
  private final String queueUrl;

  @Override
  public <T> SendEventMessageResult send(EventMessage<T> message) {
    String body = eventMessageJsonConverter.serialize(message);

    SendMessageRequest.Builder builder =
        SendMessageRequest.builder().queueUrl(queueUrl).messageBody(body);

    String groupId =
        message.getMessageGroupId() != null
            ? message.getMessageGroupId()
            : message.getEntityType().name(); // default ordering by entity type

    String dedupId =
        message.getDeduplicationId() != null
            ? message.getDeduplicationId()
            : message.getMessageId().toString(); // fallback to messageId

    builder.messageGroupId(groupId).messageDeduplicationId(dedupId);

    try {
      SendMessageResponse response = sqsClient.sendMessage(builder.build());

      log.info(
          "Published event type={} entityType={} entityId={} queue={} awsMessageId={}",
          message.getEventType(),
          message.getEntityType(),
          message.getEntityId(),
          queueUrl,
          response.messageId());

      return SendEventMessageResult.builder().success(true).messageId(response.messageId()).build();

    } catch (Exception e) {
      log.error(
          "Failed to publish event type={} entityType={} entityId={} queue={} error={}",
          message.getEventType(),
          message.getEntityType(),
          message.getEntityId(),
          queueUrl,
          e.getMessage(),
          e);

      return SendEventMessageResult.builder().success(false).error(e.getMessage()).build();
    }
  }
}
