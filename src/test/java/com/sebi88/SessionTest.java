package com.sebi88;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import com.sebi88.Session.SessionType;

public class SessionTest {

  @Test
  void should_have_length_from_type() {
    assertThat(new Session(1, SessionType.BEFORE_NOON).length()).isEqualTo(SessionType.BEFORE_NOON.length());
    assertThat(new Session(1, SessionType.AFTER_NOON).length()).isEqualTo(SessionType.AFTER_NOON.length());
  }
}
