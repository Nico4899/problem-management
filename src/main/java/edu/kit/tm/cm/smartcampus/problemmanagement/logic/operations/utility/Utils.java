package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.utility;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.ProblemFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.settings.ListSettings;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.settings.Settings;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.ProblemSorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a utility class for this service, it contains of nested utilities, such as
 * {@link Writer}, {@link Reader} or {@link Extractor}. These utilities use static methods to
 * provide global service utility logic.
 *
 * @version 1.0
 * @author Bastian Bacher, Dennis Fadeev
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

  /**
   * This class represents a writing class which translates model objects to gRPC or REST data
   * transfer objects.
   *
   * @version 1.0
   * @author Bastian Bacher, Dennis Fadeev
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Writer {

    /**
     * Write a model object to a grpc object.
     *
     * @param problem problem model object
     * @return grpc problem object
     */
    public static GrpcProblem write(Problem problem) {
      return GrpcProblem.newBuilder()
        .setCreationTime(
          com.google.protobuf.Timestamp.newBuilder()
            .setNanos(problem.getCreationTime().getNanos())
            .build())
        .setProblemDescription(problem.getDescription())
        .setProblemReporter(problem.getReporter())
        .setProblemTitle(problem.getTitle())
        .setReferenceIdentificationNumber(problem.getReferenceIdentificationNumber())
        .setProblemState(write(problem.getState()))
        .setIdentificationNumber(problem.getIdentificationNumber())
        .build();
    }

    /**
     * Write a model object to a grpc object.
     *
     * @param stateOperations problem state operations model object
     * @return grpc problem state operations object
     */
    public static Collection<GrpcStateOperation> writeStateOperations(
      Collection<Problem.State.Operation> stateOperations) {
      return stateOperations.stream().map(Utils.Writer::write).toList();
    }

    /**
     * Write a model object to a grpc object.
     *
     * @param stateOperation problem state operation model object
     * @return grpc problem state operation object
     */
    public static GrpcStateOperation write(Problem.State.Operation stateOperation) {
      return Enum.valueOf(GrpcStateOperation.class, stateOperation.name());
    }

    /**
     * Write a model object to a grpc object.
     *
     * @param problemState problem state model object
     * @return grpc problem state object
     */
    public static GrpcProblemState write(Problem.State problemState) {
      return Enum.valueOf(GrpcProblemState.class, problemState.name());
    }

    /**
     * Write a model object to a grpc object.
     *
     * @param problems problems model object
     * @return grpc problems object
     */
    public static Collection<GrpcProblem> writeProblems(Collection<Problem> problems) {
      return problems.stream().map(Utils.Writer::write).toList();
    }
  }

  /**
   * This class represents a reading class which translates gRPC or REST data transfer objects to
   * model objects.
   *
   * @version 1.0
   * @author Bastian Bacher, Dennis Fadeev
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Reader {

    /**
     * This method reads a {@link GrpcProblem} object and parses it into a {@link Problem} object.
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
            grpcListSettings.getValues().getStatesList().stream().map(Utils.Reader::read).toList());
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

  /**
   * This class represents an extractor class which extracts objects parsing them from one object to
   * another.
   *
   * @version 1.0
   * @author Bastian Bacher, Dennis Fadeev
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Extractor {

    /**
     * Extract {@link Notification} from a {@link Problem}, it extracts all fields except the
     * identification number, which has to be set later on by the building domain service.
     *
     * @param problem the problem to extract a notification from.
     * @return the extracted notification
     */
    public static Notification extract(Problem problem) {
      Notification notification = new Notification();
      notification.setTitle(problem.getTitle());
      notification.setParentIdentificationNumber(problem.getReferenceIdentificationNumber());
      notification.setCreationTime(problem.getCreationTime());
      notification.setDescription(problem.getDescription());
      return notification;
    }
  }
}

