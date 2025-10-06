package de.etherapists.ehealth.handshake.event.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CompanyCreatedPayload {
  private Integer companyId;
  private String platform;
  private String awsRegion;
}
