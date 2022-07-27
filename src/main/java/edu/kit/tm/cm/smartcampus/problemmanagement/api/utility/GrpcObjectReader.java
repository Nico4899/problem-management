package edu.kit.tm.cm.smartcampus.problemmanagement.api.utility;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration.ListSettings;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration.Settings;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.ProblemFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.ProblemSorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
   * @param grpcListSettings grpc problem list settings object.
   * @return model configuration object
   */
  public static Settings<Problem> read(GrpcListSettings grpcListSettings) {
    Map<Filter<Problem>, Collection<?>> filters = new HashMap<>();
    if (grpcListSettings.getSelection().getStateFilterSelected()) {
      filters.put(
          ProblemFilter.STATE_FILTER,
          grpcListSettings.getValues().getStatesList().stream()
              .map(GrpcObjectReader::read)
              .toList());
    }
    if (grpcListSettings.getSelection().getStateFilterSelected()) {
      filters.put(ProblemFilter.REPORTER_FILTER, grpcListSettings.getValues().getReportersList());
    }
    ListSettings<Problem> settings = new ListSettings<>();
    settings.setFilters(filters);
    settings.setSorter(read(grpcListSettings.getSelection().getGrpcSortOption()));
    return settings;
  }

  /**
   * Read a grpc object and return a model object.
   *
   * @param grpcSortOption grpc problem sort option.
   * @return model problem sorter object
   */
  public static Sorter<Problem> read(GrpcSortOption grpcSortOption) {
    return Enum.valueOf(ProblemSorter.class, grpcSortOption.name());
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
