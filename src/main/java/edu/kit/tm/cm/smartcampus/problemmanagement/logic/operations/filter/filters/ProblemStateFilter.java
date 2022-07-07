package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * This class represents an implementation of {@link Filter}, it filters a collection of problems by
 * problem state.
 */
@AllArgsConstructor
public class ProblemStateFilter implements Filter<Problem> {

  private Collection<Problem> collectionToFilter;
  private Collection<ProblemState> filterValues;

  @Override
  public void filter() {
    collectionToFilter.removeIf(problem -> !filterValues.contains(problem.getProblemState()));
  }
}
