package edu.kit.tm.cm.smartcampus.problemmanagement.api.utility;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration.Configuration;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration.ListConfiguration;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.ProblemReporterFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.ProblemStateFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters.AscendingTimeStampProblemSorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters.DefaultSorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.sorters.DescendingTimeStampProblemSorter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

/** This class provides a collection of logic methods to translate grpc objects to model objects. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GrpcObjectReader {

  /**
   * Read a grpc object and return a model object.
   *
   * @param grpcProblem grpc problem object.
   * @return model problem object
   */
  public static Problem read(GrpcProblem grpcProblem) {
    Problem problem = new Problem();
    problem.setTitle(grpcProblem.getProblemTitle());
    problem.setReporter(grpcProblem.getProblemReporter());
    problem.setDescription(grpcProblem.getProblemDescription());
    problem.setCreationTime(new Timestamp(grpcProblem.getCreationTime().getNanos()));
    problem.setReferenceIdentificationNumber(grpcProblem.getReferenceIdentificationNumber());
    problem.setState(read(grpcProblem.getProblemState()));
    return problem;
  }

  /**
   * Read a grpc object and return a model object.
   *
   * @param listProblemConfiguration grpc problem list configuration object.
   * @return model configuration object
   */
  public static Configuration<Problem> read(ListProblemConfiguration listProblemConfiguration) {
    Collection<Filter<Problem>> filters = new ArrayList<>();
    if (listProblemConfiguration.getProblemStateFilterMapping().getSelected()) {
      filters.add(new ProblemStateFilter(listProblemConfiguration.getProblemStateFilterMapping().getProblemStateList().stream().map(GrpcObjectReader::read).toList()));
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
  public static Sorter<Problem> read(GrpcSortOption grpcSortOption) {
    return switch (grpcSortOption) {
      case ASCENDING_TIME_STAMP -> new AscendingTimeStampProblemSorter();
      case DESCENDING_TIME_STAMP -> new DescendingTimeStampProblemSorter();
      default -> new DefaultSorter<>();
    };
  }

  /**
   * Read a grpc object and return a model object.
   *
   * @param grpcProblemState grpc problem state object.
   * @return model problem state object
   */
  public static Problem.State read(GrpcProblemState grpcProblemState) {
    return Enum.valueOf(Problem.State.class, grpcProblemState.name());
  }

  /**
   * Read a grpc object and return a model object.
   *
   * @param grpcStateOperation grpc problem state operation object.
   * @return model problem state operation object
   */
  public static Problem.State.Operation read(GrpcStateOperation grpcStateOperation) {
    return Enum.valueOf(Problem.State.Operation.class, grpcStateOperation.name());
  }
}
