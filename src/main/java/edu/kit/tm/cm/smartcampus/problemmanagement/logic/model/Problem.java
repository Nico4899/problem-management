package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.utility.DataTransferUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * This class represents a Problem, dependent on it's state it has a {@link Notification} belonging
 * to it. The Enums {@link State} and {@link Operation} define a statemachine to alter the current
 * problem state. For more information read their Documentation. This object should act as Data
 * object, representing a SmartCampus problem. It's purpose is explained in the <a
 * href="https://git.scc.kit.edu/cm-tm/cm-team/3.projectwork/pse/docsc/-/blob/master/pages/ubiquitous_language.md">Smart
 * Campus UL</a>.
 */
@Getter
@Setter
@NoArgsConstructor
public class Problem {
  private State state;
  private String title;
  private String description;
  private Timestamp creationTime;
  private Timestamp lastModified;
  private String reporter;
  private String identificationNumber;
  private String referenceIdentificationNumber;
  private String notificationIdentificationNumber;

  /**
   * This enum describes a number of states a {@link Problem} can have. A statemachine is
   * implemented, and to retrieve information about possible outgoing edges the method {@link
   * State#possibleOperations()} provides the information asked for.
   */
  public enum State {
    /** Accepted state. */
    ACCEPTED {
      @Override
      public Collection<Operation> possibleOperations() {
        return List.of(Operation.APPROACH, Operation.DECLINE);
      }

      @Override
      public State accept() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State approach() {
        return IN_PROGRESS;
      }

      @Override
      public State close() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State hold() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State decline() {
        return DECLINED;
      }
    },
    /** Declined state. */
    DECLINED {
      @Override
      public Collection<Operation> possibleOperations() {
        return List.of(Operation.CLOSE, Operation.ACCEPT);
      }

      @Override
      public State accept() {
        return ACCEPTED;
      }

      @Override
      public State approach() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State close() {
        return CLOSED;
      }

      @Override
      public State hold() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State decline() {
        // not a valid edge
        throw new IllegalStateException();
      }
    },
    /** In progress state. */
    IN_PROGRESS {
      @Override
      public Collection<Operation> possibleOperations() {
        return List.of(Operation.CLOSE, Operation.HOLD);
      }

      @Override
      public State accept() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State approach() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State close() {
        return CLOSED;
      }

      @Override
      public State hold() {
        return ACCEPTED;
      }

      @Override
      public State decline() {
        // not a valid edge
        throw new IllegalStateException();
      }
    },
    /** Open state. */
    OPEN {
      @Override
      public Collection<Operation> possibleOperations() {
        return List.of(Operation.DECLINE, Operation.ACCEPT);
      }

      @Override
      public State accept() {
        return ACCEPTED;
      }

      @Override
      public State approach() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State close() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State hold() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State decline() {
        // not a valid edge
        return DECLINED;
      }
    },
    /** Closed state. */
    CLOSED {
      @Override
      public Collection<Operation> possibleOperations() {
        return List.of();
      }

      @Override
      public State accept() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State approach() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State close() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State hold() {
        // not a valid edge
        throw new IllegalStateException();
      }

      @Override
      public State decline() {
        // not a valid edge
        throw new IllegalStateException();
      }
    };

    /**
     * Gets possible operations from current state.
     *
     * @return the possible operations
     */
    public abstract Collection<Operation> possibleOperations();

    /**
     * Accept problem state.
     *
     * @return the problem state
     */
    public abstract State accept();

    /**
     * Approach problem state.
     *
     * @return the problem state
     */
    public abstract State approach();

    /**
     * Close problem state.
     *
     * @return the problem state
     */
    public abstract State close();

    /**
     * Hold problem state.
     *
     * @return the problem state
     */
    public abstract State hold();

    /**
     * Decline problem state.
     *
     * @return the problem state
     */
    public abstract State decline();
  }

  /** The enum state operation. */
  public enum Operation {
    /** Accept state operation. */
    ACCEPT {
      @Override
      public void apply(
          Problem problem, BuildingConnector buildingConnector, ProblemConnector problemConnector) {

        // first try to run command
        // after create the belonging notification
        // get the created notification id and set problem notification id to that value
        // update the problem

        problem.state = problem.state.accept();
        Notification notification =
            buildingConnector.createNotification(
                DataTransferUtils.ClientRequestWriter.writeCreateNotificationRequest(problem));
        problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
        problemConnector.updateProblem(
            DataTransferUtils.ClientRequestWriter.writeUpdateProblemRequest(problem));
      }
    },
    /** Decline state operation. */
    DECLINE {
      @Override
      public void apply(
          Problem problem, BuildingConnector buildingConnector, ProblemConnector problemConnector) {

        // first try to run command
        // if not already "null" set notification id "null" and remove the notification
        // update the problem

        problem.state = problem.state.decline();
        if (problem.getNotificationIdentificationNumber() != null) {
          buildingConnector.removeNotification(problem.getNotificationIdentificationNumber());
          problem.setNotificationIdentificationNumber(null);
        }
        problemConnector.updateProblem(
            DataTransferUtils.ClientRequestWriter.writeUpdateProblemRequest(problem));
      }
    },
    /** Approach state operation. */
    APPROACH {
      @Override
      public void apply(
          Problem problem, BuildingConnector buildingConnector, ProblemConnector problemConnector) {

        // first try to run the command
        // update the problem

        problem.state = problem.state.approach();
        problemConnector.updateProblem(
            DataTransferUtils.ClientRequestWriter.writeUpdateProblemRequest(problem));
      }
    },
    /** Close state operation. */
    CLOSE {
      @Override
      public void apply(
          Problem problem, BuildingConnector buildingConnector, ProblemConnector problemConnector) {

        // first try to run command
        // if not already "null" set notification id "null" and remove the notification
        // update the problem

        problem.state = problem.state.close();
        if (problem.getNotificationIdentificationNumber() != null) {
          buildingConnector.removeNotification(problem.getNotificationIdentificationNumber());
          problem.setNotificationIdentificationNumber(null);
        }
        problemConnector.updateProblem(
            DataTransferUtils.ClientRequestWriter.writeUpdateProblemRequest(problem));
      }
    },
    /** Hold state operation. */
    HOLD {
      @Override
      public void apply(
          Problem problem, BuildingConnector buildingConnector, ProblemConnector problemConnector) {

        // first try to run the command
        // update the problem

        problem.state = problem.state.hold();
        problemConnector.updateProblem(
            DataTransferUtils.ClientRequestWriter.writeUpdateProblemRequest(problem));
      }
    };

    /**
     * Apply a state operation on a problem.
     *
     * @param problem the problem
     * @param buildingConnector the building connector
     * @param problemConnector the problem connector
     */
    public abstract void apply(
        Problem problem, BuildingConnector buildingConnector, ProblemConnector problemConnector);
  }
}
