package cfg.cli;

import java.io.IOException;

/**
 * Opens a grammar file, loading its contents into memory.
 * If the file does not exist, an empty session is started.
 * <p>
 * Usage: {@code open <filepath>}
 * </p>
 */
public class OpenCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 1) {
            System.out.println("Usage: " + usage());
            return;
        }
        String path = args[0];
        try {
            context.getSession().open(path);
            System.out.println("Successfully opened " + java.nio.file.Paths.get(path).getFileName());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid file content: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public String usage() {
        return "open <file>   opens <file>";
    }
}
