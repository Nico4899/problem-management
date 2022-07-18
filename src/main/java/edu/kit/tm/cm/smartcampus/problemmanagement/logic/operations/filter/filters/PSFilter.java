package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * This class represents an implementation of {@link Filter}, it filters a collection of {@link
 * Problem} by {@link ProblemState}. The short form "PSFilter" stands for
 * "ProblemStateFilter".
 */
@AllArgsConstructor
public class PSFilter implements Filter<Problem> {

  private Collection<ProblemState> filterValues;

  @Override
  public Collection<Problem> filter(Collection<Problem> collection) {
    return collection.stream()
        .filter(problem -> filterValues.contains(problem.getProblemState()))
        .toList();
  }

}
