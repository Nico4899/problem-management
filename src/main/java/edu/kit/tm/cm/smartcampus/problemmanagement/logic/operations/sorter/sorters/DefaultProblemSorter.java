package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * This class represents an implementation of {@link Sorter} for {@link Problem}, it doesn't sort
 * anything.
 */
@AllArgsConstructor
public class DefaultProblemSorter implements Sorter<Problem> {

  @Override
  public Collection<Problem> sort(Collection<Problem> collection) {
    return collection;
  }
}
