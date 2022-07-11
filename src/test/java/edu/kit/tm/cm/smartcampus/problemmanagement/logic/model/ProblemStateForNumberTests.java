package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

class ProblemStateForNumberTests {

  @ParameterizedTest
  @ArgumentsSource(ProblemStateNumberArgumentsProvider.class)
  void filterRoomCollectionAndValuesResultTest(ProblemState expected, int number) {
    Assertions.assertEquals(expected, ProblemState.forNumber(number));
  }

  private static class ProblemStateNumberArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(ProblemState.ACCEPTED, 5),
          Arguments.of(ProblemState.IN_PROGRESS, 4),
          Arguments.of(ProblemState.CLOSED, 3),
          Arguments.of(ProblemState.OPEN, 2),
          Arguments.of(ProblemState.DECLINED, 1),
          Arguments.of(null, 0));
    }
  }
}
