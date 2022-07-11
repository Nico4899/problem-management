package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

class NotificationTest {

  @Test
  void fromProblemTest() {
    Problem problem = new Problem(ProblemState.OPEN, "a", "b", new Timestamp(123), "c", "d", "e", "f");
    Notification notification = Notification.fromProblem(problem);
    Assertions.assertEquals(problem.getProblemDescription(), notification.getNotificationDescription());
    Assertions.assertEquals(problem.getProblemTitle(), notification.getNotificationTitle());
    Assertions.assertEquals(problem.getNotificationIdentificationNumber(), notification.getIdentificationNumber());
    Assertions.assertEquals(problem.getReferenceIdentificationNumber(), notification.getParentIdentificationNumber());
    Assertions.assertEquals(problem.getCreationTime(), notification.getCreationTime());
  }
}
