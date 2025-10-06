package de.etherapists.ehealth.handshake.event.messaging;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
  private final AtomicLong leastSigBits = new AtomicLong();

  public UUID generateId() {
    return new UUID(0L, this.leastSigBits.incrementAndGet());
  }
}
