package com.sebi88;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.sebi88.Session.SessionType;

public class TestUtils {
  
  public static List<Session> sessions(int... lengths) {
    AtomicInteger counter = new AtomicInteger();
    return Arrays.stream(lengths)
        .mapToObj(length -> new Session(counter.incrementAndGet(), SessionType.BEFORE_NOON, length))
        .toList();
  }
  
  public static List<Talk> talks(int... lengths) {
    AtomicInteger counter = new AtomicInteger();
    return Arrays.stream(lengths)
        .mapToObj(length -> new Talk("Talk " + counter.incrementAndGet() + " " + length + "min", length))
        .toList();
  }

}
