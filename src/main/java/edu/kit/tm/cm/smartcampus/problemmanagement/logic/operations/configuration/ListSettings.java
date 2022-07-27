package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * This class represents a list request configuration, implemented from {@link Settings}. It is
 * being parsed from a provided grpc object and can apply {@link Filter} and {@link Sorter} selected
 * in the settings to a given collection.
 *
 * @param <T> the type of the collection to be operated on
 */
@Setter
@NoArgsConstructor
public class ListSettings<T> implements Settings<T> {
  private Sorter<T> sorter;
  private Map<Filter<T>, Collection<?>> filters;

  @Override
  public Collection<T> apply(Collection<T> collection) {
    Collection<T> applied = new ArrayList<>(collection);
    for (Map.Entry<Filter<T>, Collection<?>> entry : filters.entrySet()) {
      applied = entry.getKey().filter(applied, entry.getValue());
    }
    applied = sorter.sort(applied);
    return applied;
  }
}
