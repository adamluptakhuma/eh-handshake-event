package de.etherapists.ehealth.handshake.event.activation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ActivationCodeAssignedPayload {
  private Integer userId;
  private Integer activationCodeId;
  private String activationCodeValue;
  private Integer companyId;
  private String platform;
  private String awsRegion;
}
