package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.ProblemFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class ProblemFilterTests {

  public static final String OPEN = "open";
  public static final String CLOSED = "closed";
  public static final String DECLINED = "declined";
  public static final String IN_PROGRESS = "inProgress";
  public static final String ACCEPTED = "accepted";

  public static final String REPORTER_1 = "reporter1";
  public static final String REPORTER_2 = "reporter2";
  public static final String REPORTER_3 = "reporter3";
  public static final String REPORTER_4 = "reporter4";
  public static final String REPORTER_5 = "reporter5";

  private static final Collection<Problem.State> ALL_PROBLEM_STATES =
      List.of(Problem.State.values());
  private static final Collection<Problem.State> SOME_PROBLEM_STATES =
      List.of(Problem.State.ACCEPTED, Problem.State.DECLINED, Problem.State.IN_PROGRESS);
  private static final Collection<Problem.State> NO_PROBLEM_STATES = List.of();

  private static final Map<String, Problem> testProblemsMap = new HashMap<>();

  // test object collections
  private static Collection<Problem> testProblems;

  @BeforeAll
  static void setUp() {
    buildTestCollections();
  }

  private static void buildTestCollections() {

    Problem problem1 = new Problem();
    problem1.setState(Problem.State.OPEN);
    problem1.setReporter(REPORTER_1);
    Problem problem2 = new Problem();
    problem2.setState(Problem.State.CLOSED);
    problem2.setReporter(REPORTER_2);
    Problem problem3 = new Problem();
    problem3.setState(Problem.State.DECLINED);
    problem3.setReporter(REPORTER_3);
    Problem problem4 = new Problem();
    problem4.setState(Problem.State.IN_PROGRESS);
    problem4.setReporter(REPORTER_4);
    Problem problem5 = new Problem();
    problem5.setState(Problem.State.ACCEPTED);
    problem5.setReporter(REPORTER_5);

    testProblemsMap.put(OPEN, problem1);
    testProblemsMap.put(CLOSED, problem2);
    testProblemsMap.put(DECLINED, problem3);
    testProblemsMap.put(IN_PROGRESS, problem4);
    testProblemsMap.put(ACCEPTED, problem5);

    testProblems = testProblemsMap.values();
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemFilterArgumentsProvider.class)
  void whenFilter_thenFilter(
      Collection<Problem> expected,
      Filter<Problem> filter,
      Collection<Problem> collection,
      Collection<?> filterValues) {
    Assertions.assertTrue(expected.containsAll(filter.filter(collection, filterValues)));
  }

  private static class ProblemFilterArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(testProblems, ProblemFilter.STATE_FILTER, testProblems, ALL_PROBLEM_STATES),
          Arguments.of(
              List.of(
                  testProblemsMap.get(ACCEPTED),
                  testProblemsMap.get(DECLINED),
                  testProblemsMap.get(IN_PROGRESS)),
              ProblemFilter.STATE_FILTER,
              testProblems,
              SOME_PROBLEM_STATES),
          Arguments.of(List.of(), ProblemFilter.STATE_FILTER, testProblems, NO_PROBLEM_STATES),
          Arguments.of(List.of(), ProblemFilter.REPORTER_FILTER, testProblems, List.of()),
          Arguments.of(
              List.of(testProblemsMap.get(OPEN),
                testProblemsMap.get(CLOSED),
                testProblemsMap.get(ACCEPTED)),
              ProblemFilter.REPORTER_FILTER,
              testProblems,
              List.of(REPORTER_1, REPORTER_2, REPORTER_5)),
          Arguments.of(
              testProblems,
              ProblemFilter.REPORTER_FILTER,
              testProblems,
              List.of(REPORTER_1, REPORTER_2, REPORTER_3, REPORTER_4, REPORTER_5)));
    }
  }
}
