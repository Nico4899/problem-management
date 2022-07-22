package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.api.utility.GrpcObjectReader;
import edu.kit.tm.cm.smartcampus.problemmanagement.api.utility.GrpcObjectWriter;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.Service;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.configuration.Configuration;
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
    ListProblemConfiguration listProblemConfiguration = request.getListProblemConfiguration();
    Configuration<Problem> configuration = GrpcObjectReader.read(listProblemConfiguration);
    Collection<Problem> problems = this.service.listProblems(configuration);
    Collection<GrpcProblem> grpcProblems = GrpcObjectWriter.writeProblems(problems);
    ListProblemsResponse response =
        ListProblemsResponse.newBuilder().addAllProblems(grpcProblems).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getProblem(
      GetProblemRequest request, StreamObserver<GetProblemResponse> responseObserver) {
    String identificationNumber = request.getIdentificationNumber();
    Problem problem = this.service.getProblem(identificationNumber);
    GrpcProblem grpcProblem = GrpcObjectWriter.write(problem);
    Collection<Problem.State.Operation> stateOperations =
        problem.getState().possibleOperations();
    Collection<GrpcStateOperation> grpcStateOperations =
        GrpcObjectWriter.writeStateOperations(stateOperations);
    GetProblemResponse response =
        GetProblemResponse.newBuilder()
            .setProblem(grpcProblem)
            .addAllPossibleStateOperations(grpcStateOperations)
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void createProblem(
      CreateProblemRequest request, StreamObserver<CreateProblemResponse> responseObserver) {
    GrpcProblem grpcRequestProblem = request.getProblem();
    Problem requestProblem = GrpcObjectReader.read(grpcRequestProblem);
    Problem responseProblem = this.service.createProblem(requestProblem);
    GrpcProblem grpcResponseProblem = GrpcObjectWriter.write(responseProblem);
    CreateProblemResponse response =
        CreateProblemResponse.newBuilder().setProblem(grpcResponseProblem).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void updateProblem(
      UpdateProblemRequest request, StreamObserver<UpdateProblemResponse> responseObserver) {
    String identificationNumber = request.getIdentificationNumber();
    GrpcProblem grpcRequestProblem = request.getProblem();
    Problem requestProblem = GrpcObjectReader.read(grpcRequestProblem);
    GrpcObjectReader.read(grpcRequestProblem);
    requestProblem.setIdentificationNumber(identificationNumber);
    Problem responseProblem = this.service.updateProblem(requestProblem);
    GrpcProblem grpcResponseProblem = GrpcObjectWriter.write(responseProblem);
    UpdateProblemResponse response =
        UpdateProblemResponse.newBuilder().setProblem(grpcResponseProblem).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void removeProblem(
      RemoveProblemRequest request, StreamObserver<RemoveProblemResponse> responseObserver) {
    String identificationNumber = request.getIdentificationNumber();
    this.service.removeProblem(identificationNumber);
    RemoveProblemResponse response = RemoveProblemResponse.newBuilder().build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void changeState(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    String identificationNumber = request.getIdentificationNumber();
    Problem.State.Operation operation = GrpcObjectReader.read(request.getGrpcStateOperation());
    this.service.changeState(identificationNumber, operation);
    ChangeStateResponse response = ChangeStateResponse.newBuilder().build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
