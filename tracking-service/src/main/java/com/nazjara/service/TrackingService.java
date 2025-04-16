package com.nazjara.service;

import com.nazjara.message.DispatchTracking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingService {

  public void process(DispatchTracking payload) {
    //some business logic
  }
}
