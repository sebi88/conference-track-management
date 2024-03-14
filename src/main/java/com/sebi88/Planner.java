package com.sebi88;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Planner {

  public static List<Plan> plan(List<Session> sessions, List<Talk> talks) throws PlanningIsNotPossibleException {
    if (sessions.isEmpty()) {
      throw new IllegalStateException("Sessions are empty.");
    }

    List<Integer> talkMapping = new ArrayList<>(Collections.nCopies(talks.size(), -1));
    int talkPointer = 0;

    while (true) {
      if (talkPointer == talks.size()) {
        break;
      }

      talkMapping.set(talkPointer, talkMapping.get(talkPointer) + 1);

      boolean isOkSoFar = IntStream.range(0, sessions.size())
          .allMatch(sessionIndex -> {
            int sessionLength = sessions.get(sessionIndex).length();
            int sessionUsed = IntStream.range(0, talks.size())
                .filter(talkIndex -> talkMapping.get(talkIndex) == sessionIndex)
                .map(talkIndex -> talks.get(talkIndex).length())
                .sum();
            return sessionUsed <= sessionLength;
          });

      if (isOkSoFar) {
        talkPointer++;
      } else {
        while (talkMapping.get(talkPointer) == sessions.size() - 1) {
          talkMapping.set(talkPointer, -1);
          talkPointer--;

          if (talkPointer < 0) {
            throw new PlanningIsNotPossibleException();
          }
        }
      }
    }

    return IntStream.range(0, sessions.size())
        .mapToObj(sessionIndex -> new Plan(sessions.get(sessionIndex),
            IntStream.range(0, talks.size())
                .filter(talkIndex -> talkMapping.get(talkIndex) == sessionIndex)
                .mapToObj(talkIndex -> talks.get(talkIndex))
                .toList()))
        .toList();
  }
}
