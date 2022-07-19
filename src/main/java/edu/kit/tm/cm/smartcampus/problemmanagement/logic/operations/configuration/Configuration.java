package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration;

import java.util.Collection;

/**
 * The interface Configuration.
 *
 * @param <T> the type the collection contains
 */
public interface Configuration<T> {
  /**
   * Apply configurations to collection.
   *
   * @param collection the collection to be operated on
   * @return the operated collection
   */
  Collection<T> apply(Collection<T> collection);
}
