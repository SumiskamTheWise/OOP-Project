package cfg.cli;

/**
 * Exits the program.
 * <p>
 * Usage: {@code exit}
 * </p>
 */
public class ExitCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        System.out.println("Exiting the program...");
        context.stop();
    }

    @Override
    public String usage() {
        return "exits the program";
    }
}
