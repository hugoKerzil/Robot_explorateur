package command;

import robot.Robot;

public class RemoveItemCommand implements Command {

    @Override
    public void execute(Robot robot) {
        robot.getBoard().removeAt(robot.getPosition());
    }

    @Override
    public Command inverse() {
        return this;
    }
}
