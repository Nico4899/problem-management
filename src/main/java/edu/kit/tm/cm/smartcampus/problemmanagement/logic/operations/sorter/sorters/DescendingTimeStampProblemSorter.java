package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Comparator;

/**
 * This class represents an implementation of {@link Sorter}, it sorts a collection of {@link
 * Problem} by newest time stamp.
 */
@AllArgsConstructor
public class DescendingTimeStampProblemSorter implements Sorter<Problem> {

  @Override
  public Collection<Problem> sort(Collection<Problem> collection) {
    return collection.stream().sorted(Comparator.comparing(Problem::getCreationTime)).toList();
  }
}
