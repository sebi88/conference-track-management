package com.sebi88;

import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import com.sebi88.Session.SessionType;

public record PlannedSession(int trackId, SessionType type,  int length, Collection<Talk> talks) {

  public PlannedSession(Session session, Collection<Talk> talks) {
    this(session.trackId(), session.type(), session.length(), talks);
  }
  
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    PlannedSession that = (PlannedSession) other;
    return trackId == that.trackId 
        && length == that.length
        && type == that.type && CollectionUtils.isEqualCollection(talks, that.talks);
  }
  
  public Session asSession() {
    return new Session(trackId, type, length);
  }
}
