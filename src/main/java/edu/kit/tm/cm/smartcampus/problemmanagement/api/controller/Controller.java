package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.Service;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.utility.DataTransferUtils;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * This class represents the gRPC controller from server side. It provides clients with the problem
 * management application microservice api.
 */
@GrpcService
public class Controller extends ProblemManagementGrpc.ProblemManagementImplBase {

  private final Service service;

  /**
   * Constructs a new building management controller.
   *
   * @param service problem management service
   */
  @Autowired
  public Controller(Service service) {
    this.service = service;
  }

  @Override
  public void listProblems(
      ListProblemsRequest request, StreamObserver<ListProblemsResponse> responseObserver) {
    Collection<Problem> problems =
        DataTransferUtils.ServerRequestReader.readListProblemsRequest(request, service);
    ListProblemsResponse response = DataTransferUtils.ServerResponseWriter.writeListProblemsResponse(problems);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void listProblemsForUser(
    ListProblemsForUserRequest request, StreamObserver<ListProblemsResponse> responseObserver) {
    Collection<Problem> problems =
      DataTransferUtils.ServerRequestReader.readListProblemsForUserRequest(request, service);
    ListProblemsResponse response = DataTransferUtils.ServerResponseWriter.writeListProblemsResponse(problems);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getProblem(
      GetProblemRequest request, StreamObserver<GetProblemResponse> responseObserver) {
    Problem problem = DataTransferUtils.ServerRequestReader.readGetProblemRequest(request, service);
    GetProblemResponse response = DataTransferUtils.ServerResponseWriter.writeGetProblemResponse(problem);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void createProblem(
      CreateProblemRequest request, StreamObserver<CreateProblemResponse> responseObserver) {
    Problem problem = DataTransferUtils.ServerRequestReader.readCreateProblemRequest(request, service);
    CreateProblemResponse response = DataTransferUtils.ServerResponseWriter.writeCreateProblemResponse(problem);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void updateProblem(
      UpdateProblemRequest request, StreamObserver<UpdateProblemResponse> responseObserver) {
    Problem problem = DataTransferUtils.ServerRequestReader.readUpdateProblemRequest(request, service);
    UpdateProblemResponse response = DataTransferUtils.ServerResponseWriter.writeUpdateProblemResponse(problem);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void removeProblem(
      RemoveProblemRequest request, StreamObserver<RemoveProblemResponse> responseObserver) {
    DataTransferUtils.ServerRequestReader.readRemoveProblemRequest(request, service);
    RemoveProblemResponse response = DataTransferUtils.ServerResponseWriter.writeRemoveProblemResponse();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void changeState(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    Problem problem = DataTransferUtils.ServerRequestReader.readChangeStateRequest(request, service);
    ChangeStateResponse response = DataTransferUtils.ServerResponseWriter.writeChangeStateResponse(problem);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
