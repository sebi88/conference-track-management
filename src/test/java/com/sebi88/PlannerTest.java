package com.sebi88;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.sebi88.Session.SessionType;

public class PlannerTest {

  @Test
  void should_throw_exception_when_no_sessions() {
    assertThatCode(() -> Planner.plan(sessions(), talks(60)))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Sessions are empty.");
  }

  @Test
  void should_throw_exception_when_talks_longer_than_session() {
    assertThatCode(() -> Planner.plan(sessions(30), talks(35)))
        .isInstanceOf(PlanningIsNotPossibleException.class);

    assertThatCode(() -> Planner.plan(sessions(35, 36, 37, 38), talks(40)))
        .isInstanceOf(PlanningIsNotPossibleException.class);
  }

  @Test
  void should_throw_exception_when_talks_dont_fit() {
    assertThatCode(() -> Planner.plan(sessions(30, 40), talks(32, 32)))
        .isInstanceOf(PlanningIsNotPossibleException.class);
    assertThatCode(() -> Planner.plan(sessions(30, 40), talks(30, 30, 10, 5, 5)))
        .isInstanceOf(PlanningIsNotPossibleException.class);
  }

  static Stream<Arguments> params() {
    return Stream.of(
        Arguments.of(sessions(100), talks()),
        Arguments.of(sessions(100), talks(70)),
        Arguments.of(sessions(100), talks(100)),
        Arguments.of(sessions(100), talks(30, 40)),
        Arguments.of(sessions(100), talks(60, 10, 20, 10)),
        
        Arguments.of(sessions(50, 50), talks()),
        Arguments.of(sessions(50, 50), talks(30, 40)),
        Arguments.of(sessions(50, 50), talks(50, 10, 20, 10)),
        
        // sample, where eager algorithm would fail
        // 1. order sessions by length descending, and talks by length descending
        // 2. go talk by talk, and session by session, put the task in the first session, where it fits 
        Arguments.of(sessions(9, 7), talks(6, 1, 5, 4))
        );
  }
  
  
  @MethodSource("params")
  @ParameterizedTest
  void should_plan_sessions_accordingly(List<Session> sessions, List<Talk> talks) throws PlanningIsNotPossibleException {
    Collection<PlannedSession> plannedSessions = Planner.plan(sessions, talks);
    
    assertThat(plannedSessions)
        .extracting(PlannedSession::asSession)
        .containsExactlyInAnyOrderElementsOf(sessions);
    
    assertThat(plannedSessions)
        .flatExtracting(planned -> planned.talks())
        .containsExactlyInAnyOrderElementsOf(talks);
    
    plannedSessions.forEach(planned -> {
      int totalTalkLength = planned.talks().stream().mapToInt(Talk::length).sum();
      assertThat(totalTalkLength).isLessThanOrEqualTo(planned.length());
    });
  }
  
  private static List<Session> sessions(int... lengths) {
    AtomicInteger counter = new AtomicInteger();
    return Arrays.stream(lengths)
        .mapToObj(length -> new Session(counter.incrementAndGet(), SessionType.BEFORE_NOON, length))
        .toList();
  }
  
  private static List<Talk> talks(int... lengths) {
    AtomicInteger counter = new AtomicInteger();
    return Arrays.stream(lengths)
        .mapToObj(length -> new Talk("Talk " + counter.incrementAndGet(), length))
        .toList();
  }

}
