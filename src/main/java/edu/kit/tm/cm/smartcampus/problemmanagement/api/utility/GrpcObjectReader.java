package edu.kit.tm.cm.smartcampus.problemmanagement.api.utility;

import edu.kit.tm.cm.proto.GrpcProblem;
import edu.kit.tm.cm.proto.GrpcProblemState;
import edu.kit.tm.cm.proto.ProblemFilterOptions;
import edu.kit.tm.cm.proto.ProblemStateFilterMapping;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOption;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOptions;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/** This class provides a collection of logic methods to translate grpc objects to model objects. */
@Component
public class GrpcObjectReader {

  /** Construct a new grpc object reader. */
  public GrpcObjectReader() {}

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
   * @param problemFilterOptions grpc problem filter options object.
   * @return model filter options object
   */
  public FilterOptions read(ProblemFilterOptions problemFilterOptions) {
    return FilterOptions.builder()
        .problemStateFilterOption(read(problemFilterOptions.getProblemStateFilterMapping()))
        .build();
  }

  /**
   * Read a grpc object and return a model object.
   *
   * @param problemStateFilterMapping grpc problem state filter mapping object.
   * @return model filter option object
   */
  public FilterOption<ProblemState> read(ProblemStateFilterMapping problemStateFilterMapping) {
    return FilterOption.<ProblemState>builder()
        .selected(problemStateFilterMapping.getSelected())
        .filterValues(
            problemStateFilterMapping.getProblemStateList().stream().map(this::read).toList())
        .build();
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
