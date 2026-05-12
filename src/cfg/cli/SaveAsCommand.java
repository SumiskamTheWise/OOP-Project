package cfg.cli;

import java.io.IOException;

/**
 * Saves all grammars to a user-specified file path.
 * <p>
 * Usage: {@code save as <filepath>}
 * </p>
 */
public class SaveAsCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 1) {
            System.out.println("Usage: " + usage());
            return;
        }
        String path = args[0];
        try {
            String name = context.getSession().saveAs(path);
            System.out.println("Successfully saved " + name);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public String usage() {
        return "save as <file> saves the currently open file in <file>";
    }
}
