package com.sebi88;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    int minDays = guessMinDays(Session.SessionType.BEFORE_NOON.length() + Session.SessionType.AFTER_NOON.length(), talks);

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

  static int guessMinDays(int trackLength, List<Talk> talks) {
    return Math.max(1,
        (int) Math.ceil(talks.stream().mapToInt(Talk::length).sum() / trackLength));
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
    
    for (PlannedSession session : plannedSessions) {
      LocalTime start = session.type().start();
      if (session.type() == Session.SessionType.BEFORE_NOON) {
        sb.append("Track ")
          .append(session.trackId())
          .append("\n");
      }
      for (Talk talk : session.talks()) {
        sb.append(start.format(formatter))
          .append(" ")
          .append(talk.title())
          .append("\n");
        start = start.plusMinutes(talk.length());
      }

      if (session.type() == Session.SessionType.BEFORE_NOON) {
        sb.append("12:00PM Lunch\n");
      } else {
        if (start.isBefore(Session.SessionType.MIN_NETWORKING_TIME)) {
          start = Session.SessionType.MIN_NETWORKING_TIME;
        }
        sb.append(start.format(formatter))
          .append(" ")
          .append("Networking Event")
          .append("\n\n");
      }
    }

    return sb.toString();
  }
}
