package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * This class represents an implementation of {@link Filter}, it filters a collection of {@link
 * Problem} by {@link ProblemState}.
 */
@AllArgsConstructor
public class ProblemStateFilter implements Filter<Problem> {

  private final Collection<ProblemState> filterValues;

  @Override
  public Collection<Problem> filter(final Collection<Problem> collection) {
    return collection.stream()
        .filter(problem -> this.filterValues.contains(problem.getProblemState()))
        .toList();
  }
}
