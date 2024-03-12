package com.sebi88;

public record Talk(String title, int length) {
  
  public static final int LIGHTNING_TALK_LENGTH = 5;
  
  public static Talk parse(String line) {
    if (line.endsWith("lightning")) {
      return new Talk(line, LIGHTNING_TALK_LENGTH);
    }
    
    if (line.matches("\\D*\\d+min$")) {
      int mins = Integer.parseInt(line.replaceAll("\\D", ""));
      return new Talk(line, mins);
    }

    throw new IllegalArgumentException("Invalid talk: " + line);
  }

}
