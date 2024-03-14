package com.sebi88;

import java.util.Collection;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;

public record Plan(Session session, Collection<Talk> talks) {
  
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    Plan that = (Plan) other;
    return Objects.equals(session, that.session)
        && CollectionUtils.isEqualCollection(talks, that.talks);
  }
  
  
}
