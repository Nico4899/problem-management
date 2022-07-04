package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.ProblemState;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;

@AllArgsConstructor
public class ProblemStateFilter implements Filter<Problem> {

  private Collection<Problem> collectionToFilter;
  private Collection<ProblemState> filterValues;

  @Override
  public Collection<Problem> filter() {
    Collection<Problem> filteredCollection = new LinkedList<>();
    for (Problem problem: collectionToFilter) {
      if (filterValues.contains(problem.getProblemState())) {
        filteredCollection.add(problem);
      }
    }
    return filteredCollection;
  }
}
