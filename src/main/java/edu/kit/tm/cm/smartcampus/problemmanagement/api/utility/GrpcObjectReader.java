package edu.kit.tm.cm.smartcampus.problemmanagement.api.utility;

import edu.kit.tm.cm.proto.GrpcProblem;
import edu.kit.tm.cm.proto.GrpcProblemState;
import edu.kit.tm.cm.proto.GrpcSortOption;
import edu.kit.tm.cm.proto.ListProblemConfiguration;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration.Configuration;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration.ListConfiguration;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.ProblemReporterFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.ProblemStateFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters.AscendingTimeStampProblemSorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters.DefaultProblemSorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters.DescendingTimeStampProblemSorter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

/** This class provides a collection of logic methods to translate grpc objects to model objects. */
@Component
@AllArgsConstructor
public class GrpcObjectReader {

  /**
   * Read a grpc object and return a model object.
   *
   * @param grpcProblem grpc problem object.
   * @return model problem object
   */
  public Problem read(GrpcProblem grpcProblem) {
    return Problem.builder()
        .problemReporter(grpcProblem.getProblemReporter())
        .problemState(read(grpcProblem.getProblemState()))
        .problemDescription(grpcProblem.getProblemDescription())
        .problemTitle(grpcProblem.getProblemTitle())
        .creationTime(new Timestamp(grpcProblem.getCreationTime().getNanos()))
        .referenceIdentificationNumber(grpcProblem.getReferenceIdentificationNumber())
        .build();
  }

  /**
   * Read a grpc object and return a model object.
   *
   * @param listProblemConfiguration grpc problem list configuration object.
   * @return model configuration object
   */
  public Configuration<Problem> read(ListProblemConfiguration listProblemConfiguration) {
    Collection<Filter<Problem>> filters = new ArrayList<>();
    if (listProblemConfiguration.getProblemStateFilterMapping().getSelected()) {
      filters.add(new ProblemStateFilter(listProblemConfiguration.getProblemStateFilterMapping().getProblemStateList().stream().map(this::read).toList()));
    }
    if (listProblemConfiguration.getProblemReporterFilterMapping().getSelected()) {
      filters.add(new ProblemReporterFilter(listProblemConfiguration.getProblemReporterFilterMapping().getProblemReporter()));
    }
    return new ListConfiguration<>(read(listProblemConfiguration.getGrpcSortOption()), filters);
  }

  /**
   * Read a grpc object and return a model object.
   *
   * @param grpcSortOption grpc problem sort option.
   * @return model problem sorter object
   */
  public Sorter<Problem> read(GrpcSortOption grpcSortOption) {
    return switch (grpcSortOption) {
      case ASCENDING_TIME_STAMP -> new AscendingTimeStampProblemSorter();
      case DESCENDING_TIME_STAMP -> new DescendingTimeStampProblemSorter();
      default -> new DefaultProblemSorter();
    };
  }

  /**
   * Read a grpc object and return a model object.
   *
   * @param grpcProblemState grpc problem state object.
   * @return model problem state object
   */
  public ProblemState read(GrpcProblemState grpcProblemState) {
    return ProblemState.forNumber(grpcProblemState.ordinal() + 1);
  }
}
