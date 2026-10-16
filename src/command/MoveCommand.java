package command;

import robot.Robot;

import java.util.List;

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

    @Override
    public Command inverse() {
        return new MoveCommand(-distance);
    }

    @Override
    public List<Command> mergeWith(Command other) {
        if (other instanceof MoveCommand moveCommand) {
            return List.of(
                    new MoveCommand(this.distance + moveCommand.distance)
            );
        }
        return List.of(this, other);
    }

    public int getDistance() {
        return distance;
    }
}
