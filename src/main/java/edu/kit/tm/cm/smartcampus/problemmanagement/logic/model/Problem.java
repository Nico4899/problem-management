package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.utility.DataTransferUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * This class represents a problem, connected to {@link Notification} and a reference, from the
 * building domain.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Problem {
  private State state;
  private String title;
  private String description;
  private Timestamp creationTime;
  private String reporter;
  private String identificationNumber;
  private String referenceIdentificationNumber;
  private String notificationIdentificationNumber;

  /** The enum problem state. */
  public enum State {
    /** Accepted state. */
    ACCEPTED {
      @Override
      public Collection<Operation> possibleOperations() {
        return List.of(Operation.APPROACH, Operation.DECLINE);
      }

      @Override
      public State accept() {
        throw new IllegalStateException();
      }

      @Override
      public State approach() {
        return IN_PROGRESS;
      }

      @Override
      public State close() {
        throw new IllegalStateException();
      }

      @Override
      public State hold() {
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
        throw new IllegalStateException();
      }

      @Override
      public State close() {
        return CLOSED;
      }

      @Override
      public State hold() {
        throw new IllegalStateException();
      }

      @Override
      public State decline() {
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
        throw new IllegalStateException();
      }

      @Override
      public State approach() {
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
        throw new IllegalStateException();
      }

      @Override
      public State close() {
        throw new IllegalStateException();
      }

      @Override
      public State hold() {
        throw new IllegalStateException();
      }

      @Override
      public State decline() {
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
        throw new IllegalStateException();
      }

      @Override
      public State approach() {
        throw new IllegalStateException();
      }

      @Override
      public State close() {
        throw new IllegalStateException();
      }

      @Override
      public State hold() {
        throw new IllegalStateException();
      }

      @Override
      public State decline() {
        throw new IllegalStateException();
      }
    };

    /**
     * Gets possible operations.
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

    /** The enum state operation. */
    public enum Operation {
      /** Accept state operation. */
      ACCEPT {
        @Override
        public void apply(
            Problem problem,
            BuildingConnector buildingConnector,
            ProblemConnector problemConnector) {
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
            Problem problem,
            BuildingConnector buildingConnector,
            ProblemConnector problemConnector) {
          problem.state = problem.state.decline();
          buildingConnector.removeNotification((problem.getNotificationIdentificationNumber()));
          problemConnector.updateProblem(
              DataTransferUtils.ClientRequestWriter.writeUpdateProblemRequest(problem));
        }
      },
      /** Approach state operation. */
      APPROACH {
        @Override
        public void apply(
            Problem problem,
            BuildingConnector buildingConnector,
            ProblemConnector problemConnector) {
          problem.state = problem.state.approach();
          problemConnector.updateProblem(
              DataTransferUtils.ClientRequestWriter.writeUpdateProblemRequest(problem));
        }
      },
      /** Close state operation. */
      CLOSE {
        @Override
        public void apply(
            Problem problem,
            BuildingConnector buildingConnector,
            ProblemConnector problemConnector) {
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
            Problem problem,
            BuildingConnector buildingConnector,
            ProblemConnector problemConnector) {
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
}
