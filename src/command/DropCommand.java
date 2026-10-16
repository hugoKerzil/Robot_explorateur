package command;

import robot.Item;
import robot.Robot;

public class DropCommand implements Command{

    private final Item item;

    public DropCommand(Item item) {
        this.item = item;
    }

    @Override
    public void execute(Robot robot) {
        if (robot.isRecord()) robot.addCommandInHistory(this);
        robot.getBoard().setAt(robot.getPosition(), this.item);
    }
}
