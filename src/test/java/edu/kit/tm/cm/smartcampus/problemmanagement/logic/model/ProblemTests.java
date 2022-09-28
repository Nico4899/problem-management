package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.ClientBuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.dto.ClientCreateNotificationRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.ClientProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto.ClientUpdateProblemRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProblemTests {

  public static final String OPEN = "OPEN";
  public static final String CLOSED = "CLOSED";
  public static final String DECLINED = "DECLINED";
  public static final String IN_PROGRESS = "IN_PROGRESS";
  public static final String ACCEPTED = "ACCEPTED";
  private static final BuildingConnector buildingConnector =
      Mockito.mock(ClientBuildingConnector.class);
  private static final ProblemConnector problemConnector =
      Mockito.mock(ClientProblemConnector.class);
  private static Map<String, Problem> testProblemsMap;
  private static Problem testProblem;
  private static Notification testNotification;

  @BeforeAll
  public static void BeforeAll() {
    initializeResources();
    when(buildingConnector.createNotification(any(ClientCreateNotificationRequest.class)))
        .thenReturn(testNotification);
    when(problemConnector.updateProblem(any(ClientUpdateProblemRequest.class))).thenReturn(testProblem);
  }

  private static void initializeResources() {
    testProblem = new Problem();
    testProblem.setCreationTime(new Timestamp(1));
    testProblem.setReferenceIdentificationNumber("b-1");
    testProblem.setState(Problem.State.OPEN);
    testProblem.setDescription("");
    testProblem.setReporter("");
    testProblem.setTitle("");
    testProblem.setIdentificationNumber("p-1");
    testProblem.setNotificationIdentificationNumber("n-1");

    testNotification = new Notification();
    testNotification.setParentIdentificationNumber(testProblem.getReferenceIdentificationNumber());
    testNotification.setCreationTime(testProblem.getCreationTime());
    testNotification.setDescription(testProblem.getDescription());
    testNotification.setTitle(testProblem.getTitle());
    testNotification.setIdentificationNumber("n-1");

    Problem problem1 = new Problem();
    problem1.setState(Problem.State.OPEN);
    Problem problem2 = new Problem();
    problem2.setState(Problem.State.CLOSED);
    Problem problem3 = new Problem();
    problem3.setState(Problem.State.DECLINED);
    Problem problem4 = new Problem();
    problem4.setState(Problem.State.IN_PROGRESS);
    Problem problem5 = new Problem();
    problem5.setState(Problem.State.ACCEPTED);

    testProblemsMap = new HashMap<>();
    testProblemsMap.put(OPEN, problem1);
    testProblemsMap.put(CLOSED, problem2);
    testProblemsMap.put(DECLINED, problem3);
    testProblemsMap.put(IN_PROGRESS, problem4);
    testProblemsMap.put(ACCEPTED, problem5);
  }

  private static void reset() {
    for (Map.Entry<String, Problem> entry : testProblemsMap.entrySet()) {
      entry.getValue().setState(Problem.State.valueOf(entry.getKey()));
    }
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemStateChangeValidArgumentProvider.class)
  void whenValidOperation_thenExecuteStateChange(
      Problem problem, Problem.Operation operation, Problem.State expected) {
    reset();
    operation.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(expected, problem.getState());
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemStateChangeInvalidArgumentProvider.class)
  void whenInvalidOperation_thenThrowsInvalidStateChangeRequestException(
      Problem problem, Problem.Operation operation) {
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> operation.apply(problem, buildingConnector, problemConnector));
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemStateArgumentsProvider.class)
  void whenFilterProblemsByFilterValues_thenFilterRooms(
      Collection<Problem.Operation> expected, Problem.State problemState) {
    Assertions.assertTrue(problemState.possibleOperations().containsAll(expected));
  }

  private static class ProblemStateChangeInvalidArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(testProblemsMap.get(ACCEPTED), Problem.Operation.ACCEPT),
          Arguments.of(testProblemsMap.get(ACCEPTED), Problem.Operation.CLOSE),
          Arguments.of(testProblemsMap.get(ACCEPTED), Problem.Operation.HOLD),
          Arguments.of(testProblemsMap.get(DECLINED), Problem.Operation.APPROACH),
          Arguments.of(testProblemsMap.get(DECLINED), Problem.Operation.HOLD),
          Arguments.of(testProblemsMap.get(DECLINED), Problem.Operation.DECLINE),
          Arguments.of(testProblemsMap.get(IN_PROGRESS), Problem.Operation.APPROACH),
          Arguments.of(testProblemsMap.get(IN_PROGRESS), Problem.Operation.ACCEPT),
          Arguments.of(testProblemsMap.get(IN_PROGRESS), Problem.Operation.DECLINE),
          Arguments.of(testProblemsMap.get(OPEN), Problem.Operation.CLOSE),
          Arguments.of(testProblemsMap.get(OPEN), Problem.Operation.APPROACH),
          Arguments.of(testProblemsMap.get(OPEN), Problem.Operation.HOLD),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.Operation.ACCEPT),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.Operation.DECLINE),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.Operation.CLOSE),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.Operation.APPROACH),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.Operation.HOLD));
    }
  }

  private static class ProblemStateChangeValidArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(
              testProblemsMap.get(ACCEPTED),
              Problem.Operation.DECLINE,
              Problem.State.DECLINED),
          Arguments.of(
              testProblemsMap.get(ACCEPTED),
              Problem.Operation.APPROACH,
              Problem.State.IN_PROGRESS),
          Arguments.of(
              testProblemsMap.get(DECLINED),
              Problem.Operation.ACCEPT,
              Problem.State.ACCEPTED),
          Arguments.of(
              testProblemsMap.get(DECLINED), Problem.Operation.CLOSE, Problem.State.CLOSED),
          Arguments.of(
              testProblemsMap.get(IN_PROGRESS),
              Problem.Operation.HOLD,
              Problem.State.ACCEPTED),
          Arguments.of(
              testProblemsMap.get(IN_PROGRESS),
              Problem.Operation.CLOSE,
              Problem.State.CLOSED),
          Arguments.of(
              testProblemsMap.get(OPEN), Problem.Operation.ACCEPT, Problem.State.ACCEPTED),
          Arguments.of(
              testProblemsMap.get(OPEN), Problem.Operation.DECLINE, Problem.State.DECLINED));
    }
  }

  private static class ProblemStateArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(
              List.of(Problem.Operation.DECLINE, Problem.Operation.ACCEPT),
              Problem.State.OPEN),
          Arguments.of(
              List.of(Problem.Operation.DECLINE, Problem.Operation.APPROACH),
              Problem.State.ACCEPTED),
          Arguments.of(
              List.of(Problem.Operation.CLOSE, Problem.Operation.ACCEPT),
              Problem.State.DECLINED),
          Arguments.of(
              List.of(Problem.Operation.CLOSE, Problem.Operation.HOLD),
              Problem.State.IN_PROGRESS),
          Arguments.of(List.of(), Problem.State.CLOSED));
    }
  }
}
