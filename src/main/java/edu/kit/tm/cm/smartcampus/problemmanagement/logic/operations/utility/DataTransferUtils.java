package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.utility;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.dto.ClientCreateNotificationRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.dto.ClientUpdateNotificationRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto.ClientCreateProblemRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto.ClientUpdateProblemRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.Service;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.ProblemFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.settings.ListSettings;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.settings.Settings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a utility class for this service, it contains of nested utilities, such as
 * {@link ServerResponseWriter}, {@link ServerRequestReader} or {@link ClientRequestWriter}. These
 * utilities use static methods to provide global service utility logic.
 *
 * @version 1.0
 * @author Bastian Bacher, Dennis Fadeev
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataTransferUtils {

  /**
   * This class represents a writing class which translates model objects to gRPC or REST data
   * transfer objects.
   *
   * @version 1.0
   * @author Bastian Bacher, Dennis Fadeev
   */
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ServerResponseWriter {

    /**
     * Write list problems response list problems response.
     *
     * @param problems the problems
     * @return the list problems response
     */
    public static ListProblemsResponse writeListProblemsResponse(Collection<Problem> problems) {
      return ListProblemsResponse.newBuilder()
          .addAllProblems(problems.stream().map(ServerResponseWriter::writeGrpcProblem).toList())
          .build();
    }

    /**
     * Write get problem response get problem response.
     *
     * @param problem the problem
     * @return the get problem response
     */
    public static GetProblemResponse writeGetProblemResponse(Problem problem) {
      return GetProblemResponse.newBuilder().setProblem(writeGrpcProblem(problem)).build();
    }

    /**
     * Write create problem response create problem response.
     *
     * @param problem the problem
     * @return the create problem response
     */
    public static CreateProblemResponse writeCreateProblemResponse(Problem problem) {
      return CreateProblemResponse.newBuilder().setProblem(writeGrpcProblem(problem)).build();
    }

    /**
     * Write update problem response update problem response.
     *
     * @param problem the problem
     * @return the update problem response
     */
    public static UpdateProblemResponse writeUpdateProblemResponse(Problem problem) {
      return UpdateProblemResponse.newBuilder().setProblem(writeGrpcProblem(problem)).build();
    }

    /**
     * Write remove problem response remove problem response.
     *
     * @return the remove problem response
     */
    public static RemoveProblemResponse writeRemoveProblemResponse() {
      return RemoveProblemResponse.newBuilder().build();
    }

    /**
     * Write change state response change state response.
     *
     * @return the change state response
     */
    public static ChangeStateResponse writeChangeStateResponse() {
      return ChangeStateResponse.newBuilder().build();
    }

    private static GrpcProblem writeGrpcProblem(Problem problem) {
      return GrpcProblem.newBuilder()
          .setCreationTime(
              com.google.protobuf.Timestamp.newBuilder()
                  .setNanos(problem.getCreationTime().getNanos())
                  .build())
          .setLastModified(
              com.google.protobuf.Timestamp.newBuilder()
                  .setNanos(problem.getLastModifiedTime().getNanos())
                  .build())
          .setProblemDescription(problem.getDescription())
          .setProblemReporter(problem.getReporter())
          .setProblemTitle(problem.getTitle())
          .setReferenceIdentificationNumber(problem.getReferenceIdentificationNumber())
          .setProblemState(writeGrpcProblemState(problem.getState()))
          .setIdentificationNumber(problem.getIdentificationNumber())
          .addAllPossibleStateOperations(
              problem.getState().possibleOperations().stream()
                  .map(ServerResponseWriter::writeGrpcStateOperation)
                  .toList())
          .build();
    }

    private static GrpcStateOperation writeGrpcStateOperation(Problem.Operation stateOperation) {
      return Enum.valueOf(GrpcStateOperation.class, stateOperation.name());
    }

    private static GrpcProblemState writeGrpcProblemState(Problem.State problemState) {
      return Enum.valueOf(GrpcProblemState.class, problemState.name());
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
  public static class ServerRequestReader {

    /**
     * Read list problems request collection.
     *
     * @param listProblemsRequest the list problems request
     * @param service the service
     * @return the collection
     */
    public static Collection<Problem> readListProblemsRequest(
        ListProblemsRequest listProblemsRequest, Service service) {
      return service.listProblems(
          readGrpcFilterSelection(listProblemsRequest.getGrpcFilterValueSelection()));
    }

    /**
     * Read list problems for user request collection.
     *
     * @param listProblemsRequest the list problems request
     * @param service the service
     * @return the collection
     */
    public static Collection<Problem> readListProblemsForUserRequest(
        ListProblemsForUserRequest listProblemsRequest, Service service) {
      return service.listProblemsForUser(
          readGrpcFilterSelection(listProblemsRequest.getGrpcFilterValueSelection()),
          listProblemsRequest.getReporter());
    }

    /**
     * Read get problem request problem.
     *
     * @param getProblemRequest the get problem request
     * @param service the service
     * @return the problem
     */
    public static Problem readGetProblemRequest(
        GetProblemRequest getProblemRequest, Service service) {
      return service.getProblem(getProblemRequest.getIdentificationNumber());
    }

    /**
     * Read create problem request problem.
     *
     * @param createProblemRequest the create problem request
     * @param service the service
     * @return the problem
     */
    public static Problem readCreateProblemRequest(
        CreateProblemRequest createProblemRequest, Service service) {
      Problem problem = new Problem();
      problem.setReporter(createProblemRequest.getProblemReporter());
      problem.setDescription(createProblemRequest.getProblemDescription());
      problem.setTitle(createProblemRequest.getProblemTitle());
      problem.setReferenceIdentificationNumber(
          createProblemRequest.getReferenceIdentificationNumber());
      return service.createProblem(problem);
    }

    /**
     * Read update problem request problem.
     *
     * @param clientUpdateProblemRequest the client update problem request
     * @param service the service
     * @return the problem
     */
    public static Problem readUpdateProblemRequest(
        UpdateProblemRequest clientUpdateProblemRequest, Service service) {
      Problem problem = new Problem();
      problem.setTitle(clientUpdateProblemRequest.getProblemTitle());
      problem.setReporter(clientUpdateProblemRequest.getProblemReporter());
      problem.setDescription(clientUpdateProblemRequest.getProblemDescription());
      problem.setReferenceIdentificationNumber(
          clientUpdateProblemRequest.getReferenceIdentificationNumber());
      problem.setIdentificationNumber(clientUpdateProblemRequest.getIdentificationNumber());
      return service.updateProblem(problem);
    }

    /**
     * Read remove problem request.
     *
     * @param removeProblemRequest the remove problem request
     * @param service the service
     */
    public static void readRemoveProblemRequest(
        RemoveProblemRequest removeProblemRequest, Service service) {
      service.removeProblem(removeProblemRequest.getIdentificationNumber());
    }

    /**
     * Read change state request.
     *
     * @param changeStateRequest the change state request
     * @param service the service
     */
    public static void readChangeStateRequest(
        ChangeStateRequest changeStateRequest, Service service) {
      service.changeState(
          changeStateRequest.getIdentificationNumber(),
          readGrpcProblemStateOperation(changeStateRequest.getGrpcStateOperation()));
    }

    private static Settings<Problem> readGrpcFilterSelection(
        GrpcFilterValueSelection grpcFilterValueSelection) {
      Map<Filter<Problem>, Collection<?>> filters = new HashMap<>();
      if (!grpcFilterValueSelection.getStatesList().isEmpty()) {
        filters.put(
            ProblemFilter.STATE_FILTER,
            grpcFilterValueSelection.getStatesList().stream()
                .map(ServerRequestReader::readGrpcProblemState)
                .toList());
      }
      ListSettings<Problem> settings = new ListSettings<>();
      settings.setFilters(filters);
      return settings;
    }

    private static Problem.State readGrpcProblemState(GrpcProblemState grpcProblemState) {
      return Enum.valueOf(Problem.State.class, grpcProblemState.name());
    }

    private static Problem.Operation readGrpcProblemStateOperation(
        GrpcStateOperation grpcStateOperation) {
      return Enum.valueOf(Problem.Operation.class, grpcStateOperation.name());
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
  public static class ClientRequestWriter {

    /**
     * Write create notification request client create notification request.
     *
     * @param problem the problem
     * @return the client create notification request
     */
    public static ClientCreateNotificationRequest writeCreateNotificationRequest(Problem problem) {
      ClientCreateNotificationRequest clientCreateNotificationRequest =
          new ClientCreateNotificationRequest();
      clientCreateNotificationRequest.setTitle(problem.getTitle());
      clientCreateNotificationRequest.setParentIdentificationNumber(
          problem.getReferenceIdentificationNumber());
      clientCreateNotificationRequest.setDescription(problem.getDescription());
      return clientCreateNotificationRequest;
    }

    /**
     * Write create problem request client create problem request.
     *
     * @param problem the problem
     * @return the client create problem request
     */
    public static ClientCreateProblemRequest writeCreateProblemRequest(Problem problem) {
      ClientCreateProblemRequest clientCreateProblemRequest = new ClientCreateProblemRequest();
      clientCreateProblemRequest.setTitle(problem.getTitle());
      clientCreateProblemRequest.setReporter(problem.getReporter());
      clientCreateProblemRequest.setDescription(problem.getDescription());
      clientCreateProblemRequest.setReferenceIdentificationNumber(
          problem.getReferenceIdentificationNumber());
      clientCreateProblemRequest.setNotificationIdentificationNumber(
          problem.getNotificationIdentificationNumber());
      return clientCreateProblemRequest;
    }

    /**
     * Write update problem request client update problem request.
     *
     * @param problem the problem
     * @return the client update problem request
     */
    public static ClientUpdateProblemRequest writeUpdateProblemRequest(Problem problem) {
      ClientUpdateProblemRequest clientUpdateProblemRequest = new ClientUpdateProblemRequest();
      clientUpdateProblemRequest.setTitle(problem.getTitle());
      clientUpdateProblemRequest.setDescription(problem.getDescription());
      clientUpdateProblemRequest.setReporter(problem.getReporter());
      clientUpdateProblemRequest.setReferenceIdentificationNumber(
          problem.getReferenceIdentificationNumber());
      clientUpdateProblemRequest.setNotificationIdentificationNumber(
          problem.getNotificationIdentificationNumber());
      clientUpdateProblemRequest.setState(problem.getState());
      clientUpdateProblemRequest.setIdentificationNumber(problem.getIdentificationNumber());
      return clientUpdateProblemRequest;
    }

    /**
     * Write update notification request client update notification request.
     *
     * @param problem the problem
     * @return the client update notification request
     */
    public static ClientUpdateNotificationRequest writeUpdateNotificationRequest(Problem problem) {
      ClientUpdateNotificationRequest clientUpdateNotificationRequest =
          new ClientUpdateNotificationRequest();
      clientUpdateNotificationRequest.setTitle(problem.getTitle());
      clientUpdateNotificationRequest.setDescription(problem.getDescription());
      clientUpdateNotificationRequest.setParentIdentificationNumber(
          problem.getReferenceIdentificationNumber());
      clientUpdateNotificationRequest.setIdentificationNumber(
          problem.getNotificationIdentificationNumber());
      return clientUpdateNotificationRequest;
    }
  }
}
