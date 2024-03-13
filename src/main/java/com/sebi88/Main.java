package com.sebi88;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.io.FileUtils;
import com.sebi88.Session.SessionType;

public class Main {
  
  public static void main(String[] args) throws IOException {
    List<Talk> talks = readTalks();
    int minDays = 1 + talks.stream().mapToInt(Talk::length).sum() / ( Session.SessionType.BEFORE_NOON.length() + Session.SessionType.AFTER_NOON.length());
    for(int days = minDays; days<minDays * 3; days++) {
      List<Session> sessions = generateSessions(days);
      
      try {
        List<PlannedSession> planned = Planner.plan(sessions, talks);
        Printer.print(planned);
        return;
      } catch (PlanningIsNotPossibleException e) {
        System.out.println("Could not plan for " + days + " days");
      }

    }
    
    System.out.println("Giving up.");
  }

  private static List<Session> generateSessions(int days) {
    List<Session> sessions = new ArrayList<>();

    IntStream.range(0, days)
        .forEach(day -> {
          sessions.add(new Session(day + 1, SessionType.BEFORE_NOON));
          sessions.add(new Session(day + 1, SessionType.AFTER_NOON));
        });

    return sessions;
  }

  private static List<Talk> readTalks() throws IOException {
    return FileUtils.readLines(new File("input.txt"), StandardCharsets.UTF_8)
      .stream()
      .map(Talk::parse)
      .toList();
  }

}
