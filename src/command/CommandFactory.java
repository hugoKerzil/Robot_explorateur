package command;

@FunctionalInterface
public interface CommandFactory {
    Command create(String[] parts);
}