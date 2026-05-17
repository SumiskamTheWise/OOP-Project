package cfg.cli;

import cfg.model.Grammar;
import java.io.IOException;

/**
 * Saves a single grammar to a specified file.
 * <p>
 * Usage: {@code save <id> <filename>}
 * </p>
 */
public class SaveGrammarCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 2) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g = PrintCommand.resolveGrammar(args[0], context);
        if (g == null) return;
        String path = args[1];
        try {
            context.getSession().getFormat().saveSingle(path, g);
            System.out.println("Successfully saved grammar #" + g.getId() + " to " + java.nio.file.Paths.get(path).getFileName());
        } catch (IOException e) {
            System.out.println("Error saving grammar: " + e.getMessage());
        }
    }

    @Override
    public String usage() {
        return "save <id> <file>  saves grammar #<id> to <file>";
    }
}
