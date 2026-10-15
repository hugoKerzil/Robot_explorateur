package robot;

public class Robot {
    private Position position;
    private Direction direction;
    private Board board;

    public Robot(Board board) {
        this.direction = Direction.EAST;
        this.board = board;
    }

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

    public Position getPosition() {
        return position;
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

        Boolean startScript = true;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");

            switch (parts[0]) {
                case "base" -> {
                    if (parts.length < 3) throw new IllegalArgumentException("Missing arguments for base");

                    if (startScript) {
                        startScript = false;
                        this.setPosition(new Position(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                    } else {
                        throw new IllegalArgumentException("startScript is false");
                    }
                }
                case "dir" -> {
                    if (parts.length < 2) throw new IllegalArgumentException("Missing argument for dir");

                    this.setDirection(Direction.valueOf(parts[1]));
                }
                case "mov" -> {
                    if (parts.length < 2) throw new IllegalArgumentException("Missing argument for mov");

                    this.move(Integer.parseInt(parts[1]));
                }
                case "drop" -> {
                    if (parts.length < 2) throw new IllegalArgumentException("Missing argument for drop");

                    this.board.setAt(this.getPosition(), new Item(parts[1]));
                }
                default -> throw new IllegalArgumentException("A line is not correct: " + line);
            }

        }
    }
}