package com.sebi88;

public record Session(int trackId, SessionType type, int length) {
  
  public enum SessionType {
    BEFORE_NOON(180),
    AFTER_NOON(240);
    
    private final int length;
    
    SessionType(int length) {
      this.length = length;
    }
    
    public int length() {
      return length;
    }
  }
  
  Session(int trackId, SessionType type) {
    this(trackId, type, type.length());
  }

  public int length() {
    return length;
  }
}
