package command;

import robot.Robot;

public class StopMCommand implements Command{
    @Override
    public void execute(Robot robot) {
        robot.setRecord(false);
    }
}
