package com.nazjara.message;

import java.util.UUID;
import lombok.Builder;

@Builder
public record DispatchPreparing(UUID orderId) {

}
