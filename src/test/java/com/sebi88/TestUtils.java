package com.sebi88;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.sebi88.Session.SessionType;

public class TestUtils {

  public static Session session(int length) {
    return session(-1, length);
  }

  public static Session session(int trackId, int length) {
    return new Session(trackId, SessionType.BEFORE_NOON, LocalTime.MIN, length);
  }

  public static List<Session> sessions(int... lengths) {
    AtomicInteger counter = new AtomicInteger();
    return Arrays.stream(lengths)
        .mapToObj(length -> session(counter.incrementAndGet(), length))
        .toList();
  }

  public static List<Talk> talks(int... lengths) {
    AtomicInteger counter = new AtomicInteger();
    return Arrays.stream(lengths)
        .mapToObj(length -> new Talk("Talk " + counter.incrementAndGet() + " " + length + "min", length))
        .toList();
  }

}
