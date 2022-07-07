package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters;


/**
 * This interface describes a generic problem management filter for problem collections.
 *
 * @param <T> the object type to filter a collection of
 */
public interface Filter<T> {
  void filter();
}
