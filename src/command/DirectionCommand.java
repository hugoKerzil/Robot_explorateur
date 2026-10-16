package command;

import robot.Direction;
import robot.Robot;

public class DirectionCommand implements Command {
    private final Direction direction;

    public DirectionCommand(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void execute(Robot robot) {
        if (robot.isRecord()) robot.addCommandInHistory(this);
        robot.setDirection(this.direction);
    }
}