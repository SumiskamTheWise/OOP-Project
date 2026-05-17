package cfg.cli;

/**
 * Represents a CLI command that can be executed with a set of arguments.
 * <p>
 * Follows the Command pattern: each command encapsulates the action
 * and knows how to execute itself given the application context.
 */
public interface Command {

    /**
     * Executes the command with the given arguments.
     *
     * @param args    the arguments provided by the user (not including the command name)
     * @param context the shared application context (grammar store, session, etc.)
     */
    void execute(String[] args, AppContext context);

    /**
     * Returns the expected usage string for this command.
     *
     * @return usage description
     */
    String usage();

    /**
     * Whether this command requires an open file session before it can run.
     * <p>
     * Defaults to {@code true}. Commands that can run without an open file
     * (like {@code open}, {@code help}, {@code exit}) should override this to
     * return {@code false}.
     *
     * @return {@code true} if the command needs an open session
     */
    default boolean requiresOpenSession() {
        return true;
    }
}