package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import edu.kit.tm.cm.proto.*;
import io.grpc.stub.StreamObserver;

public class ProblemManagementController extends ProblemManagementGrpc.ProblemManagementImplBase {
    public ProblemManagementController() {
    }

    @Override
    public void get(IN request, StreamObserver<Problem> responseObserver) {
        super.get(request, responseObserver);
    }

    @Override
    public void getProblems(Empty request, StreamObserver<Problems> responseObserver) {
        super.getProblems(request, responseObserver);
    }

    @Override
    public void getFilteredProblems(FilterValues request, StreamObserver<Problems> responseObserver) {
        super.getFilteredProblems(request, responseObserver);
    }

    @Override
    public void add(Problem request, StreamObserver<Empty> responseObserver) {
        super.add(request, responseObserver);
    }

    @Override
    public void update(Problem request, StreamObserver<Problem> responseObserver) {
        super.update(request, responseObserver);
    }

    @Override
    public void delete(IN request, StreamObserver<Empty> responseObserver) {
        super.delete(request, responseObserver);
    }

    @Override
    public void accept(Problem request, StreamObserver<Empty> responseObserver) {
        super.accept(request, responseObserver);
    }

    @Override
    public void decline(Problem request, StreamObserver<Empty> responseObserver) {
        super.decline(request, responseObserver);
    }

    @Override
    public void close(Problem request, StreamObserver<Empty> responseObserver) {
        super.close(request, responseObserver);
    }

    @Override
    public void approach(Problem request, StreamObserver<Empty> responseObserver) {
        super.approach(request, responseObserver);
    }

    @Override
    public void hold(Problem request, StreamObserver<Empty> responseObserver) {
        super.hold(request, responseObserver);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
