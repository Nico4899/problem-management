package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;

import java.util.Collection;

public enum ProblemFilter implements Filter<Problem> {
  REPORTER_FILTER {
    @Override
    public Collection<Problem> filter(Collection<Problem> collection, Collection<?> values) {
      return collection.stream().filter(problem -> values.contains(problem.getReporter())).toList();
    }
  },
  STATE_FILTER {
    @Override
    public Collection<Problem> filter(Collection<Problem> collection, Collection<?> values) {
      return collection.stream().filter(problem -> values.contains(problem.getState())).toList();
    }
  },
}
