package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter;

import lombok.NonNull;

import java.util.Collection;

/**
 * This interface describes a generic problem management service filter.
 *
 * @param <T> the object type to filter a collection of
 * @param <S> the object type to filter for
 */
public interface Filter<T, S> {
  /**
   * Filters a given collection by filter values.
   *
   * @param collection collection to be filtered
   * @return filtered collection
   */
  Collection<T> filter(Collection<T> collection);

  /**
   * Adds filter values to filter. If not used, filter values are all type constants.
   *
   * @param filterValues filter values to be filtered by
   */
  void setFilterValues(Collection<S> filterValues);
}
