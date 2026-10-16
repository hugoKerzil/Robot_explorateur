package command;

import robot.Robot;

public interface Command {

    void execute(Robot robot);

     default Command inverse() {
         throw new UnsupportedOperationException("Cette opération n'est pas suporter par cette classe.");
     };
}
