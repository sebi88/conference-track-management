package com.sebi88;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TalkTest {

  @ValueSource(strings = {
      "Rails for Python Developers lightning",
      "Don't let the lighting hit you lightning"
  })
  @ParameterizedTest
  void should_parse_lightning_talks(String line) {
    Talk talk = Talk.parse(line);
    assertThat(talk.title()).isEqualTo(line);
    assertThat(talk.length()).isEqualTo(Talk.LIGHTNING_TALK_LENGTH);
  }

  static Stream<Arguments> talksWithMinutes() {
    return Stream.of(
        Arguments.of("Rails for Python Developers 1min", 1),
        Arguments.of("Rails for Python Developers 10min", 10),
        Arguments.of("Rails for Python Developers 100min", 100),
        Arguments.of("Rails lightning 99min", 99));
  }
  
  @MethodSource("talksWithMinutes")
  @ParameterizedTest
  void shoul_parse_talks_with_minutes(String line, int mins) {
    Talk talk = Talk.parse(line);
    assertThat(talk.title()).isEqualTo(line);
    assertThat(talk.length()).isEqualTo(mins);
  }
  
  @ValueSource(strings = {
      "Rails for Python Developers",
      "10 rules of programming 10min",
      "Don't let the lighting hit you"
  })
  @ParameterizedTest
  void should_throw_exception_for_invalid_talk(String line) {
    assertThatThrownBy(() -> Talk.parse(line))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid talk: " + line);
  }
}
