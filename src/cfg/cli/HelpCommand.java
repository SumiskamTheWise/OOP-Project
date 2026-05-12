package cfg.cli;

import java.util.Map;

/**
 * Prints a summary of all supported commands.
 * <p>
 * Usage: {@code help}
 * </p>
 */
public class HelpCommand implements Command {

    private final Map<String, Command> commandRegistry;

    /**
     * Constructs a HelpCommand that reads usage strings from the full command registry.
     *
     * @param commandRegistry all registered commands, keyed by name
     */
    public HelpCommand(Map<String, Command> commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(String[] args, AppContext context) {
        System.out.println("The following commands are supported:");
        commandRegistry.forEach((name, cmd) -> System.out.printf("  %-30s%n", cmd.usage()));
    }

    @Override
    public String usage() {
        return "prints this information";
    }
}
