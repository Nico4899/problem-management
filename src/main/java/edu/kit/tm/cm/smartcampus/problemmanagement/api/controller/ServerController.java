package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.Service;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.utility.Utils;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * This class represents the gRPC controller from server side. It provides clients with the problem
 * management application microservice api.
 */
@GrpcService
public class ServerController extends ProblemManagementGrpc.ProblemManagementImplBase {

  private final Service service;

  /**
   * Constructs a new building management controller.
   *
   * @param service problem management service
   */
  @Autowired
  public ServerController(Service service) {
    this.service = service;
  }

  @Override
  public void listProblems(
      ListProblemsRequest request, StreamObserver<ListProblemsResponse> responseObserver) {
    Collection<Problem> problems =
        Utils.ServerRequestReader.readListProblemsRequest(request, service);
    ListProblemsResponse response = Utils.ServerResponseWriter.writeListProblemsResponse(problems);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getProblem(
      GetProblemRequest request, StreamObserver<GetProblemResponse> responseObserver) {
    Problem problem = Utils.ServerRequestReader.readGetProblemRequest(request, service);
    GetProblemResponse response = Utils.ServerResponseWriter.writeGetProblemResponse(problem);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void createProblem(
      CreateProblemRequest request, StreamObserver<CreateProblemResponse> responseObserver) {
    Problem problem = Utils.ServerRequestReader.readCreateProblemRequest(request, service);
    CreateProblemResponse response = Utils.ServerResponseWriter.writeCreateProblemResponse(problem);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void updateProblem(
      UpdateProblemRequest request, StreamObserver<UpdateProblemResponse> responseObserver) {
    Problem problem = Utils.ServerRequestReader.readUpdateProblemRequest(request, service);
    UpdateProblemResponse response = Utils.ServerResponseWriter.writeUpdateProblemResponse(problem);
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void removeProblem(
      RemoveProblemRequest request, StreamObserver<RemoveProblemResponse> responseObserver) {
    Utils.ServerRequestReader.readRemoveProblemRequest(request, service);
    RemoveProblemResponse response = Utils.ServerResponseWriter.writeRemoveProblemResponse();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void changeState(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    Utils.ServerRequestReader.readChangeStateRequest(request, service);
    ChangeStateResponse response = Utils.ServerResponseWriter.writeChangeStateResponse();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
