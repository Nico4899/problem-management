package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InternalServerErrorException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidStateChangeRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/** The type Problem. */
@Getter
@Setter
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

  /**
   * Extract {@link Notification} from this {@link Problem}.
   *
   * @return the extracted notification
   */
  public Notification extractNotification() {
    Notification notification = new Notification();
    notification.setTitle(title);
    notification.setParentIdentificationNumber(referenceIdentificationNumber);
    notification.setCreationTime(creationTime);
    notification.setDescription(description);
    return notification;
  }

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
        throw new InvalidStateChangeRequestException(Operation.ACCEPT.name());
      }

      @Override
      public State approach() {
        return IN_PROGRESS;
      }

      @Override
      public State close() {
        throw new InvalidStateChangeRequestException(Operation.CLOSE.name());
      }

      @Override
      public State hold() {
        throw new InvalidStateChangeRequestException(Operation.HOLD.name());
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
        throw new InvalidStateChangeRequestException(Operation.APPROACH.name());
      }

      @Override
      public State close() {
        return CLOSED;
      }

      @Override
      public State hold() {
        throw new InvalidStateChangeRequestException(Operation.HOLD.name());
      }

      @Override
      public State decline() {
        throw new InvalidStateChangeRequestException(Operation.DECLINE.name());
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
        throw new InvalidStateChangeRequestException(Operation.ACCEPT.name());
      }

      @Override
      public State approach() {
        throw new InvalidStateChangeRequestException(Operation.APPROACH.name());
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
        throw new InvalidStateChangeRequestException(Operation.DECLINE.name());
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
        throw new InvalidStateChangeRequestException(Operation.APPROACH.name());
      }

      @Override
      public State close() {
        throw new InvalidStateChangeRequestException(Operation.CLOSE.name());
      }

      @Override
      public State hold() {
        throw new InvalidStateChangeRequestException(Operation.HOLD.name());
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
        throw new InvalidStateChangeRequestException(Operation.ACCEPT.name());
      }

      @Override
      public State approach() {
        throw new InvalidStateChangeRequestException(Operation.APPROACH.name());
      }

      @Override
      public State close() {
        throw new InvalidStateChangeRequestException(Operation.CLOSE.name());
      }

      @Override
      public State hold() {
        throw new InvalidStateChangeRequestException(Operation.HOLD.name());
      }

      @Override
      public State decline() {
        throw new InvalidStateChangeRequestException(Operation.DECLINE.name());
      }
    };

    /**
     * This static method provides a {@link State} for its ordinal.
     *
     * @param ordinal the ordinal number of the enum constant
     * @return the campus location with the ordinal number
     */
    public static State forOrdinal(int ordinal) {
      if (ordinal >= values().length || ordinal < 0)
        throw new InternalServerErrorException("value with ordinal does not exist");
      return values()[ordinal];
    }

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
              buildingConnector.createNotification(problem.extractNotification());
          problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
          problemConnector.updateProblem(problem);
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
          problemConnector.updateProblem(problem);
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
          problemConnector.updateProblem(problem);
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
          buildingConnector.removeNotification(problem.getNotificationIdentificationNumber());
          problem.setNotificationIdentificationNumber(BLANK_STRING);
          problemConnector.updateProblem(problem);
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
          problemConnector.updateProblem(problem);
        }
      };

      private static final String BLANK_STRING = "";

      /**
       * This static method provides a {@link Operation} for its ordinal.
       *
       * @param ordinal the ordinal number of the enum constant
       * @return the campus location with the ordinal number
       */
      public static State.Operation forOrdinal(int ordinal) {
        if (ordinal >= values().length  || ordinal < 0)
          throw new InternalServerErrorException("value with ordinal does not exist");
        return values()[ordinal];
      }

      public abstract void apply(
          Problem problem, BuildingConnector buildingConnector, ProblemConnector problemConnector);
    }
  }
}
