package com.sebi88;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class Planner {

  public static List<PlannedSession> plan(Collection<Session> sessions,
      Collection<Talk> talks) throws PlanningIsNotPossibleException {
    if (sessions.isEmpty()) {
      throw new IllegalStateException("Sessions are empty.");
    }

    return planOnSorted(sessions.stream().toArray(Session[]::new),
        talks.stream().toArray(Talk[]::new));
  }

  private static List<PlannedSession> planOnSorted(Session[] sessions, Talk[] talks) throws PlanningIsNotPossibleException {
    int[] talkMapping = new int[talks.length];
    Arrays.fill(talkMapping, -1);
    int talkPointer = 0;

    while (true) {
      if (talkPointer == talks.length) {
        break;
      }

      talkMapping[talkPointer]++;

      boolean isOkSoFar = IntStream.range(0, sessions.length)
          .allMatch(sessionIndex -> {
            int sessionLength = sessions[sessionIndex].length();
            int sessionUsed = IntStream.range(0, talks.length)
                .filter(talkIndex -> talkMapping[talkIndex] == sessionIndex)
                .map(talkIndex -> talks[talkIndex].length())
                .sum();
            return sessionUsed <= sessionLength;
          });

      if (isOkSoFar) {
        talkPointer++;
      } else {
        while(talkMapping[talkPointer] == sessions.length - 1) {
          talkMapping[talkPointer] = -1;
          talkPointer--;
          
          if (talkPointer < 0) {
            throw new PlanningIsNotPossibleException();
          }
        }
      }
    }

    return IntStream.range(0, sessions.length)
        .mapToObj(sessionIndex -> new PlannedSession(sessions[sessionIndex],
            IntStream.range(0, talks.length)
                .filter(talkIndex -> talkMapping[talkIndex] == sessionIndex)
                .mapToObj(talkIndex -> talks[talkIndex])
                .toList()))
        .toList();
  }
}
