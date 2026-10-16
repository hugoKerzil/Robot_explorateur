package command;

import robot.Direction;
import robot.Robot;

public class DirectionCommand implements Command {
    private final Direction direction;
    private Direction previousDirection;

    public DirectionCommand(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void execute(Robot robot) {
        if (robot.isRecord()) robot.addCommandInHistory(this);
        previousDirection = robot.getDirection();
        robot.setDirection(this.direction);
    }

    @Override
    public Command inverse() {
        return new DirectionCommand(previousDirection);
    }
}