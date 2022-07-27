package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;

import java.util.Collection;
import java.util.Comparator;

public enum ProblemSorter implements Sorter<Problem> {
  DEFAULT_SORTER {
    @Override
    public Collection<Problem> sort(Collection<Problem> collection) {
      return collection;
    }
  },
  ASCENDING_TIME_STAMP_SORTER {
    @Override
    public Collection<Problem> sort(Collection<Problem> collection) {
      return collection.stream()
          .sorted(Comparator.comparing(Problem::getCreationTime).reversed())
          .toList();
    }
  },
  DESCENDING_TIME_STAMP_SORTER {
    @Override
    public Collection<Problem> sort(Collection<Problem> collection) {
      return collection.stream().sorted(Comparator.comparing(Problem::getCreationTime)).toList();
    }
  }
}
