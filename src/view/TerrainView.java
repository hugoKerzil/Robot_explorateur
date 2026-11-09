package view;

import nicellipse.component.NiEllipse;
import nicellipse.component.NiLabel;
import nicellipse.component.NiRectangle;
import nicellipse.component.NiSpace;
import robot.*;
import robot.Robot;

import javax.swing.*;
import java.awt.*;

public class TerrainView extends JFrame implements Observer {
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 12;
    private static final int CELL_SIZE = 40;

    private final Board board;
    private final Robot robot;
    private final NiSpace boardView;

    public TerrainView(Board board, Robot robot) {
        super("Terrain");

        this.board = board;
        this.robot = robot;

        boardView = new NiSpace(
                "Terrain",
                new Dimension(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE)
        );

        add(boardView);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        robot.addObserver(this);
        board.addObserver(this);

        updated();
    }

    @Override
    public void updated() {
        SwingUtilities.invokeLater(this::refresh);
    }

    private void refresh() {
        boardView.removeAll();

        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                addCell(x, y);
            }
        }

        addRobot();

        boardView.revalidate();
        boardView.repaint();
    }

    private void addRobot() {
        Position position = robot.getPosition();

        if (position == null || !insideBoard(position)) {
            return;
        }

        NiEllipse robotView = new NiEllipse();
        robotView.setBackground(new Color(37, 99, 235));
        robotView.setBorderColor(new Color(219, 234, 254));
        robotView.setStrokeWidth(2);
        int robotSize = CELL_SIZE - 14;
        robotView.setBounds(
                position.getX() * CELL_SIZE + 7,
                (BOARD_HEIGHT - position.getY() - 1) * CELL_SIZE + 7,
                robotSize,
                robotSize
        );

        NiLabel direction = new NiLabel(
                directionSymbol(robot.getDirection())
        );
        direction.setForeground(Color.WHITE);
        direction.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        direction.setLocation(4, 0);

        robotView.add(direction);
        boardView.add(robotView);
    }

    private void addCell(int x, int y) {
        NiRectangle cell = new NiRectangle();

        cell.setBounds(
                x * CELL_SIZE,
                (BOARD_HEIGHT - y - 1) * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE
        );

        Position cellPosition = new Position(x, y);
        Item item = board.getItemAt(cellPosition);

        boolean robotHere = robot.getPosition() != null
                && robot.getPosition().equals(cellPosition);

        if (robotHere) {
            cell.setBackground(new Color(239, 68, 68));
            cell.setBorder(BorderFactory.createLineBorder(
                    new Color(185, 28, 28), 2
            ));
        } else if (item != null) {
            cell.setBackground(new Color(253, 224, 71));
            cell.setBorder(BorderFactory.createLineBorder(
                    new Color(202, 138, 4), 2
            ));
        } else {
            cell.setBackground(
                    (x + y) % 2 == 0
                            ? new Color(226, 232, 240)
                            : new Color(241, 245, 249)
            );
            cell.setBorder(BorderFactory.createLineBorder(
                    new Color(203, 213, 225)
            ));
        }

        if (item != null) {
            NiLabel itemLabel = new NiLabel(item.getName());
            itemLabel.setForeground(new Color(120, 53, 15));
            itemLabel.setBackground(new Color(253, 224, 71));
            itemLabel.setOpaque(true);
            itemLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 9));
            itemLabel.setLocation(2, 2);
            cell.add(itemLabel);
        }

        boardView.add(cell);
    }

    private String directionSymbol(Direction direction) {
        return switch (direction) {
            case NORTH -> "^";
            case SOUTH -> "v";
            case EAST -> ">";
            case WEST -> "<";
        };
    }

    private boolean insideBoard(Position position) {
        return position.getX() >= 0 && position.getX() < BOARD_WIDTH
                && position.getY() >= 0 && position.getY() < BOARD_HEIGHT;
    }
}
