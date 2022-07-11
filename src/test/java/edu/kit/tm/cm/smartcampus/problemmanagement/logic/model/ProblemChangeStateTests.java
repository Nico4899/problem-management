package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidStateChangeRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class ProblemChangeStateTests {

  public static final String OPEN = "open";
  public static final String CLOSED = "closed";
  public static final String DECLINED = "declined";
  public static final String IN_PROGRESS = "inProgress";
  public static final String ACCEPTED = "accepted";

  private static final Map<String, Problem> testProblemsMap = new HashMap<>();

  // test object collections
  private static Collection<Problem> testProblems;

  private static void buildTestCollections() {

    testProblemsMap.put(
        OPEN, new Problem(ProblemState.OPEN, null, null, null, null, null, null, null));
    testProblemsMap.put(
        CLOSED, new Problem(ProblemState.CLOSED, null, null, null, null, null, null, null));
    testProblemsMap.put(
        DECLINED, new Problem(ProblemState.DECLINED, null, null, null, null, null, null, null));
    testProblemsMap.put(
        IN_PROGRESS,
        new Problem(ProblemState.IN_PROGRESS, null, null, null, null, null, null, null));
    testProblemsMap.put(
        ACCEPTED, new Problem(ProblemState.ACCEPTED, null, null, null, null, null, null, null));
  }

  @BeforeEach
  public void setUp() {
    buildTestCollections();
  }

  @Test
  void acceptedInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(ACCEPTED);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::accept);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::close);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::hold);
  }

  @Test
  void acceptedValidProblemChangeTest() {
    Problem problem = testProblemsMap.get(ACCEPTED);
    Assertions.assertEquals(ProblemState.ACCEPTED, problem.getProblemState());
    problem.approach();
    Assertions.assertEquals(ProblemState.IN_PROGRESS, problem.getProblemState());
    problem.setProblemState(ProblemState.ACCEPTED);
    Assertions.assertEquals(ProblemState.ACCEPTED, problem.getProblemState());
    problem.decline();
    Assertions.assertEquals(ProblemState.DECLINED, problem.getProblemState());
  }

  @Test
  void declinedInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(DECLINED);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::approach);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::decline);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::hold);
  }

  @Test
  void declinedValidProblemChangeTest() {
    Problem problem = testProblemsMap.get(DECLINED);
    Assertions.assertEquals(ProblemState.DECLINED, problem.getProblemState());
    problem.accept();
    Assertions.assertEquals(ProblemState.ACCEPTED, problem.getProblemState());
    problem.setProblemState(ProblemState.DECLINED);
    Assertions.assertEquals(ProblemState.DECLINED, problem.getProblemState());
    problem.close();
    Assertions.assertEquals(ProblemState.CLOSED, problem.getProblemState());
  }

  @Test
  void closedInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(CLOSED);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::accept);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::close);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::hold);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::decline);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::approach);
  }

  @Test
  void inProgressInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(IN_PROGRESS);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::accept);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::decline);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::approach);
  }

  @Test
  void inProgressValidProblemChangeTest() {
    Problem problem = testProblemsMap.get(IN_PROGRESS);
    Assertions.assertEquals(ProblemState.IN_PROGRESS, problem.getProblemState());
    problem.hold();
    Assertions.assertEquals(ProblemState.ACCEPTED, problem.getProblemState());
    problem.setProblemState(ProblemState.IN_PROGRESS);
    Assertions.assertEquals(ProblemState.IN_PROGRESS, problem.getProblemState());
    problem.close();
    Assertions.assertEquals(ProblemState.CLOSED, problem.getProblemState());
  }

  @Test
  void openInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(OPEN);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::approach);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::close);
    Assertions.assertThrows(InvalidStateChangeRequestException.class, problem::hold);
  }

  @Test
  void openValidProblemChangeTest() {
    Problem problem = testProblemsMap.get(OPEN);
    Assertions.assertEquals(ProblemState.OPEN, problem.getProblemState());
    problem.accept();
    Assertions.assertEquals(ProblemState.ACCEPTED, problem.getProblemState());
    problem.setProblemState(ProblemState.OPEN);
    Assertions.assertEquals(ProblemState.OPEN, problem.getProblemState());
    problem.decline();
    Assertions.assertEquals(ProblemState.DECLINED, problem.getProblemState());
  }
}
