package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents a list request configuration, implemented from {@link Configuration}. It is
 * being parsed from a provided grpc object and can apply {@link Filter} and {@link Sorter} selected
 * in the settings to a given collection.
 *
 * @param <T> the type of the collection to be operated on
 */
@AllArgsConstructor
public class ListConfiguration<T> implements Configuration<T> {
  private final Sorter<T> sorter;
  private final Collection<Filter<T>> filters;

  @Override
  public Collection<T> apply(Collection<T> collection) {
    Collection<T> applied = new ArrayList<>(collection);
    for (Filter<T> filter : filters) {
      applied = filter.filter(applied);
    }
    applied = sorter.sort(applied);
    return applied;
  }
}
