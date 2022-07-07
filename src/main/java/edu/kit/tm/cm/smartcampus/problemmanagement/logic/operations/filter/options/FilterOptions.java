package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This class describes an object containing of filter options for the problem management service
 * filter system. It also handles their execution.
 */
@Data
@AllArgsConstructor
@Builder
public class FilterOptions {
  private FilterOption<ProblemState> problemStateFilterOption;
}
