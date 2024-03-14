package com.sebi88;

import static com.sebi88.TestUtils.session;
import static com.sebi88.TestUtils.talks;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
public class PlanTest {

  @Test
  void should_be_equual() {
    assertThat(new Plan(session(1, 37), new HashSet<>(talks(20, 30))))
        .isEqualTo(new Plan(session(1, 37), new ArrayList<>((talks(20, 30)))));
  }
}
