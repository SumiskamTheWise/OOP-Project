package cfg.cli;

import cfg.model.Grammar;

/**
 * Prints a grammar in a human-readable format with numbered rules.
 * <p>
 * Usage: print ID
 * </p>
 */
public class PrintCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 1) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g = resolveGrammar(args[0], context);
        if (g == null) return;
        context.getPrinter().print(g);
    }

    @Override
    public String usage() {
        return "print <id> prints grammar #<id> with numbered rules";
    }

    static Grammar resolveGrammar(String idStr, AppContext context) {
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            System.out.println("Error: '" + idStr + "' is not a valid grammar id.");
            return null;
        }
        Grammar g = context.getStore().get(id);
        if (g == null) {
            System.out.println("Error: No grammar with id " + id + ".");
        }
        return g;
    }
}
