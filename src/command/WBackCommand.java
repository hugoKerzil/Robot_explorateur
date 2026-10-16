package command;

import robot.Robot;

import java.util.List;

public class WBackCommand implements Command{

    @Override
    public void execute(Robot robot) {
        boolean wasRecording = robot.isRecord();
        robot.setRecord(false);

        List<Command> history = robot.getHistory();

        for (int i = history.size() - 1; i >= 0; i--) {
            history.get(i).inverse().execute(robot);
        }

        robot.setRecord(wasRecording);
    }

    @Override
    public Command inverse() {
        return this;
    }
}
