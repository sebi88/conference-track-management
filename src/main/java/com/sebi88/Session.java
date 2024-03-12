package com.sebi88;

public record Session(int trackId, SessionType type, int length) {
  
  public enum SessionType {
    BEFORE_NOON, AFTER_NOON;
  }
  
  Session(int trackId, SessionType type) {
    this(trackId, type, type == SessionType.BEFORE_NOON ? 180 : 240);
  }

  public int length() {
    return length;
  }
}
