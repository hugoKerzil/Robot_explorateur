package command;

import robot.Robot;

import java.util.List;

public class ReplayCommand implements Command{
    @Override
    public void execute(Robot robot) {
        // ou avant l'appel
        robot.setRecord(false);
        List<Command> commands = robot.getHistory();
        for ( Command command : commands) {
            command.execute(robot);
        }
    }
}
