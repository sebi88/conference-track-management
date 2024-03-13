package com.sebi88;

import java.time.LocalTime;
import java.util.List;

public class Printer {
  
  private static final LocalTime MIN_NETWORKING_TIME = LocalTime.of(16, 0);

  public static void print(List<PlannedSession> plannedSessions) {
    for (PlannedSession session : plannedSessions) {
      
      LocalTime start;
      if (session.type() == Session.SessionType.BEFORE_NOON) {
        System.out.println("Track " + session.trackId() + ":");
        start = LocalTime.of(9, 0);
      } else {
        start = LocalTime.of(13, 0);
      }
      
      for (Talk talk : session.talks()) {
        System.out.println(start + " " + talk.title());
        start = start.plusMinutes(talk.length());
      }
      
      if (session.type() == Session.SessionType.BEFORE_NOON) {
        System.out.println("12:00 PM Lunch");
      } else {
        if (start.isBefore(MIN_NETWORKING_TIME)) {
          start = MIN_NETWORKING_TIME;
        }
        System.out.println(start + " Networking Event");
        System.out.println();
      }
    }
    
  }
}
