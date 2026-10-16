package command;

import robot.Robot;

public class ResMCommand implements Command{

    @Override
    public void execute(Robot robot) {
        robot.resetHistory();
    }

    @Override
    public Command inverse() {
        return this;
    }
}
