package view;

import command.DirectionCommand;
import robot.Direction;
import robot.Position;
import robot.Robot;

import javax.swing.*;
import java.awt.*;

public class ControlView extends JFrame implements Observer {
    private final Robot robot;
    private final JTextArea script;
    private final JLabel status;

    public ControlView(Robot robot) {
        super("Contrôles");

        this.robot = robot;
        this.script = new JTextArea();
        this.status = new JLabel();

        JButton execute = new JButton("Exécuter");
        execute.addActionListener(event -> executeScript());

        setLayout(new BorderLayout());
        add(status, BorderLayout.NORTH);
        add(new JScrollPane(script), BorderLayout.CENTER);
        add(execute, BorderLayout.SOUTH);

        setSize(350, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        robot.addObserver(this);

        updated();
    }

    private void executeScript() {
        try {
            robot.execute(script.getText());
        } catch (RuntimeException exception) {
            status.setText("Erreur : " + exception.getMessage());
        }
    }

    @Override
    public void updated() {
        SwingUtilities.invokeLater(this::refresh);
    }

    private void refresh() {
        Position position = robot.getPosition();

        status.setText(
                "Position : " + position.getX() + ", " + position.getY()
                        + " | Direction : " + robot.getDirection()
        );
    }
}
