package robot;

import command.Command;
import command.CommandParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Robot {
    private Position position;
    private Direction direction;
    private Board board;
    private final CommandParser parser;
    private List<Command> history;
    private boolean record;

    public Robot(Board board) {
        this.direction = Direction.EAST;
        this.board = board;
        this.parser = new CommandParser();
        this.history = new ArrayList<Command>();
        this.record = false;
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

    public boolean isRecord() {
        return record;
    }

    public void setRecord(boolean record) { this.record = record; }

    public void resetHistory() { this.history = new ArrayList<Command>(); }

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

    public List<Command> getHistory() {
        return history;
    }

    public void addCommandInHistory(Command command) {
        history.add(command);
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
        List<Command> commands = this.parser.parseScript(script);
        for (Command command : commands) {
            command.execute(this);
        }
    }
}