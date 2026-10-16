package command;

import robot.Robot;

public class MoveCommand implements Command{

    private final int distance;

    public MoveCommand(int distance) {
        this.distance = distance;
    }

    @Override
    public void execute(Robot robot) {
        if (robot.isRecord()) robot.addCommandInHistory(this);
        robot.move(distance);
    }
}
