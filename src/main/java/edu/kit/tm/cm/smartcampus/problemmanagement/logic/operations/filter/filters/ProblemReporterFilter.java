package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import lombok.AllArgsConstructor;

import java.util.Collection;

/**
 * This class represents an implementation of {@link Filter}, it filters a collection of {@link
 * Problem} by problem reporter.
 */
@AllArgsConstructor
public class ProblemReporterFilter implements Filter<Problem> {

  private final String problemReporter;

  @Override
  public Collection<Problem> filter(final Collection<Problem> collection) {
    return collection.stream()
        .filter(problem -> problem.getReporter().equals(this.problemReporter))
        .toList();
  }
}
