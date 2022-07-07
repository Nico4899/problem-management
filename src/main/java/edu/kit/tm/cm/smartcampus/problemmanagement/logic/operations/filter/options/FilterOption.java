package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

/**
 * This class describes a generic filter option, is contains of a boolean to give information about
 * selection status and a collection of filter values.
 *
 * @param <T> the type of the filtwer values
 */
@Data
@AllArgsConstructor
@Builder
public class FilterOption<T> {
  private boolean selected;
  private Collection<T> filterValues;
}
