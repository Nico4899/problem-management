package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter;

import java.util.Collection;

/**
 * This interface describes a generic problem management service filter.
 *
 * @param <T> the object type to filter a collection of
 */
public interface Filter<T> {
  /**
   * Filters a given collection by filter values.
   *
   * @param collection collection to be filtered
   * @param values filter values to filter with
   * @return filtered collection
   */
  Collection<T> filter(Collection<T> collection, Collection<?> values);
}
