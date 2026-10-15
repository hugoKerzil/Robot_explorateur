package command;

import robot.Direction;
import robot.Item;
import robot.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandParser {

    private final Map<String, CommandFactory> factories = new HashMap<>();

    public CommandParser() {
        factories.put("base", parts -> {
            if (parts.length < 3) {
                throw new IllegalArgumentException("Missing arguments for base");
            }
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            return new BaseCommand(new Position(x, y));
        });

        factories.put("dir", parts -> {
            if (parts.length < 2) {
                throw new IllegalArgumentException("Missing argument for dir");
            }
            return new DirectionCommand(Direction.valueOf(parts[1]));
        });

        factories.put("mov", parts -> {
            if (parts.length < 2) {
                throw new IllegalArgumentException("Missing argument for mov");
            }
            return new MoveCommand(Integer.parseInt(parts[1]));
        });

        factories.put("drop", parts -> {
            if (parts.length < 2) {
                throw new IllegalArgumentException("Missing argument for drop");
            }
            return new DropCommand(new Item(parts[1]));
        });
    }

    // Permet d'ajouter facilement de nouvelles commandes plus tard (Étapes 4 et 5)
    public void registerCommand(String name, CommandFactory factory) {
        factories.put(name, factory);
    }

    public Command parseLine(String line) {
        String[] parts = line.trim().split("\\s+");
        String commandName = parts[0];

        CommandFactory factory = factories.get(commandName);
        if (factory == null) {
            throw new IllegalArgumentException("A line is not correct: " + line);
        }

        return factory.create(parts);
    }

    public List<Command> parseScript(String script) {
        if (script == null || script.isBlank()) {
            throw new IllegalArgumentException("The script is not correct");
        }

        List<Command> commands = new ArrayList<>();
        String[] lines = script.split("\\R");

        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                commands.add(parseLine(line));
            }
        }
        return commands;
    }
}