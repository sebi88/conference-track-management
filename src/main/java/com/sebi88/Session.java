package com.sebi88;

import java.time.LocalTime;

public record Session(int trackId, SessionType type, LocalTime start, int length) {
  
  public enum SessionType {
    BEFORE_NOON,
    AFTER_NOON;
  }

  public int length() {
    return length;
  }
}
