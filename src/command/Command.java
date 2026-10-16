package command;

import robot.Robot;

import java.util.List;

public interface Command {

    void execute(Robot robot);

     default Command inverse() {
         throw new UnsupportedOperationException("Cette opération n'est pas suporter par cette classe.");
     };

     default List<Command> mergeWith(Command other) {
         throw new UnsupportedOperationException("Cette opération n'est pas supporter par cette classe.");
     }
}
