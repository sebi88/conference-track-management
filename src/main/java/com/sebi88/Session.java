package com.sebi88;

import java.time.LocalTime;

public record Session(int trackId, SessionType type, int length) {
  
  public enum SessionType {
    BEFORE_NOON(180, LocalTime.of(9, 0)),
    AFTER_NOON(240,  LocalTime.of(13, 0));
    
    public static final LocalTime MIN_NETWORKING_TIME = LocalTime.of(16, 0);
    
    private final int length;
    private final LocalTime start;
    
    SessionType(int length, LocalTime start) {
      this.length = length;
      this.start = start;
    }
    
    public int length() {
      return length;
    }
    
    public LocalTime start() {
      return start;
    }
  }
  
  Session(int trackId, SessionType type) {
    this(trackId, type, type.length());
  }

  public int length() {
    return length;
  }
}
