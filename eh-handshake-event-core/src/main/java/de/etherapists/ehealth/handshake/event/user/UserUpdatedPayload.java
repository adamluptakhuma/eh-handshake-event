package de.etherapists.ehealth.handshake.event.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserUpdatedPayload {
  private Integer userId;
  private Integer companyId;
  private String platform;
  private String awsRegion;
}
