package com.sebi88;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import com.sebi88.Session.SessionType;

public class PlannedSessionTest {

  @Test
  void should_be_equual() {
    assertThat(new PlannedSession(1, SessionType.BEFORE_NOON, 37, List.of(new Talk("Talk 1", 60), new Talk("Talk 2", 60))))
        .isEqualTo(new PlannedSession(1, SessionType.BEFORE_NOON, 37, Set.of(new Talk("Talk 2", 60), new Talk("Talk 1", 60))));
  }
}
