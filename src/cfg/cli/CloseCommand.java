package cfg.cli;

/**
 * Closes the currently open file without saving.
 * <p>
 * Usage: {@code close}
 * </p>
 */
public class CloseCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        try {
            String name = context.getSession().close();
            System.out.println("Successfully closed " + name);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String usage() {
        return "closes currently opened file";
    }
}
