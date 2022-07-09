package edu.kit.tm.cm.smartcampus.problemmanagement.api.utility;

import edu.kit.tm.cm.proto.GrpcProblem;
import edu.kit.tm.cm.proto.GrpcProblemState;
import edu.kit.tm.cm.proto.ProblemFilterOptions;
import edu.kit.tm.cm.proto.ProblemStateFilterMapping;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOption;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOptions;

import java.sql.Timestamp;

public final class GrpcObjectReader {

  private GrpcObjectReader() {}

  public static Problem read(GrpcProblem grpcProblem) {
    return Problem.builder()
        .problemReporter(grpcProblem.getProblemReporter())
        .problemState(read(grpcProblem.getProblemState()))
        .problemDescription(grpcProblem.getProblemDescription())
        .problemTitle(grpcProblem.getProblemTitle())
        .creationTime(new Timestamp(grpcProblem.getCreationTime().getNanos()))
        .referenceIdentificationNumber(grpcProblem.getReferenceIdentificationNumber())
        .build();
  }

  public static FilterOptions read(ProblemFilterOptions problemFilterOptions) {
    return FilterOptions.builder()
        .problemStateFilterOption(read(problemFilterOptions.getProblemStateFilterMapping()))
        .build();
  }

  public static FilterOption<ProblemState> read(
      ProblemStateFilterMapping problemStateFilterMapping) {
    return FilterOption.<ProblemState>builder()
        .selected(problemStateFilterMapping.getSelected())
        .filterValues(
            problemStateFilterMapping.getProblemStateList().stream()
                .map(GrpcObjectReader::read)
                .toList())
        .build();
  }

  public static ProblemState read(GrpcProblemState grpcProblemState) {
    return ProblemState.forNumber(grpcProblemState.ordinal() + 1);
  }
}
