package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.ProblemStateFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * This class describes an object containing of filter options for the problem management service
 * filter system. It also handles their execution.
 */
@Data
@AllArgsConstructor
@Builder
public class FilterOptions {
  private FilterOption<ProblemState> problemStateFilterOption;

  public void executeProblemFilter(Collection<Problem> collection) {
    if (problemStateFilterOption.isSelected()) {
      Filter<Problem> filter =
          new ProblemStateFilter(collection, problemStateFilterOption.getFilterValues());
      filter.filter();
    }
  }
}
