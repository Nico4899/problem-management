package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * This class represents an implementation of {@link Sorter} for type <T> it doesn't sort
 * anything.
 */
@AllArgsConstructor
public class DefaultSorter<T> implements Sorter<T> {

  @Override
  public Collection<T> sort(Collection<T> collection) {
    return collection;
  }
}
