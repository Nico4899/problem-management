package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.ProblemSorter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.sorter.Sorter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class SorterTests {

  public static final String OLDEST_PROBLEM_1 = "1_oldest_problem";
  public static final String OLDEST_PROBLEM_2 = "2_oldest_problem";
  public static final String OLDEST_PROBLEM_3 = "3_oldest_problem";
  public static final String OLDEST_PROBLEM_4 = "4_oldest_problem";
  private static final Map<String, Problem> testProblemsMap = new HashMap<>();

  @BeforeAll
  static void setUp() {
    buildResources();
  }

  private static void buildResources() {
    Problem problem1 = new Problem();
    problem1.setCreationTime(new Timestamp(1));

    Problem problem2 = new Problem();
    problem2.setCreationTime(new Timestamp(2));

    Problem problem3 = new Problem();
    problem3.setCreationTime(new Timestamp(3));

    Problem problem4 = new Problem();
    problem4.setCreationTime(new Timestamp(4));

    testProblemsMap.put(OLDEST_PROBLEM_1, problem1);
    testProblemsMap.put(OLDEST_PROBLEM_2, problem2);
    testProblemsMap.put(OLDEST_PROBLEM_3, problem3);
    testProblemsMap.put(OLDEST_PROBLEM_4, problem4);
  }

  @ParameterizedTest
  @ArgumentsSource(SortArgumentsProvider.class)
  void whenSortByProvidedSorter_thenSort(List<Problem> expected, Sorter<Problem> sorter) {
    List<Problem> result = new ArrayList<>(sorter.sort(testProblemsMap.values()));
    for (int i = 1; i < expected.size(); i++) {
      Assertions.assertEquals(expected.get(i), result.get(i));
    }
  }

  private static class SortArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(
              List.of(
                  testProblemsMap.get(OLDEST_PROBLEM_1),
                  testProblemsMap.get(OLDEST_PROBLEM_2),
                  testProblemsMap.get(OLDEST_PROBLEM_3),
                  testProblemsMap.get(OLDEST_PROBLEM_4)),
              ProblemSorter.DESCENDING_TIME_STAMP_SORTER),
          Arguments.of(
              List.of(
                  testProblemsMap.get(OLDEST_PROBLEM_4),
                  testProblemsMap.get(OLDEST_PROBLEM_3),
                  testProblemsMap.get(OLDEST_PROBLEM_2),
                  testProblemsMap.get(OLDEST_PROBLEM_1)),
              ProblemSorter.ASCENDING_TIME_STAMP_SORTER),
          Arguments.of(new ArrayList<>(testProblemsMap.values()), ProblemSorter.DEFAULT_SORTER));
    }
  }
}
