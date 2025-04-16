package com.nazjara.message;

import java.util.UUID;
import lombok.Builder;

@Builder
public record TrackingStatusUpdated(UUID orderId, Status status) {

}
