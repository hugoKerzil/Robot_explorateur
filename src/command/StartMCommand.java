package command;

import robot.Robot;

public class StartMCommand implements Command{
    @Override
    public void execute(Robot robot) {
        robot.setRecord(true);
    }
}
