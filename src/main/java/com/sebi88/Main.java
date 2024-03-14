package com.sebi88;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.apache.commons.io.FileUtils;
import com.sebi88.Session.SessionType;

public class Main {

  private static final String TEST_FILE = "input.txt";

  public static void main(String[] args) throws IOException {
    List<Talk> talks = readTalks(TEST_FILE);
    int minDays = calculateMinDays(talks);

    String schedule = IntStream.range(minDays, minDays * 3)
        .mapToObj(Integer.class::cast)
        .map(days -> {
          List<Session> sessions = generateSessions(days);
          try {
            return Optional.of(Planner.plan(sessions, talks));
          } catch (PlanningIsNotPossibleException e) {
            System.out.println("Could not plan for " + days + " days");
            return Optional.<List<PlannedSession>>empty();
          }
        })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(Main::renderPlanned)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Giving up, could not fit talks in tracks even with 1/3 fullness."));

    System.out.println(schedule);
  }

  static int calculateMinDays(List<Talk> talks) {
    return 1 + talks.stream().mapToInt(Talk::length).sum() / (Session.SessionType.BEFORE_NOON.length() + Session.SessionType.AFTER_NOON.length());
  }

  static List<Session> generateSessions(int days) {
    List<Session> sessions = new ArrayList<>();

    IntStream.range(0, days)
        .forEach(day -> {
          sessions.add(new Session(day + 1, SessionType.BEFORE_NOON));
          sessions.add(new Session(day + 1, SessionType.AFTER_NOON));
        });

    return sessions;
  }

  static List<Talk> readTalks(String pathname) throws IOException {
    return FileUtils.readLines(new File(pathname), StandardCharsets.UTF_8)
        .stream()
        .map(Talk::parse)
        .toList();
  }

  static String renderPlanned(List<PlannedSession> plannedSessions) {
    StringBuilder sb = new StringBuilder();
    for (PlannedSession session : plannedSessions) {
      LocalTime start = session.type().start();
      for (Talk talk : session.talks()) {
        sb.append(start)
          .append(" ")
          .append(talk.title())
          .append("\n");
        start = start.plusMinutes(talk.length());
      }

      if (session.type() == Session.SessionType.BEFORE_NOON) {
        sb.append("12:00PM Lunch");
      } else {
        if (start.isBefore(Session.SessionType.MIN_NETWORKING_TIME)) {
          start = Session.SessionType.MIN_NETWORKING_TIME;
        }
        sb.append(start)
          .append(" ")
          .append("Networking Event")
          .append("\n\n");
      }
    }

    return sb.toString();

  }
}
