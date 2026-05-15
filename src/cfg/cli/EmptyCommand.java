package cfg.cli;

import cfg.model.Grammar;

/**
 * Checks whether the language of a grammar is empty.
 * <p>
 * Usage: {@code empty <id>}
 */
public class EmptyCommand implements Command {

    @Override
    public void execute(String[] args, AppContext context) {
        if (args.length < 1) {
            System.out.println("Usage: " + usage());
            return;
        }
        Grammar g = PrintCommand.resolveGrammar(args[0], context);
        if (g == null) return;

        boolean empty = context.getEmptyCheck().isEmpty(g);
        System.out.println("The language of grammar #" + g.getId() + " is " + (empty ? "EMPTY." : "NOT empty."));
    }

    @Override
    public String usage() {
        return "empty <id> checks if L(grammar #<id>) is empty";
    }
}
