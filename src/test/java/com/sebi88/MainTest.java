package com.sebi88;

import static com.sebi88.TestUtils.talks;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import com.sebi88.Session.SessionType;

public class MainTest {

  @Test
  void should_guess_min_days() {
    assertThat(Main.guessMinDays(30, talks())).isEqualTo(1);
    assertThat(Main.guessMinDays(30, talks(20))).isEqualTo(1);
    assertThat(Main.guessMinDays(30, talks(30))).isEqualTo(1);
    assertThat(Main.guessMinDays(30, talks(10, 5, 10, 5))).isEqualTo(1);
    assertThat(Main.guessMinDays(30, talks(20, 20, 20))).isEqualTo(2);
  }

  @Test
  void should_generate_sessions_for_single_day() {
    assertThat(Main.generateSessions(1)).containsExactly(
        new Session(1, SessionType.BEFORE_NOON),
        new Session(1, SessionType.AFTER_NOON));
  }

  @Test
  void should_generate_sessions_for_two_days() {
    assertThat(Main.generateSessions(2)).containsExactly(
        new Session(1, SessionType.BEFORE_NOON),
        new Session(1, SessionType.AFTER_NOON),
        new Session(2, SessionType.BEFORE_NOON),
        new Session(2, SessionType.AFTER_NOON));
  }

  @Test
  void should_read_empty_talks(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("input.txt");
    FileUtils.writeLines(file.toFile(), List.of());
    assertThat(Main.readTalks(file.toString())).isEmpty();
  }

  @Test
  void should_read_talks(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("input.txt");
    FileUtils.writeLines(file.toFile(), List.of(
        "Writing Fast Tests Against Enterprise Rails 60min",
        "Overdoing it in Python 45min"));

    assertThat(Main.readTalks(file.toString())).containsExactly(
        new Talk("Writing Fast Tests Against Enterprise Rails 60min", 60),
        new Talk("Overdoing it in Python 45min", 45));
  }


  @Test
  void should_render_empty() {
    List<PlannedSession> planned = List.of(
        new PlannedSession(new Session(1, SessionType.BEFORE_NOON), talks()),
        new PlannedSession(new Session(1, SessionType.AFTER_NOON), talks()));

    assertThat(Main.renderPlanned(planned)).isEqualTo("""
        Track 1
        12:00PM Lunch
        04:00PM Networking Event

        """);
  }

  @Test
  void should_render_rare() {
    List<PlannedSession> planned = List.of(
        new PlannedSession(new Session(1, SessionType.BEFORE_NOON), talks(60, 75, 10)),
        new PlannedSession(new Session(1, SessionType.AFTER_NOON), talks(5, 5, 5)));

    assertThat(Main.renderPlanned(planned)).isEqualTo("""
        Track 1
        09:00AM Talk 1 60min
        10:00AM Talk 2 75min
        11:15AM Talk 3 10min
        12:00PM Lunch
        01:00PM Talk 1 5min
        01:05PM Talk 2 5min
        01:10PM Talk 3 5min
        04:00PM Networking Event

        """);
  }

  @Test
  void should_render_multi_day() {
    List<PlannedSession> planned = List.of(
        new PlannedSession(new Session(1, SessionType.BEFORE_NOON), talks(60, 75, 10, 20)),
        new PlannedSession(new Session(1, SessionType.AFTER_NOON), talks(5, 5, 5, 200)),
        new PlannedSession(new Session(2, SessionType.BEFORE_NOON), talks(60, 60)),
        new PlannedSession(new Session(2, SessionType.AFTER_NOON), talks(60, 90, 63)));

    assertThat(Main.renderPlanned(planned)).isEqualTo("""
          Track 1
          09:00AM Talk 1 60min
          10:00AM Talk 2 75min
          11:15AM Talk 3 10min
          11:25AM Talk 4 20min
          12:00PM Lunch
          01:00PM Talk 1 5min
          01:05PM Talk 2 5min
          01:10PM Talk 3 5min
          01:15PM Talk 4 200min
          04:35PM Networking Event
          
          Track 2
          09:00AM Talk 1 60min
          10:00AM Talk 2 60min
          12:00PM Lunch
          01:00PM Talk 1 60min
          02:00PM Talk 2 90min
          03:30PM Talk 3 63min
          04:33PM Networking Event
  
          """);
  }
}
