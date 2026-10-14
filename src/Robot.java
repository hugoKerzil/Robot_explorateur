import java.nio.file.Path;
import java.nio.file.Paths;

public class Robot {
    private Position position;
    private Direction direction;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Item getCarriedItem() {
        return carriedItem;
    }

    public void setCarriedItem(Item carriedItem) {
        this.carriedItem = carriedItem;
    }

    private Board board;
    private Item carriedItem;

    public Position getPosition() {
        return position;
    }

    public Robot(Board board) {
        this.direction = Direction.EAST;
        this.board = board;
    }

    public void setPosition(Position newPos) {
        if (this.position != null && this.board != null) {
            this.board.restoreTerrain(this.position);
        }
        this.position = newPos;
        if (this.board != null && newPos != null) {
            this.board.setAt(newPos, this);
        }
    }

    public void move(int value) {
        int newX = position.getX();
        int newY = position.getY();

        switch (direction) {
            case NORTH -> newY += value;
            case SOUTH -> newY -= value;
            case EAST  -> newX += value;
            case WEST  -> newX -= value;
        }

        this.setPosition(new Position(newX, newY));
    }

    public void execute(String script) {
        if (script == null || script.isBlank()) {
            throw new IllegalArgumentException("The script is not correct");
        }

        String[] lines = script.split("\\R");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            String command = parts[0];

            switch (parts[0]) {
                case "base" -> {}
                case "dir" -> {
                    if (parts.length < 2) {
                        throw new IllegalArgumentException("Missing argument for dir");
                    }
                    this.setDirection(Direction.valueOf(parts[1]));
                }
                case "mov" -> {}
                case "drop" -> {}
                default -> throw new IllegalArgumentException("A line is not correct: " + line);
            }

        }
    }
}