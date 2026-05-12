package cfg.cli;

import java.io.IOException;

/**
 * Saves all grammars back to the currently open file.
 * <p>
 * Usage: {@code save}
 * </p>
 */
public class SaveCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        try {
            String name = context.getSession().save();
            System.out.println("Successfully saved " + name);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public String usage() {
        return "saves the currently open file";
    }
}
