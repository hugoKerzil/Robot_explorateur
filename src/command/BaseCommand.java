package command;

import robot.Position;
import robot.Robot;

public class BaseCommand implements Command {
    private final Position position;

    public BaseCommand(Position position) {
        this.position = position;
    }

    @Override
    public void execute(Robot robot) {
        robot.setPosition(this.position);
    }
}